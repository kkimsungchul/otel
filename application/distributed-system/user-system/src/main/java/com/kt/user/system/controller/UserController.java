package com.kt.user.system.controller;


import com.kt.user.system.service.UserService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class.getName());

    private final UserService userService;

    @WithSpan
    @GetMapping("")
    public boolean getUser(){
        logger.info("## UserController in ");
        userService.getSleepTime(100,500);
        return userService.getUserList();

    }
}
