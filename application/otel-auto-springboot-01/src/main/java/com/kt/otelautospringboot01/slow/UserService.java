package com.kt.otelautospringboot01.slow;


import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final LdapService ldapService;

    @WithSpan
    public void getUser(boolean flag) throws Exception{
        if(flag){
            Thread.sleep(3500);
        }
        ldapService.getLdapUser();
    }
}
