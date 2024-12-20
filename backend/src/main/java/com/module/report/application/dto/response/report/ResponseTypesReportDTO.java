package com.module.report.application.dto.response.report;



import com.module.report.domain.filters.EnumType;
import com.module.report.domain.filters.Filter;

import java.util.List;

public record ResponseTypesReportDTO(
        String name,
        String type,
        boolean useDate,
        List<Filter> filters,
        List<EnumType> groupers,
        ResponseReportCategory category

) {
    public record ResponseReportCategory(
        String label,
        String value
    ){
    }

}
