# Otel 테스트 환경 실행(roll-dice)

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
    * [JAVA(Tomcat) (Agent) 실행](#javatomcat-agent-실행) 
    * [Python(SDK) 실행](#pythonsdk-실행)
        + [파이썬 가상환경 설정](#파이썬-가상환경-설정)
        + [pip 업데이트](#pip-업데이트)
        + [플라스크 설치](#플라스크-설치)
        + [opentelemetry 필요 라이브러리 설치](#opentelemetry-필요-라이브러리-설치)
        + [플라스크 실행(windows)](#플라스크-실행windows)
    * [테스트](#테스트)
        + [SpringBoot(agent) 환경 접속](#springbootagent-환경-접속)
        + [SpringBoot(sdk) 환경 접속](#springbootsdk-환경-접속)
        + [python 환경 접속](#python-환경-접속)
        + [로그 확인](#로그-확인)

# windows Spring

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
set OTEL_SERVICE_NAME =Springboot-agent-test
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
```

### SpringBoot 프로젝트 실행
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent
java -jar otel-test-springboot.jar
```
---

## JAVA(SpringBoot) (SDK) 실행
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent
java -jar otel-sdk-test-springboot.jar
```

## JAVA(Tomcat) (Agent) 실행
- tomcat의 경우에는 [Otel]Tomcat Agent 적용.md 파일을 참고하여 톰캣 설치 및 세팅 완료 후 아래의 명령어 실행
```shell
cd C:\Users\sung\Desktop\otel\OTel-git\otel-java-agent\apache-tomcat-9.0.89\bin
startup.bat
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

### SpringBoot(agent) 환경 접속
- http://localhost:8080/java/rolldice

### SpringBoot(sdk) 환경 접속
- http://localhost:19090/java-custom/rolldice?rolls=12

### python 환경 접속
- http://localhost:18080/python/rolldice

### tomcat 환경 접속
- http://localhost:9080/

### 로그 확인
```shell
cd C:\Users\sung\Desktop\개발\otel\test\otel\otel-collector
```
- 아래의 파일 확인<br>
example.log

---
# [Otel]JAVA-Agent_사용-windows.txt

[ 공식문서 따라하기 ]

### java 예제
	https://opentelemetry.io/docs/languages/java/getting-started/
	https://opentelemetry.io/docs/languages/java/instrumentation/

### java
	https://github.com/open-telemetry/opentelemetry-java

### JAVA 서버에 agent 추가 방법
	https://opentelemetry.io/docs/languages/java/automatic/server-config/

### sdk
	
	https://opentelemetry.io/docs/languages/java/
	java의 경우 1.8이 미니멈임
=====================================================================
### 참고사항
	권장사양
	https://github.com/open-telemetry/opentelemetry-java/blob/main/VERSIONING.md#language-version-compatibility
	https://opentelemetry.io/docs/demo/development/

	
### 프로젝트 생성 및 빌드
	- 스프링부트 프로젝트로 생성했으며, 별도의 라이브러리 임포트 없음,다만 로깅내용만 간단히 남김
	- jar 로 빌드하여 실행 파일 생성
	- 컨트롤러 하나를 생성해 두고, 사용자가 접속할수 있도록 설정
	ex) http://localhost:8080/rolldice

### agent jar 다운로드
	java 에이전트임
	https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

### agent 다운로드 후 cmd창에서 아래와 같이 설정
	설정 관련 정보 링크 : 
		https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#logging-exporter
	엔드포인트(콜렉터) 설정 방법 예시 : 
		https://opentelemetry.io/docs/collector/deployment/agent/
	

	- 데스크탑 (김성철)
	=====================================================================
	set JAVA_TOOL_OPTIONS=-javaagent:C:\Users\sung\Desktop\개발\otel\OTel-git\otel-java-agent\opentelemetry-javaagent.jar
	set OTEL_METRIC_EXPORT_INTERVAL=1000
	set OTEL_TRACES_EXPORTER=otlp
	set OTEL_METRICS_EXPORTER=otlp
	set OTEL_LOGS_EXPORTER=otlp
	set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
	=====================================================================
	
	-노트북 (김성철)
	==========================================================================
	set JAVA_TOOL_OPTIONS=-javaagent:C:\IntelliJ_Project\otel-util\opentelemetry-javaagent.jar
	set OTEL_METRIC_EXPORT_INTERVAL=15000
	set OTEL_TRACES_EXPORTER=otlp
	set OTEL_METRICS_EXPORTER=otlp
	set OTEL_LOGS_EXPORTER=otlp
	set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
	set OTEL_EXPORTER_OTLP_PROTOCOL=grpc

	==========================================================================

	- 콘솔에만 출력
	=====================================================================
	set JAVA_TOOL_OPTIONS=-javaagent:C:\Users\sung\Desktop\개발\otel\OTel-git\otel-java-agent\opentelemetry-javaagent.jar
	set OTEL_TRACES_EXPORTER=logging
	set OTEL_METRICS_EXPORTER=logging
	set OTEL_LOGS_EXPORTER=logging
	set OTEL_METRIC_EXPORT_INTERVAL=1000
	=====================================================================

	- 프로메테우스로 바로 전송
		set OTEL_METRICS_EXPORTER=prometheus
		set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317 <- 라인제거
	
	※ 기본값
		set OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
	※ 여기서 삽질 오지게함.
		http/protobuf 로 설정하고 보내면 콜렉터에서 받질못햇음.
		뭐 아무리 설정을 뒤져봐도 안됐음.
		프로토콜을 grpc로 바꾸고 콜렉터 설정에도 grpc를 넣어주면 작동함
	
	※ TRACES , METRICS , LOGS 의 ENDPOINT가 똑같다면 아래의 옵션 하나로 설정 가능
		set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
		
		---아래 세개의 옵션은 TRACES , METRICS , LOGS 가 각각 다를떄 설정
		set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
		set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
		set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317


### 위 설정 완료 후 프로젝트 파일 실행
	java -jar otel-test-springboot.jar
	=====================================================================
	[otel.javaagent 2024-05-09 00:18:05:280 +0900] [PeriodicMetricReader-1] INFO io.opentelemetry.exporter.logging.LoggingMetricExporter - metric: ImmutableMetricData{resource=Resource{schemaUrl=https://opentelemetry.io/schemas/1.24.0, attributes={host.arch="amd64", host.name="sung-PC", os.description="Windows 10 10.0", os.type="windows", process.command_line="C:\Program Files\Java\jdk-17.0.2\bin\java.exe -javaagent:C:\Users\sung\Desktop\개발\otel\opentelemetry-javaagent.jar -jar otel-0.0.1-SNAPSHOT.jar", process.executable.path="C:\Program Files\Java\jdk-17.0.2\bin\java.exe", process.pid=13864, process.runtime.description="Oracle Corporation OpenJDK 64-Bit Server VM 17.0.2+8-86", process.runtime.name="OpenJDK Runtime Environment", process.runtime.version="17.0.2+8-86", service.instance.id="33a15e56-8598-44bd-b586-6daf91fcfcca", service.name="otel-test", service.version="0.0.1-SNAPSHOT", telemetry.sdk.language="java", telemetry.sdk.name="opentelemetry", telemetry.sdk.version="1.37.0"}}, instrumentationScopeInfo=InstrumentationScopeInfo{name=io.opentelemetry.tomcat-10.0, version=2.3.0-alpha, schemaUrl=null, attributes={}}, name=http.server.request.duration, description=Duration of HTTP server requests., unit=s, type=HISTOGRAM, data=ImmutableHistogramData{aggregationTemporality=CUMULATIVE, points=[ImmutableHistogramPointData{getStartEpochNanos=1715181305211707500, getEpochNanos=1715181485223774900, getAttributes={http.request.method="GET", http.response.status_code=200, http.route="/rolldice", network.protocol.version="1.1", url.scheme="http"}, getSum=0.20853419999999998, getCount=2, hasMin=true, getMin=0.0043199, hasMax=true, getMax=0.2042143, getBoundaries=[0.005, 0.01, 0.025, 0.05, 0.075, 0.1, 0.25, 0.5, 0.75, 1.0, 2.5, 5.0, 7.5, 10.0], getCounts=[1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0], getExemplars=[]}]}}
	[otel.javaagent 2024-05-09 00:21:35:279 +0900] [PeriodicMetricReader-1] INFO io.opentelemetry.exporter.logging.LoggingMetricExporter - metric: ImmutableMetricData{resource=Resource{schemaUrl=https://opentelemetry.io/schemas/1.24.0, attributes={host.arch="amd64", host.name="sung-PC", os.description="Windows 10 10.0", os.type="windows", process.command_line="C:\Program Files\Java\jdk-17.0.2\bin\java.exe -javaagent:C:\Users\sung\Desktop\개발\otel\opentelemetry-javaagent.jar -jar otel-0.0.1-SNAPSHOT.jar", process.executable.path="C:\Program Files\Java\jdk-17.0.2\bin\java.exe", process.pid=13864, process.runtime.description="Oracle Corporation OpenJDK 64-Bit Server VM 17.0.2+8-86", process.runtime.name="OpenJDK Runtime Environment", process.runtime.version="17.0.2+8-86", service.instance.id="33a15e56-8598-44bd-b586-6daf91fcfcca", service.name="otel-test", service.version="0.0.1-SNAPSHOT", telemetry.sdk.language="java", telemetry.sdk.name="opentelemetry", telemetry.sdk.version="1.37.0"}}, instrumentationScopeInfo=InstrumentationScopeInfo{name=io.opentelemetry.tomcat-10.0, version=2.3.0-alpha, schemaUrl=null, attributes={}}, name=http.server.request.duration, description=Duration of HTTP server requests., unit=s, type=HISTOGRAM, data=ImmutableHistogramData{aggregationTemporality=CUMULATIVE, points=[ImmutableHistogramPointData{getStartEpochNanos=1715181305211707500, getEpochNanos=1715181695225332200, getAttributes={http.request.method="GET", http.response.status_code=200, http.route="/rolldice", network.protocol.version="1.1", url.scheme="http"}, getSum=0.20853419999999998, getCount=2, hasMin=true, getMin=0.0043199, hasMax=true, getMax=0.2042143, getBoundaries=[0.005, 0.01, 0.025, 0.05, 0.075, 0.1, 0.25, 0.5, 0.75, 1.0, 2.5, 5.0, 7.5, 10.0], getCounts=[1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0], getExemplars=[]}]}}
	=====================================================================


### 콜렉터 실행
	[Otel]otel-collector-설치-및-사용.txt 파일 참고

---
# [Otel]python-사용-windows.txt
python.exe -m pip install --upgrade pip

### 윈도우에서 가상 환경 진입
venv\Scripts\activate

### 플라스크 설치
pip install flask

### 플라스크 실행
flask run -p 9090

### opentelemetry-distro  설치
pip install opentelemetry-distro
opentelemetry-bootstrap -a install


### opentelemetry-exporter-otlp 설치
pip install opentelemetry-exporter-otlp


### 플라스크 실행(windows)
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


※ 아래와 같이 설정하면 프로메테우스로 바로 전송함
--exporter_otlp_metrics_endpoint=http://127.0.0.1:4317 `< 해당 라인을 제거하고
--metrics_exporter=prometheus <프로메테우스로 변경
