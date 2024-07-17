# OpenTelemetry 테스트 환경 구성하기


---
### 서버 환경
- Windows 10
- java 17
- python 3.12
---
### 프로젝트 내려 받기
```
git clone https://github.com/kkimsungchul/otel.git
```
---
### 시각화 툴 설치 및 실행
- [Tool] 모니터링_툴_사용법_v1.0.md 파일 참고
---
### 오픈텔레메트리 콜렉터 설치 및 실행
- [Otel] otel-collector-설치-및-사용.md 파일 참고
---
### 어플리케이션 실행
- SpringBoot(agent)
```
cd .\build\libs
set JAVA_TOOL_OPTIONS=-javaagent:..\..\opentelemetry-javaagent.jar
set OTEL_METRIC_EXPORT_INTERVAL=1000
set OTEL_TRACES_EXPORTER=otlp
set OTEL_METRICS_EXPORTER=otlp
set OTEL_LOGS_EXPORTER=otlp
set OTEL_SERVICE_NAME=otel-agent-springboot-01
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
java -jar otel-agent-springboot-01-0.0.1-SNAPSHOT.jar
```
---
- SpringBoot(auto)
```
cd .\build\libs
java -jar otel-auto-springboot-01-project-0.0.1-SNAPSHOT.jar
```
---
- SpringBoot(sdk)
```
cd .\build\libs
java -jar otel-sdk-springboot-01-project-0.0.1-SNAPSHOT.jar
```
---
- Flask(auto)
```
venv\Scripts\activate
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
set OTEL_INSTRUMENTATION_HTTP_CAPTURE_HEADERS_SERVER_REQUEST="content-type,custom_request_header"
opentelemetry-instrument
 --log_level info
 --traces_exporter otlp
 --metrics_exporter otlp
 --metric_export_interval 500
 --logs_exporter otlp
 --service_name otel-auto-flask-01
 --exporter_otlp_endpoint http://127.0.0.1:9999
 --exporter_otlp_protocol=grpc python app.py
 --port=5001
```
---
- Flask(sdk)
```
venv\Scripts\activate
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
set OTEL_INSTRUMENTATION_HTTP_CAPTURE_HEADERS_SERVER_REQUEST="content-type,custom_request_header"
python app.py
```
---
- Django(auto)
```
venv\Scripts\activate
set DJANGO_SETTINGS_MODULE=otelautodjango01.settings
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument
 --log_level info
 --traces_exporter otlp
 --metrics_exporter otlp
 --metric_export_interval 500
 --logs_exporter otlp
 --service_name otel-auto-django-01
 --exporter_otlp_endpoint http://127.0.0.1:9999
 --exporter_otlp_protocol=grpc
 -- waitress-serve
 --port=8001 otelautodjango01.wsgi:application
```
---
- Django(sdk)
```
venv\Scripts\activate
python manage.py runserver
```
---
### 어플리케이션 접속
- SpringBoot(agent) 환경 접속: http://localhost:5050/board/10
- SpringBoot(auto) 환경 접속: http://localhost:6060/board/10
- SpringBoot(sdk) 환경 접속: http://localhost:8080/board/10
- Flask(auto) 환경 접속: http://localhost:5001/board/10
- Flask(sdk) 환경 접속: http://localhost:5000/board/10
- Django(auto) 환경 접속: http://localhost:8001/board/10
- Django(sdk) 환경 접속: http://localhost:8000/board/10
