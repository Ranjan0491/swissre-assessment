package com.swissre.app.exceptions;

import com.swissre.app.enums.ErrorDetail;

import java.util.Map;

public class BadRequestException extends CoreException {

    public BadRequestException(ErrorDetail errorDetail, Map<String, String> invalidParams) {
        super(errorDetail, invalidParams);
    }

    public BadRequestException(ErrorDetail errorDetail) {
        super(errorDetail);
    }
}
