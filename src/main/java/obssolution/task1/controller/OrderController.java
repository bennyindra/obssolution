package obssolution.task1.controller;

import jakarta.validation.Valid;
import obssolution.task1.entity.Order;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.OrderDto;
import obssolution.task1.records.OrderRequestDto;
import obssolution.task1.service.OrderService;
import obssolution.task1.service.interfaces.IOrderService;
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
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get all orders (with optional 'active' filter)
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PageableDefault(sort = "orderNo", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderDto> orders = orderService.findAll(pageable).map(this::constructOrderDto);
        return ResponseEntity.ok(orders);
    }

    // Get a single order by ID
    @GetMapping("/{orderNo}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderNo) throws BusinessException {
        return ResponseEntity.ok(constructOrderDto(orderService.getByOrderNo(orderNo)));
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) throws BusinessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(constructOrderDto(orderService.save(orderRequestDto)));
    }

    // Update an order by ID
    @PutMapping("/{orderNo}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable String orderNo,
            @RequestBody @Valid OrderRequestDto orderRequestDto
    ) throws BusinessException {
        return ResponseEntity.ok(constructOrderDto(orderService.update(orderNo, orderRequestDto)));
    }

    // Delete an order by ID
    @DeleteMapping("/{orderNo}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNo) throws BusinessException {
        orderService.delete(orderNo);
        return ResponseEntity.noContent().build();
    }

    private OrderDto constructOrderDto(Order order) {
        ItemDto itemDto;
        if(order.getItem() != null){
            itemDto = new ItemDto(order.getItem().getId(), order.getItem().getName(), order.getItem().getPrice(), null);
        } else {
            itemDto = new ItemDto(order.getItemIdSnapshot(), null, null, null);
        }
        return new OrderDto(order.getOrderNo(), order.getQuantity(), order.getPrice(), itemDto);
    }

}