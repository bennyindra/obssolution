package obssolution.task1.controller;

import jakarta.validation.Valid;
import obssolution.task1.entity.Item;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.ItemRequestDto;
import obssolution.task1.service.ItemService;
import obssolution.task1.service.interfaces.IItemService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final IItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Get all items
    @GetMapping
    public ResponseEntity<Page<ItemDto>> getAllItems(@RequestParam(required = false, defaultValue = "false") boolean includeStock, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(itemService.findAll(includeStock, pageable));
    }

    // Get item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id, @RequestParam(required = false, defaultValue = "false") boolean includeStock) throws BusinessException {
        return ResponseEntity.ok(itemService.getByID(id, includeStock));
    }

    // Create new item
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid ItemRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(constructItemDto(itemService.save(dto)));
    }

    // Update an item by ID
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequestDto item) throws BusinessException {
        return ResponseEntity.ok(constructItemDto(itemService.update(id, item)));
    }

    // Delete an item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ItemDto constructItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getPrice(), null);
    }
}