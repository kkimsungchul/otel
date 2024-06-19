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



    public Span getChildSpan(String spanName,Tracer tracer){
        Span parentSpan = Span.current();
        Span childSpan = tracer.spanBuilder(spanName)
                .setParent(Context.current().with(parentSpan))
                .startSpan();
        return childSpan;
    }
}
