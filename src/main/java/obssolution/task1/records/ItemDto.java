package obssolution.task1.records;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemDto(Long id, String name, BigDecimal price, Integer stock) {

}
