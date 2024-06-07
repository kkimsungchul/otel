package com.kt.otelsdkspringboot01.service;


import com.kt.otelsdkspringboot01.domain.LogApi;
import com.kt.otelsdkspringboot01.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;


    public void save(){

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        System.out.println(ip);
        if (ip == null) {
            ip = req.getRemoteAddr();
        }
        System.out.println(ip);
        LogApi logApi = new LogApi();
        logApi.setUserId("kimsungchul");
        logApi.setUserIp(ip);
        logApi.setCallUrl("api/test");
        logApi.setCallUrlParameter("?limit=10");
        logRepository.save(logApi);
        System.out.println("logApi seq : " + logApi);
    }

    public List<LogApi> all(){
        return logRepository.all();
    }
}
