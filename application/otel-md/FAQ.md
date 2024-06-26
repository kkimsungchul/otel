# 시각화 도구
## Q1. 시각화 도구의 데이터 적재 및 모니터링 역할

---

### 도구 소개
#### Jaeger
- 역할: Jaeger는 분산 트레이싱 시스템입니다. 마이크로서비스 아키텍처에서 서비스 간의 호출 관계와 지연 시간을 추적하는 데 사용됩니다.
- 데이터 적재: Jaeger 에이전트 또는 콜렉터가 애플리케이션에서 수집한 트레이스 데이터를 저장합니다. 기본적으로 Jaeger는 다양한 백엔드 스토리지를 지원하며, 주로 Elasticsearch, Kafka, Cassandra 등이 사용됩니다.

#### Prometheus
- 역할: Prometheus는 모니터링과 경고 시스템입니다. 애플리케이션, 시스템 및 서비스의 메트릭 데이터를 수집하고 저장합니다.
- 데이터 적재: Prometheus는 타임 시리즈 데이터베이스에 메트릭 데이터를 저장합니다. 애플리케이션에서 Prometheus 클라이언트 라이브러리를 사용하여 메트릭 데이터를 노출하고, Prometheus 서버는 이를 스크랩하여 저장합니다.

#### Grafana
- 역할: Grafana는 시각화 도구입니다. Prometheus 및 Jaeger와 같은 데이터 소스에서 데이터를 가져와 대시보드를 통해 시각화합니다.
- 데이터 가져오기: Grafana는 다양한 데이터 소스(예: Prometheus, Jaeger 등)에 연결하여 데이터를 쿼리하고 시각화합니다.

---

### 상호작용 흐름

#### Jaeger 데이터 적재
애플리케이션에서 Jaeger 클라이언트를 사용하여 트레이스 데이터를 Jaeger 에이전트나 콜렉터로 전송합니다.
Jaeger 콜렉터는 이 데이터를 저장 백엔드(예: Elasticsearch)에 저장합니다.

#### Prometheus 데이터 적재
애플리케이션에서 Prometheus 클라이언트 라이브러리를 사용하여 메트릭 데이터를 노출합니다.
Prometheus 서버는 정기적으로 이 메트릭 데이터를 스크랩하여 자체 타임 시리즈 데이터베이스에 저장합니다.

#### Grafana 데이터 시각화
Grafana는 데이터 소스로 Jaeger와 Prometheus를 설정합니다.
Grafana 대시보드에서 Jaeger의 트레이스 데이터와 Prometheus의 메트릭 데이터를 쿼리하고 시각화합니다.

---

### Grafana 설정
Grafana에서 Jaeger와 Prometheus 데이터 소스를 설정하고 대시보드를 생성하여 데이터를 시각화합니다.

#### Prometheus 데이터 소스 설정
Grafana UI에서 Configuration -> Data Sources로 이동하여 Prometheus를 추가합니다.
URL에 Prometheus 서버의 주소를 입력합니다.

#### Jaeger 데이터 소스 설정
Grafana UI에서 Configuration -> Data Sources로 이동하여 Jaeger를 추가합니다.
URL에 Jaeger 콜렉터의 주소를 입력합니다.

#### 대시보드 생성
Grafana UI에서 Create -> Dashboard로 이동하여 새로운 대시보드를 생성합니다.
패널을 추가하여 Prometheus와 Jaeger 데이터를 쿼리하고 시각화합니다.

---
## Q2. 오픈텔레메트리 콜렉터에서 그라파나로 직접 데이터를 송신할 수 있는가?

OpenTelemetry 수집기(Collector)에서 데이터를 Grafana로 직접 전송하는 것은 일반적이지 않습니다. 대신, OpenTelemetry 수집기에서 데이터를 Prometheus 같은 백엔드 시스템으로 전송하고, Grafana가 이 데이터를 시각화하는 방법을 사용합니다. 

참고 링크
- https://grafana.com/docs/agent/latest/flow/tasks/collect-opentelemetry-data/

### OpenTelemetry 프로토콜 내보내기 구성
구성 요소가 OpenTelemetry 데이터를 수신하려면 먼저 OpenTelemetry 데이터 내보내기를 담당하는 구성 요소가 있어야 합니다. OpenTelemetry 내보내기 구성 요소는 OpenTelemetry 데이터를 외부 시스템에 쓰기(내보내기)를 담당합니다. 
이 작업에서는 otelcol.exporter.otlp 구성 요소를 사용하여 OTLP(OpenTelemetry Protocol)를 통해 OpenTelemetry 데이터를 서버로 보냅니다. 내보내기 구성요소가 정의된 후 다른 Grafana 에이전트 흐름 구성요소를 사용하여 데이터를 전달할 수 있습니다.

