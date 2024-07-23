## SpringBoot 적용 방법


### SpringBoot Manual(SDK)
1. 라이브러리 추가
```groovy
dependencyManagement {
    imports {
        mavenBom("io.opentelemetry:opentelemetry-bom:1.39.0")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.4.0-alpha")
    }
}


dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    implementation "io.opentelemetry:opentelemetry-api"
    implementation "io.opentelemetry:opentelemetry-sdk"
    implementation "io.opentelemetry:opentelemetry-exporter-logging"
    implementation "io.opentelemetry:opentelemetry-exporter-otlp"
    implementation "io.opentelemetry.semconv:opentelemetry-semconv"
    implementation "io.opentelemetry:opentelemetry-sdk-extension-autoconfigure"
    implementation 'io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0:2.5.0-alpha'

    //implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter'

    //아래의 라이브러리 추가시 별도 설정 없이 JPA 계측 가능
    implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot:2.4.0-alpha'


    implementation 'io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.4.0'
    implementation 'io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations-support:2.4.0-alpha'

}
```

2. config class 작성
- 해당 클래스는 opentelemetry의 설정에 관련된 정보를 가지고 있음
```java
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@DependsOn("micrometerConfig")
public class OpenTelemetryConfig {

    private static final long METRIC_EXPORT_INTERVAL_MS = 800L;
    private static final Logger logger = LogManager.getLogger(OpenTelemetryConfig.class.getName());

    String prefix = "sdk_spr_";
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final long NO_HEAP_LIMIT = -1;

    @Autowired
    private MeterRegistry meterRegistry;

    @Bean
    public OpenTelemetry openTelemetry() {

        Resource resource = Resource.getDefault()
                .toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, "otel-sdk-springboot-01-service")     //서비스명 설정
                .put(ResourceAttributes.SERVICE_VERSION, "1.0.0")                           //서비스 버전 설정
                .build();

        //OpenTelemetry SDK 초기화
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(
                        BatchSpanProcessor.builder(
                                OtlpGrpcSpanExporter.builder()
                                        .setEndpoint("http://127.0.0.1:9999")
                                        .build()
                        ).build()
                ).setResource(resource)
                .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
            .registerMetricReader(
                PeriodicMetricReader.builder(
                        OtlpGrpcMetricExporter.builder()
                                .setEndpoint("http://127.0.0.1:9999")
                                .build()
                    ).setInterval(Duration.ofMillis(500)
                    ).build()
            )
        .setResource(resource)
        .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(
                                OtlpGrpcLogRecordExporter.builder()
                                        .setEndpoint("http://127.0.0.1:9999")
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

        OpenTelemetryAppender.install(openTelemetry);
        // 메트릭 설정 추가
        configureMetrics(openTelemetry.getMeter("com.kt.otelsdkspringboot01"));
        return openTelemetry;
    }

    //AOP에서 사용하기 위해 Bean 등록
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer(prefix + "com.kt.otelsdkspringboot01");
    }

    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter(prefix + "com.kt.otelsdkspringboot01");
    }


    //메트릭 데이터를 수집하기 위해 작성
    private void configureMetrics(Meter meter) {
        meter.gaugeBuilder(prefix + "jvm.memory.totalMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
        meter.gaugeBuilder(prefix + "jvm.memory.usedMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
        meter.gaugeBuilder(prefix + "jvm.memory.freeMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
        meter.gaugeBuilder(prefix + "jvm.memory.heapUsage")
                .buildWithCallback(measurement -> measurement.record(getHeapUsage()));
        meter.gaugeBuilder(prefix + "system.cpu.usage")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("system.cpu.usage").gauge().value()));
        meter.gaugeBuilder(prefix + "jvm.memory.used")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("jvm.memory.used").gauge().value() / 1024 / 1024));
    }

    public Map<String, Double> getMemory() {
        Map<String, Double> memoryMap = new HashMap<>();
        double totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        double freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        double usedMemory = totalMemory - freeMemory;
        memoryMap.put("totalMemory", totalMemory);
        memoryMap.put("usedMemory", usedMemory);
        memoryMap.put("freeMemory", freeMemory);
        return memoryMap;
    }

    public double getHeapUsage() {
        MemoryUsage heapProps = memoryMXBean.getHeapMemoryUsage();
        long heapUsed = heapProps.getUsed();
        long heapMax = heapProps.getMax();

        if (heapMax == NO_HEAP_LIMIT) {
            if (logger.isDebugEnabled()) {
                logger.debug("No maximum heap is set");
            }
            return NO_HEAP_LIMIT;
        }

        double heapUsage = (double) heapUsed / heapMax;
        if (logger.isDebugEnabled()) {
            logger.debug("Current heap usage is {0} percent" + (heapUsage * 100));
        }
        return heapUsage;
    }
}

```
3. child span 생성 util 클래스 작성
- 해당 클래스는 하위(child) span을 생성할때 편리하게 사용하려고 작성한 클래스
- 필요하지 않으면 작성하지 않아도 되며, 직접 Span.current(); 을 사용하여 child span을 메서드 내에서 생성해도 됨
```java
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpanUtils {

    @Autowired
    private final Tracer tracer;

    public Span getChildSpan(String spanName){
        Span parentSpan = Span.current();
        Span childSpan = tracer.spanBuilder(spanName)
                .setParent(Context.current().with(parentSpan))
                .startSpan();
        return childSpan;
    }
}
```

