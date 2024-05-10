# otel collector 설치 및 사용

---
## 목차
- [otel collector란](#otel-collector란)
- [참고 이미지](#참고-이미지)
- [collector 설치](#collector-설치)
- [설정 가이드](#설정-가이드)
- [customconfig.yaml 파일 생성](#customconfig.yaml-파일-생성)
- [customconfig.yaml 파일 검증](#customconfig.yaml-파일 검증)
- [collector 실행](#collector-실행)

---
## otel collector란
- 각각의 어플리케이션에서 보내주는 정보를 수집
- 수집하여 파일로 저장하거나 모니터링 툴로 데이터를 보낼 수 있음

## 참고 이미지 
- otel-collector-도식도.png

## collector 설치
- https://opentelemetry.io/docs/collector/installation/
- https://github.com/open-telemetry/opentelemetry-collector-releases/releases/tag/v0.100.0
- 윈도우용 다운로드 <br>
https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_windows_386.tar.gz

## 설정 가이드
- https://opentelemetry.io/docs/collector/configuration/

## customconfig.yaml 파일 생성
- https://opentelemetry.io/docs/collector/configuration/

```yaml
receivers:
  otlp: # the OTLP receiver the app is sending logs to
    protocols:
      grpc:
        endpoint: 127.0.0.1:4317
      http:
        endpoint: 127.0.0.1:4318
processors:
  batch:

exporters:
  logging :
    verbosity: detailed 
  file: # the File Exporter, to ingest logs to local file
    path: example.log
    rotation:

service:
  pipelines:
    logs/dev:
      receivers: [otlp]
      processors: [batch]
      exporters: [file]
    traces:
      receivers: [otlp]
      exporters: [file]
      processors: [batch]
    metrics:
      receivers: [otlp]
      exporters: [file]
      processors: [batch]
```

## customconfig.yaml 파일 검증
	otelcol validate --config=customconfig.yaml

## collector 실행
	otelcol --config=customconfig.yaml

