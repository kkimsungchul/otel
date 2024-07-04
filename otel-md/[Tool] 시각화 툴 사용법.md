# 시각화 툴 사용법
- Prometheus
- Jaeger
- Loki
- Grafana


# 프로메테우스란?
Prometheus는 오픈 소스 시스템 모니터링 및 경고 도구입니다. Prometheus는 SoundCloud에서 시작되었으며, 현재는 CNCF(Cloud Native Computing Foundation)의 프로젝트로 관리되고 있습니다. Prometheus는 다음과 같은 주요 기능과 특징을 가지고 있습니다:

### 프로메테우스 구성 요소
- Prometheus 서버: 데이터를 수집하고 저장하며, 사용자가 쿼리를 통해 데이터를 조회할 수 있게 합니다.

- Pushgateway: 짧은 수명을 가지는 잡(job) 또는 배치 작업(batch job)에서 메트릭 데이터를 푸시할 수 있게 합니다.
- 클라이언트 라이브러리: 애플리케이션에서 메트릭 데이터를 수집하고 Prometheus 서버에 제공하는 데 사용됩니다.
다양한 언어를 지원합니다: Go, Java, Python, Ruby 등.

- Exporters: 기존의 모니터링 시스템이나 서비스로부터 메트릭을 수집하여 Prometheus 형식으로 변환하는 데 사용됩니다. 
``` Node Exporter, Blackbox Exporter, MySQL Exporter ```   

- Alertmanager: 알림을 관리하고 라우팅합니다.
```이메일, Slack, PagerDuty```

### 사용 사례
- 시스템 모니터링: CPU, 메모리, 디스크 I/O와 같은 시스템 리소스를 모니터링합니다.
- 애플리케이션 모니터링: 애플리케이션의 성능 메트릭과 오류를 모니터링합니다.
- 인프라스트럭처 모니터링: 클라우드 서비스, 컨테이너, 오케스트레이션 시스템(Kubernetes) 등을 모니터링합니다.


# Prometheus(프로메테우스) 사용 방법

### 설치 파일 다운로드
	https://prometheus.io/download/

### prometheus.yml 설정
※ 프로메테우스 다운로드 후 압축을 푼다음, exe 파일이 있는 경로에 yml파일을 생성

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: "prometheus"
    static_configs:
    - targets: ["localhost:9090"]

  - job_name: dice-service          # 서비스 명
    scrape_interval: 5s
    static_configs:
      - targets: ["localhost:9464"]  # 로컬에서 실행 중인 서비스의 포트
```

### 실행
	prometheus.exe

### 접속
	http://localhost:9090/

### 데이터 저장 용량
Prometheus가 로컬 디스크에 얼마나 많은 데이터를 저장할 수 있는지는 사용 가능한 디스크 용량과 설정에 따라 달라집니다. 실제로 Prometheus의 데이터 저장 용량에는 다음 요소들이 영향을 미칩니다

- 스크레이프 주기: 얼마나 자주 메트릭 데이터를 수집하는지
- 타겟 수: 모니터링되는 타겟의 수와 각 타겟에서 수집되는 메트릭의 수
- 데이터 압축: Prometheus는 효율적인 압축 알고리즘을 사용하여 데이터 저장 


### 추가 설정 옵션 (prometheus 실행 시 전달할 수 있는 명령어)
이 설정은 Prometheus가 30일 동안 최대 100GB의 데이터를 저장하도록 설정합니다. 실제로 이러한 설정 값은 모니터링 요구 사항에 맞게 조정해야 합니다.
```
--storage.tsdb.path=/path/to/prometheus/data
--storage.tsdb.retention.time=30d
--storage.tsdb.retention.size=100GB
--storage.tsdb.min-block-duration=2h
--storage.tsdb.max-block-duration=36h
```

## 참고 자료
- Prometheus 공식 웹사이트

https://prometheus.io/docs/introduction/overview/

- Prometheus GitHub 리포지토리

https://github.com/prometheus/prometheus


# Jaeger(예거) 사용법

# 목차

## 참고 링크
- URL : https://twofootdog.tistory.com/67
- URL : https://blog.advenoh.pe.kr/cloud/Jaeger%EC%97%90-%EB%8C%80%ED%95%9C-%EC%86%8C%EA%B0%9C/
- URL : https://afsdzvcx123.tistory.com/entry/%EC%9D%B8%ED%94%84%EB%9D%BC-Jaeger-OpenTelemetry-Grafana-%EC%97%B0%EB%8F%99
- URL : https://tommypagy.tistory.com/618
- URL : https://velog.io/@yange/Jaeger

## 다운로드 링크
Binaries 파일로 다운로드
- URL : https://www.jaegertracing.io/download/
- Windows : https://github.com/jaegertracing/jaeger/releases/download/v1.57.0/jaeger-1.57.0-windows-amd64.tar.gz



## 실행
```shell
cd C:\Users\sung\Desktop\otel\jaeger-1.57.0-windows-amd64
jaeger-all-in-one.exe 
```

## 접속
- URL : http://localhost:16686

## OpenTelemetry 연동
참고 링크 : https://www.jaegertracing.io/docs/1.21/opentelemetry/

## OpenTelemetry collector 설정 변경
- jaeger에서 gRPC를 지원해주면서 OpenTelemetry Collector는 OpenTelemetry SDK와 Jaeger 백엔드 사이에 배포할 필요가 없어짐
- jaeger와 어플리케이션이 직접 통신은 가능하나 OpenTelemetry collector를 사용하여 통신할 경우 OpenTelemetry collector 설정을 변경
- exporters 설정에서 jaeger만 사용하는 부분이 사라지고 "otlp/jaeger" 로 변경되었음 
```yaml
exporters:
  logging :
    verbosity: detailed
  file: # the File Exporter, to ingest logs to local file
    path: example.log
    rotation:
  prometheus:
    endpoint: 127.0.0.1:9464 # 프로메테우스에서 수집에 사용하는 IP
  
  otlp/jaeger:
    endpoint: 127.0.0.1:4317
    tls :
      insecure : true
