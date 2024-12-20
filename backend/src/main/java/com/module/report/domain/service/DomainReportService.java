package com.module.report.domain.service;

import com.module.report.application.dto.request.RequestCreateReportDTO;
import com.module.report.application.dto.response.report.ResponseReportDTO;
import com.module.report.domain.ReportEntity;
import com.module.report.domain.annotations.ReportClass;
import com.module.report.domain.annotations.ReportField;
import com.module.report.domain.builder.Column;
import com.module.report.domain.builder.Report;
import com.module.report.domain.enums.groupers.Grouper;
import com.module.report.domain.exception.ReportNotFoundException;
import com.module.report.domain.filters.EnumType;
import com.module.report.domain.filters.Filter;
import com.module.report.domain.filters.TypeFilter;
import com.module.report.domain.filters.enums.Rating;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class DomainReportService implements ReportService {

    private final List<Report> handlers = new LinkedList<>();
    private final Map<String, Filter> filters = new HashMap<>();
    private final Map<String, ReportEntity> reportInstances = new HashMap<>();
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void setup() throws ClassNotFoundException {
        var rated = Rating.getLabelValue();
        filters.put("rated", new Filter("Rate", "rated", TypeFilter.ENUM, new ArrayList<>(rated)));
        filters.put("genres", new Filter("Genero(s)", "genres", TypeFilter.STRING, new ArrayList<>()));

        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(ReportClass.class));
        for (var candidateComponent : provider.findCandidateComponents("com/module/report/domain/handlers")) {
            var aClass = Class.forName(candidateComponent.getBeanClassName());
            var queryAnnotation = aClass.getAnnotation(ReportClass.class);
            var fields = aClass.getFields();
            var report = new Report();
            var count = 0;
            for (var field : fields) {
                var reportField = field.getAnnotation(ReportField.class);
                report.getTitles().add(new Column(reportField.title(), reportField.type(), count, reportField.key()));
                count++;
            }
            report.setCollection(queryAnnotation.collection());
            report.setName(queryAnnotation.name());
            report.setUseDate(queryAnnotation.useDate());
            report.setType(aClass.getSimpleName());
            report.setCategory(queryAnnotation.category());
            report.setFilters(Arrays.stream(queryAnnotation.filters()).map(filters::get).collect(Collectors.toList()));

            List<String> grouperNames = Arrays.asList(queryAnnotation.groupers());

            List<EnumType> enumTypes = grouperNames.stream()
                    .map(name -> {
                        try {
                            Grouper grouper = Grouper.valueOf(name.toUpperCase());
                            return new EnumType(grouper.name());
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            report.setGroupers(enumTypes);

            handlers.add(report);

            try {
                ReportEntity reportInstance = (ReportEntity) aClass.getDeclaredConstructor().newInstance();
                reportInstances.put(aClass.getSimpleName(), reportInstance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.error("Erro ao instanciar a classe de relatório", e);
            }
        }
        handlers.sort(Comparator.comparing(Report::getName));
        log.info("{} relatorios registrado", handlers.size());
    }


    @Override
    public List<Report> getReports() {
        return handlers;
    }

    @Override
    public ResponseReportDTO generateReport(RequestCreateReportDTO dto) {
        var report = handlers.stream().filter($ -> $.getType().equals(dto.type())).findFirst().orElse(null);

        if (report == null) {
            throw new ReportNotFoundException();
        }

        var reportEntity = reportInstances.get(report.getType());
        if (reportEntity == null) {
            throw new ReportNotFoundException();
        }


        List<Document> aggregationPipeline = reportEntity.buildAggregation(dto.filters() == null ? new HashMap<>() : dto.filters(), dto.grouper());

        if (report.isUseDate())
            addDateSearch(report, dto.startDate(), dto.endDate(), aggregationPipeline);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        for (Document doc : aggregationPipeline) {
            aggregationOperations.add(context -> doc);
        }

        TypedAggregation<Document> aggregation = Aggregation.newAggregation(Document.class, aggregationOperations);
        List<Document> results = mongoTemplate.aggregate(aggregation, report.getCollection(), Document.class).getMappedResults();

        return new ResponseReportDTO(report.getTitles(), results, aggregationOperations.toString(), new Date());
    }


    private void addDateSearch(Report report, Date startDate, Date endDate, List<Document> aggregationPipeline) {
        if (startDate == null && endDate == null)
            return;

        var dateField = switch (report.getType()) {
            default -> "createdAt";
        };

        for (Document stage : aggregationPipeline) {
                if (stage.containsKey("$match")) {
                    var matchDocument = stage.get("$match", Document.class);
                    if (matchDocument == null) {
                        matchDocument = new Document();
                    }

                    Document dateConditions = (Document) matchDocument.getOrDefault(dateField, new Document());

                    if (startDate != null) {
                        dateConditions.append("$gte", startDate);
                    }
                    if (endDate != null) {
                        dateConditions.append("$lte", endDate);
                    }

                    matchDocument.put(dateField, dateConditions);
                    stage.put("$match", matchDocument);
                    break;
                }
        }

    }
}
