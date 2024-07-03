package com.kt.ldap.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getLdapUserInfo(){

        getSleepTime(100,500);
        return true;
    }

    public void getSleepTime(int rangeStart , int rangeEnd){
        Random random = new Random();
        int sleepTime = random.nextInt(rangeEnd) + rangeStart;
        sleep(sleepTime);
    }

    public void sleep(int sleepTime){
        try {
            Thread.sleep(sleepTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
