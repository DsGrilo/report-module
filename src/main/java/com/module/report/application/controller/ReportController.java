package com.module.report.application.controller;


import com.module.report.application.dto.request.RequestCreateReportDTO;

import com.module.report.application.dto.response.report.ResponseReportDTO;
import com.module.report.application.dto.response.report.ResponseTypesReportDTO;
import com.module.report.application.service.ReportApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportApplicationService reportApplication;


    @GetMapping
    public List<ResponseTypesReportDTO> getReports() {
        return reportApplication.getReports();
    }

    @PostMapping
    public ResponseReportDTO generateReport(@RequestBody RequestCreateReportDTO dto) {
        return reportApplication.generateReport(dto);
    }




}
