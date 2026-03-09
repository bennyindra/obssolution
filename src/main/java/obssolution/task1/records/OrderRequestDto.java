package obssolution.task1.records;

public record OrderRequestDto(@jakarta.validation.constraints.NotNull Long itemId,
                              @jakarta.validation.constraints.NotNull Integer quantity) {
}
