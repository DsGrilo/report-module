package com.module.report.domain.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnumType {
    private final String value;
    private final String label;

    @Override
    public String toString() {
        return "PaymentMethodDTO{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

}
