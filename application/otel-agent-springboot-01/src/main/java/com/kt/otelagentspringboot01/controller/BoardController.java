package com.kt.otelagentspringboot01.controller;


import com.kt.otelagentspringboot01.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    @GetMapping("/{count}")
    public String boardList(@PathVariable int count){

        boardService.boardListService(count);
        return "board controller in"+count;

    }
}
