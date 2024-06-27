package com.rider.payment.reponses;

public class ApiResponse {
    public String message;

    public ApiResponseStatus status;

    public Object data;

    public ApiResponse(String message) {
        this.message = message;

        this.status = ApiResponseStatus.ERROR;
    }

    public ApiResponse(Object data) {
        this.data = data;

        this.status = ApiResponseStatus.SUCCESSFUL;
    }
}



