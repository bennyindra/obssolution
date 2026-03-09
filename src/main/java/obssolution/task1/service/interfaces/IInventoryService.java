package obssolution.task1.service.interfaces;

import obssolution.task1.entity.Inventory;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.InventoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IInventoryService {
    public Inventory getByID(Long id) throws BusinessException;
    public Page<Inventory> findAll(Pageable pageable);
    public Inventory save(InventoryRequestDto obj) throws BusinessException;
    public Inventory update(Long id, InventoryRequestDto obj) throws BusinessException;
    public void delete(Long id);
}
