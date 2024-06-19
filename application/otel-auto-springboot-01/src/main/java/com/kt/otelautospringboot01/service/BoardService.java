package com.kt.otelautospringboot01.service;

import org.springframework.stereotype.Service;

@Service
public class BoardService {


    public String boardListService(int count){

        return "board service in"+count;
    }
}
