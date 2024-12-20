package com.module.report.domain.filters.enums;

import com.module.report.domain.filters.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Rating {

    G,
    PG,
    PG_13,
    R,
    NC_17,
    UNRATED,
    TV_Y,
    TV_Y7,
    TV_G,
    TV_PG,
    TV_14,
    TV_MA,
    L,
    DEZ,
    DOZE,
    QUATORZE,
    DEZESSEIS,
    DEZOITO;

    public static List<EnumType> getLabelValue() {
        return Stream.of(Rating.values())
                .map(enumType -> new EnumType(enumType.name()))
                .collect(Collectors.toList());
    }
}
