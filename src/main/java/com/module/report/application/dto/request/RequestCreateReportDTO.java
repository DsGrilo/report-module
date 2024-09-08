package com.module.report.application.dto.request;



import com.module.report.domain.enums.groupers.Grouper;

import java.util.Date;
import java.util.HashMap;

public record RequestCreateReportDTO(
        String type,
        Date startDate,
        Date endDate,
        HashMap<String, Object> filters,
        Grouper grouper
) {


}
