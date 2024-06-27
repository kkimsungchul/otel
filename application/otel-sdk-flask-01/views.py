from flask import Blueprint, request, jsonify
from models import Board, LogAPI, db
from datetime import datetime
import pytz, secrets, string
from opentelemetry import trace, metrics

main_blueprint = Blueprint('main', __name__)

def get_random_string(length=12):
    characters = string.ascii_letters + string.digits
    return ''.join(secrets.choice(characters) for _ in range(length))

@main_blueprint.route('/board/<int:num_items>/', methods=['GET'])
def get_data(num_items):

    tracer = trace.get_tracer(__name__)
    meter = metrics.get_meter(__name__)
    request_counter = meter.create_counter(
        name="request_count",
        description="Number of requests received",
        unit="1"
    )
    with tracer.start_as_current_span("GET /board/<int:num_items>/") as span:

        request_counter.add(1, {"path": "/board/<int:num_items>/"})

        utc_now = datetime.now(pytz.utc)
        seoul_tz = pytz.timezone('Asia/Seoul')
        start_time = utc_now.astimezone(seoul_tz)

        BoardData = Board.query.limit(num_items).all()

        data_list = [
            {
                "seq": item.seq,
                "title": item.title,
                "content": item.content,
                "create_date": item.create_date.astimezone(seoul_tz).strftime('%Y-%m-%d %H:%M:%S %Z%z'),
                "update_date": item.update_date.astimezone(seoul_tz).strftime('%Y-%m-%d %H:%M:%S %Z%z'),
            }
            for item in BoardData
        ]

        end_time = datetime.now(pytz.utc).astimezone(seoul_tz)
        duration = (end_time - start_time).total_seconds() * 1000
        client_ip = request.remote_addr or "127.0.0.1"

        LogData = LogAPI(
            user_ip=client_ip,
            user_id=get_random_string(30),
            start_time=start_time,
            end_time=end_time,
            call_url=request.path,
            call_url_parameter=str(num_items)
        )

        db.session.add(LogData)
        db.session.commit()

        span.set_attribute("http.method", "GET")
        span.set_attribute("http.status_code", 200)
        span.set_attribute("http.target", request.path)
        span.set_attribute("operation_duration_ms", duration)

        return jsonify(data_list) if num_items <= 100 else f"Successfully fetched {num_items} data items in {duration:.2f} ms"


@main_blueprint.route('/log/', methods=['GET'])
def log_data():

    LogData = LogAPI.query.all()

    # 데이터 변환
    data_list = [
        {
            "seq": item.seq,
            "user_ip": item.user_ip,
            "user_id": item.user_id,
            "start_time": item.start_time,
            "end_time": item.end_time,
            "call_url": item.call_url,
            "call_url_parameter": item.call_url_parameter,
        }
        for item in LogData
    ]

    db.session.commit()

    return jsonify(data_list)

@main_blueprint.route('/user/')
def get_user():
    try:
        raise Exception('get_user error')
    except Exception as e:  # 예외가 발생했을 때 실행됨
        return e  # JSON 형식으로 에러 메시지와 500 상태 코드 반환
    return 'error~~'

@main_blueprint.route('/')
def home():
    tracer = trace.get_tracer(__name__)
    meter = metrics.get_meter(__name__)
    request_counter = meter.create_counter(
        name="request_count",
        description="Number of requests received",
        unit="1"
    )

    with tracer.start_as_current_span("GET /") as span:
        request_counter.add(1, {"path": "/"})
        return "Welcome to my Flask app!"
