package com.kt.otelsdkspringboot01.controller;


import com.kt.otelsdkspringboot01.domain.LogApi;
import com.kt.otelsdkspringboot01.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {

    private final LogService logService;
    private static final Logger logger = LogManager.getLogger(LogController.class.getName());

    //postman 사용하기 귀찮아서 일단은 get
    @GetMapping("/save")
    public void save(){

        LogApi logApi = logService.save();
        logger.info("### save in ### {}",logApi.toString());
        try {
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        logService.update(logApi);
        logger.info("### save out ### {}",logApi.toString());
    }

    @GetMapping("")
    public List<LogApi> logApiList(){
        return logService.all();
    }
}
