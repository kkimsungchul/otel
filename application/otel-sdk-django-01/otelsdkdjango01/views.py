from django.http import JsonResponse
from django.utils import timezone
from django.utils.crypto import get_random_string

# 메서드 임포트
from .models import log_api, board
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


def get_data(request, num_items):
    with tracer.start_as_current_span("GET board/<int:num_items>/", kind=SpanKind.SERVER) as span:

        start_time = timezone.now()

        # 데이터 조회를 위한 자식 스팬 생성
        with tracer.start_as_current_span("INSERT_BOARD", kind=SpanKind.INTERNAL) as child_span_select:
            data = board.objects.all()[:num_items].values('seq',
                                                          'title',
                                                          'content',
                                                          'create_date',
                                                          'update_date')
            data_list = list(data)

            # 자식 스팬에 결과 속성 추가
            child_span_select.set_attribute("db.system", "sqlite")
            child_span_select.set_attribute("span.kind", "client")
            child_span_select.set_attribute("num_items", num_items)

        end_time = timezone.now()
        duration = (end_time - start_time).total_seconds() * 1000  # 밀리초 단위로 변환
        client_ip = get_client_ip(request)

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.method", "GET")
        span.set_attribute("http.status_code", 200)
        span.set_attribute("http.target", "/board/10/")
        span.set_attribute("operation_duration_ms", duration)

        with tracer.start_as_current_span("SELECT_LOG", kind=SpanKind.INTERNAL) as child_span_insert:
            log_api.objects.create(
                user_ip=client_ip,
                user_id=get_random_string(30),
                start_time=start_time,
                end_time=end_time,
                call_url=request.path,
                call_url_parameter=num_items
            )

            request_duration.record(duration, {"method": request.method, "path": request.path})

            # 자식 스팬에 결과 속성 추가
            child_span_insert.set_attribute("db.system", "sqlite")
            child_span_insert.set_attribute("span.kind", "client")

            if num_items <= 100:
                return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환

            else:
                return HttpResponse(f"Successfully fetched {num_items} data items in {duration:.2f} ms", status=200)


def get_data_all(request):
    with tracer.start_as_current_span("get_data_all", kind=SpanKind.SERVER) as span:
        start_time = timezone.now()  # 처리 시작 시간 기록

        # 데이터 조회를 위한 자식 스팬 생성
        with tracer.start_as_current_span("fetch_data", kind=SpanKind.INTERNAL) as child_span:
            data = board.objects.all().values('seq', 'title', 'content', 'create_date', 'update_date')
            data_list = list(data)  # 데이터 리스트 생성
            num_items = len(data_list)
            child_span.set_attribute("num_items", num_items)

        end_time = timezone.now()  # 처리 종료 시간 기록
        duration = (end_time - start_time).total_seconds() * 1000  # 처리 시간을 밀리초 단위로 변환

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.status_code", 200)
        span.set_attribute("operation_duration_ms", duration)

        # 성공 메시지와 함께 처리 시간 반환
        return HttpResponse(f"Successfully fetched {num_items} data in {duration:.2f} ms", status=200)

def log_data(request):
    # Spankind.Server: 서버가 클라이언트의 요청을 받고 처리하는 전체 과정
    with tracer.start_as_current_span("log_data", kind=SpanKind.SERVER) as span:

        data = log_api.objects.all().values('seq',
                                            'user_ip',
                                            'user_id',
                                            'start_time',
                                            'end_time',
                                            'call_url',
                                            'call_url_parameter')
        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.status_code", 200)

        return JsonResponse(list(data), safe=False)


def home(request):
    return HttpResponse("Welcome to my Django app!")
