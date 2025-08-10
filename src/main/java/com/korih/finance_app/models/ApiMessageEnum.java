package com.korih.finance_app.models;

public enum ApiMessageEnum {
    SUCCESS("Success"),
    ERROR("Error"),
    NOT_FOUND("Not Found"),
    BAD_REQUEST("Bad Request"),
    UNAUTHORIZED("Unauthorized"),
    FORBIDDEN("Forbidden"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;

    ApiMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
