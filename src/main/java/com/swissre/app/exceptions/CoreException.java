package com.swissre.app.exceptions;

import com.swissre.app.enums.ErrorDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CoreException extends RuntimeException {
    private String code;
    private Map<String, String> invalidParams;

    public CoreException(ErrorDetail errorDetail, Map<String, String> invalidParams) {
        this(errorDetail);
        this.code = errorDetail.getCode();
        this.invalidParams = invalidParams;
    }

    public CoreException(ErrorDetail errorDetail) {
        super(errorDetail.getMessage());
    }
}
