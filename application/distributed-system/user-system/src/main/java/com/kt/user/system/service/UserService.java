package com.kt.user.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getUserList(){
        String endpoint = "http://localhost:10030/ldap";
        getUserLog();
        return restTemplate.getForObject(endpoint, Boolean.class);
    }

    @WithSpan
    public boolean getUserLog(){
        String endpoint = "http://localhost:10040/log";
        return restTemplate.getForObject(endpoint, Boolean.class);
    }


}