3. logabck 적용
- opentelemetry에서는 기본적으로 log4j와 logback 연동을 지원해 주고 있음
- 아래의 내용은 logback 설정 부분 
- logback 사용시 opentelemetry-logback-appender 라이브러리 추가 필수
- ※ log4j2 사용시 적용이 잘 안되는 부분이 있음. logback 권장
- 공식 가이드 문서 : https://opentelemetry.io/docs/languages/java/instrumentation/#log-appenders
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>
    <appender name="OpenTelemetry" class="io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender">
        <captureExperimentalAttributes>true</captureExperimentalAttributes>
        <captureMdcAttributes>*</captureMdcAttributes>
    </appender>

    <root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="OpenTelemetry"/>
    </root>

</configuration>
```

4. 원하는 위치에 매트릭 및 span 생성
- 아래 컨트롤러와 서비스는 예시로 작성한 코드임
- child span 을 생성해야 하나의 trace에서 트리구조로 확인이 가능
- controller
```java
    @GetMapping("")
    public List<Board> all() {
        logger.info("### HIHIHIHIHI");
        Span childSpan = spanUtils.getChildSpan("BoardController.all");
        //새로 생성한 span이 상위 span과 일치하는지 확인
        try (Scope scope = childSpan.makeCurrent()) {
            // Your business logic
            return boardService.all();
        } catch (Exception e) {
            childSpan.recordException(e);
            throw e;
        } finally {
            childSpan.end();
        }
    }
```
- service

```java
     public List<Board> all(){
    Span childSpan = spanUtils.getChildSpan("BoardService.all");
    List<Board> boardList = null;
    try (Scope scope = childSpan.makeCurrent()) {
        LogApi logApi = logService.save();
        PageRequest pageRequest = PageRequest.of(0, 100);
        boardList = boardRepository.findAll(pageRequest).getContent();
        logService.update(logApi);

    } catch (Exception e) {
        childSpan.recordException(e);
        throw e;
    } finally {
        childSpan.end();
    }
    return boardList;
}
```


### SpringBoot Auto(Agent)
1. Agent 다운로드
- 공식 문서 : https://opentelemetry.io/docs/zero-code/java/agent/
- 다운로드 링크 : https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

3. springboot.jar 파일 실행 시 환경변수 적용
- 환경변수 참고 링크 : https://opentelemetry.io/docs/languages/java/configuration/
- 환경변수 참고 링크 : https://github.com/open-telemetry/opentelemetry-dotnet-instrumentation/blob/main/docs/config.md
```shell
JAVA_TOOL_OPTIONS=-javaagent:..\..\opentelemetry-javaagent.jar
set OTEL_METRIC_EXPORT_INTERVAL=1000
set OTEL_TRACES_EXPORTER=otlp
set OTEL_METRICS_EXPORTER=otlp
set OTEL_LOGS_EXPORTER=otlp
set OTEL_SERVICE_NAME =otel-agent-springboot-01
set OTEL_EXPORTER_OTLP_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://127.0.0.1:9999
set OTEL_EXPORTER_OTLP_PROTOCOL=grpc
java -jar otel-agent-springboot-01-0.0.1-SNAPSHOT.jar
```

### SpringBoot Auto(Library)
1. 라이브러리 추가
```groovy
dependencyManagement {
	imports {
		mavenBom("io.opentelemetry:opentelemetry-bom:1.39.0")
		mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.4.0-alpha")
	}
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'


	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter'
	implementation 'io.opentelemetry.instrumentation:opentelemetry-log4j-appender-2.17'
}
```

2. yaml 파일에 설정 추가
```yaml
logging:
  level:
    io.opentelemetry: DEBUG
    io.opentelemetry.instrumentation.spring: TRACE

# https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/out-of-the-box-instrumentation/
otel:
  sdk:
    metrics:
      period: 500
    traces:
      period: 500
  service:
    name: otel-auto-springboot-01-service
  resource:
    attributes:
      deployment:
        environment: dev
      service:
        name: otel-auto-springboot-01
      env: dev
  instrumentation:
    annotations:
      enabled: true
    methods:
      enabled: true
    micrometer:
      enabled: true
    spring:
      enabled : true
    spring-core:
      enabled: true
    spring-web:
      enabled: true
    spring-webmvc:
      enabled: true
    spring-webflux:
      enabled: true
    kafka:
      enabled: true
  exporter:
    otlp:
      protocol : grpc
      endpoint: http://127.0.0.1:9999 # Opentelemetry Collector
  metrics:
    exporter: otlp, logging
  logs:
    exporter: otlp
  traces:
    exporter: otlp
  log:
    level: debug
  propagators:
    tracecontext
```

3. @WithSpan 어노테이션 적용
- yaml 파일에 설정을 다 적용해 보았지만 컨트롤러와 서비스(JAVA) 부분에서 span 생성이 안되었음, 그래서 수동으로 span 생성 
- @WithSpan 어노테이션을 적용하여 자동으로 span 생성을 하도록 함

- controller
```java
    @WithSpan
    @GetMapping("/normal")
    public boolean normalTest(){
        return replyService.getReply(false);
    }
```
- service
```java
    @WithSpan
    public List<Board> boardServiceSlow(){
        LogApi logApi = logService.save();
        List<Board> boardList = boardRepository.findAll();
        logService.update(logApi);

        return boardList;
    }
```


