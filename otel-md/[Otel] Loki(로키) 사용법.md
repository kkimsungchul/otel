# 로키 설치
참고 링크 : 
https://grafana.com/docs/loki/latest/setup/install/local/
https://velog.io/@flaehdan/Grafana-Loki-%EB%A1%9C%EA%B7%B8-%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81
https://blog.naver.com/bokmail83/221812445084
https://velog.io/@junsj119/%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81-%EA%B4%80%EB%A0%A8
1. 아래의 링크 접속
https://github.com/grafana/loki/releases/

2. 원하는 버전에서 Assets 클릭 후 윈도우 버전 다운로드
Loki는 Grafana에서 제공하는 오픈소스 기반의 로그 집계 서비스입니다.
Promtail과 함께 사용해서 로그를 수집하고 Grafana로 로그를 보여줄 수 있습니다.
Loki를 사용하기 위해서는 로그를 Loki로 보내주는 Promtail도 같이 설치해야 합니다.
<br>
Promtail은 로그 파일이 존재하는 서버에 설치되어 로그 파일을 관리하는 서버로 로그를 전송한다.
Loki는 Promtail로부터 로그를 수신한다.


https://github.com/grafana/loki/releases/download/v2.9.8/loki-windows-amd64.exe.zip
https://github.com/grafana/loki/releases/download/v2.9.8/promtail-windows-amd64.exe.zip

3. 설정 파일 다운로드
해당 링크의 yaml 파일을 다운로드
https://raw.githubusercontent.com/grafana/loki/v2.9.8/cmd/loki/loki-local-config.yaml
https://raw.githubusercontent.com/grafana/loki/main/clients/cmd/promtail/promtail-local-config.yaml
cmd에서
curl https://raw.githubusercontent.com/grafana/loki/v2.9.8/cmd/loki/loki-local-config.yaml > loki-local-config.yaml
curl https://raw.githubusercontent.com/grafana/loki/main/clients/cmd/promtail/promtail-local-config.yaml > promtail-local-config.yaml 
해당 파일의 하단부분에 아래의 내용 추가
```yaml
# otlp setting
limits_config:
  allow_structured_metadata: true
```

4. loki 실행
cmd에서 아래의 명령어 입력
.\loki-windows-amd64.exe --config.file=loki-local-config.yaml

5. 접속
http://localhost:3100/metrics

# 로키 - 오픈텔레메트리 콜렉터 설정
https://grafana.com/docs/loki/latest/send-data/otel/
https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/exporter/lokiexporter/README.md
- GPT나 깃허브 링크에 있는 설정으로 콜렉터를 설정하면 오류가발생함
- exporters에 loki 라는 옵션이 없다는 오류가 나옴.

## 콜렉터 yml 파일 수정
https://github.com/open-telemetry/opentelemetry-collector/blob/main/exporter/README.md
https://github.com/open-telemetry/opentelemetry-collector-contrib/blob/main/exporter/lokiexporter/README.md

- customconfig.yaml
```yaml
exporters:
  otlphttp:
    endpoint: http://localhost:3100/otlp

service:
  pipelines:
    logs/dev:
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
