package com.kt.otelsdkspringboot01.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    private String id;
    private String pw;

}

