@echo off
start cmd /k "cd ./nginx && run_nginx.bat"
start cmd /k "cd ./otel-collector && run_otelcol.bat"
start cmd /k "cd ./otel-sdk-django-01 && run_django.bat"
start cmd /k "cd ./otel-sdk-springboot-01 && run_springboot.bat"