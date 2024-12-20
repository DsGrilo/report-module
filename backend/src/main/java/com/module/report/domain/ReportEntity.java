package com.module.report.domain;

import com.module.report.domain.enums.groupers.Grouper;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface ReportEntity {

    List<Document> buildAggregation(Map<String, Object> filters, Grouper grouper);

}