service:
  pipelines:
    logs/dev:
      receivers: [otlp]
      exporters: [file]
      processors: [batch]
    traces:
      receivers: [otlp]
      exporters: [otlp/jaeger,file]
      processors: [batch]
```


## tempo ,opentelemetry 내용
https://nangman14.tistory.com/69

## Tempo vs Jaeger
https://www.reddit.com/r/grafana/comments/vy4beq/tempo_vs_jaeger/?rdt=45362
https://signoz.io/blog/jaeger-vs-tempo/
https://sysnet4admin.gitbook.io/cncf/blog-and-news-ko/blog/member/tracing
https://codersociety.com/blog/articles/jaeger-vs-zipkin-vs-tempo

## OpenTelemetry collector 에서 jaeger export 변경 사항 내용

```text
* error decoding 'exporters': unknown type: "jaeger" for id: "jaeger" (valid values: [logging otlp prometheus prometheusremotewrite debug nop otlphttp file kafka opencensus zipkin])
2024/06/04 01:07:29 collector server run finished with error: failed to get config: cannot unmarshal the configuration: 1 error(s) decoding:
```

- 2023년 11월

    https://stackoverflow.com/questions/77475771/error-when-running-otel-collector-with-jaeger-in-docker-containers
  
- 2023년 9월

    https://github.com/open-telemetry/opentelemetry-collector-contrib/issues/26685

- jaeger 릴리즈 노트

    https://www.jaegertracing.io/docs/1.38/apis/#opentelemetry-protocol-stable

- OpenTelemetry 릴리즈 노트

    https://github.com/open-telemetry/opentelemetry-collector-releases/releases/tag/v0.85.0
    https://github.com/open-telemetry/opentelemetry-collector-releases/pull/396
    https://github.com/open-telemetry/opentelemetry.io/pull/3273
	


----
콜렉터 - Jaeger 마이그레이션
https://opentelemetry.io/blog/2023/jaeger-exporter-collector-migration/
	
데모에 있는 콜렉터 설정
https://github.com/open-telemetry/opentelemetry-demo/blob/main/src/otelcollector/otelcol-config.yml

위에 콜렉터 설정을 보고
1. 예거 포트를 바꾸고
2. 콜렉터에서 span 데이터를 익스포트를 otlp로 바꾸고
3. 콜렉터에서 otlp 익스포터 설정을 해주면될듯

----
windows 에서 예거 사용
https://www.jaegertracing.io/docs/1.58/windows/


## [ Loki 사용 ]

### 참고링크 :
  - opentelemetry collector 에서 사용하는 API
    - https://grafana.com/docs/loki/latest/reference/loki-http-api/#ingest-logs-using-otlp
  - Loki 설치 
    - https://grafana.com/docs/loki/latest/setup/install/local/ 
  - Loki - Opentelemetry 지원 문서
    - https://grafana.com/docs/loki/latest/send-data/otel/ 

### 시작 전 참고사항
- Loki의 3.0.0 버전부터 Opentelemetry 에서 바로 수신하는 기능을 제공해주며, 해당 문서는 3.0.0 버전 기준으로 작성되었습니다.
- 3.0.0 미만의 버전에서는 promtail을 사용하여 Opentelemetry 의 로그를 수신해야 합니다. 


### Loki 설치
1. 아래의 URL 접속
    - https://github.com/grafana/loki/releases/
2. 3.0.0 releases의 Assets 클릭 후 아래의 파일 다운로드
    - loki-windows-amd64.exe.zip
    - 다운로드 링크 : https://github.com/grafana/loki/releases/download/v3.0.0/loki-windows-amd64.exe.zip

### Loki 설정 다운로드
- 설정파일은 loki-windows-amd64.exe.zip 의 압축 푼 경로에 생성
1. https://raw.githubusercontent.com/grafana/loki/v3.0.0/cmd/loki/loki-local-config.yaml 링크 접속 
2. 해당 링크의 내용을 복사하여 loki-local-config.yaml 파일 생성 후 붙여넣기
    - 또는 cmd에서 아래의 명령어 입력
```shell
curl https://raw.githubusercontent.com/grafana/loki/v3.0.0/cmd/loki/loki-local-config.yaml > loki-local-config.yaml
```

### Loki 설정 파일 수정
1. loki-local-config.yaml 파일을 열어 아래의 내용으로 변경
```yaml
auth_enabled: false

