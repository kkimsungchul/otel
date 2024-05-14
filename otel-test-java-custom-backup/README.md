# SpringBoot Customizing - opentelemetry SDK 사용

## 목차
- [공식문서 및 참고 문서](#공식문서-및-참고-문서)
    * [JAVA-OpenTelemetry SDK](#java-opentelemetry-sdk)
    * [java-otlp 설정](#java-otlp-설정)
    * [java-span 설정](#java-span-설정)
    * [java-metrics 설정](#java-metrics-설정)
    * [open-telemetry logback 사용](#open-telemetry-logback-사용)
- [프로젝트 개발 및 실행 환경](#프로젝트-개발-및-실행-환경)
- [의존성 추가](#의존성-추가)
- [Spring Bean에 OpenTelemetry 등록](#spring-bean에-opentelemetry-등록)
- [Sample 코드 작성](#sample-코드-작성)
    * [Sample 코드 위치](#sample-코드-위치)
        + [컨트롤러](#컨트롤러)
        + [서비스](#서비스)
- [exporter 설정 방법](#exporter-설정-방법)
- [빌드](#빌드)
- [실행 전 유의사항](#실행-전-유의사항)
- [실행](#실행)
- [접속](#접속)
- [테스트 및 확인](#테스트-및-확인)

---
## 공식문서 및 참고 문서
### JAVA-OpenTelemetry SDK
- https://opentelemetry.io/docs/languages/java/instrumentation/
### java-otlp 설정
- https://opentelemetry.io/docs/languages/java/exporters/#otlp-dependencies
### java-span 설정
※ span은 추적(Traces)에 사용됨
- https://opentelemetry.io/docs/languages/java/instrumentation/#create-spans

※ span(Traces) 설명
- https://opentelemetry.io/docs/concepts/signals/traces/#tracer

### java-metrics 설정
- https://opentelemetry.io/docs/languages/java/instrumentation/#initialize-metrics

### open-telemetry logback 사용
※ 속성 및 가이드
- https://opentelemetry.io/docs/languages/java/automatic/spring-boot/#logback

※ readme.md
- https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/instrumentation/logback/logback-appender-1.0/library/README.md

※ maven 라이브러리
- ps://mvnrepository.com/artifact/io.opentelemetry.instrumentation/opentelemetry-logback-appender-1.0/2.3.0-alpha

---
## 프로젝트 개발 및 실행 환경
- windows 10
- SpringBoot 3.2.5
- Open JDK 17.0.2
- Gradle 8.7

※ OpenTelemetry Java 자체는 Java 8 버전 이상에서 사용 가능

---
## 의존성 추가
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation platform("io.opentelemetry:opentelemetry-bom:1.37.0")
    implementation "io.opentelemetry:opentelemetry-api"
    implementation "io.opentelemetry:opentelemetry-sdk"
    //logging 사용
    implementation "io.opentelemetry:opentelemetry-exporter-logging"
    implementation "io.opentelemetry.semconv:opentelemetry-semconv:1.25.0-alpha"
    implementation "io.opentelemetry:opentelemetry-sdk-extension-autoconfigure"
    //otlp 사용
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
    //logback 사용
    runtimeOnly 'io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0:2.3.0-alpha'
}
```
---
## Spring Bean에 OpenTelemetry 등록
※ 내용이 길어 아래의 파일 참고
- 경로 : otel-test-java-custom/src/main/java/sungchul/com/otel/
- 파일명 : OtelTestApplication.java
---
## Sample 코드 작성
### Sample 코드 위치
#### 컨트롤러
※ 코드 내에 주석이 포함되어 있음
- 경로 : otel-test-java-custom/src/main/java/sungchul/com/otel/controller/
- 파일명 : RollController.java
#### 서비스
- 경로 : otel-test-java-custom/src/main/java/sungchul/com/otel/service/
- 파일명 : DiceService.java
---
## exporter 설정 방법
- exporter 설정방법은 세가지가 존재함
1. JAVA 환경 변수로 설정

어플리케이션 실행 시 아래와 같이 환경변수로 설정 후 실행
```shell
set OTEL_METRIC_EXPORT_INTERVAL=1000
set OTEL_TRACES_EXPORTER=otlp
set OTEL_METRICS_EXPORTER=otlp
set OTEL_LOGS_EXPORTER=otlp
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:4317
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
java -jar otel-0.0.1-SNAPSHOT.jar
```
2. java 파일 내에 설정
- 경로 : otel-test-java-custom/src/main/java/sungchul/com/otel/
- 파일명 : OtelTestApplication.java

※ 예시로 Metric만 기재하였으며, 다른 설정들은 해당 파일 참고
```java
// otlp 사용 설정 시작
SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        .addSpanProcessor(BatchSpanProcessor.builder(
                        OtlpGrpcSpanExporter.builder()
//                            .setEndpoint("http://127.0.0.1:4317")
                                .build()
                ).build()
        ).setResource(resource)
        .build();

SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
        .registerMetricReader(
                PeriodicMetricReader
                        .builder(
                                OtlpGrpcMetricExporter.builder()
//                                .setEndpoint("http://127.0.0.1:4317")
                                        .build()
                        )
                        //yaml파일의 설정이 적용 안되므로 아래에서 적용
                        .setInterval(Duration.ofMillis(1000))
                        .build())
        .setResource(resource)
        .build();
SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
        .addLogRecordProcessor(
                BatchLogRecordProcessor.builder(
                        OtlpGrpcLogRecordExporter.builder()
//                            .setEndpoint("http://127.0.0.1:4317")
                                .build()
                ).build()
        ).setResource(resource)
        .build();

OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
        .setTracerProvider(sdkTracerProvider)
        .setMeterProvider(sdkMeterProvider)
        .setLoggerProvider(sdkLoggerProvider)
        .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
        .buildAndRegisterGlobal();
// otlp 사용 설정 종료
```

3. application.yml 또는 application.properties 파일에 설정

※ yaml 파일의 경우 interval 옵션이 적용 안됨<br>
※ java파일과 yaml파일을 같이 사용해야 함

```yaml
otel:
  traces:
    exporter: otlp
  metrics:
    exporter: otlp
  logs:
    exporter: otlp
  exporter:
    otlp:
      endpoint: 127.0.0.1:4317
  metric:
    export:
      interval: 10000 #옵션이 적용 안됨
```

※ 설정 옵션 링크
- https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md#otlp-exporter-span-metric-and-log-exporters
---
## 빌드
```shell
gradlew.bat assemble
```
---
## 실행 전 유의사항
- 프로메테우스가 실행되어 있어야 함
- otel collector 가 실행되어 있어야 함

※ 단독으로 실행 가능하지만, 데이터를 전송하지 못하므로 오류로그 발생함
```text
WARN  i.o.e.internal.grpc.GrpcExporter - Failed to export metrics. Server responded with gRPC status code 2. Error message: Failed to connect to localhost/[0:0:0:0:0:0:0:1]:4317
```
---
## 실행
```shell
java -jar ./build/libs/otel-0.0.1-SNAPSHOT.jar
```
---
## 접속
http://localhost:19090/java-custom/rolldice?rolls=12
---
## 테스트 및 확인
- 위 접속 URL 호출 시 span(Traces) 데이터는 collector에서 설정한 파일에 생성이 됨
- - 현재는 example.log 파일명으로 저장됨
- 프로메테우스의 http://localhost:9464/metrics 로 접속 시 metric 데이터 확인 가능

