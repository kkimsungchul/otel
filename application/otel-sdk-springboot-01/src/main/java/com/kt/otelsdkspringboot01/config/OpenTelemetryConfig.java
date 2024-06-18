package com.kt.otelsdkspringboot01.config;

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
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenTelemetryConfig {

    private static final long METRIC_EXPORT_INTERVAL_MS = 800L;

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
                                )
                                .setInterval(Duration.ofMillis(500))
                                .build())
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
        return openTelemetry;
    }
    
    //AOP에서 사용하기 위해 Bean 등록
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("com.kt.otelsdkspringboot01");
    }

    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter("com.kt.otelsdkspringboot01");
    }

//    @Bean
//    public void setGauge (OpenTelemetry openTelemetry){
//        Meter meter = openTelemetry.getMeter("com.kt.otelsdkspringboot01");
//        ObservableDoubleGauge heapSizeGauge = meter.gaugeBuilder("jvm.memory.totalMemory")
//                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
//        ObservableDoubleGauge heapMaxSizeGauge = meter.gaugeBuilder("jvm.memory.freeMemory")
//                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
//        ObservableDoubleGauge heapFreeSizeGauge = meter.gaugeBuilder("jvm.memory.usedMemory")
//                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
//    }
//
//    public Map<String,Double> getMemory(){
//        Map<String,Double> memoryMap = new HashMap<>();
//        //메모리는 byte 단위로 반환, 1024로 두번나누면 kb - mb 로 변환
//        double totalMemory = Runtime.getRuntime().totalMemory()/1024/1024;
//        double freeMemory =Runtime.getRuntime().freeMemory()/1024/1024;
//        double usedMemory =totalMemory - freeMemory;
//        memoryMap.put("totalMemory",totalMemory);
//        memoryMap.put("usedMemory",usedMemory);
//        memoryMap.put("freeMemory",freeMemory);
//        return memoryMap;
//    }

}
