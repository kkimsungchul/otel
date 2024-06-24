@echo off
REM Create a virtual environment
echo ## python -m venv venv
python -m venv venv

REM Activate the virtual environment
echo ## call venv\Scripts\activate
call venv\Scripts\activate

REM Install the waitress
echo ## pip install waitress
pip install waitress

REM Install the requirements
echo ## pip install -r requirements.txt
pip install -r requirements.txt

REM Install the requirements
echo ## opentelemetry-bootstrap -a install
opentelemetry-bootstrap -a install

REM Make migrations for the otelautodjango01 app
echo ## python manage.py makemigrations otelautodjango01
python manage.py makemigrations otelautodjango01

REM Apply the migrations for the otelautodjango01 app
echo ## python manage.py migrate otelautodjango01
python manage.py migrate otelautodjango01

REM Custom command to handle boards
echo ## python manage.py boards
python manage.py boards

REM set DJANGO_SETTINGS_MODULE=otelautodjango01.settings

REM Run the Django development server
REM echo ## set DJANGO_SETTINGS_MODULE=otelautodjango01.settings
set DJANGO_SETTINGS_MODULE=otelautodjango01.settings

REM Run the Django development server with logs_exporter
REM echo ## OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true

REM Run the Django with waitress
echo ## opentelemetry-instrument --log_level info --traces_exporter otlp --metrics_exporter otlp --metric_export_interval 500 --logs_exporter otlp --service_name otel-auto-django-01 --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc waitress-serve --port=8001 otelautodjango01.wsgi:application
opentelemetry-instrument --log_level info --traces_exporter otlp --metrics_exporter otlp --metric_export_interval 500 --logs_exporter otlp --service_name otel-auto-django-01 --exporter_otlp_endpoint http://127.0.0.1:9999 --exporter_otlp_protocol=grpc waitress-serve --port=8001 otelautodjango01.wsgi:application

REM Deactivate the virtual environment
echo ## call venv\Scripts\deactivate
REM call venv\Scripts\deactivate