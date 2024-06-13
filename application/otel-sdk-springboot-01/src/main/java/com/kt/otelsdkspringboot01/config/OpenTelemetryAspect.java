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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
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



    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

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
        String ip = req.getHeader("x-forwarded-for");
        System.out.println("ip : " + ip);
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

        this.gauge = meter.gaugeBuilder("jvm.memory.totalMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("totalMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.usedMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("usedMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.freeMemory")
                .buildWithCallback(measurement -> measurement.record(getMemory().get("freeMemory")));
        this.gauge = meter.gaugeBuilder("jvm.memory.heapUsage")
                .buildWithCallback(measurement -> measurement.record(getHeapUsage()));


        io.micrometer.core.instrument.Meter cpuUsageMeter = meterRegistry.find("system.cpu.usage").meter();
        if (cpuUsageMeter != null) {
            double cpuUsage = meterRegistry.get("system.cpu.usage").gauge().value();
            System.out.println("Current CPU Usage: " + cpuUsage);
            this.gauge = meter.gaugeBuilder("system.cpu.usage")
                    .buildWithCallback(measurement -> measurement.record(cpuUsage));
        }



        io.micrometer.core.instrument.Meter memoryUsageMeter = meterRegistry.find("jvm.memory.used").meter();
        if (memoryUsageMeter != null) {
            double memoryUsage = meterRegistry.get("jvm.memory.used").gauge().value();
            System.out.println("Current JVM Memory Usage: " + memoryUsage);
            this.gauge = meter.gaugeBuilder("jvm.memory.used")
                    .buildWithCallback(measurement -> measurement.record(memoryUsage/1024/1024));
        }




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

    public Map<String,Double> getMemory(){
        Map<String,Double> memoryMap = new HashMap<>();
        //메모리는 byte 단위로 반환, 1024로 두번나누면 kb - mb 로 변환
        double totalMemory = Runtime.getRuntime().totalMemory()/1024/1024;
        double freeMemory =Runtime.getRuntime().freeMemory()/1024/1024;
        double usedMemory =totalMemory - freeMemory;
        memoryMap.put("totalMemory",totalMemory);
        memoryMap.put("usedMemory",usedMemory);
        memoryMap.put("freeMemory",freeMemory);
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
