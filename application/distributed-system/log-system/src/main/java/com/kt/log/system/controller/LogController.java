package com.kt.log.system.controller;


import com.kt.log.system.service.LogService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {

    private static final Logger logger = LogManager.getLogger(LogController.class.getName());

    private final LogService logService;

    @WithSpan
    @GetMapping("")
    public boolean getLog(){
        logger.info("## LogController in ");
        return logService.getUserLog();
    }
}
