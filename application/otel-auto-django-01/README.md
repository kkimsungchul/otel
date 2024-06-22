## 개발환경
- python 3.12
- sqlite3 database

## 가상환경 생성 및 라이브러리 설치 (python 3.12)
### 1. 개별 패키지 설치를 통한 환경 설정 (django)
``` 
pip install virtualenv
virtualenv venv --python=python3.12
venv\Scripts\activate
pip install django
python.exe -m pip install --upgrade pip
python -m django startproject otelautodjango01
```
### 2. django migration
```
python manage.py makemigrations otelautodjango01
python manage.py migrate otelautodjango01
```
### 3. db 적재
```
python manage.py boards
```
### 3. 개별 패키지 설치를 통한 환경 설정 (opentelementry)
```
pip install opentelemetry-api
pip install opentelemetry-sdk
pip install opentelemetry-instrumentation
opentelemetry-bootstrap -a install
```
### 4. 실행 명령어
```
opentelemetry-instrument --traces_exporter console --metrics_exporter none python manage.py runserver
```



---
### waitress 로 실행
- opentelemetry-instrumentation-system-metrics 이거 있어야 CPU , Memory를 자동으로 보냄

python -m venv venv
venv\Scripts\activate
pip install --upgrade pip
pip install django
pip install opentelemetry-distro opentelemetry-exporter-otlp
pip install opentelemetry-instrumentation-system-metrics
pip install opentelemetry-instrumentation-django
opentelemetry-bootstrap -a install
pip install waitress
python.exe -m pip install --upgrade pip



python manage.py makemigrations otelautodjango01
python manage.py migrate otelautodjango01
python manage.py boards



set DJANGO_SETTINGS_MODULE=otelautodjango01.settings
opentelemetry-instrument --log_level info --traces_exporter console,otlp --metrics_exporter console,otlp --metric_export_interval 500 --logs_exporter console,otlp --service_name auto-python-service --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc waitress-serve --port=8001 otelautodjango01.wsgi:application

- 여러줄로 확인
```text
opentelemetry-instrument 
--log_level debug 
--traces_exporter console,otlp 
--metrics_exporter console,otlp 
--metric_export_interval 500 
--logs_exporter console,otlp 
--service_name otel-auto-django-01 
--exporter_otlp_endpoint http://127.0.0.1:9999 
--exporter_otlp_protocol=grpc 
waitress-serve --port=8000 otelautodjango01.wsgi:application
```

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