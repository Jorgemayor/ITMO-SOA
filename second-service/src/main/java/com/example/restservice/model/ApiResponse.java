package com.example.restservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    
    private String message;
    private Object data;
    private String status;

    public ApiResponse() {
    }

    public ApiResponse(String message, Object data, String status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse("Success", data, "OK");
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(message, data, "OK");
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(message, null, "ERROR");
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

