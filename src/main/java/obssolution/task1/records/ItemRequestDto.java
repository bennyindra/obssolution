package obssolution.task1.records;

import java.math.BigDecimal;

public record ItemRequestDto(
        @jakarta.validation.constraints.NotBlank String name,
        @jakarta.validation.constraints.NotNull BigDecimal price) {
}
