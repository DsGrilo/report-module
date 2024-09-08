package com.module.report.domain.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Filter {
    private String name; //Forma de Pagamento, Dispositivos
    private String key; //paymentMethod, device
    private TypeFilter type;
    private List<Object> dataFilter;

}
