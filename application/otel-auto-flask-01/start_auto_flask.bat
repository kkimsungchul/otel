@echo off
REM Create a virtual environment
echo ## python -m venv venv
python -m venv venv

REM Activate the virtual environment
echo ## call venv\Scripts\activate
call venv\Scripts\activate

REM Install the requirements
REM echo ## pip install -r requirements.txt
REM pip install -r requirements.txt

REM Install the requirements
echo ## opentelemetry-bootstrap -a install
opentelemetry-bootstrap -a install

REM Run the flask development server with logs_exporter
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true

REM Run the Flask application with OpenTelemetry instrumentation
echo ## opentelemetry-instrument --log_level info --traces_exporter otlp --metrics_exporter otlp --metric_export_interval 500 --logs_exporter otlp --service_name otel-auto-flask-01 --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc python app.py --port=5001
opentelemetry-instrument --log_level info --traces_exporter otlp --metrics_exporter otlp --metric_export_interval 500 --logs_exporter otlp --service_name otel-auto-flask-01 --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc python app.py --port=5001

REM Deactivate the virtual environment
echo ## call venv\Scripts\deactivate
REM call venv\Scripts\deactivate