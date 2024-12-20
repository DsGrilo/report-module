package com.module.report.domain.enums;

import com.module.report.domain.enums.groupers.Grouper;
import com.module.report.domain.filters.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum CategoryReport {
    DEFAULT("Geral");

    private final String label;

    public static List<EnumType> getLabelValue() {
        return Stream.of(Grouper.values())
                .map(enumType -> new EnumType(enumType.name()))
                .collect(Collectors.toList());
    }
}
