from django.http import JsonResponse
from django.utils import timezone
from django.utils.crypto import get_random_string

# 메서드 임포트
from .models import log_api, board, Session
from django.http import HttpResponse

# Python
from opentelemetry import trace
from opentelemetry import metrics
from opentelemetry.trace import SpanKind

tracer = trace.get_tracer("django-app.tracer")
meter = metrics.get_meter("django-app.meter")

request_duration = meter.create_histogram(
    "request_duration",
    description="Request processing time in milliseconds",
    unit="ms"
)

def get_client_ip(request):
    # ip_address = request.META["HTTP_X_REAL_IP"]
    ip_address = "127.0.0.1"
    return ip_address


session = Session()

def get_data(request, num_items):

    with tracer.start_as_current_span("GET board/<int:num_items>/", kind=SpanKind.SERVER) as span:

        start_time = timezone.now()

        # 데이터 조회를 위한 자식 스팬 생성
        with tracer.start_as_current_span("SELECT_BOARD", kind=SpanKind.INTERNAL) as child_span_select:
            BoardData = session.query(board).limit(num_items).all()

            # 데이터 변환
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

        end_time = timezone.now()
        duration = (end_time - start_time).total_seconds() * 1000  # 밀리초 단위로 변환
        client_ip = get_client_ip(request)

        with tracer.start_as_current_span("INSERT_LOG", kind=SpanKind.INTERNAL) as child_span_insert:
            LogData = [
                log_api(
                    user_ip=client_ip,
                    user_id=get_random_string(30),
                    start_time=start_time,
                    end_time=end_time,
                    call_url=request.path,
                    call_url_parameter=str(num_items)
                )
            ]

            session.add_all(LogData)
            session.commit()

            request_duration.record(duration, {"method": request.method, "path": request.path})

            # 자식 스팬에 결과 속성 추가
            child_span_insert.set_attribute("db.system", "sqlite")
            child_span_insert.set_attribute("span.kind", "client")

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.method", "GET")
        span.set_attribute("http.status_code", 200)
        span.set_attribute("http.target", "/board/10/")
        span.set_attribute("operation_duration_ms", duration)

    if num_items <= 100:
        return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환

    else:
        return HttpResponse(f"Successfully fetched {num_items} data items in {duration:.2f} ms", status=200)


def get_data_all(request):

    with tracer.start_as_current_span("GET board", kind=SpanKind.SERVER) as span:
        start_time = timezone.now()  # 처리 시작 시간 기록

        # 데이터 조회를 위한 자식 스팬 생성
        with tracer.start_as_current_span("fetch_data", kind=SpanKind.INTERNAL) as child_span:
            BoardData = session.query(board).all()
            data_list = list(BoardData)  # 데이터 리스트 생성
            num_items = len(data_list)
            child_span.set_attribute("num_items", num_items)

        end_time = timezone.now()  # 처리 종료 시간 기록
        duration = (end_time - start_time).total_seconds() * 1000  # 처리 시간을 밀리초 단위로 변환

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.status_code", 200)
        span.set_attribute("operation_duration_ms", duration)

        session.commit()

        # 성공 메시지와 함께 처리 시간 반환
        return HttpResponse(f"Successfully fetched {num_items} data in {duration:.2f} ms", status=200)

def log_data(request):
    # Spankind.Server: 서버가 클라이언트의 요청을 받고 처리하는 전체 과정
    with tracer.start_as_current_span("GET log", kind=SpanKind.SERVER) as span:

        LogData = session.query(log_api).all()

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.status_code", 200)

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

        session.commit()

        return JsonResponse(data_list, safe=False)


def home(request):
    return HttpResponse("Welcome to my Django app!")

def get_user(request):
    # Spankind.Server: 서버가 클라이언트의 요청을 받고 처리하는 전체 과정
    with tracer.start_as_current_span("user_data", kind=SpanKind.SERVER) as span:

        try:
            raise Exception('get_user error')
        except Exception as e:                             # 예외가 발생했을 때 실행됨
            print('error : ', e)
            return e
        return 'error~~'