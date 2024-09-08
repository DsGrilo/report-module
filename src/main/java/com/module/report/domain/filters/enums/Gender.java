package com.module.report.domain.filters.enums;

import com.module.report.domain.filters.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("Feminino"), MALE("Masculino"), OTHERS("Outros");

    private final String label;

    public static List<EnumType> getLabelValue() {
        return Stream.of(Gender.values())
                .map(enumType -> new EnumType(enumType.name(), enumType.getLabel()))
                .collect(Collectors.toList());
    }
}
