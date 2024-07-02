package com.kt.board.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getBoardList(){

        String endpoint = "http://localhost:10020/user";
        return restTemplate.getForObject(endpoint, Boolean.class);
    }

}
