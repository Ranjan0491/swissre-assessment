package com.swissre.app.enums;

import com.swissre.app.exceptions.BadRequestException;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class EnumUtils {
    public static <T extends Enum<T>> T convertAndGetEnum(String value, T[] enumValues) {
        if (Objects.isNull(value)) {
            return null;
        }
        return Stream.of(enumValues)
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorDetail.BAD_REQUEST,
                        Map.ofEntries(Map.entry("enum", "Could not convert '" + value + "' to enum")))
                );
    }
}
