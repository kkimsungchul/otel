package com.kt.ldap.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getLdapUserInfo(){
        return true;
    }

}
