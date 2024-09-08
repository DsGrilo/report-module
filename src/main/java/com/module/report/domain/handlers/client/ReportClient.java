package com.module.report.domain.handlers.client;

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

import static com.module.report.domain.enums.CategoryReport.CLIENT;

@ReportClass(name = "Pessoa física", collection = "clients", useDate = true,
        filters = {"UF", "gender"}  , category = CLIENT)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportClient implements ReportEntity {
    @ReportField(title = "Nome", type = ColumnType.TEXT, key = "name")
    public String name;

    @ReportField(title = "CPF", type = ColumnType.TEXT, key = "document")
    public String document;

    @ReportField(title = "Data de Nasc", type = ColumnType.DATE, key = "birthDate")
    public Date birthDate;

    @ReportField(title = "Gênero", type = ColumnType.TEXT, key = "gender")
    public String gender;

    @ReportField(title = "Contato", type = ColumnType.TEXT, key = "phone")
    public String phone;

    @ReportField(title = "E-mail", type = ColumnType.TEXT, key = "email")
    public String email;

    @ReportField(title = "Endereços", type = ColumnType.TEXT, key = "fullAddress")
    public String fullAddress;

    @ReportField(title = "Data de Cadastro", type = ColumnType.DATE, key = "createdAt")
    public Date createdAt;

    @Override
    public List<Document> buildAggregation(Map<String, Object> filters, Grouper grouper) {
        List<Document> pipeline = new ArrayList<>();


        Document matchConditions = new Document();

        if (filters.containsKey("gender")) {
            matchConditions.append("gender", new Document("$in", filters.get("gender")));
        }

        if (filters.containsKey("UF")) {
            matchConditions.append("addresses",
                    new Document("$elemMatch",
                            new Document("district",
                                    new Document("$in", filters.get("UF"))
                            )
                    )
            );
        }

        matchConditions.append("isPerson", true);
        matchConditions.append("deleted", false);

        Document addFullAddressField = new Document("$addFields",
                new Document("fullAddress",
                        new Document("$reduce",
                                new Document("input", "$addresses")
                                        .append("initialValue", "")
                                        .append("in", new Document("$cond", Arrays.asList(
                                                new Document("$eq", Arrays.asList("$$value", "")),
                                                "$$this.fullAddress",
                                                new Document("$concat", Arrays.asList("$$value", "; ", "$$this.fullAddress"))
                                        )))
                        )
                )
        );

        Document projectFields = new Document("$project",
                new Document("0", "$name")
                        .append("1", new Document("$cond", Arrays.asList(
                                new Document("$or", Arrays.asList(
                                        new Document("$eq", Arrays.asList(new Document("$ifNull", Arrays.asList("$document", null)), null)), // Verifica se $document é null ou não existe
                                        new Document("$eq", Arrays.asList("$document", "")) // Verifica se $document é uma string vazia
                                )),
                                null,
                                new Document("$concat", Arrays.asList(
                                        new Document("$substrBytes", Arrays.asList(new Document("$ifNull", Arrays.asList("$document", "")), 0, 3)), ".",  // Primeiros 3 dígitos + ponto
                                        new Document("$substrBytes", Arrays.asList(new Document("$ifNull", Arrays.asList("$document", "")), 3, 3)), ".",  // Próximos 3 dígitos + ponto
                                        new Document("$substrBytes", Arrays.asList(new Document("$ifNull", Arrays.asList("$document", "")), 6, 3)), "-",  // Próximos 3 dígitos + hífen
                                        new Document("$substrBytes", Arrays.asList(new Document("$ifNull", Arrays.asList("$document", "")), 9, 2))        // Últimos 2 dígitos
                                ))
                        )))
                        .append("2", new Document("$dateToString", new Document("format", "%Y-%m-%dT%H:%M:%S%z").append("date", "$birthDate")))
                        .append("3", new Document("$switch", new Document("branches", Arrays.asList(
                                new Document("case", new Document("$eq", Arrays.asList("$gender", "MALE"))).append("then", "Masculino"),
                                new Document("case", new Document("$eq", Arrays.asList("$gender", "FEMALE"))).append("then", "Feminino"),
                                new Document("case", new Document("$eq", Arrays.asList("$gender", "OTHERS"))).append("then", "Outros")
                        )).append("default", "Não especificado")))
                        .append("4", "$phone")
                        .append("5", "$email")
                        .append("6", "$fullAddress")
                        .append("7", new Document("$dateToString", new Document("format", "%Y-%m-%dT%H:%M:%S%z").append("date", "$createdAt")))
                        .append("_id", 0)
        );

        pipeline.add(new Document("$match", matchConditions));
        pipeline.add(addFullAddressField);
        pipeline.add(projectFields);

        return pipeline;
    }
}
