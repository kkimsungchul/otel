import logging
import os
from flask import Flask
from models import db
from views import main_blueprint

from opentelemetry import trace, metrics
from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
from opentelemetry._logs import set_logger_provider
from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.sdk.resources import Resource
from opentelemetry.instrumentation.flask import FlaskInstrumentor
import psutil
from opentelemetry.metrics import CallbackOptions, Observation

os.environ['OTEL_METRIC_EXPORT_INTERVAL'] = '500'

prefix = "sdk_fls_"

def create_app():
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///example.db'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    db.init_app(app)
    app.register_blueprint(main_blueprint)

    # OpenTelemetry 설정
    resource = Resource(attributes={
        "service.name": "otel-sdk-flask-01",
        "service.instance.id": prefix+"Flask-Testapplication",
        "job": prefix+"flask-service"
    })

    # Logger provider 설정
    logger_provider = LoggerProvider(resource=resource)
    set_logger_provider(logger_provider)

    # Log Exporter 설정
    logger_exporter = OTLPLogExporter(endpoint="127.0.0.1:9999", insecure=True)
    logger_provider.add_log_record_processor(BatchLogRecordProcessor(logger_exporter))

    handler = LoggingHandler(level=logging.INFO, logger_provider=logger_provider)
    logging.getLogger().addHandler(handler)

    # Span Exporter 설정
    span_exporter = OTLPSpanExporter(endpoint="127.0.0.1:9999", insecure=True)
    span_processor = BatchSpanProcessor(span_exporter)
    trace.set_tracer_provider(TracerProvider(resource=resource))
    trace.get_tracer_provider().add_span_processor(span_processor)
    tracer = trace.get_tracer(__name__)

    # Metric Exporter 설정
    metric_exporter = OTLPMetricExporter(endpoint="127.0.0.1:9999", insecure=True)
    metric_reader = PeriodicExportingMetricReader(metric_exporter)
    meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])
    metrics.set_meter_provider(meter_provider)
    meter = metrics.get_meter(__name__, version="0.1")

    # CPU 사용량 콜백 함수
    def get_cpu_usage_callback(_: CallbackOptions):
        for (number, percent) in enumerate(psutil.cpu_percent(percpu=True)):
            attributes = {"cpu_number": str(number)}
            yield Observation(percent, attributes)

    # RAM 사용량 콜백 함수
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

    # Flask 애플리케이션 계측
    FlaskInstrumentor().instrument_app(app)

    return app

if __name__ == '__main__':
    app = create_app()
    app.run(debug=True)
