package com.module.report.domain.handlers.movies;

import com.module.report.domain.ReportEntity;
import com.module.report.domain.annotations.ReportClass;
import com.module.report.domain.annotations.ReportField;
import com.module.report.domain.enums.columns.ColumnType;
import com.module.report.domain.enums.groupers.Grouper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.*;

import static com.module.report.domain.enums.CategoryReport.DEFAULT;

@ReportClass(name = "Filmes 2", collection = "movies", useDate = false,
        filters = {"year", "genres", "rated"}  , category = DEFAULT)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportMovies2 implements ReportEntity {
    @ReportField(title = "Titulo", type = ColumnType.TEXT, key = "title")
    public String name;

    @ReportField(title = "Rated", type = ColumnType.ENUM, key = "rated")
    public String rated;

    @ReportField(title = "Enredo", type = ColumnType.TEXT, key = "plot")
    public Date plot;

    @ReportField(title = "Ano", type = ColumnType.TEXT, key = "year")
    public String year;

    @ReportField(title = "Genero(s)", type = ColumnType.TEXT, key = "genres")
    public String genres;

    @Override
    public List<Document> buildAggregation(Map<String, Object> filters, Grouper grouper) {
        List<Document> pipeline = new ArrayList<>();


        Document matchConditions = new Document();

        if (filters.containsKey("genres")) {
            matchConditions.append("genres", new Document("$in", filters.get("genres")));
        }

        if (filters.containsKey("year")) {
            matchConditions.append("year", filters.get("year"));
        }

        if(filters.containsKey("rated")){
            matchConditions.append("rated", filters.get("rated"));
        }

        Document projectFields = new Document("$project",
                new Document("0", "$title")
                        .append("1", "$rated")
                        .append("2", "$plot")
                        .append("3", "$year")
                        .append("4", new Document("$reduce",
                                new Document("input", "$genres")
                                        .append("initialValue", "")
                                        .append("in", new Document("$cond", Arrays.asList(
                                                new Document("$eq", Arrays.asList("$$value", "")),
                                                "$$this",
                                                new Document("$concat", Arrays.asList("$$value", ";", "$$this"))
                                        )))
                        ))
                        .append("_id", 0)
        );

        pipeline.add(new Document("$match", matchConditions));
        pipeline.add(projectFields);

        return pipeline;
    }
}
