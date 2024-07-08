## 개발환경
- python 3.12
- sqlite3 database

## API 설명
- 로그 조회 : localhost:5001/log
- 게시글 전체 조회 : localhost:5001/board
- 게시글 수량 지정 조회 및 저장 : localhost:5001/board/{count}
- 오류 발생: localhost:5001/user

## 가상환경 생성 및 라이브러리 설치 (python 3.12)

### 1. 개별 패키지 설치를 통한 환경 설정 (flask)

``` 
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
python.exe -m pip install --upgrade pip
pip install flask
pip install Flask-SQLAlchemy Flask-Migrate
```

### 2. 개별 패키지 설치를 통한 환경 설정 (opentelementry)
```
pip install opentelemetry-api
pip install opentelemetry-sdk
pip install opentelemetry-instrumentation
opentelemetry-bootstrap -a install
```

### 3. 실행 명령어
- 실행 시 명령어에 원하는 설정을 입력
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
set OTEL_INSTRUMENTATION_HTTP_CAPTURE_HEADERS_SERVER_REQUEST="content-type,custom_request_header"
opentelemetry-instrument --log_level info --traces_exporter otlp --metrics_exporter otlp --metric_export_interval 500 --logs_exporter otlp --service_name otel-auto-flask-01 --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc python app.py --port=5001

- 여러줄로 확인
```text
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
--exporter_otlp_protocol=grpc python app.py --port=5001
```

### 실행 명령어 내용
1. OpenTelemetry 자동 계측 사용
2. 로그 레벨: info
3. 트레이스, 메트릭스, 로그를 OTLP(OpenTelemetry Protocol) 익스포터로 내보냄
4. 메트릭 export 간격: 500ms
5. 서비스 이름: 'otel-auto-flask-01'
6. OTLP 엔드포인트: http://127.0.0.1:9999
7. OTLP 프로토콜: gRPC
8. Flask 앱은 포트 5001에서 실행

