package obssolution.task1.service;

import obssolution.task1.entity.Item;
import obssolution.task1.entity.Order;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.OrderRequestDto;
import obssolution.task1.repository.InventoryRepository;
import obssolution.task1.repository.ItemRepository;
import obssolution.task1.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final InventoryRepository inventoryRepository = mock(InventoryRepository.class);

    private final OrderService orderService = new OrderService(orderRepository, itemRepository,inventoryRepository);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByOrderNo() throws BusinessException {
        String orderNo = "12345";
        Order order = new Order();
        order.setOrderNo(orderNo);

        when(orderRepository.findByOrderNo(orderNo)).thenReturn(Optional.of(order));

        Order result = orderService.getByOrderNo(orderNo);

        assertNotNull(result);
        assertEquals(orderNo, result.getOrderNo());
        verify(orderRepository, times(1)).findByOrderNo(orderNo);
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(java.util.Collections.singletonList(order));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        Page<Order> result = orderService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    void save() throws BusinessException {
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 5);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal("10.00"));

        when(itemRepository.findByIdWithLock(orderRequestDto.itemId())).thenReturn(Optional.of(item));
        when(inventoryRepository.findTotalStockByItemId(item.getId())).thenReturn(10);

        Order order = new Order();
        order.setPrice(item.getPrice());
        order.setQuantity(orderRequestDto.quantity());
        order.setItem(item);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.save(orderRequestDto);

        assertNotNull(result);
        assertEquals(orderRequestDto.quantity(), result.getQuantity());
        assertEquals(orderRequestDto.itemId(), result.getItem().getId());
        verify(itemRepository, times(1)).findByIdWithLock(orderRequestDto.itemId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void update() throws BusinessException {
        String id = "12345";
        OrderRequestDto orderRequestDto = new OrderRequestDto(1L, 5);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal("10.00"));
        Order order = new Order();
        order.setOrderNo(id);
        order.setItem(item);
        order.setQuantity(3);

        when(orderRepository.findByOrderNo(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(inventoryRepository.findTotalStockByItemId(item.getId())).thenReturn(10);
        when(itemRepository.findByIdWithLock(any(Long.class))).thenReturn(Optional.of(item));

        Order result = orderService.update(id, orderRequestDto);

        assertNotNull(result);
        assertEquals(orderRequestDto.quantity(), result.getQuantity());
        verify(orderRepository, times(1)).findByOrderNo(id);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void delete() throws BusinessException {
        String orderNo = "12345";
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setQuantity(5);
        Item item = new Item();
        order.setItem(item);

        when(orderRepository.findByOrderNo(orderNo)).thenReturn(Optional.of(order));

        orderService.delete(orderNo);

        verify(orderRepository, times(1)).findByOrderNo(orderNo);
        verify(orderRepository, times(1)).deleteByOrderNo(orderNo);
    }
}