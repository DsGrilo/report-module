package com.module.report.domain.builder;

import com.module.report.domain.enums.CategoryReport;
import com.module.report.domain.filters.EnumType;
import com.module.report.domain.filters.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Report {
    private String name;
    private String type;
    private String collection;
    private boolean useDate;
    private List<Column> titles = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();
    private List<EnumType> groupers;
    private CategoryReport category;
}
