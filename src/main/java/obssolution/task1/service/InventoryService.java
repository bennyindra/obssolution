package obssolution.task1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import obssolution.task1.entity.Inventory;
import obssolution.task1.entity.Item;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.InventoryRequestDto;
import obssolution.task1.repository.InventoryRepository;
import obssolution.task1.repository.ItemRepository;
import obssolution.task1.service.interfaces.IInventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public Inventory getByID(Long id) throws BusinessException {
        return inventoryRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Inventory with id %s not found", id)));
    }

    @Override
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Override
    public Inventory save(InventoryRequestDto obj) throws BusinessException {
        Item item = itemRepository.findById(obj.itemId()).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", obj.itemId())));
        Inventory inventory = new Inventory();
        inventory.setType(obj.type());
        inventory.setQuantity(obj.quantity());
        inventory.setItem(item);
        inventory.setType(obj.type());
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory update(Long id, InventoryRequestDto obj) throws BusinessException {
        Inventory updatedInventory = inventoryRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Inventory with id %s not found", id)));
        updatedInventory.setType(obj.type());
        updatedInventory.setQuantity(obj.quantity());
        if (!obj.itemId().equals(updatedInventory.getItem().getId())) {
            Item item = itemRepository.findById(obj.itemId()).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", obj.itemId())));
            updatedInventory.setItem(item);
        }

        inventoryRepository.save(updatedInventory);
        return updatedInventory;
    }

    @Override
    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

}
