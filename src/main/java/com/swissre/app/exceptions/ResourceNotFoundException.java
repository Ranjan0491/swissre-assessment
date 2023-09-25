package com.swissre.app.exceptions;

import com.swissre.app.enums.ErrorDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResourceNotFoundException extends CoreException {

    public ResourceNotFoundException(ErrorDetail errorDetail, Map<String, String> invalidParams) {
        super(errorDetail, invalidParams);
    }

    public ResourceNotFoundException(ErrorDetail errorDetail) {
        super(errorDetail);
    }
}