#### 요약
- 직접 전송 불가: OpenTelemetry 수집기에서 데이터를 Grafana로 직접 전송하는 것은 불가능합니다.
- 백엔드 사용: OpenTelemetry 수집기에서 데이터를 Prometheus 같은 백엔드 시스템으로 전송하고, Grafana가 이 데이터를 시각화합니다.
- 설정 방법: OpenTelemetry 수집기, Prometheus, Grafana의 설정 파일을 적절히 구성하여 데이터를 수집하고 시각화합니다.


 아래에 각 단계별 설정 방법을 설명합니다.

### OpenTelemetry 수집기 설정
OpenTelemetry 수집기를 설정하여 데이터를 수집하고 Prometheus로 전송하는 방법입니다.

```
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: "127.0.0.1:4317"
      http:
        endpoint: "127.0.0.1:4318"

exporters:
  prometheus:
    endpoint: "0.0.0.0:8889"

service:
  pipelines:
    metrics:
      receivers: [otlp]
      exporters: [prometheus]
```

이 설정 파일은 OpenTelemetry 수집기가 gRPC와 HTTP 프로토콜을 통해 데이터를 수신하고, 이를 Prometheus로 내보내도록 설정합니다.

### Prometheus 설정

#### Prometheus 설정 예시 (prometheus.yml)

```
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'otel-collector'
    scrape_interval: 10s
    static_configs:
      - targets: ['localhost:8889']
```
---

### Grafana 설정
Grafana에서 Prometheus를 데이터 소스로 설정하고, 시각화를 설정합니다.

#### Grafana에서 Prometheus 데이터 소스 추가

- Grafana 웹 UI로 이동합니다.
- 왼쪽 사이드바에서 Configuration -> Data Sources로 이동합니다.
- Add data source 버튼을 클릭하고, Prometheus를 선택합니다.
- URL에 Prometheus 서버의 주소 (http://localhost:9090)를 입력합니다.

#### 대시보드 생성
- Grafana 대시보드에서 + 아이콘을 클릭하고 Dashboard를 선택합니다.
- Add new panel을 클릭합니다.
- 쿼리 편집기에서 데이터 소스로 Prometheus를 선택하고, 원하는 메트릭을 입력하여 시각화합니다.





---

## 로깅
### logging example
https://opentelemetry.io/docs/zero-code/python/logs-example/


## 트레이싱
### span example
https://opentelemetry.io/docs/zero-code/python/example/

## 메트릭
### metric 측정 항목

측정항목과 관련된 다음과 같은 의미 체계 규칙이 정의됩니다.

- 일반 지침 : 일반 측정항목 지침입니다.
- 데이터베이스 : SQL 및 NoSQL 클라이언트 측정항목용입니다.
  - https://opentelemetry.io/docs/specs/semconv/database/sql/
- FaaS : 서비스로서의 기능(Function as a Service) 지표용.
  - https://opentelemetry.io/docs/specs/semconv/faas/faas-metrics/
- HTTP : HTTP 클라이언트 및 서버 측정항목용입니다.
  - https://opentelemetry.io/docs/specs/semconv/http/http-metrics/
- 메시징 : 메시징 시스템(큐, 게시/구독 등) 지표용입니다.
  - https://opentelemetry.io/docs/specs/semconv/messaging/kafka/
- RPC : RPC 클라이언트 및 서버 측정항목용입니다. 
  - https://opentelemetry.io/docs/specs/semconv/rpc/rpc-metrics/

시스템 측정항목
- 시스템 : 표준 시스템 측정항목용입니다.
  - https://opentelemetry.io/docs/specs/semconv/system/system-metrics/
- 하드웨어 : 하드웨어 관련 측정항목입니다.
  - https://opentelemetry.io/docs/specs/semconv/system/hardware-metrics/
- 프로세스 : 표준 프로세스 측정항목용입니다.
  - https://opentelemetry.io/docs/specs/semconv/system/process-metrics/
- 런타임 환경 : 런타임 환경 측정항목용입니다.
  - https://opentelemetry.io/docs/specs/semconv/runtime/jvm-metrics/