package com.kt.otelautospringboot01.slow;

import com.kt.otelautospringboot01.service.BoardService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final AuthService authService;
    private final LdapService ldapService;
    private final UserService userService;
    private final BoardService boardService;


    @WithSpan
    public boolean getReply() {
        try {
            Thread.sleep(300);
            authService.checkAuth();
            userService.getUser();
            Thread.sleep(1000);
            getReplyCount();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @WithSpan
    public int getReplyCount()throws Exception{
        boardService.boardServiceSlow();
        Thread.sleep(1000);
        return 1;
    }


}
