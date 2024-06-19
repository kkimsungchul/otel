package com.kt.otelsdkspringboot01.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.Context;
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


        //metric 세팅
        meter.gaugeBuilder("jvm.memory.totalMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
        meter.gaugeBuilder("jvm.memory.usedMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
        meter.gaugeBuilder("jvm.memory.freeMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
        meter.gaugeBuilder("jvm.memory.heapUsage")
                .buildWithCallback(measurement -> measurement.record(getHeapUsage()));
        io.micrometer.core.instrument.Meter cpuUsageMeter = meterRegistry.find("system.cpu.usage").meter();
        meter.gaugeBuilder("system.cpu.usage")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("system.cpu.usage").gauge().value()));
        io.micrometer.core.instrument.Meter memoryUsageMeter = meterRegistry.find("jvm.memory.used").meter();
        meter.gaugeBuilder("jvm.memory.used")
                .buildWithCallback(measurement -> measurement.record(meterRegistry.get("jvm.memory.used").gauge().value() / 1024 / 1024));

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
