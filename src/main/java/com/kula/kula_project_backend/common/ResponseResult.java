package com.kula.kula_project_backend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {
    private int code;
    private String message;
    private Object data;

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public ResponseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
