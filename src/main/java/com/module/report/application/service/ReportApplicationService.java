package com.module.report.application.service;

import com.module.report.application.dto.request.RequestCreateReportDTO;
import com.module.report.application.dto.response.report.ResponseReportDTO;
import com.module.report.application.dto.response.report.ResponseTypesReportDTO;
import com.module.report.domain.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportApplicationService {
    private final ReportService reportService;

    public List<ResponseTypesReportDTO> getReports() {
        var reports = reportService.getReports().stream();
        return reports.map($ -> new ResponseTypesReportDTO(
                $.getName(),
                $.getType(),
                $.isUseDate(),
                $.getFilters(),
                $.getGroupers(),
                new ResponseTypesReportDTO.ResponseReportCategory(
                        $.getCategory().getLabel(),
                        $.getCategory().name()
                )
        )).toList();
    }

    public ResponseReportDTO generateReport(RequestCreateReportDTO dto) {
        return reportService.generateReport(dto);
    }
}
