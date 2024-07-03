@echo off
start cmd /k "cd ./nginx && start_nginx.bat"
start cmd /k "cd ./otel-collector && start_otelcol.bat"
start cmd /k "cd ./loki && start_loki.bat"
start cmd /k "cd ./otel-sdk-django-01 && start_django.bat"
start cmd /k "cd ./otel-auto-django-01 && start_auto_django.bat"
