package com.module.report.domain.filters.enums;

import com.module.report.domain.filters.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum State {
    AC("Acre"), AL("Alagoas"), AP("Amapá"), AM("Amazonas"),
    BA("Bahia"), CE("Ceará"), DF("Distrito Federal"), ES("Espírito Santo"), GO("Goiás"),
    MA("Maranhão"), MT("Mato Grosso"), MS("Mato Grosso do Sul"), MG("Minas Gerais"),
    PA("Pará"), PB("Paraíba"), PR("Paraná"), PE("Pernambuco"), PI("Piauí"),
    RJ("Rio de Janeiro"), RN("Rio Grande do Norte"), RS("Rio Grande do Sul"),
    RO("Rondônia"), RR("Roraima"), SC("Santa Catarina"), SP("São Paulo"),
    SE("Sergipe"), TO("Tocantins");


    private final String label;

    public static List<EnumType> getLabelValue() {
        return Stream.of(State.values())
                .map(enumType -> new EnumType(enumType.name(), enumType.getLabel()))
                .collect(Collectors.toList());
    }
}
