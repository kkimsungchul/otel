# Otel 테스트 환경 실행

# 목차
- [windows](#windows)
    * [코드 내려받기](#코드-내려받기)
    * [사전 환경 구성 및 유의사항](#사전-환경-구성-및-유의사항)
    * [collector 실행](#collector-실행)
        + [collector 경로 이동](#collector-경로-이동)
        + [customconfig.yaml 파일 검증](#customconfigyaml-파일-검증)
        + [collector 실행](#collector-실행-1)
    * [prometheus 실행](#prometheus-실행)
        + [경로 이동](#경로-이동)
        + [실행](#실행)
    * [JAVA(SpringBoot) (agent) 실행](#javaspringboot-agent-실행)
        + [otel 변수 설정](#otel-변수-설정)
        + [SpringBoot 프로젝트 실행](#springboot-프로젝트-실행)
    * [JAVA(SpringBoot) (SDK) 실행](#javaspringboot-sdk-실행)
    * [Python(SDK) 실행](#pythonsdk-실행)
        + [파이썬 가상환경 설정](#파이썬-가상환경-설정)
        + [pip 업데이트](#pip-업데이트)
        + [플라스크 설치](#플라스크-설치)
        + [opentelemetry 필요 라이브러리 설치](#opentelemetry-필요-라이브러리-설치)
        + [플라스크 실행(windows)](#플라스크-실행windows)
    * [테스트](#테스트)
        + [SrpingBoot(agent) 환경 접속](#srpingbootagent-환경-접속)
        + [SrpingBoot(sdk) 환경 접속](#srpingbootsdk-환경-접속)
        + [python 환경 접속](#python-환경-접속)
        + [로그 확인](#로그-확인)

# windows 

## 코드 내려받기
```git
git clone https://github.com/kkimsungchul/otel.git
```
## 사전 환경 구성 및 유의사항
- java 17 
- python 3.x
- 파이썬 경로에 한글이 있으면 안됨


## collector 실행

### collector 경로 이동
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-collector
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

## prometheus 실행
※ 프로메테우스의 경우 현재 깃 리파지토리에 포함되어 있지 않으며, 별도 설치 후 config만 변경해주면 바로 사용 가능
### 경로 이동
```shell
cd C:\Users\sung\Desktop\otel\prometheus-2.52.0.windows-amd64
```
### 실행
```shell
prometheus.exe
```


## JAVA(SpringBoot) (agent) 실행
### otel 변수 설정
```
set JAVA_TOOL_OPTIONS=-javaagent:C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent\opentelemetry-javaagent.jar
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
cd C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent
java -jar otel-sdk-test-springboot.jar
```
---

## JAVA(SpringBoot) (SDK) 실행
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent

```
---
## Python(SDK) 실행

### 파이썬 가상환경 설정
※ 경로에 한글이 있으면 실행이 안됨
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-test-pytyon-run
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

### opentelemetry 필요 라이브러리 설치
```shell
pip install opentelemetry-distro
opentelemetry-bootstrap -a install
pip install opentelemetry-exporter-otlp
pip install opentelemetry-exporter-otlp-proto-grpc
pip install opentelemetry-api
pip install opentelemetry-sdk
pip install opentelemetry-instrumentation-flask
pip install opentelemetry-exporter-prometheus
```


### 플라스크 실행(windows)
※ 윈도우의 경우에는 opentelemetry-instrument 의 실행 옵션을 여러줄로 작성하면 오류가 발생할수 있으므로 한줄로 실행함
※ 경로에 한글이 있으면 실행이 안됨
```shell
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument --service_name=python-roll-dice-service --metric_export_interval=1000 --traces_exporter=otlp --metrics_exporter=otlp --logs_exporter=otlp --exporter_otlp_endpoint=http://127.0.0.1:4317 --exporter_otlp_traces_endpoint=http://127.0.0.1:4317 --exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 --exporter_otlp_logs_endpoint=http://127.0.0.1:4317 --exporter_otlp_protocol=grpc flask run -p 18080
```

- 아래는 가시성 확보를 위해 여러줄로 작성
```shell
set OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument `
--service_name=python-roll-dice-service `
--metric_export_interval=1000 `
--traces_exporter=otlp `
--metrics_exporter=otlp `
--logs_exporter=otlp `
--exporter_otlp_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_traces_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_logs_endpoint=http://127.0.0.1:4317 `
--exporter_otlp_protocol=grpc `
flask run -p 18080
```


---
## 테스트

### SrpingBoot(agent) 환경 접속
- http://localhost:8080/java/rolldice

### SrpingBoot(sdk) 환경 접속
- http://localhost:19090/java-custom/rolldice?rolls=12

### python 환경 접속
- http://localhost:18080/python/rolldice

### 로그 확인
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-collector
```
- 아래의 파일 확인<br>
example.log
