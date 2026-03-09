package obssolution.task1.service;

import obssolution.task1.entity.Item;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.ItemRequestDto;
import obssolution.task1.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @InjectMocks
    private ItemService itemService = new ItemService(itemRepository);

    public ItemServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByID() throws BusinessException {
        Long itemId = 1L;
        Item mockItem = new Item();
        mockItem.setId(itemId);
        mockItem.setName("Sample Item");
        mockItem.setPrice(BigDecimal.TEN);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        when(itemRepository.findByIdIncludeStock(itemId)).thenReturn(Optional.of(new ItemDto(itemId, "Sample Item", BigDecimal.TEN, 5)));

        ItemDto resultWithoutStock = itemService.getByID(itemId, false);
        assertEquals("Sample Item", resultWithoutStock.name());
        assertEquals(0, resultWithoutStock.price().compareTo(BigDecimal.TEN));
        assertNull(resultWithoutStock.stock());

        ItemDto resultWithStock = itemService.getByID(itemId, true);
        assertEquals("Sample Item", resultWithStock.name());
        assertEquals(0, resultWithStock.price().compareTo(BigDecimal.TEN));
        assertEquals(5, resultWithStock.stock());

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).findByIdIncludeStock(itemId);

        when(itemRepository.findById(-1L)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> itemService.getByID(-1L, false));
        assertEquals(" 400 BAD_REQUEST : Item with id -1 not found", exception.getMessage());
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        List<Item> items = List.of(new Item(1L, "Item1", BigDecimal.TEN, null, null), new Item(2L, "Item2", BigDecimal.valueOf(15.00), null, null));
        Page<Item> itemPage = new PageImpl<>(items);

        when(itemRepository.findAll(pageable)).thenReturn(itemPage);
        Page<ItemDto> resultWithoutStock = itemService.findAll(false, pageable);

        assertEquals(2, resultWithoutStock.getContent().size());
        assertEquals("Item1", resultWithoutStock.getContent().get(0).name());

        verify(itemRepository, times(1)).findAll(pageable);
    }

    @Test
    void save() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("New Item", BigDecimal.valueOf(20.0));
        Item mockItem = new Item();
        mockItem.setId(1L);
        mockItem.setName("New Item");
        mockItem.setPrice(BigDecimal.valueOf(20.00));

        when(itemRepository.save(any(Item.class))).thenReturn(mockItem);
        Item result = itemService.save(itemRequestDto);

        assertNotNull(result);
        assertEquals("New Item", result.getName());
        assertEquals(0, result.getPrice().compareTo(BigDecimal.valueOf(20.00)));

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void update() throws BusinessException {
        Long itemId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto("Updated Item", BigDecimal.valueOf(25.0));
        Item existingItem = new Item(itemId, "Old Item", BigDecimal.valueOf(15.00), null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(existingItem);

        Item updatedItem = itemService.update(itemId, itemRequestDto);

        assertEquals("Updated Item", updatedItem.getName());
        assertEquals(0, updatedItem.getPrice().compareTo(BigDecimal.valueOf(25.00)));

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(any(Item.class));

        when(itemRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> itemService.update(-1L, itemRequestDto));
    }

    @Test
    void delete() {
        Long itemId = 1L;

        doNothing().when(itemRepository).deleteById(itemId);
        itemService.delete(itemId);

        verify(itemRepository, times(1)).deleteById(itemId);
    }
}