- 적용 가능 옵션
```text
[--logs_exporter LOGS_EXPORTER] 
[--metrics_exporter METRICS_EXPORTER] [--propagators PROPAGATORS] [--context CONTEXT]
[--id_generator ID_GENERATOR] [--meter_provider METER_PROVIDER] [--tracer_provider TRACER_PROVIDER] [--traces_exporter TRACES_EXPORTER]
[--configurator CONFIGURATOR] [--disabled_instrumentations DISABLED_INSTRUMENTATIONS] [--distro DISTRO]
[--attribute_count_limit ATTRIBUTE_COUNT_LIMIT] [--attribute_value_length_limit ATTRIBUTE_VALUE_LENGTH_LIMIT]
[--blrp_export_timeout BLRP_EXPORT_TIMEOUT] [--blrp_max_export_batch_size BLRP_MAX_EXPORT_BATCH_SIZE]
[--blrp_max_queue_size BLRP_MAX_QUEUE_SIZE] [--blrp_schedule_delay BLRP_SCHEDULE_DELAY] [--bsp_export_timeout BSP_EXPORT_TIMEOUT]
[--bsp_max_export_batch_size BSP_MAX_EXPORT_BATCH_SIZE] [--bsp_max_queue_size BSP_MAX_QUEUE_SIZE]
[--bsp_schedule_delay BSP_SCHEDULE_DELAY] [--event_attribute_count_limit EVENT_ATTRIBUTE_COUNT_LIMIT]
[--experimental_resource_detectors EXPERIMENTAL_RESOURCE_DETECTORS] [--exporter_jaeger_agent_host EXPORTER_JAEGER_AGENT_HOST]
[--exporter_jaeger_agent_port EXPORTER_JAEGER_AGENT_PORT]
[--exporter_jaeger_agent_split_oversized_batches EXPORTER_JAEGER_AGENT_SPLIT_OVERSIZED_BATCHES]
[--exporter_jaeger_certificate EXPORTER_JAEGER_CERTIFICATE] [--exporter_jaeger_endpoint EXPORTER_JAEGER_ENDPOINT]
[--exporter_jaeger_grpc_insecure EXPORTER_JAEGER_GRPC_INSECURE] [--exporter_jaeger_password EXPORTER_JAEGER_PASSWORD]
[--exporter_jaeger_timeout EXPORTER_JAEGER_TIMEOUT] [--exporter_jaeger_user EXPORTER_JAEGER_USER]
[--exporter_otlp_certificate EXPORTER_OTLP_CERTIFICATE] [--exporter_otlp_compression EXPORTER_OTLP_COMPRESSION]
[--exporter_otlp_endpoint EXPORTER_OTLP_ENDPOINT] [--exporter_otlp_headers EXPORTER_OTLP_HEADERS]
[--exporter_otlp_insecure EXPORTER_OTLP_INSECURE] [--exporter_otlp_logs_certificate EXPORTER_OTLP_LOGS_CERTIFICATE]
[--exporter_otlp_logs_compression EXPORTER_OTLP_LOGS_COMPRESSION] [--exporter_otlp_logs_endpoint EXPORTER_OTLP_LOGS_ENDPOINT]
[--exporter_otlp_logs_headers EXPORTER_OTLP_LOGS_HEADERS] [--exporter_otlp_logs_insecure EXPORTER_OTLP_LOGS_INSECURE]
[--exporter_otlp_logs_protocol EXPORTER_OTLP_LOGS_PROTOCOL] [--exporter_otlp_logs_timeout EXPORTER_OTLP_LOGS_TIMEOUT]
[--exporter_otlp_metrics_certificate EXPORTER_OTLP_METRICS_CERTIFICATE]
[--exporter_otlp_metrics_client_certificate EXPORTER_OTLP_METRICS_CLIENT_CERTIFICATE]
[--exporter_otlp_metrics_client_key EXPORTER_OTLP_METRICS_CLIENT_KEY]
[--exporter_otlp_metrics_compression EXPORTER_OTLP_METRICS_COMPRESSION]
[--exporter_otlp_metrics_default_histogram_aggregation EXPORTER_OTLP_METRICS_DEFAULT_HISTOGRAM_AGGREGATION]
[--exporter_otlp_metrics_endpoint EXPORTER_OTLP_METRICS_ENDPOINT] [--exporter_otlp_metrics_headers EXPORTER_OTLP_METRICS_HEADERS]
[--exporter_otlp_metrics_insecure EXPORTER_OTLP_METRICS_INSECURE] [--exporter_otlp_metrics_protocol EXPORTER_OTLP_METRICS_PROTOCOL]
[--exporter_otlp_metrics_temporality_preference EXPORTER_OTLP_METRICS_TEMPORALITY_PREFERENCE]
[--exporter_otlp_metrics_timeout EXPORTER_OTLP_METRICS_TIMEOUT] [--exporter_otlp_protocol EXPORTER_OTLP_PROTOCOL]
[--exporter_otlp_timeout EXPORTER_OTLP_TIMEOUT] [--exporter_otlp_traces_certificate EXPORTER_OTLP_TRACES_CERTIFICATE]
[--exporter_otlp_traces_compression EXPORTER_OTLP_TRACES_COMPRESSION] [--exporter_otlp_traces_endpoint EXPORTER_OTLP_TRACES_ENDPOINT]
[--exporter_otlp_traces_headers EXPORTER_OTLP_TRACES_HEADERS] [--exporter_otlp_traces_insecure EXPORTER_OTLP_TRACES_INSECURE]
[--exporter_otlp_traces_protocol EXPORTER_OTLP_TRACES_PROTOCOL] [--exporter_otlp_traces_timeout EXPORTER_OTLP_TRACES_TIMEOUT]
[--exporter_prometheus_host EXPORTER_PROMETHEUS_HOST] [--exporter_prometheus_port EXPORTER_PROMETHEUS_PORT]
[--exporter_zipkin_endpoint EXPORTER_ZIPKIN_ENDPOINT] [--exporter_zipkin_timeout EXPORTER_ZIPKIN_TIMEOUT]
[--link_attribute_count_limit LINK_ATTRIBUTE_COUNT_LIMIT] [--log_level LOG_LEVEL] [--metrics_exemplar_filter METRICS_EXEMPLAR_FILTER]
[--metric_export_interval METRIC_EXPORT_INTERVAL] [--metric_export_timeout METRIC_EXPORT_TIMEOUT]
[--experimental_disable_prometheus_unit_normalization EXPERIMENTAL_DISABLE_PROMETHEUS_UNIT_NORMALIZATION]
[--resource_attributes RESOURCE_ATTRIBUTES] [--sdk_disabled SDK_DISABLED] [--service_name SERVICE_NAME]
[--span_attribute_count_limit SPAN_ATTRIBUTE_COUNT_LIMIT] [--span_attribute_value_length_limit SPAN_ATTRIBUTE_VALUE_LENGTH_LIMIT]
[--span_event_count_limit SPAN_EVENT_COUNT_LIMIT] [--span_link_count_limit SPAN_LINK_COUNT_LIMIT] [--traces_sampler TRACES_SAMPLER]
[--traces_sampler_arg TRACES_SAMPLER_ARG] [--version]
```

## Opentelemetry 계측 내용
a. GET /board/int:num_items
- Trace: API 호출의 전체 실행 시간 측정
- Metrics: 요청 수, 응답 시간 등
- Log: API 호출 정보, 클라이언트 IP, 요청된 count 수 등

b. GET /board
- Trace: 전체 게시글을 가져오는 작업의 실행 시간을 측정
- Metrics: 총 게시글 수, 응답 시간 등
- Log: API 호출 정보와 반환된 게시글 수 등

c. GET /log
- Trace: 로그 데이터를 가져오는 작업의 실행 시간
- Metrics: API 호출 정보, 응답 시간 등 측정
- Log: API 호출 정보

d. GET /user
- Trace: 오류 발생 지점 및 실행 시간
- Metrics: 오류 발생 횟수
- Log: 발생한 Exception 정보


---
## Data end point
- Span(trace) Data
    - Application -> OpenTelemetry collector -> Jaeger

- Metric
    - Application -> OpenTelemetry collector ->  Prometheus

- log
    - Application -> OpenTelemetry collector -> file & loki
	
---
## 기타 참고 링크

### Python zero-code
- https://opentelemetry.io/docs/zero-code/python/example/
