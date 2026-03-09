package obssolution.task1.records;

import obssolution.task1.enums.InventoryType;

public record InventoryDto(Long id, ItemDto item, int quantity, InventoryType type) {
}
