package com.example.demo.payload;

import lombok.Getter;

@Getter
public class IdenficationCodeRequest {
    private long code;

    public IdenficationCodeRequest(long code){
        this.code = code;
    }
}
