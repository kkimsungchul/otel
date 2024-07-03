@echo off
start cmd /k "cd ../infrastructure/nginx && start_nginx.bat"
start cmd /k "cd ../infrastructure/otel-collector && start_otelcol.bat"
start cmd /k "cd ../infrastructure/loki && start_loki.bat"
start cmd /k "cd ./otel-auto-springboot-01 && start_auto_springboot.bat"