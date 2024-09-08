package com.module.report.domain.builder;

import com.module.report.domain.enums.columns.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Column {
    private String name;
    private ColumnType type;
    private int index;
    private String key;

}
