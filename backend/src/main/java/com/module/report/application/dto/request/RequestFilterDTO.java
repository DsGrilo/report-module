package com.module.report.application.dto.request;



import java.util.List;
public record RequestFilterDTO(
        String key,
        List<Object> dataFilter
) {


}
