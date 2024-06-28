from flask import Blueprint, request, jsonify
from models import Board, LogAPI, db
from datetime import datetime
import pytz, secrets, string
from opentelemetry import trace, metrics
from opentelemetry.trace import SpanKind


main_blueprint = Blueprint('main', __name__)

tracer = trace.get_tracer(__name__)
meter = metrics.get_meter(__name__)

request_counter = meter.create_counter(
    name="request_count",
    description="Number of requests received",
    unit="1"
)

request_duration = meter.create_histogram(
    "request_duration",
    description="Request processing time in milliseconds",
    unit="ms"
)

def get_random_string(length=12):
    characters = string.ascii_letters + string.digits
    return ''.join(secrets.choice(characters) for _ in range(length))

@main_blueprint.route('/board/<int:num_items>/', methods=['GET'])
def get_data(num_items):

    utc_now = datetime.now(pytz.utc)
    seoul_tz = pytz.timezone('Asia/Seoul')
    start_time = utc_now.astimezone(seoul_tz)

    with tracer.start_as_current_span("SELECT_BOARD", kind=SpanKind.INTERNAL) as child_span_select:
        request_counter.add(1, {"path": "/board/<int:num_items>/"})
        BoardData = Board.query.limit(num_items).all()
        data_list = [
            {
                "seq": item.seq,
                "title": item.title,
                "content": item.content,
                "create_date": item.create_date,
                "update_date": item.update_date,
            }
            for item in BoardData
        ]
        # 자식 스팬에 결과 속성 추가
        child_span_select.set_attribute("db.system", "sqlite")
        child_span_select.set_attribute("span.kind", "client")
        child_span_select.set_attribute("num_items", num_items)

    end_time = datetime.now(pytz.utc).astimezone(seoul_tz)
    client_ip = request.remote_addr or "127.0.0.1"

    duration = (end_time - start_time).total_seconds() * 1000

    with tracer.start_as_current_span("INSERT_LOG", kind=SpanKind.INTERNAL) as child_span_insert:
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
        request_duration.record(duration, {"method": request.method, "path": request.path})

        # 자식 스팬에 결과 속성 추가
        child_span_insert.set_attribute("db.system", "sqlite")
        child_span_insert.set_attribute("span.kind", "client")

    return jsonify(data_list) if num_items <= 100 else f"Successfully fetched {num_items} data items in {duration:.2f} ms"

@main_blueprint.route('/board/', methods=['GET'])
def get_data_all():
    utc_now = datetime.now(pytz.utc)
    seoul_tz = pytz.timezone('Asia/Seoul')
    start_time = utc_now.astimezone(seoul_tz)

    with tracer.start_as_current_span("SELECT_BOARD", kind=SpanKind.INTERNAL) as child_span:
        BoardData = Board.query.all()
        data_list = [
            {
                "seq": item.seq,
                "title": item.title,
                "content": item.content,
                "create_date": item.create_date,
                "update_date": item.update_date,
            }
            for item in BoardData
        ]
        num_items = len(data_list)
        child_span.set_attribute("num_items", num_items)

    end_time = datetime.now(pytz.utc).astimezone(seoul_tz)
    duration = (end_time - start_time).total_seconds() * 1000  # 처리 시간을 밀리초 단위로 변환
    db.session.commit()

    if duration > 1000:
        raise TimeoutError(f"Query took {duration:.2f} milliseconds, which exceeds the limit of 1 seconds.")

    return jsonify(data_list) if num_items <= 100 else f"Successfully fetched {num_items} data items in {duration:.2f} ms"


@main_blueprint.route('/log/', methods=['GET'])
def log_data():
    with tracer.start_as_current_span("SELECT_BOARD", kind=SpanKind.INTERNAL) as child_span_select:
        LogData = LogAPI.query.all()
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

        # 자식 스팬에 결과 속성 추가
        child_span_select.set_attribute("db.system", "sqlite")
        child_span_select.set_attribute("span.kind", "client")

        return jsonify(data_list)

@main_blueprint.route('/user/')
def get_user():
    try:
        raise Exception('get_user_error')
    except Exception as e:  # 예외가 발생했을 때 실행됨
        return e

@main_blueprint.route('/')
def home():
    with tracer.start_as_current_span("GET /") as span:
        request_counter.add(1, {"path": "/"})
        return "Welcome to my Flask app!"
