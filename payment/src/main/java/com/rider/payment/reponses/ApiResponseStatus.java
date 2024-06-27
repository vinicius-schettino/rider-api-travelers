package com.rider.payment.reponses;

public enum ApiResponseStatus {
    SUCCESSFUL("successful"), ERROR("error");

    public final String status;

    ApiResponseStatus(String label) {
        this.status = label;
    }
}
