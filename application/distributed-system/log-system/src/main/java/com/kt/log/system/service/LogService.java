package com.kt.log.system.service;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LogService {

    private final RestTemplate restTemplate;

    @WithSpan
    public boolean getUserLog(){
        return true;
    }

}
