package com.swissre.app.enums;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    TASK_NOT_FOUND("TASKS_404", "Task could not be found"),
    BAD_REQUEST("TASKS_400", "Request parameters does not have proper values")
    ;

    private String code;
    private String message;

    ErrorDetail(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
