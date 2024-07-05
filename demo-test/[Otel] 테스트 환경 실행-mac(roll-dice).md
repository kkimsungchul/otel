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

## 프로메테우스 실행
```shell
./prometheus --config.file=prometheus.yml
```

## 프로메테우스 메트릭 데이터 및 접속 확인
- 메트릭: http://localhost:9090/
- 접속: http://localhost:9090/
