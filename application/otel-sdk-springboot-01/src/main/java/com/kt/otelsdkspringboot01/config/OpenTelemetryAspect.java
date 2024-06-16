package com.kt.otelsdkspringboot01.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
public class OpenTelemetryAspect {
    private static final Logger logger = LogManager.getLogger(OpenTelemetryAspect.class.getName());

    @Autowired
    private Tracer tracer;

    @Autowired
    private Meter meter;

    @Autowired
    private MeterRegistry meterRegistry;

    private final LongCounter requestCounter;
    private ObservableDoubleGauge gauge;
    private String hostName;

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final long NO_HEAP_LIMIT = -1;

    private static final ThreadLocal<Span> currentSpan = new ThreadLocal<>();

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

        AttributesBuilder attrsBuilder = Attributes.builder()
                .put("hostname", hostName)
                .put("ip-address", ip)
                .put("region", "korea")
                .put("http.method", httpMethod)
                .put("http.url", httpUrl)
                .put("http.uri", httpUri)
                .put("http.queryString", queryString)
                .put("http.fullUrl", fullUrl);

        Span span = tracer.spanBuilder(joinPoint.getSignature().getName())
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        span.setAllAttributes(attrsBuilder.build());

        requestCounter.add(1, attrsBuilder.build());

        currentSpan.set(span);

        this.gauge = meter.gaugeBuilder("jvm.memory.totalMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.usedMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.freeMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.heapUsage")
                .buildWithCallback(measurement -> measurement.record(getHeapUsage()));

        io.micrometer.core.instrument.Meter cpuUsageMeter = meterRegistry.find("system.cpu.usage").meter();
        this.gauge = meter.gaugeBuilder("system.cpu.usage")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("system.cpu.usage").gauge().value()));

        io.micrometer.core.instrument.Meter memoryUsageMeter = meterRegistry.find("jvm.memory.used").meter();
        this.gauge = meter.gaugeBuilder("jvm.memory.used")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("jvm.memory.used").gauge().value() / 1024 / 1024));
    }

    @After("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void endSpan(JoinPoint joinPoint) {
        Span span = currentSpan.get();
        if (span != null) {
            try {
                span.end();
            } finally {
                currentSpan.remove();
            }
        }
        logger.info("### end span ###");
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
