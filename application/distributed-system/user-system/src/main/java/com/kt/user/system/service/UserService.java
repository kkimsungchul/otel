package com.kt.user.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getUserList(){
        String endpoint = "http://localhost:10030/ldap";
        getSleepTime(100,500);
        boolean result = restTemplate.getForObject(endpoint, Boolean.class);
        getUserLog();
        return result;
    }

    @WithSpan
    public boolean getUserLog(){
        String endpoint = "http://localhost:10040/log";
        return restTemplate.getForObject(endpoint, Boolean.class);
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
