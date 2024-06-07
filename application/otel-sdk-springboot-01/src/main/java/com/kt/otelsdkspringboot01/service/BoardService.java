package com.kt.otelsdkspringboot01.service;


import com.kt.otelsdkspringboot01.domain.Board;
import com.kt.otelsdkspringboot01.domain.LogApi;
import com.kt.otelsdkspringboot01.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> all(){
        return boardRepository.all();
    }
}
