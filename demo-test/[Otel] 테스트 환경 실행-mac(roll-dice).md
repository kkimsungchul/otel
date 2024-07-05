# Mac에서 Otel 테스트 환경 구성하기(roll-dice)
## 목차
- [Otel 환경 구성](#otel-환경-구성)
- [Otel Collector](#otel-collector)
- [JAVA(SpringBoot) Agent 실행](#javaspringboot-agent-실행)
- [Python(SDK) 실행](#pythonsdk-실행)

---
## Otel 환경 구성
### git 코드 내려받기
```shell
git clone https://github.com/kkimsungchul/otel.git
```
## 환경 구성
- java 17
- python 3.x
- ※ ~user-path는 본인이 사용할 경로를 설정
---
## Otel Collector

### collector 설치
```shell
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_darwin_arm64.tar.gz
tar -xvf otelcol_0.100.0_darwin_arm64.tar.gz
```
### collector 권한부여
```shell
chmod +x /Users/jihyoun/Otel/otelcol
```

### collector 경로로 이동
```shell
cd 2024/otel/otel-collector
```

### customconfig.yaml 파일 검증
```shell
./otelcol validate --config=customconfig.yaml
```

### collector 실행
```shell
./otelcol --config=customconfig.yaml
```
---
## JAVA(SpringBoot) Agent 실행

### otel 변수 설정
```
export JAVA_TOOL_OPTIONS=-javaagent:/Users/jihyoun/2024/otel/otel-java-agent/opentelemetry-javaagent.jar
export OTEL_METRIC_EXPORT_INTERVAL=1000
export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=otlp
export OTEL_LOGS_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
export OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
export OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

### SpringBoot 프로젝트 실행
```shell
cd /Users/jihyoun/2024/otel/otel-java-agent
java -jar otel-test-springboot.jar
```

### JAVA(SpringBoot) (SDK) 실행
```shell
cd /Users/jihyoun/2024/otel/otel-java-agent
java -jar otel-sdk-test-springboot.jar
```

### JAVA(Tomcat) (Agent) 실행
```shell
cd /Downloads/apache-tomcat-9.0.89/bin/
./startup.sh
```
---
## Python(SDK) 실행

### 파이썬 가상환경 설정
```shell
cd /Users/jihyoun/2024/otel/otel-test-pytyon-run
python3 -m venv venv
source venv/bin/activate
```
### pip 업데이트
```shell
pip3 install --upgrade pip
```

### 플라스크 설치
```shell
pip3 install flask
```

### 라이브러리 설치
```shell
pip3 install opentelemetry-distro
opentelemetry-bootstrap -a install
pip3 install opentelemetry-exporter-otlp
pip3 install opentelemetry-exporter-otlp-proto-grpc
pip3 install opentelemetry-api
pip3 install opentelemetry-sdk
pip3 install opentelemetry-instrumentation-flask
pip3 install opentelemetry-exporter-prometheus install flask
```
---

## 플라스크 실행(mac)
### 트레이스, 메트릭, 로그 확인
```shell
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
    --traces_exporter console \
    --metrics_exporter console \
    --logs_exporter console \
    --service_name dice-server \
    python3 flask run -p 8080
```
### 플라스크 실행
```shell
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
--service_name=python-roll-dice-service \
--metric_export_interval=1000 \
--traces_exporter=otlp \
--metrics_exporter=otlp \
--logs_exporter=otlp \
--exporter_otlp_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_traces_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_logs_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_protocol=grpc \
python3 -m flask run -p 18080
```
---
## 테스트

### SpringBoot(agent) 환경 접속
- http://localhost:8080/java/rolldice

### SpringBoot(sdk) 환경 접속
- http://localhost:19090/java-custom/rolldice?rolls=6

### python 환경 접속
- http://localhost:18080/python/rolldice

### tomcat 환경 접속
- http://localhost:9080/

### 로그 확인
```shell
cd /Users/jihyoun/2024/otel/otel-collector/example.log
```
- 아래의 파일 확인<br>
※ root 경로(jihyoun)에 log파일 생성됨
example.log

---
#[Otel]JAVA-Agent_사용-mac.txt
[ 공식문서 따라하기 ]

# java 예제
	https://opentelemetry.io/docs/languages/java/getting-started/
	https://opentelemetry.io/docs/languages/java/instrumentation/

# java
	https://github.com/open-telemetry/opentelemetry-java

# sdk
	https://opentelemetry.io/docs/languages/java/
	java의 경우 1.8이 미니멈임
=====================================================================
# 참고사항
	권장사양
	https://opentelemetry.io/docs/demo/development/

	
# 프로젝트 생성 및 빌드
	- 스프링부트 프로젝트로 생성했으며, 별도의 라이브러리 임포트 없음,다만 로깅내용만 간단히 남김
	- jar 로 빌드하여 실행 파일 생성
	- 컨트롤러 하나를 생성해 두고, 사용자가 접속할수 있도록 설정
	ex) http://localhost:8080/rolldice

# agent jar 다운로드
	java 에이전트임
	https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# agent 다운로드 후 cmd창에서 아래와 같이 설정
	설정 관련 정보 링크 : 
		https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#logging-exporter
	엔드포인트(콜렉터) 설정 방법 예시 : 
		https://opentelemetry.io/docs/collector/deployment/agent/
	

	- mac (이지현)
	=====================================================================
	export JAVA_TOOL_OPTIONS=-javaagent:/Users/jihyoun/2024/otel/otel-java-agent/opentelemetry-javaagent.jar
    export OTEL_METRIC_EXPORT_INTERVAL=1000
    export OTEL_TRACES_EXPORTER=otlp
    export OTEL_METRICS_EXPORTER=otlp
    export OTEL_LOGS_EXPORTER=otlp
    export OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
    export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
    export OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
    export OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
    export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
	==========================================================================

	- 콘솔에만 출력
	=====================================================================
	export JAVA_TOOL_OPTIONS=-javaagent:/Users/jihyoun/2024/otel/otel-java-agent/opentelemetry-javaagent.jar
    export OTEL_METRIC_EXPORT_INTERVAL=1000
    export OTEL_TRACES_EXPORTER=logging
    export OTEL_METRICS_EXPORTER=logging
    export OTEL_LOGS_EXPORTER=logging
	=====================================================================

	※ 기본값
	set OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
	※ 여기서 삽질 오지게함.
		http/protobuf 로 설정하고 보내면 콜렉터에서 받질못햇음.
		뭐 아무리 설정을 뒤져봐도 안됐음.
		프로토콜을 grpc로 바꾸고 콜렉터 설정에도 grpc를 넣어주면 작동함

# 위 설정 완료 후 프로젝트 파일 실행
	java -jar otel-test-springboot.jar
	=====================================================================
    [otel.javaagent 2024-05-10 14:16:13:567 +0900] [main] INFO io.opentelemetry.javaagent.tooling.VersionLogger - opentelemetry-javaagent - version: 2.3.0
    [otel.javaagent 2024-05-10 14:16:15:673 +0900] [PeriodicMetricReader-1] INFO io.opentelemetry.exporter.logging.LoggingMetricExporter - Received a collection of 12 metrics for export.
	=====================================================================


# 콜렉터 실행
	[Otel]otel-collector-설치-및-사용.txt 파일 참고
    /Users/jihyoun/Otel/otelcol --config=/Users/jihyoun/2024/otel/otel-collector//otel-collector-config.yaml
---
#[Otel]python-사용-mac.txt
[공식문서 따라하기]
1. python
https://opentelemetry.io/docs/languages/python/getting-started/


# 맥에서 가상 환경 설치 및 필요한 라이브러리 설치
Pycharm > Settings > Python Interpreter > python v3.11

# 플라스크 설치
pip install flask

# 플라스크 실행
flask run -p 8080

# opentelemetry-distro  설치 (interpreter settings에서)
pip install opentelemetry-distro
opentelemetry-bootstrap -a install

# opentelemetry-exporter-otlp 설치
pip install opentelemetry-exporter-otlp

# 트레이스, 메트릭, 로그 확인
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
    --traces_exporter console \
    --metrics_exporter console \
    --logs_exporter console \
    --service_name dice-server \
    python3 flask run -p 8080

# tmp 폴더에 yaml 파일 생성
# /tmp/otel-collector-config.yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 127.0.0.1:4317
      http:
        endpoint: 127.0.0.1:4318

exporters:
  logging :
    verbosity: detailed
  file: # the File Exporter, to ingest logs to local file
    path: example.log
    rotation:

processors:
  batch:

service:
  pipelines:
    logs/dev:
      receivers: [otlp]
      processors: [batch]
      exporters: [file]

# collector 설치
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_darwin_arm64.tar.gz
tar -xvf otelcol_0.100.0_darwin_arm64.tar.gz

# collector 권한부여
chmod +x /Users/jihyoun/Otel/otelcol

# collector 실행
/Users/jihyoun/Otel/otelcol --config=/Users/jihyoun/2024/otel/otel-test-python/tmp/otel-collector-config.yaml

# 플라스크 실행
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
--metric_export_interval=15000 \
--traces_exporter=otlp \
--metrics_exporter=otlp \
--logs_exporter=otlp \
--exporter_otlp_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_traces_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_logs_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_protocol=grpc \
python3 -m flask run -p 8080

root 경로(jihyoun)에 log파일 생성됨
git에서는 python3 -m flask run -p 8080으로 해야 실행됨
(기존에는 flask run -p 8080)


# prometheus 설치
brew install prometheus

# yml 파일 경로
/opt/homebrew/etc/prometheus.yml

# prometheus yml 파일 수정
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: "prometheus"
    static_configs:
    - targets: ["localhost:9090"]

  - job_name: dice-service
    scrape_interval: 5s
    static_configs:
      - targets: ["localhost:9464"]  # 로컬에서 실행 중인 서비스의 포트

# yaml 파일 수정
# /tmp/otel-collector-config.yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 127.0.0.1:4317
      http:
        endpoint: 127.0.0.1:4318


exporters:
  logging :
    verbosity: detailed
  file: # the File Exporter, to ingest logs to local file
    path: example.log
    rotation:
  prometheus:
    endpoint: 127.0.0.1:9464

processors:
  batch:

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
      exporters: [prometheus]
      processors: [batch]

# export 시 필요한 라이브러리 설치
pip3 install opentelemetry-exporter-otlp-proto-grpc
pip3 install flask opentelemetry-api opentelemetry-sdk opentelemetry-instrumentation-flask opentelemetry-exporter-otlp
pip3 install opentelemetry-exporter-prometheus


# 플라스크 실행
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
--service_name=python-roll-dice-service \
--metric_export_interval=1000 \
--traces_exporter=otlp \
--metrics_exporter=otlp \
--logs_exporter=otlp \
--exporter_otlp_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_traces_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_logs_endpoint=http://127.0.0.1:4317 \
--exporter_otlp_protocol=grpc \
python3 -m flask run -p 18080
