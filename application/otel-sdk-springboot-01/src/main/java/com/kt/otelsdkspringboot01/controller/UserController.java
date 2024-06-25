package com.kt.otelsdkspringboot01.controller;

import com.kt.otelsdkspringboot01.utils.SpanUtils;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

    @Autowired
    private SpanUtils spanUtils;

    @GetMapping("")
    public String getUser() {

        String a = null;
        logger.info("### getUser log start");
        Span childSpan = spanUtils.getChildSpan("error.all");
        //새로 생성한 span이 상위 span과 일치하는지 확인
        try (Scope scope = childSpan.makeCurrent()) {
            a.charAt(10);
        } catch (Exception e) {
            childSpan.recordException(e);
            e.printStackTrace();
            throw e;
        } finally {
            childSpan.end();
        }
        return a;
    }
}
