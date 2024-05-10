package sungchul.com.otel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;


public class DiceService {

    private int min;
    private int max;
    private Tracer tracer;


    public DiceService(int min, int max) {
        this(min, max, OpenTelemetry.noop());
    }

    public DiceService(int min, int max, OpenTelemetry openTelemetry) {
        this.min = min;
        this.max = max;
        this.tracer = openTelemetry.getTracer(DiceService.class.getName(), "0.1.0");
    }

    public List<Integer> rollTheDice(int rolls) {
        Span parentSpan = tracer.spanBuilder("parent").startSpan();
        try (Scope scope = parentSpan.makeCurrent()) {
            List<Integer> results = new ArrayList<Integer>();
            for (int i = 0; i < rolls; i++) {
                results.add(this.rollOnce());
            }
            return results;
        } finally {
            parentSpan.end();
        }
    }

    private int rollOnce() {
        Span childSpan = tracer.spanBuilder("child")
                // NOTE: setParent(...) is not required;
                // `Span.current()` is automatically added as the parent
                .startSpan();
        try(Scope scope = childSpan.makeCurrent()) {
            return ThreadLocalRandom.current().nextInt(this.min, this.max + 1);
        } finally {
            childSpan.end();
        }
    }
}
