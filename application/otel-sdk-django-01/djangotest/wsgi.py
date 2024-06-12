import os
from django.core.wsgi import get_wsgi_application
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.instrumentation.django import DjangoInstrumentor

from opentelemetry import metrics
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'djangotest.settings')

application = get_wsgi_application()

# OpenTelemetry 설정
trace.set_tracer_provider(TracerProvider())
tracer_provider = trace.get_tracer_provider()

# Span Exporter 설정
span_exporter = OTLPSpanExporter(endpoint=os.getenv('OTLP_GRPC_ENDPOINT', '127.0.0.1:4317'))
span_processor = BatchSpanProcessor(span_exporter)
tracer_provider.add_span_processor(span_processor)

# Metric Exporter 설정
metric_exporter = OTLPMetricExporter(endpoint=os.getenv('OTLP_GRPC_ENDPOINT', '127.0.0.1:4317'))
metric_reader = PeriodicExportingMetricReader(metric_exporter)
meter_provider = MeterProvider(metric_readers=[metric_reader])

metrics.set_meter_provider(meter_provider)
meter = metrics.get_meter("my.meter.name")

# Django에 OpenTelemetry Instrumentation 적용
DjangoInstrumentor().instrument()