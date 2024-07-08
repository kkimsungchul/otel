package com.kt.otelautospringboot01.controller;


import com.kt.otelautospringboot01.domain.Board;
import com.kt.otelautospringboot01.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private static final Logger logger = LogManager.getLogger(BoardController.class.getName());


    @GetMapping("")
    public List<Board> all() {
        logger.info("### HIHIHIHIHI");
        return boardService.all();

    }


    @GetMapping("/{pageSize}")
    public List<Board> all(@PathVariable int pageSize) {
        return boardService.all(pageSize);

    }
}

