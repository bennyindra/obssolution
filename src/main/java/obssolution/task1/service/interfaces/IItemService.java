package obssolution.task1.service.interfaces;

import obssolution.task1.entity.Item;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.ItemRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IItemService {
    public ItemDto getByID(Long aLong, boolean includeStock) throws BusinessException;

    public Page<ItemDto> findAll(boolean includeStock, Pageable pageable);

    public Item save(ItemRequestDto item);

    public Item update(Long id, ItemRequestDto item) throws BusinessException;

    public void delete(Long item);
}
