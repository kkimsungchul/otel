package com.kt.ldap.system.controller;


import com.kt.ldap.system.service.LdapService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ldap")
public class LdapController {

    private static final Logger logger = LogManager.getLogger(LdapController.class.getName());

    private final LdapService ldapService;

    @WithSpan
    @GetMapping("")
    public boolean getLdapInfo(){
        ldapService.getSleepTime(100,500);
        logger.info("## LdapController in ");
        return ldapService.getLdapUserInfo();
    }
}
