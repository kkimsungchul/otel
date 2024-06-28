@echo off
REM Create a virtual environment
echo ## python -m venv venv
python -m venv venv

REM Activate the virtual environment
echo ## call venv\Scripts\activate
call venv\Scripts\activate

REM Install the requirements
echo ## pip install -r requirements.txt
pip install -r requirements.txt

REM Run the flask development server with logs_exporter
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
set OTEL_INSTRUMENTATION_HTTP_CAPTURE_HEADERS_SERVER_REQUEST="content-type,custom_request_header"

REM Run the Flask application with OpenTelemetry instrumentation
echo ## python app.py
python app.py

REM Deactivate the virtual environment
echo ## call venv\Scripts\deactivate
REM call venv\Scripts\deactivate