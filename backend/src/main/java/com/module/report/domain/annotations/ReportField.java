package com.module.report.domain.annotations;

import com.module.report.domain.enums.columns.ColumnType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReportField {
    String title() default "";
    String key() default "";

    ColumnType type() default ColumnType.TEXT;
}
