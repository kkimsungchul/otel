# OpenTelemetry 테스트 환경 구성하기

## Windows
### 서버 환경
- windows 10
- java 17
- python 3.12

### 프로젝트 내려 받기
```
git clone https://github.com/kkimsungchul/otel.git
```

### 시각화 툴 설치 및 실행
- [Tool] 시각화 툴 사용법_v1.0.md 파일 참고

### 오픈텔레메트리 콜렉터 설치 및 실행
- [Otel] otel-collector-설치-및-사용.md 파일 참고

### 어플리케이션 실행 (Rolldice)
- OpenTelemetry 공식 사이트에 있는 Getting-started 예제를 사용해서 어플리케이션 실행
- https://opentelemetry.io/docs/languages/java/getting-started/
- https://opentelemetry.io/docs/languages/python/getting-started/
### 자바 어플리케이션 실행 - Agent
```
cd C:\otel\otel-java-agent
```
```
set JAVA_TOOL_OPTIONS=-javaagent:C:\otel\otel-java-agent\opentelemetry-javaagent.jar
set OTEL_METRIC_EXPORT_INTERVAL=1000
set OTEL_TRACES_EXPORTER=otlp
set OTEL_METRICS_EXPORTER=otlp
set OTEL_LOGS_EXPORTER=otlp
set OTEL_SERVICE_NAME =Springboot-agent-test
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
java -jar otel-test-springboot.jar
```
```
http://localhost:8080
```
### 자바 어플리케이션 실행 - SDK
```
cd C:\otel\otel-java-agent
java -jar otel-sdk-test-springboot.jar
```

### 파이썬 어플리케이션 실행 - SDK
```
cd C:\otel\otel-test-pytyon-run
python -m venv venv
venv\Scripts\activate
```
```
pip install opentelemetry-distro
opentelemetry-bootstrap -a install
pip install opentelemetry-exporter-otlp
pip install opentelemetry-exporter-otlp-proto-grpc
pip install opentelemetry-api
pip install opentelemetry-sdk
pip install opentelemetry-instrumentation-flask
pip install opentelemetry-exporter-prometheus
```
```
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument --service_name=python-roll-dice-service --metric_export_interval=1000 --traces_exporter=otlp --metrics_exporter=otlp --logs_exporter=otlp --exporter_otlp_endpoint=http://127.0.0.1:4317 --exporter_otlp_traces_endpoint=http://127.0.0.1:4317 --exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 --exporter_otlp_logs_endpoint=http://127.0.0.1:4317 --exporter_otlp_protocol=grpc flask run -p 18080
```

### 어플리케이션 접속
- SpringBoot(agent) 환경 접속: http://localhost:8080/java/rolldice
- SpringBoot(sdk) 환경 접속: http://localhost:19090/java-custom/rolldice?rolls=12
- python(sdk) 환경 접속: http://localhost:18080/python/rolldice

### 어플리케이션 실행 ()

## Mac
