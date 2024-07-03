@echo off
start /d "C:/jaeger-1.58.0-windows-amd64/" /b jaeger-all-in-one.exe
start /d "C:/prometheus-2.52.0.windows-amd64/" /b prometheus.exe
start /d "C:/grafana-v11.0.0/bin/" /b grafana-server.exe