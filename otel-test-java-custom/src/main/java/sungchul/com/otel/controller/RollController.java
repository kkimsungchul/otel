package sungchul.com.otel.controller;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.SpanKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sungchul.com.otel.service.DiceService;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

@RestController
public class RollController {
    private static final Logger logger = LoggerFactory.getLogger(RollController.class);
    private final Tracer tracer;
    Meter meter;

    @Autowired
    RollController(OpenTelemetry openTelemetry) {
        tracer = openTelemetry.getTracer(RollController.class.getName(), "0.1.0");
        meter = openTelemetry.meterBuilder("dice-server")
                .setInstrumentationVersion("0.1.0")
                .build();
    }

    @GetMapping("/java-custom/rolldice")
    public List<Integer> index(@RequestParam("player") Optional<String> player,
                               @RequestParam("rolls") Optional<Integer> rolls) {

        String hostName ="";
        String address = "";
        try{
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostName = inetAddress.getHostName();
            address = inetAddress.getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }

        // 사용할 속성 정의
        Attributes attrs = Attributes.of(
                stringKey("hostname"), hostName,
                stringKey("address"), address,
                stringKey("region"), "korea");

        //span 정의
        Span span = tracer.spanBuilder("rollTheDice").setSpanKind(SpanKind.CLIENT).startSpan();
        span.setAttribute("http.method", "GET");
        span.setAttribute("http.url", "/java-custom/rolldice");
        span.setAllAttributes(attrs);



        //카운터 추가
        LongCounter counter = meter.counterBuilder("dice-lib.rolls.counter")
                .setDescription("How many times the dice have been rolled.")
                .setUnit("rolls")
                .build();
        counter.add(1, attrs);


        // Make the span the current span
        try (Scope scope = span.makeCurrent()) {
            if (!rolls.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing rolls parameter", null);
            }

            List<Integer> result = new DiceService(1, 6).rollTheDice(rolls.get());

            if (player.isPresent()) {
                logger.info("{} is rolling the dice: {}", player.get(), result);
            } else {
                logger.info("Anonymous player is rolling the dice: {}", result);
            }
            return result;
        } catch(Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }
}