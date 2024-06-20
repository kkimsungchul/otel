package com.kt.otelsdkspringboot01.config;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Aspect
@Configuration
public class OpenTelemetryAspect {
    private static final Logger logger = LogManager.getLogger(OpenTelemetryAspect.class.getName());

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

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void startSpan(JoinPoint joinPoint) {
        logger.info("### start span ###");
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("x-forwarded-for");
        String httpMethod = req.getMethod();
        String httpUrl = req.getRequestURL().toString();
        String httpUri = req.getRequestURI();
        String queryString = req.getQueryString();
        String fullUrl = queryString == null ? httpUrl : httpUrl + "?" + queryString;

        //attribute 세팅
        Attributes attributes = Attributes.builder()
                .put("hostname", hostName)
                .put("ip-address", ip)
                .put("region", "korea")
                .put("http.method", httpMethod)
                .put("http.url", httpUrl)
                .put("http.uri", httpUri)
                .put("http.queryString", queryString)
                .put("http.fullUrl", fullUrl)
                .build();

        Span span = tracer.spanBuilder(joinPoint.getSignature().getName())
                .setSpanKind(SpanKind.SERVER)  // Use SERVER to indicate an incoming request
                .startSpan();
        span.setAllAttributes(attributes);
        Context context = Context.current().with(span);
        Scope scope = context.makeCurrent();

        requestCounter.add(1, attributes);

        //histogram 세팅
        LongHistogram histogram = meter.histogramBuilder("SpringBoot-app-histogram")
                .ofLongs() // Required to get a LongHistogram, default is DoubleHistogram
                .setDescription("SpringBoot-app-URL-histogram")
                .setUnit("call count")
                .build();
        histogram.record(1,attributes);

//        // Make the span current so that it becomes the parent of future spans created in the same context
//        span.makeCurrent();


        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().setAttribute("otelScope", scope);
    }

    @After("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void endSpan(JoinPoint joinPoint) {
        Span span = Span.current();
        if (span != null) {
            span.end();
        }
        // Retrieve and close the scope
        Scope scope = (Scope) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getAttribute("otelScope");
        if (scope != null) {
            scope.close();
        }
        logger.info("### end span ###");
    }


}
