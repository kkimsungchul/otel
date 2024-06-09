package com.kt.otelsdkspringboot01.service;


import com.kt.otelsdkspringboot01.domain.LogApi;
import com.kt.otelsdkspringboot01.repository.LogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;


    public LogApi save(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getRemoteAddr();
        LogApi logApi = new LogApi();
        logApi.setUserId("kimsungchul");
        logApi.setUserIp(ip);
        logApi.setCallUrl("api/test");
        logApi.setCallUrlParameter("?limit=10");
        logRepository.save(logApi);
//        System.out.println("logApi seq : " + logApi);
        return logApi;
    }

    public void update(LogApi logApi){
//        System.out.println("## log update");
        LocalDateTime now = LocalDateTime.now();
        logApi.setEndTime(now);
        logRepository.save(logApi);
    }

    public List<LogApi> all(){
        return logRepository.findAll();
    }


}
