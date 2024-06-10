package com.kt.otelsdkspringboot01.config;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Aspect
@Configuration
public class OpenTelemetryAspect {

    @Autowired
    private Tracer tracer;

    @Autowired
    private Meter meter;

    private final LongCounter requestCounter;

    private String hostName;
    @Autowired
    public OpenTelemetryAspect(Meter meter) {
        this.requestCounter = meter.counterBuilder("requests_total")
                .setDescription("Total number of requests")
                .setUnit("requests")
                .build();
        this.hostName = getHostname();
    }

    private String getHostname() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "unknown-host";
        }
    }


    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        
        //호출된 URL의 정보 정의
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getRemoteAddr();
        String httpMethod = req.getMethod();
        String httpUrl = req.getRequestURL().toString();
        String httpUri = req.getRequestURI();
        String queryString = req.getQueryString();
        String fullUrl = queryString == null ? httpUrl : httpUrl + "?" + queryString;



        //span과 meter에서 사용할 속성 정의
        AttributesBuilder attrsBuilder = Attributes.builder()
                .put("hostname", hostName)
                .put("ip-address", ip)
                .put("region", "korea")
                .put("http.method", httpMethod)
                .put("http.url", httpUrl)
                .put("http.uri", httpUri)
                .put("http.queryString", queryString)
                .put("http.fullUrl", fullUrl);
        
        // Span 생성
        Span span = tracer.spanBuilder(joinPoint.getSignature().getName())
                .setSpanKind(SpanKind.SERVER)
                .startSpan();


        span.setAllAttributes(attrsBuilder.build());

        // 메트릭 등록 (URL별로)
        requestCounter.add(1, attrsBuilder.build());
                //.add(1, Labels.of("method", joinPoint.getSignature().getName()));

        // Span을 현재 Scope에 연결
        try (Scope scope = span.makeCurrent()) {
            return joinPoint.proceed();
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }



    }
}
