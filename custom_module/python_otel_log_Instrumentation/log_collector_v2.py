import psutil
import enum
import time
from pathlib import Path
from opentelemetry import metrics
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.exporter.otlp.proto.grpc.metric_exporter import OTLPMetricExporter
from opentelemetry.sdk._logs import LoggerProvider, LogRecord
from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
from opentelemetry.exporter.otlp.proto.grpc._log_exporter import OTLPLogExporter
from opentelemetry.sdk.resources import SERVICE_NAME, Resource
from opentelemetry.trace import INVALID_SPAN_ID, INVALID_TRACE_ID, TraceFlags

# SeverityNumber를 직접 정의
class SeverityNumber(enum.Enum):
    """Numerical value of severity.

    Smaller numerical values correspond to less severe events
    (such as debug events), larger numerical values correspond
    to more severe events (such as errors and critical events).

    See the `Log Data Model`_ spec for more info and how to map the
    severity from source format to OTLP Model.

    .. _Log Data Model: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/logs/data-model.md#field-severitynumber
    """

    UNSPECIFIED = 0
    TRACE = 1
    TRACE2 = 2
    TRACE3 = 3
    TRACE4 = 4
    DEBUG = 5
    DEBUG2 = 6
    DEBUG3 = 7
    DEBUG4 = 8
    INFO = 9
    INFO2 = 10
    INFO3 = 11
    INFO4 = 12
    WARN = 13
    WARN2 = 14
    WARN3 = 15
    WARN4 = 16
    ERROR = 17
    ERROR2 = 18
    ERROR3 = 19
    ERROR4 = 20
    FATAL = 21
    FATAL2 = 22
    FATAL3 = 23
    FATAL4 = 24



_STD_TO_OTEL = {
    10: SeverityNumber.DEBUG,
    11: SeverityNumber.DEBUG2,
    12: SeverityNumber.DEBUG3,
    13: SeverityNumber.DEBUG4,
    14: SeverityNumber.DEBUG4,
    15: SeverityNumber.DEBUG4,
    16: SeverityNumber.DEBUG4,
    17: SeverityNumber.DEBUG4,
    18: SeverityNumber.DEBUG4,
    19: SeverityNumber.DEBUG4,
    20: SeverityNumber.INFO,
    21: SeverityNumber.INFO2,
    22: SeverityNumber.INFO3,
    23: SeverityNumber.INFO4,
    24: SeverityNumber.INFO4,
    25: SeverityNumber.INFO4,
    26: SeverityNumber.INFO4,
    27: SeverityNumber.INFO4,
    28: SeverityNumber.INFO4,
    29: SeverityNumber.INFO4,
    30: SeverityNumber.WARN,
    31: SeverityNumber.WARN2,
    32: SeverityNumber.WARN3,
    33: SeverityNumber.WARN4,
    34: SeverityNumber.WARN4,
    35: SeverityNumber.WARN4,
    36: SeverityNumber.WARN4,
    37: SeverityNumber.WARN4,
    38: SeverityNumber.WARN4,
    39: SeverityNumber.WARN4,
    40: SeverityNumber.ERROR,
    41: SeverityNumber.ERROR2,
    42: SeverityNumber.ERROR3,
    43: SeverityNumber.ERROR4,
    44: SeverityNumber.ERROR4,
    45: SeverityNumber.ERROR4,
    46: SeverityNumber.ERROR4,
    47: SeverityNumber.ERROR4,
    48: SeverityNumber.ERROR4,
    49: SeverityNumber.ERROR4,
    50: SeverityNumber.FATAL,
    51: SeverityNumber.FATAL2,
    52: SeverityNumber.FATAL3,
    53: SeverityNumber.FATAL4,
}
# 리소스 설정
resource = Resource(attributes={
    SERVICE_NAME: "log-collector-service",
    "service.version": "1.0.0",
    "deployment.environment": "production"
})

# 로그 설정
logger_provider = LoggerProvider(resource=resource)
log_exporter = OTLPLogExporter(endpoint="http://localhost:9999")
log_processor = BatchLogRecordProcessor(log_exporter)
logger_provider.add_log_record_processor(log_processor)
logger = logger_provider.get_logger(__name__)

# 매트릭 설정
metric_exporter = OTLPMetricExporter(endpoint="http://localhost:9999")
# 매트릭 전송주기
reader = PeriodicExportingMetricReader(metric_exporter, export_interval_millis=1000)
meter_provider = MeterProvider(resource=resource, metric_readers=[reader])
metrics.set_meter_provider(meter_provider)
meter = metrics.get_meter(__name__)

cpu_gauge = meter.create_gauge("cpu_usage", unit="1", description="CPU usage")
memory_gauge = meter.create_gauge("memory_usage", unit="1", description="Memory usage")
disk_gauge = meter.create_gauge("disk_usage", unit="1", description="Disk usage")


def get_system_metrics():
    cpu_percent = psutil.cpu_percent(interval=1)
    memory = psutil.virtual_memory()
    disk = psutil.disk_usage('/')
    #print(cpu_percent)
    #print(memory.percent)
    #print(disk.percent)
    cpu_gauge.set(cpu_percent)
    memory_gauge.set(memory.percent)
    disk_gauge.set(disk.percent)

def tail_file(filepath):
    with open(filepath, 'r') as file:
        file.seek(0, 2)
        while True:
            line = file.readline()
            if not line:
                return  # 파일의 끝에 도달하면 함수 종료
            yield line

def main():
    log_file = Path(r"C:\ConvertedLogs\Application.log")
    
    print(f"Monitoring {log_file} and system metrics. Press Ctrl+C to stop.")
    
    while True:
        # 시스템 메트릭 수집
        get_system_metrics()
        
        # 로그 파일 모니터링
        for line in tail_file(log_file):
            log_record = LogRecord(
                timestamp=int(time.time() * 1_000_000_000),
                trace_id=INVALID_TRACE_ID,
                span_id=INVALID_SPAN_ID,
                trace_flags=TraceFlags(0),
                severity_text="INFO",
                severity_number=SeverityNumber.INFO,
                body=line.strip(),
                resource=resource
            )
            logger.emit(log_record)
        
        # 1초 쉬고 반복
        time.sleep(1)

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("Stopping log collection and metric export...")
    finally:
        logger_provider.shutdown()
        meter_provider.shutdown()