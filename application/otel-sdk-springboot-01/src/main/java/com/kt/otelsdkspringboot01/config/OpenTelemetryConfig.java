package com.kt.otelsdkspringboot01.config;

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
                .put(ResourceAttributes.SERVICE_NAME, "otel-sdk-springboot-01-service")    //서비스명 설정
                .put(ResourceAttributes.SERVICE_VERSION, "1.0.0")       //서비스 버전 설정
                .build();

        //OpenTelemetry SDK 초기화
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(
                                OtlpGrpcSpanExporter.builder()
                                        .setEndpoint("http://127.0.0.1:9999")
                                        .build()
                        ).build()
                ).setResource(resource)
                .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(
                        PeriodicMetricReader
                                .builder(
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
        return openTelemetry.getTracer(prefix+"com.kt.otelsdkspringboot01");
    }

    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter(prefix+"com.kt.otelsdkspringboot01");
    }



    private void configureMetrics(Meter meter) {
        meter.gaugeBuilder(prefix+"jvm.memory.totalMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
        meter.gaugeBuilder(prefix+"jvm.memory.usedMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
        meter.gaugeBuilder(prefix+"jvm.memory.freeMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
        meter.gaugeBuilder(prefix+"jvm.memory.heapUsage")
                .buildWithCallback(measurement -> measurement.record(getHeapUsage()));
//        io.micrometer.core.instrument.Meter cpuUsageMeter = meterRegistry.find("system.cpu.usage").meter();
        meter.gaugeBuilder(prefix+"system.cpu.usage")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("system.cpu.usage").gauge().value()));
//        io.micrometer.core.instrument.Meter memoryUsageMeter = meterRegistry.find("jvm.memory.used").meter();
        meter.gaugeBuilder(prefix+"jvm.memory.used")
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
