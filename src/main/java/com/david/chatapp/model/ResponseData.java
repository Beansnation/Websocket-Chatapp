package com.david.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    private String message;
    private int status;
    private boolean success;
    private Object data;

    public ResponseData(String message, int status, boolean success) {
        this.message = message;
        this.status = status;
        this.success = success;
    }
}