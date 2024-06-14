@echo off
start cmd /k "cd ./nginx && start_nginx.bat"
start cmd /k "cd ./otel-collector && start_otelcol.bat"
start cmd /k "cd ./otel-sdk-django-01 && start_django.bat"
start cmd /k "cd ./otel-sdk-springboot-01 && start_springboot.bat"