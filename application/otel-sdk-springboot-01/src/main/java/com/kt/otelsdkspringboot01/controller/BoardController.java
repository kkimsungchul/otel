package com.kt.otelsdkspringboot01.controller;

import com.kt.otelsdkspringboot01.domain.Board;
import com.kt.otelsdkspringboot01.service.BoardService;
import com.kt.otelsdkspringboot01.utils.SpanUtils;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SpanUtils spanUtils;

    @Autowired
    private final Tracer tracer;
    @GetMapping("")
    public List<Board> all() {
        logger.info("### HIHIHIHIHI");
        Span childSpan = spanUtils.getChildSpan("BoardController.all", tracer);
        //새로 생성한 span이 상위 span과 일치하는지 확인
        try (Scope scope = childSpan.makeCurrent()) {
            // Your business logic
            return boardService.all();
        } catch (Exception e) {
            childSpan.recordException(e);
            throw e;
        } finally {
            childSpan.end();
        }
    }

    @GetMapping("/{pageSize}")
    public List<Board> all(@PathVariable int pageSize) {
        logger.info("### HIHIHIHIHI");
        // Create a child span
        Span childSpan = spanUtils.getChildSpan("BoardController.all/count", tracer);
        try (Scope scope = childSpan.makeCurrent()) {
            // Your business logic
            return boardService.all(pageSize);
        } catch (Exception e) {
            childSpan.recordException(e);
            throw e;
        } finally {
            childSpan.end();
        }
    }
}
