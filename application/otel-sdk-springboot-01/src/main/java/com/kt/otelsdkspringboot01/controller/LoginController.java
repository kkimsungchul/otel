package com.kt.otelsdkspringboot01.controller;

import com.kt.otelsdkspringboot01.utils.SpanUtils;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private SpanUtils spanUtils;

    @Autowired
    Meter meter;

    @GetMapping("/lamp/users")
    public Object users(HttpServletRequest req){
        Span childSpan = spanUtils.getChildSpan("LoginController.users");
        //새로 생성한 span이 상위 span과 일치하는지 확인
        try (Scope scope = childSpan.makeCurrent()) {
            // Your business logic
            String endpoint = "http://127.0.0.1:14040/error/test05";
            Attributes attributes = createAttributes(req);
            childSpan.addEvent("users_attribute",attributes);
            return restTemplate.getForObject(endpoint, Boolean.class);
        } catch (Exception e) {
            childSpan.recordException(e);
            throw e;
        } finally {
            childSpan.end();
        }

    }


    @GetMapping("/lamp/login")
    public void login(HttpServletRequest req) {
        Random random = new Random();
        LongCounter requestCounter = meter.counterBuilder("LAMP_LOGIN_requests_")
                .setDescription("Total number of LAMP login requests")
                .setUnit("requests")
                .build();
        Attributes attributes_total = createAttributes(req,"total");
        requestCounter.add(1,attributes_total);
        if(random.nextBoolean()){
            Attributes attributes = createAttributes(req,"success");
            requestCounter.add(1,attributes);
            logger.info("### login success");
        }else{
            Attributes attributes = createAttributes(req,"fail");
            requestCounter.add(1,attributes);
            logger.info("### login fail");
        }
    }

    public Attributes createAttributes(HttpServletRequest req , String login) {

        String ip = req.getHeader("x-forwarded-for");
        String httpMethod = req.getMethod();
        String httpUrl = req.getRequestURL().toString();
        String httpUri = req.getRequestURI();
        String queryString = req.getQueryString();
        String fullUrl = queryString == null ? httpUrl : httpUrl + "?" + queryString;
        //attribute 세팅
        Attributes attributes = Attributes.builder()
                .put("hostname", "LAMP")
                .put("ip-address", ip)
                .put("region", "korea")
                .put("http.method", httpMethod)
                .put("http.url", httpUrl)
                .put("http.uri", httpUri)
                .put("http.queryString", queryString)
                .put("http.fullUrl", fullUrl)
                .put("login", login)
                .build();
        return attributes;
    }

    public Attributes createAttributes(HttpServletRequest req) {

        String ip = req.getHeader("x-forwarded-for");
        String httpMethod = req.getMethod();
        String httpUrl = req.getRequestURL().toString();
        String httpUri = req.getRequestURI();
        String queryString = req.getQueryString();
        String fullUrl = queryString == null ? httpUrl : httpUrl + "?" + queryString;
        //attribute 세팅
        Attributes attributes = Attributes.builder()
                .put("hostname", "LAMP")
                .put("ip-address", ip)
                .put("region", "korea")
                .put("http.method", httpMethod)
                .put("http.url", httpUrl)
                .put("http.uri", httpUri)
                .put("http.queryString", queryString)
                .put("http.fullUrl", fullUrl)
                .put("otel_type","trace")
                .build();
        return attributes;
    }
}