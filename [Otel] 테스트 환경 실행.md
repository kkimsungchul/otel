# Otel 테스트 환경 실행

# 목차
- [windows](#windows)
    * [코드 내려받기](#-------)
    * [사전 환경 구성 및 유의사항](#---------------)
    * [collector 실행](#collector---)
        + [customconfig.yaml 파일 검증](#customconfigyaml------)
        + [collector 실행](#collector----1)
    * [JAVA(SpringBoot) (agent) 실행](#java-springboot---agent----)
        + [otel 변수 설정](#otel------)
        + [SpringBoot 프로젝트 실행](#springboot--------)
    * [Python(SDK) 실행](#python-sdk----)
        + [파이썬 가상환경 설정](#-----------)
        + [pip 업데이트](#pip-----)
        + [플라스크 설치](#-------)
        + [opentelemetry-distro  설치](#opentelemetry-distro----)
        + [opentelemetry-exporter-otlp 설치](#opentelemetry-exporter-otlp---)
        + [플라스크 실행(windows)](#--------windows-)
    * [테스트](#---)
        + [SrpingBoot 환경 접속](#srpingboot------)
        + [python 환경 접속](#python------)
        + [로그 확인](#-----)
- [Mac](#mac)
# windows 

## 코드 내려받기
```git
git clone https://github.com/kkimsungchul/otel.git
```
## 사전 환경 구성 및 유의사항
- java 17 
- python 3.x
- ~user-path는 본인이 사용할 경로를 설정


## collector 실행
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-collector
```
### customconfig.yaml 파일 검증
```shell
otelcol validate --config=customconfig.yaml
````
### collector 실행
```shell
otelcol --config=customconfig.yaml
```
---
## JAVA(SpringBoot) (agent) 실행

### otel 변수 설정
```
set JAVA_TOOL_OPTIONS=-javaagent:C:\Users\sung\Desktop\개발\otel\test\otel\otel-java-agent\opentelemetry-javaagent.jar
set OTEL_METRIC_EXPORT_INTERVAL=1000
set OTEL_TRACES_EXPORTER=otlp
set OTEL_METRICS_EXPORTER=otlp
set OTEL_LOGS_EXPORTER=otlp
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

### SpringBoot 프로젝트 실행
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-java-agent
java -jar otel-test-springboot.jar
```
---
## Python(SDK) 실행

### 파이썬 가상환경 설정
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-test-python
python -m venv venv
venv\Scripts\activate
```
### pip 업데이트
```shell
python.exe -m pip install --upgrade pip
```

### 플라스크 설치
```shell
pip install flask
```

### opentelemetry-distro  설치
```shell
pip install opentelemetry-distro
opentelemetry-bootstrap -a install
```
### opentelemetry-exporter-otlp 설치
```shell
pip install opentelemetry-exporter-otlp
```

### 플라스크 실행(windows)
※ 윈도우의 경우에는 opentelemetry-instrument 의 실행 옵션을 여러줄로 작성하면 오류가 발생할수 있으므로 한줄로 실행함
```shell
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument --metric_export_interval=1000 --traces_exporter=otlp --metrics_exporter=otlp --logs_exporter=otlp --exporter_otlp_endpoint=http://127.0.0.1:4317 --exporter_otlp_traces_endpoint=http://127.0.0.1:4317 --exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 --exporter_otlp_logs_endpoint=http://127.0.0.1:4317 --exporter_otlp_protocol=grpc flask run -p 9090
```

- 아래는 가시성 확보를 위해 여러줄로 작성
```shell
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument `
--metric_export_interval=1000 `
--traces_exporter=otlp `
--metrics_exporter=otlp `
--logs_exporter=otlp `
--exporter_otlp_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_traces_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_logs_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_protocol=grpc `
flask run -p 9090
```
---
## 테스트

### SrpingBoot 환경 접속
- localhost:8080/rolldice

### python 환경 접속
- http://localhost:9090/rolldice

### 로그 확인
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-collector
```
- 아래의 파일 확인<br>
example.log


# Mac
## 코드 내려받기
```shell
git clone https://github.com/kkimsungchul/otel.git
```
## 사전 환경 구성 및 유의사항
- java 17
- python 3.x
- ~user-path는 본인이 사용할 경로를 설정

## collector 실행
```shell
/Users/jihyoun/Otel/otelcol --config=/Users/jihyoun/2024/otel/otel-collector//otel-collector-config.yaml
```
---
## JAVA(SpringBoot) (agent) 실행

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
---
## Python(SDK) 실행

### 파이썬 가상환경 설정
```shell
Pycharm > Settings > Python Interpreter
> Virtualenv Environment
> Location
 path = /Users/jihyoun/2024/otel/otel-test-python/venv 
> Base Interpreter
 python v3.11
```
### pip 업데이트
```shell
pip3 install --upgrade pip
```

### 플라스크 설치
```shell
pip3 install flask
```

### opentelemetry-distro  설치
※ Python Interpreter에서 수동으로도 설치 가능
```shell
pip3 install opentelemetry-distro
opentelemetry-bootstrap -a install
```
### opentelemetry-exporter-otlp 설치
```shell
pip3 install opentelemetry-exporter-otlp
```
### collector 설치
```shell
curl --proto '=https' --tlsv1.2 -fOL https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.100.0/otelcol_0.100.0_darwin_arm64.tar.gz
tar -xvf otelcol_0.100.0_darwin_arm64.tar.gz
```
### collector 권한부여
```shell
chmod +x /Users/jihyoun/Otel/otelcol
```
### collector 실행

```shell
/Users/jihyoun/Otel/otelcol --config=/Users/jihyoun/2024/otel/otel-test-python/tmp/otel-collector-config.yaml
```

### 플라스크 실행(mac)
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
```
---
## 테스트

### SrpingBoot 환경 접속
- localhost:8080/rolldice

### python 환경 접속
- http://localhost:9090/rolldice

### 로그 확인
```shell
cd /Users/jihyoun/
```
- 아래의 파일 확인<br>
※ root 경로(jihyoun)에 log파일 생성됨
example.log