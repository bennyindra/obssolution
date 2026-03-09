package obssolution.task1.service;

import obssolution.task1.entity.Inventory;
import obssolution.task1.entity.Item;
import obssolution.task1.enums.InventoryType;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.InventoryRequestDto;
import obssolution.task1.repository.InventoryRepository;
import obssolution.task1.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private final InventoryRepository inventoryRepository = Mockito.mock(InventoryRepository.class);
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final InventoryService inventoryService = new InventoryService(inventoryRepository, itemRepository);
    
    @Test
    void getByID() throws BusinessException {
        Long id = 1L;
        Inventory inventory = new Inventory();
        inventory.setId(id);
    
        Mockito.when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
    
        Inventory result = inventoryService.getByID(id);
    
        assertNotNull(result);
        assertEquals(id, result.getId());
    
        Mockito.verify(inventoryRepository).findById(id);
    }
    
    @Test
    void getByIdNotFound() {
        Long id = 999L;
        Mockito.when(inventoryRepository.findById(id)).thenReturn(Optional.empty());
    
        BusinessException exception = assertThrows(BusinessException.class, () -> inventoryService.getByID(id));
        assertEquals(" 400 BAD_REQUEST : Inventory with id 999 not found", exception.getMessage());
    }


    @Test
    void findAll() {
        Page<Inventory> page = new PageImpl<>(List.of(new Inventory()));
        Pageable pageable = Pageable.ofSize(10);

        Mockito.when(inventoryRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
    
        Page<Inventory> result = inventoryService.findAll(pageable);
    
        assertNotNull(result);
        Mockito.verify(inventoryRepository).findAll(pageable);
    }
    
    @Test
    void save() throws BusinessException {
        InventoryRequestDto dto = new InventoryRequestDto(1L, 10, InventoryType.T);
        Item item = new Item();
        item.setId(1L);
    
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(inventoryRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
    
        Inventory result = inventoryService.save(dto);
    
        assertNotNull(result);
        assertEquals(10, result.getQuantity());
        assertEquals(InventoryType.T, result.getType());
        Mockito.verify(itemRepository).findById(1L);
        Mockito.verify(inventoryRepository).save(Mockito.any(Inventory.class));
    }
    
    @Test
    void save_ItemNotFound() {
        InventoryRequestDto dto = new InventoryRequestDto(999L, 10, InventoryType.T);
    
        Mockito.when(itemRepository.findById(999L)).thenReturn(Optional.empty());
    
        Exception exception = assertThrows(BusinessException.class, () -> inventoryService.save(dto));
        assertEquals(" 400 BAD_REQUEST : Item with id 999 not found", exception.getMessage());
    }
    
    @Test
    void update() throws BusinessException {
        Long id = 1L;
        InventoryRequestDto dto = new InventoryRequestDto(2L, 20, InventoryType.W);
    
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setItem(new Item());
        inventory.getItem().setId(1L);
    
        Item newItem = new Item();
        newItem.setId(2L);
    
        Mockito.when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        Mockito.when(itemRepository.findById(2L)).thenReturn(Optional.of(newItem));
    
        Inventory result = inventoryService.update(id, dto);
    
        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        assertEquals(InventoryType.W, result.getType());
        assertEquals(2L, result.getItem().getId());
        Mockito.verify(inventoryRepository).findById(id);
        Mockito.verify(itemRepository).findById(2L);
        Mockito.verify(inventoryRepository).save(Mockito.any(Inventory.class));
    }
    
    @Test
    void delete() {
        Long id = 1L;
    
        Mockito.doNothing().when(inventoryRepository).deleteById(id);
    
        inventoryService.delete(id);
    
        Mockito.verify(inventoryRepository).deleteById(id);
    }
}