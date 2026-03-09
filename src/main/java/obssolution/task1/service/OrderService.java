package obssolution.task1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import obssolution.task1.entity.Inventory;
import obssolution.task1.entity.Item;
import obssolution.task1.entity.Order;
import obssolution.task1.enums.InventoryType;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.OrderRequestDto;
import obssolution.task1.repository.InventoryRepository;
import obssolution.task1.repository.ItemRepository;
import obssolution.task1.repository.OrderRepository;
import obssolution.task1.service.interfaces.IOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public Order getByOrderNo(String orderNo) throws BusinessException {
        return orderRepository.findByOrderNo(orderNo).orElseThrow(() -> new BusinessException(String.format("OrderNo : %s not found", orderNo)));
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Order save(OrderRequestDto orderRequestDto) throws BusinessException {
        Item item = itemRepository.findByIdWithLock(orderRequestDto.itemId()).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", orderRequestDto.itemId())));
        this.validateStockInventory(item.getId(), orderRequestDto.quantity());
        Order order = new Order();
        order.setItem(item);
        //set price based on item
        order.setPrice(item.getPrice());
        order.setQuantity(orderRequestDto.quantity());
        order.setItemIdSnapshot(orderRequestDto.itemId());
        insertInventory(InventoryType.W, orderRequestDto.quantity(), item);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order update(String id, OrderRequestDto orderRequestDto) throws BusinessException {
        Order updatedOrder = getByOrderNo(id);
        Item item = itemRepository.findByIdWithLock(updatedOrder.getItem().getId()).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", orderRequestDto.itemId())));
        this.validateStockInventory(item.getId(), orderRequestDto.quantity());
        updatedOrder.setItem(item);
        //update price based on item
        updatedOrder.setPrice(item.getPrice());

        if (updatedOrder.getQuantity() != orderRequestDto.quantity()){
            int gap = updatedOrder.getQuantity() - orderRequestDto.quantity();
            insertInventory(gap >= 0 ? InventoryType.T: InventoryType.W, Math.abs(gap), item);
        }
        updatedOrder.setQuantity(orderRequestDto.quantity());
        return orderRepository.save(updatedOrder);
    }

    @Override
    @Transactional
    public void delete(String orderNo) throws BusinessException {
        Order order = orderRepository.findByOrderNo(orderNo).orElseThrow(() -> new BusinessException(String.format("OrderNo : %s not found", orderNo)));
        orderRepository.deleteByOrderNo(orderNo);
        insertInventory(InventoryType.T, order.getQuantity(), order.getItem());
    }

    private void validateStockInventory(Long itemId, int quantity) throws BusinessException {
        int stock = inventoryRepository.findTotalStockByItemId(itemId);
        if (stock < quantity){
            throw new BusinessException("stock exceeds total stock");
        }
    }

    private void insertInventory(InventoryType type, int quantity, Item item) {
        Inventory inventory = new Inventory();
        inventory.setType(type);
        inventory.setQuantity(quantity);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
    }

}