server:
  http_listen_port: 3100
  grpc_listen_port: 9096

common:
  instance_addr: 127.0.0.1
  path_prefix: ./loki
  storage:
    filesystem:
      chunks_directory: ./loki/chunks
      rules_directory: ./loki/rules
  replication_factor: 1
  ring:
    kvstore:
      store: inmemory

query_range:
  results_cache:
    cache:
      embedded_cache:
        enabled: true
        max_size_mb: 100

schema_config:
  configs:
    - from: 2020-10-24
      store: tsdb
      object_store: filesystem
      schema: v13
      index:
        prefix: index_
        period: 24h

ruler:
  alertmanager_url: http://localhost:9093

# otlp setting
limits_config:
  allow_structured_metadata: true
```

### Opentelemetry Colector 의 yml 파일 수정
- 파일명 : customconfig.yaml
    - 설정 시 Loki의 API문서의 내용을 보면 http://127.0.0.1:3100/otlp/v1/logs 의 URL 전체를 입력하지 않고 otlp까지만 입력함
    - 콜렉터에서 자동으로 엔드포인트 URL을 완성시킴
```yaml
exporters:
  otlphttp:
    endpoint: http://127.0.0.1:3100/otlp
    ...중략...
service:
  pipelines:
    logs:
      receivers: [otlp]
      exporters: [file,otlphttp]
    ...중략...
```


### Loki 실행
- cmd에서 아래의 명령어 입력
```shell
.\loki-windows-amd64.exe --config.file=loki-local-config.yaml
```
- 또는 start_loki.bat 파일 실행

# Grafana(그라파나) 사용법

# 목차

## 참고 링크
- URL : https://hstory0208.tistory.com/entry/Grafana-%EA%B7%B8%EB%9D%BC%ED%8C%8C%EB%82%98-%EC%84%A4%EC%B9%98-%EB%B0%A9%EB%B2%95-Window
- URL : https://hstory0208.tistory.com/entry/Grafana-%EA%B7%B8%EB%9D%BC%ED%8C%8C%EB%82%98-%EC%84%A4%EC%B9%98-%EB%B0%A9%EB%B2%95-Window
- URL : https://2hyes.tistory.com/76
## 다운로드 링크
- URL : https://grafana.com/grafana/download
- Windows Installer : https://dl.grafana.com/enterprise/release/grafana-enterprise-11.0.0.windows-amd64.msi
- Windows Binaries: https://dl.grafana.com/enterprise/release/grafana-enterprise-11.0.0.windows-amd64.zip
  - 아래의 내용은 Binaries 로 진행함


## 실행
- 경로 : grafana-v11.0.0\bin
- 파일 : grafana-server.exe

## 접속
- URL : http://localhost:3000/
- ID / PW : admin / admin
  - 처음 로그인 시 비밀번호를 변경하라고 나오는데, 스킵도 가능함

## Prometheus(프로메테우스) 연동
1. 좌측의 Connections 클릭
2. Add new connection 클릭
3. Prometheus 검색 후 클릭
4. 우측 상단의 Add new data source 클릭
5. 설정화면에서 아래의 정보 입력
```text
name : 대시보드에서 사용할 이름
connection : 프로메테우스 접속 URL:port
```
6. 정보 입력 후 하단의 Save & test 클릭
7. save 후 좌측 탭 메뉴의 Connections 하위 메뉴에서 Data sources 클릭
8. Prometheus가 추가된것을 확인하고 Build a dashboard 클릭
9. Add visualization 클릭
10. 화면 설정 후 우측 상단의 Save 클릭
11. 대시보드 이름 및 설명 저장

## Jaeger(예거) 연동
- 프로메테우스 연동 방법과 똑같으며 connection 부분만 jaeger URL로 변경

## 알람기능 추가
이메일, 슬랙, 텔레그램 등 이상 발생 시 알람 발송 가능
- 구글 메일서버 사용하여 알람기능 추가
- 2차 인증 메일인 경우 앱 비밀번호 부여
    - https://myaccount.google.com/apppasswords

- C:\grafana-v11.0.0\conf\defaults.ini 수정
```
#################################### SMTP / Emailing #####################
[smtp]
enabled = true
host = smtp.gmail.com:587
user = leejihyoun.ba@gmail.com
# If the password contains # or ; you have to wrap it with triple quotes. Ex """#password;"""
password = qdxd fhrz issz pxdu
cert_file =
key_file =
skip_verify = false
from_address = leejihyoun.ba@gmail.com
from_name = Grafana
ehlo_identity =
startTLS_policy =
enable_tracing = false
```
- Grafana(localhost:3000) > Alerting > Alert rules에 알람 룰 추가
- Grafana(localhost:3000) > Alerting > Contact points에 메일 수신자 지정
- 사내메일로도 발송 가능
