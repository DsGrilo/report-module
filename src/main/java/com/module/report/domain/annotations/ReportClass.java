package com.module.report.domain.annotations;


import com.module.report.domain.enums.CategoryReport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReportClass {
    String name() default "";

    String collection() default "";

    boolean useDate() default false;

    String[] filters() default {};

    String[] groupers() default {};

    CategoryReport category() default CategoryReport.DEFAULT;
}
