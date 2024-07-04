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
