package com.kt.otelsdkspringboot01.utils;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpanUtils {

    @Autowired
    private final Tracer tracer;

    public Span getChildSpan(String spanName){
        Span parentSpan = Span.current();
        Span childSpan = tracer.spanBuilder(spanName)
                .setParent(Context.current().with(parentSpan))
                .startSpan();
        return childSpan;
    }
}
