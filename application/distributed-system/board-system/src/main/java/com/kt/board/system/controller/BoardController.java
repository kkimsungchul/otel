package com.kt.board.system.controller;


import com.kt.board.system.service.BoardService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private static final Logger logger = LogManager.getLogger(BoardController.class.getName());

    private final BoardService boardService;

    @WithSpan
    @GetMapping("")
    public boolean getBoard(){
        logger.info("## BoardController in ");
        return boardService.getBoardList();


    }
}
