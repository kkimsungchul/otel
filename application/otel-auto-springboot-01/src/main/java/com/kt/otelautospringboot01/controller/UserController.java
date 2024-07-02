package com.kt.otelautospringboot01.controller;


import com.kt.otelautospringboot01.domain.LogApi;
import com.kt.otelautospringboot01.slow.ReplyService;
import com.kt.otelautospringboot01.slow.UserService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

    private final ReplyService replyService;

    @GetMapping("")
    public String getUser() {
        logger.info("### getUser log start");
        String a = null;
        a.charAt(10);
        return a;
    }


    @WithSpan
    @GetMapping("/slow")
    public boolean slowTest(){
        return replyService.getReply(true);
    }


    @WithSpan
    @GetMapping("/normal")
    public boolean normalTest(){
        return replyService.getReply(false);
    }
}
