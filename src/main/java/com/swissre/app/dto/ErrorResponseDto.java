package com.swissre.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponseDto {
    private String application;
    private String code;
    private String message;
    private Map<String, String> invalidParams;
}
