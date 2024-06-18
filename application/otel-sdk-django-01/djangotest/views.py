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
    ip_address = request.META["HTTP_X_REAL_IP"]
    return ip_address


def get_data(request, num_items):
    with tracer.start_as_current_span("get_data", kind=SpanKind.SERVER) as span:
        # span.set_attribute("operation", "get_data")
        print('get_data_span')
        start_time = timezone.now()

        # 데이터 조회를 위한 자식 스팬 생성
        with tracer.start_as_current_span("fetch_data", kind=SpanKind.INTERNAL) as child_span:
            print('fetch_data_span')
            child_span.set_attribute("num_items", num_items)
            data = board.objects.all()[:num_items].values('seq',
                                                          'title',
                                                          'content',
                                                          'create_date',
                                                          'update_date')
            data_list = list(data)

        end_time = timezone.now()
        duration = (end_time - start_time).total_seconds() * 1000  # 밀리초 단위로 변환

        # 부모 스팬에 결과 속성 추가
        span.set_attribute("http.status_code", 200)
        span.set_attribute("operation_duration_ms", duration)
        print('parent/attribute')

        client_ip = get_client_ip(request)

        log_api.objects.create(
            user_ip=client_ip,
            user_id=get_random_string(30),
            start_time=start_time,
            end_time=end_time,
            call_url=request.path,
            call_url_parameter=num_items
        )

        request_duration.record(duration, {"method": request.method, "path": request.path})

        return JsonResponse(data_list, safe=False)  # 데이터를 JSON 형식으로 반환


def log_data(request):
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
        print('2parent/attribute')

        return JsonResponse(list(data), safe=False)


def home(request):
    return HttpResponse("Welcome to my Django app!")
