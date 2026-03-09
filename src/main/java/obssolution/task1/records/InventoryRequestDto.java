package obssolution.task1.records;

import obssolution.task1.enums.InventoryType;

public record InventoryRequestDto(
        @jakarta.validation.constraints.NotNull Long itemId,
        @jakarta.validation.constraints.NotNull Integer quantity,
        @jakarta.validation.constraints.NotNull InventoryType type) {
}
