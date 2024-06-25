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