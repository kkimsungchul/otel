package com.kt.otelsdkspringboot01.service;


import com.kt.otelsdkspringboot01.domain.Board;
import com.kt.otelsdkspringboot01.domain.LogApi;
import com.kt.otelsdkspringboot01.repository.BoardRepository;
import com.kt.otelsdkspringboot01.utils.SpanUtils;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final LogService logService;

    @Autowired
    private SpanUtils spanUtils;

    public List<Board> all(){
        Span childSpan = spanUtils.getChildSpan("BoardService.all");
        try (Scope scope = childSpan.makeCurrent()) {
            LogApi logApi = logService.save();
            PageRequest pageRequest = PageRequest.of(0, 100);
            List<Board> boardList = boardRepository.findAll(pageRequest).getContent();
            logService.update(logApi);
            return boardList;
        } catch (Exception e) {
            childSpan.recordException(e);
            throw e;
        } finally {
            childSpan.end();
        }
    }
    public List<Board> all(int pageSize){
        Span childSpan = spanUtils.getChildSpan("BoardService.all/count");
        LogApi logApi = logService.save();
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        List<Board> boardList = boardRepository.findAll(pageRequest).getContent();
        logService.update(logApi);
        return boardList;
    }
}
