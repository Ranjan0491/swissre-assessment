package com.swissre.app.enums;

import lombok.Getter;

@Getter
public enum SortColumn {
    PRIORITY("priority");

    private String columnName;

    SortColumn(String columnName) {
        this.columnName = columnName;
    }
}
