## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치 (python 3.12)
### 개별 패키지 설치를 통한 환경 설정
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install Flask
pip install Flask-SQLAlchemy Flask-Migrate
pip install opentelemetry-api opentelemetry-sdk opentelemetry-instrumentation-flask opentelemetry-exporter-otlp
pip install pytz
pip install psutil
```
### requirements.txt 파일을 사용하여 의존성 관리
```
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install -r requirements.txt
python app.py
```

## nginx 설정
### hosts 파일 수정
- 192.168.0.52 java.sdk.otel-app.co.kr
- 127.0.0.1 python.sdk.otel-app.co.kr

### confing 파일 수정
- C:\Users\HP\Downloads\nginx-1.26.1\conf\nginx.conf

## 프로세스 흐름

---
1. 사용자(Client)가 Request를 보냄, Flask의 라우팅 시스템이 요청된 URL을 분석하여 해당하는 View 함수를 매핑, 이 과정에서 OpenTelemetry를 통해 요청을 추적하는 첫 번째 스팬 생성
2. 결정된 View 함수에서 비즈니스 로직 처리
3. SQLAlchemy를 통해 Model과 상호작용하며 DB 작업 수행
4. 모든 요청과 응답의 로깅 및 메트릭을 수집
5. OpenTelemetry를 통해 이 데이터를 외부 시스템(OTLP Collector)으로 전송
6. 사용자에게 Response 전송

## API 설명

---
- 게시글 선택조회 및 로그 저장: localhost:5000/board/{가져올 게시글 개수} 
- 게시글 전체조회: localhost:5000/board
- 로그 조회: localhost:5000/log

## 작업순서

---

## models.py
* SQLAlchemy를 이용하여 sqlite3의 DB 스키마 생성
``` python
class LogAPI(db.Model):
    __tablename__ = 'log_api'
    seq = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_ip = db.Column(db.String(15))
    user_id = db.Column(db.String(30))
    start_time = db.Column(db.DateTime)
    end_time = db.Column(db.DateTime)
    call_url = db.Column(db.String(100))
    call_url_parameter = db.Column(db.String(300))

class Board(db.Model):
    __tablename__ = 'board'
    seq = db.Column(db.Integer, primary_key=True, autoincrement=True)
    title = db.Column(db.String(30))
    content = db.Column(db.String(2000))
    create_date = db.Column(db.DateTime)
    update_date = db.Column(db.DateTime)

```
## app.py
- 수집 주기 설정
``` python
# 환경 변수 설정
os.environ['OTEL_METRIC_EXPORT_INTERVAL'] = '500'
```

### Resource 설정
  - service name, instance id, job 설정 가능 
  - 기존: unknownjob 현재: flask-service
``` python
# 리소스 설정: 여기에서 'job' 라벨을 지정합니다.
# OpenTelemetry 설정
resource = Resource(attributes={
    "service.name": "otel-sdk-flask-01",
    "service.instance.id": prefix+"Flask-Testapplication",
    "job": prefix+"flask-service"
})
```

### Logger 설정
``` python
# Logger provider 설정
logger_provider = LoggerProvider(resource=resource)
set_logger_provider(logger_provider)

# Log Exporter 설정
logger_exporter = OTLPLogExporter(endpoint="127.0.0.1:9999", insecure=True)
logger_provider.add_log_record_processor(BatchLogRecordProcessor(logger_exporter))

handler = LoggingHandler(level=logging.INFO, logger_provider=logger_provider)
```

### Tracer 설정
``` python
# Span Exporter 설정
span_exporter = OTLPSpanExporter(endpoint="127.0.0.1:9999", insecure=True)
span_processor = BatchSpanProcessor(span_exporter)
trace.set_tracer_provider(TracerProvider(resource=resource))
trace.get_tracer_provider().add_span_processor(span_processor)
tracer = trace.get_tracer(__name__)

```
### Metric 설정
``` python
# Metric Exporter 설정
metric_exporter = OTLPMetricExporter(endpoint="127.0.0.1:9999", insecure=True)
metric_reader = PeriodicExportingMetricReader(metric_exporter)
meter_provider = MeterProvider(resource=resource, metric_readers=[metric_reader])
metrics.set_meter_provider(meter_provider)
meter = metrics.get_meter(__name__, version="0.1")
```
---

### CPU와 메모리 사용량 측정
* 오픈텔레메트리에서 게이지 메트릭에 제공되는 Observation 라이브러리 사용
* cpu사용량과 ram 사용량을 수집하는 콜백 함수를 만들어서 각 cpu 코어의 사용률과 ram 사용률 실시간 업데이트
``` python
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
```

# Opentelemetry Provider & Exporter
```
Provider는 OpenTelemetry에서 데이터를 생성하고 관리하는 주체입니다. 데이터는 스팬(트레이싱), 메트릭, 로그 등이 있으며, 이 데이터를 적절하게 처리하여 OpenTelemetry의 다양한 컴포넌트로 전달하는 역할을 합니다. 다음과 같은 프로바이더들이 있습니다:

TracerProvider: 트레이싱 데이터를 관리하며, 스팬을 생성하고 종료하는 데 사용됩니다.
MeterProvider: 메트릭 데이터를 생성하고 관리합니다.
LoggerProvider: 로깅 데이터의 생성과 관리를 담당합니다.
각 프로바이더는 데이터를 생성하고, 필요한 설정을 적용하여 데이터의 생명 주기를 관리합니다. 예를 들어, TracerProvider는 트레이싱 컨텍스트를 유지하고, 새로운 스팬을 시작하거나 종료하는 기능을 제공합니다.
```
### Exporter
```
Exporter는 OpenTelemetry에서 수집된 데이터를 외부 시스템이나 서비스로 전송하는 역할을 합니다. 이는 데이터를 시각화하거나 분석할 수 있는 다양한 백엔드 시스템(예: Jaeger, Prometheus, 로그 서버 등)으로 데이터를 내보내는 기능을 담당합니다. Exporter는 다음과 같이 구분됩니다:

Span Exporter: 트레이싱 데이터를 받아서 설정된 백엔드(예: Jaeger, Zipkin 등)로 전송합니다.
Metric Exporter: 수집된 메트릭 데이터를 백엔드 시스템(예: Prometheus)으로 보냅니다.
Log Exporter: 로그 데이터를 외부 로그 관리 시스템으로 전송합니다.
Exporter는 통합된 백엔드 시스템에 맞춰 데이터를 적절한 형식으로 변환하고, 네트워크를 통해 데이터를 전송하는 역할을 수행합니다. 데이터는 주기적으로 또는 이벤트 발생 시 내보내질 수 있습니다.
```