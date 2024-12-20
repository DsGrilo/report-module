package com.module.report.application.dto.response.report;


import com.module.report.domain.builder.Column;

import java.util.Date;
import java.util.List;

public record ResponseReportDTO(
        List<Column> columns,
        Object values,
        String query,
        Date executionTime
) {


}
