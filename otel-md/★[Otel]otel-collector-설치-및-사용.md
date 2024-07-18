# otel collector 설치 및 사용

---
## 목차
- [otel collector란](#otel-collector란)
- [참고 이미지](#참고-이미지)
- [collector 설치](#collector-설치)
- [설정 가이드](#설정-가이드)
- [customconfig.yaml 파일 생성](#customconfig.yaml-파일-생성)
- [customconfig.yaml 파일 검증](#customconfig.yaml-파일-검증)
- [collector 실행](#collector-실행)

---
## opentelemetry collector란
- OpenTelemetry Collector는 오픈소스 관측 도구로, 다양한 데이터 소스에서 수집된 메트릭, 로그, 트레이스 데이터를 처리하고 내보내는 역할을 합니다. 이 도구는 다양한 관측 데이터 수집, 처리 및 내보내기 작업을 중앙집중화하고 표준화하는 데 사용됩니다.
- 원격 측정 데이터를 수신(Receiver), 처리(Processor) 및 내보내기(Exporter)를 통해 다양한 백엔드와 통신할 수 있는 도구입니다.
- 각각의 어플리케이션에서 보내주는 정보를 수집하여 파일로 저장하거나 모니터링 툴로 데이터를 보낼 수 있습니다.


### 구성 요소
- Receivers: 다양한 소스와 프로토콜을 통해 데이터를 수집합니다. 예를 들어, Jaeger, Zipkin, Prometheus와 같은 다양한 트레이스 및 메트릭 수집기를 지원합니다.
- Processors: 수집된 데이터를 처리하는 중간 단계입니다. 예를 들어, 배치 처리, 필터링, 집계 등을 수행합니다.
- Exporters: 처리된 데이터를 다양한 백엔드로 내보내는 역할을 합니다. 예를 들어, Jaeger, Prometheus, Elasticsearch 등으로 데이터를 전송합니다.
- Extensions: 기본 데이터 수집, 처리, 내보내기 기능 이외에 추가적인 기능을 제공하는 모듈로 플러그인처럼 작동하여 Collector의 기능을 확장하거나 향상시킵니다.

## collector 설치
- 설치 사이트<br>
https://opentelemetry.io/docs/collector/installation/
- Windows용 zip 파일 다운로드<br>
https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_windows_386.tar.gz



```yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 127.0.0.1:9999
      http:
        endpoint: 127.0.0.1:4318

exporters:
  logging :
    verbosity: detailed
  file: # the File Exporter, to ingest logs to local file
    path: example.json
    rotation:
  prometheus:
    endpoint: 127.0.0.1:9464 # 프로메테우스에서 수집에 사용하는 IP
  
  otlp/jaeger:
    endpoint: 127.0.0.1:4317
    tls :
      insecure : true
  otlphttp:
    endpoint: http://127.0.0.1:3100/otlp

processors:
  batch:

service:
  pipelines:
    logs:
      receivers: [otlp]
      exporters: [file,otlphttp]
      processors: [batch]
    traces:
      receivers: [otlp]
      exporters: [otlp/jaeger,file]
      processors: [batch]
    metrics:
      receivers: [otlp]
      exporters: [prometheus]
      processors: [batch]
```

## customconfig.yaml 파일 검증
```shell
otelcol validate --config=customconfig.yaml
```

## collector 실행
```shell
otelcol --config=customconfig.yaml
```

## 참고 사이트
### collector 설정
- https://opentelemetry.io/docs/collector/configuration/

### customconfig.yaml 파일 생성
- https://opentelemetry.io/docs/collector/configuration/