package sungchul.com.otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;

import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;

import java.time.Duration;

@SpringBootApplication
public class OtelTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtelTestApplication.class, args);
    }

//    @Bean
//    public OpenTelemetry openTelemetry() {
//        return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
//    }

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "dice-server").put(ResourceAttributes.SERVICE_VERSION, "0.1.0").build();

//        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
//                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
//                .setResource(resource)
//                .build();
//
////        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
////                .registerMetricReader(PeriodicMetricReader.builder(LoggingMetricExporter.create()).build())
////                .setResource(resource)
////                .build();
//        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//                .registerMetricReader(
//                    PeriodicMetricReader
//                        .builder(LoggingMetricExporter.create())
//                        // Default is 60000ms (60 seconds). Set to 10 seconds for demonstrative purposes only.
//                        .setInterval(Duration.ofSeconds(10)).build())
//                        .setResource(resource)
//                .build();
//
//        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
//                .addLogRecordProcessor(BatchLogRecordProcessor.builder(SystemOutLogRecordExporter.create()).build())
//                .setResource(resource)
//                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
                .setResource(resource)
                .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(
                        PeriodicMetricReader
                            .builder(OtlpGrpcMetricExporter.builder().build())
                            .setInterval(Duration.ofSeconds(10)).build())
                .setResource(resource)
                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder().build()).build())
                .setResource(resource)
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
                .buildAndRegisterGlobal();


        return openTelemetry;
    }

}
