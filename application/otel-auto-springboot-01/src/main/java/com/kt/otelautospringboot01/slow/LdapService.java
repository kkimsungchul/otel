package com.kt.otelautospringboot01.slow;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LdapService {

    @WithSpan
    public void getLdapUser()throws Exception{
        Thread.sleep(200);
    }
}
