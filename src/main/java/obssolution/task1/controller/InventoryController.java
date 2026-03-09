package obssolution.task1.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obssolution.task1.entity.Inventory;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.InventoryDto;
import obssolution.task1.records.InventoryRequestDto;
import obssolution.task1.records.ItemDto;
import obssolution.task1.service.interfaces.IInventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryService inventoryService;

    // 1. GET: Fetch paginated inventory items
    @GetMapping
    public ResponseEntity<Page<InventoryDto>> getInventoryList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(inventoryService.findAll(pageable).map(this::constructInventoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long id) throws BusinessException {
        return ResponseEntity.ok(constructInventoryDto(inventoryService.getByID(id)));
    }

    // 2. POST: Save new inventory item
    @PostMapping
    public ResponseEntity<InventoryDto> saveInventory(@RequestBody @Valid InventoryRequestDto inventoryDTO) throws BusinessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(constructInventoryDto(inventoryService.save(inventoryDTO)));
    }

    // 3. PUT: Update existing inventory item
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> editInventory(@PathVariable Long id, @RequestBody @Valid InventoryRequestDto inventoryDTO) throws BusinessException {
        return ResponseEntity.ok(constructInventoryDto(inventoryService.update(id, inventoryDTO)));
    }

    // 4. DELETE: Soft delete an inventory item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private InventoryDto constructInventoryDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                new ItemDto(inventory.getItem().getId(), inventory.getItem().getName(), inventory.getItem().getPrice(), null),
                inventory.getQuantity(),
                inventory.getType()
        );
    }
}