package com.swissre.app.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.swissre.app.dto.ErrorResponseDto;
import com.swissre.app.enums.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String applicationName;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleException(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildAndPrintErrorResponseDto(resourceNotFoundException.getCode(),
                        resourceNotFoundException.getMessage(), resourceNotFoundException.getInvalidParams()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleException(BadRequestException badRequestException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildAndPrintErrorResponseDto(badRequestException.getCode(),
                        badRequestException.getMessage(), badRequestException.getInvalidParams()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildAndPrintErrorResponseDto(ErrorDetail.BAD_REQUEST.getCode(),
                        httpMessageNotReadableException.getMessage(), Map.ofEntries(
                                Map.entry("Specific cause", httpMessageNotReadableException.getMostSpecificCause().getMessage()))));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildAndPrintErrorResponseDto("TASKS_500", exception.getMessage()));
    }

    private ErrorResponseDto buildAndPrintErrorResponseDto(String code, String message, Map<String, String> invalidParams) {
        var errorResponseDto = ErrorResponseDto.builder()
                .application(applicationName)
                .code(code)
                .message(message)
                .invalidParams(invalidParams)
                .build();
        log.error("Handled error :: " + errorResponseDto);
        return errorResponseDto;
    }

    private ErrorResponseDto buildAndPrintErrorResponseDto(String code, String message) {
        return buildAndPrintErrorResponseDto(code, message, null);
    }
}
