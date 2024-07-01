@echo off
start cmd /k "cd ./nginx && start_nginx.bat"
start cmd /k "cd ./otel-collector && start_otelcol.bat"
start cmd /k "cd ./loki && start_loki.bat"
start cmd /k "cd ./otel-sdk-flask-01 && start_sdk_flask.bat"
start cmd /k "cd ./otel-auto-flask-01 && start_auto_flask.bat"
