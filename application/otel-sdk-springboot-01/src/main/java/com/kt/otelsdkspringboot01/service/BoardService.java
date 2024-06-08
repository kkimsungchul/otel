package com.kt.otelsdkspringboot01.service;


import com.kt.otelsdkspringboot01.domain.Board;
import com.kt.otelsdkspringboot01.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> all(){
        PageRequest pageRequest = PageRequest.of(0, 100);
        return boardRepository.findAll(pageRequest).getContent();
    }
}
