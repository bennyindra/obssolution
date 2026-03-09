package obssolution.task1.records;

import java.math.BigDecimal;

public record OrderDto(String orderNo, int quantity, BigDecimal price, ItemDto item) {
}