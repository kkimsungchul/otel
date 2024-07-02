import os
import sys
import psutil
import logging

from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
from pythonjsonlogger import jsonlogger

from django.core.wsgi import get_wsgi_application

from opentelemetry._logs import set_logger_provider
from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
from opentelemetry.sdk._logs.export import BatchLogRecordProcessor

from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter

from opentelemetry.trace import SpanKind
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader

from opentelemetry.sdk.resources import Resource

from opentelemetry.instrumentation.django import DjangoInstrumentor

from opentelemetry.metrics import (
    CallbackOptions,
    Observation,
)

from opentelemetry import metrics
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'otelsdkdjango01.settings')

# WSGI 애플리케이션 초기화
application = get_wsgi_application()
prefix = "sdk_djn_"


# 리소스 설정: 여기에서 'job' 라벨을 지정합니다.
resource = Resource.create({
    "service.name": "otel-sdk-django-01",
    "service.instance.id": prefix+"Django-Testapplication",
    "job": prefix+"django-service"  # 이렇게 job 이름을 설정할 수 있습니다.
})

# Logger provider: Opentelemetry 로깅 시스템에서 로그를 관리하는 컴포넌트
logger_provider = LoggerProvider(resource=resource)
set_logger_provider(logger_provider)

# Log Exporter 설정
# insecure=True: TLS를 사용하지 않고 데이터 전송
# BatchLogRecordProcessor: 로그 레코드를 배치로 처리하여 네트워크 사용 최적화
logger_exporter = OTLPLogExporter(insecure=True)
logger_provider.add_log_record_processor(BatchLogRecordProcessor(logger_exporter))

# LoggingHandler: 파이썬 표준 로깅 모듈과 opentelemetry 로깅 시스템 연결, 모든 로그 레벨(NOTSET) 로그 캡처
handler = LoggingHandler(level=logging.INFO, logger_provider=logger_provider)

# 파이썬의 루트 로거에 handler 추가, 모든 로그 이벤트가 opentelemetry 로깅 시스템을 통해 처리
logging.getLogger().addHandler(handler) # OpenTelemetry 설정


# Span Exporter 설정
# opentelemetry span 데이터를 otlp를 사용하여 지정된 엔드포인트로 전송
# endpoint: 데이터 전송할 서버의 주소와 포트 지정
span_exporter = OTLPSpanExporter(endpoint=os.getenv('OTLP_GRPC_ENDPOINT', '127.0.0.1:9999'))

# batchspanprocessor: 수집된 스팬 데이터를 일정량 또는 일정 시간 간격으로 배치 처리하여 설정된 exporter를 통해 전송
# 리소스 사용을 최적화하고 네트워크 호출의 효율성을 높임
span_processor = BatchSpanProcessor(span_exporter)

# Trace Exporter 설정
# 설정된 tracer_provider에 span_processor를 추가 -> tracer_provider가 생성하는 모든 스팬은
# span_processor를 통해 처리되어 exporter로 전송됨

trace.set_tracer_provider(TracerProvider())
trace.get_tracer_provider().add_span_processor(span_processor)
tracer = trace.get_tracer(__name__)

# 특정 작업을 나타내는 스팬 시작 및 다양한 속성 추가
with tracer.start_as_current_span("operation", kind=SpanKind.INTERNAL) as span:
    # 스팬에 속성 추가
    span.set_attribute(prefix+"hostname", "hostName")
    span.set_attribute(prefix+"ip-address", "ip")
    span.set_attribute(prefix+"region", "korea")
    span.set_attribute(prefix+"http.method", "httpMethod")
    span.set_attribute(prefix+"http.url", "httpUrl")
    span.set_attribute(prefix+"http.uri", "httpUri")
    span.set_attribute(prefix+"http.queryString", "queryString")
    span.set_attribute(prefix+"http.fullUrl", "fullUrl")
    logging.getLogger().error("This is a log message")

# Metric Exporter 설정
# 메트릭 데이터를 같은 엔드포인트로 전송, PeriodicExportingMetricReader를 사용하여 주기적으로 메트릭 수집 및 전송
metric_exporter = OTLPMetricExporter(endpoint=os.getenv('OTLP_GRPC_ENDPOINT', '127.0.0.1:9999'))
metric_reader = PeriodicExportingMetricReader(metric_exporter)
meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])

metrics.set_meter_provider(meter_provider)
meter = metrics.get_meter(__name__, version="0.1")

# CallBack function for Async Counter
# Callback to gather cpu usage
def get_cpu_usage_callback(_: CallbackOptions):
    for (number, percent) in enumerate(psutil.cpu_percent(percpu=True)):
        attributes = {"cpu_number": str(number)}
        yield Observation(percent, attributes)


# Callback to gather RAM memory usage
def get_ram_usage_callback(_: CallbackOptions):
    ram_percent = psutil.virtual_memory().percent
    yield Observation(ram_percent)


requests_counter = meter.create_counter(
    name=prefix+"requests",
    description="number of requests",
    unit="1"
)

requests_size = meter.create_histogram(
    name=prefix+"request_size_bytes",
    description="size of requests",
    unit="byte"
)


cpu_gauge = meter.create_observable_gauge(
    callbacks=[get_cpu_usage_callback],
    name=prefix+"cpu_percent",
    description="per-cpu usage",
    unit="1"
)

ram_gauge = meter.create_observable_gauge(
    callbacks=[get_ram_usage_callback],
    name=prefix+"ram_percent",
    description="RAM memory usage",
    unit="1",
)

# Django에 OpenTelemetry Instrumentation 적용
# Django 애플리케이션에 자동 추적 기능 추가
# -> Django에서 발생하는 요청 및 응답을 자동으로 추적하여 관련 스팬 생성

DjangoInstrumentor().instrument()



