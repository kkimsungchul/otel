package com.kt.board.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getBoardList(){

        getSleepTime(100,500);
        String endpoint = "http://localhost:10020/user";
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
