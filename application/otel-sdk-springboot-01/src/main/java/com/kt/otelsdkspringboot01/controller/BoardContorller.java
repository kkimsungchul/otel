package com.kt.otelsdkspringboot01.controller;


import com.kt.otelsdkspringboot01.domain.Board;
import com.kt.otelsdkspringboot01.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardContorller {

    private final BoardService boardService;


    @GetMapping("")
    public List<Board> all(){
        return boardService.all();
    }

    @GetMapping("/{pageSize}")
    public List<Board> all(@PathVariable int pageSize){
        return boardService.all(pageSize);
    }

}
