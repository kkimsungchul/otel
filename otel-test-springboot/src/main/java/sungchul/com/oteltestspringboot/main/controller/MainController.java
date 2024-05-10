package sungchul.com.oteltestspringboot.main.controller;


import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public void getMain(){
        LOGGER.info("HI");
        doSomeWorkNewSpa();

    }



    private void doSomeWorkNewSpa(){
        LOGGER.info("new span");
        Span span = Span.current();
        span.setAttribute("attribute.a2" , "value");
        span.addEvent("app.processing2.start" , attributes("321"));
        span.addEvent("app.processing2.end" , attributes("321"));
    }

    private Attributes attributes(String id){
        return Attributes.of(AttributeKey.stringKey("app.id"),id);
    }



}
