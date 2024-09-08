package com.module.report.domain.service;



import com.module.report.application.dto.request.RequestCreateReportDTO;
import com.module.report.application.dto.response.report.ResponseReportDTO;
import com.module.report.domain.builder.Report;

import java.util.List;

public interface ReportService {

    List<Report> getReports();

    ResponseReportDTO generateReport(RequestCreateReportDTO dto);
}
