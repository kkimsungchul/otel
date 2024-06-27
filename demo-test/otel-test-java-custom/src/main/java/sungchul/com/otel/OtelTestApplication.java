package sungchul.com.otel;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class OtelTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtelTestApplication.class, args);
    }


    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault()
                .toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, "java-custom-dice-service")    //서비스명 설정
                .put(ResourceAttributes.SERVICE_VERSION, "0.1.0")       //서비스 버전 설정
                .build();

//    @Bean
//        public OpenTelemetry openTelemetry() {
//            Resource resource = Resource.getDefault()
//                    .merge(ContainerResource.get())
//                    .merge(HostResource.get())
//                    .merge(OsResource.get())
//                    .merge(ProcessResource.get())
//                    .merge(ProcessRuntimeResource.get())
//                    .merge(Resource.create(Attributes.builder()
//                            .put(ResourceAttributes.SERVICE_NAME, "java-custom-dice-service")    //서비스명 설정
//                            .put(ResourceAttributes.SERVICE_VERSION, "0.1.0")       //서비스 버전 설정
//                            .build()
//                    ));

        //아래의 주석은 어플리케이션의 콘솔에 Otel 정보를 출력할 때 사용함
/*
         SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                .setResource(resource)
                .build();

//        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//                .registerMetricReader(PeriodicMetricReader.builder(LoggingMetricExporter.create()).build())
//                .setResource(resource)
//                .build();
        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(
                    PeriodicMetricReader
                        .builder(LoggingMetricExporter.create())
                        // Default is 60000ms (60 seconds). Set to 10 seconds for demonstrative purposes only.
                        .setInterval(Duration.ofSeconds(10)).build())
                        .setResource(resource)
                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(BatchLogRecordProcessor.builder(SystemOutLogRecordExporter.create()).build())
                .setResource(resource)
                .build();
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
                .buildAndRegisterGlobal();                
*/

        //아래부터는 otlp를 사용하여 콜렉터로 데이터를 전송할때 사용
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
                            .setInterval(Duration.ofMillis(500))
                            .build())
                .setResource(resource)
                .build();

        //아래의 설정은 collector를 거치지 않고 prometheus로 바로 전송할때 사용
        //프로메테우스로 바로 전송 설정 시작
//        int prometheusPort = 9464;
//        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//                .registerMetricReader(PrometheusHttpServer.builder()
//                        .setHost("127.0.0.1")
//                        .setPort(prometheusPort).build())
//                .setResource(resource)
//                .build();
        //프로메테우스로 바로 전송 설정 종료

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                    BatchLogRecordProcessor.builder(
                        OtlpGrpcLogRecordExporter.builder()
//                            .setEndpoint("http://127.0.0.1:4317")
                            .build()
                    ).build()
                ).setResource(resource)
                .build();
        //OpenTelemetry openTelemetry = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
                .buildAndRegisterGlobal();
        // otlp 사용 설정 종료

        return openTelemetry;
    }

}
