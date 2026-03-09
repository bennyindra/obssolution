package obssolution.task1.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import obssolution.task1.entity.Item;
import obssolution.task1.exception.BusinessException;
import obssolution.task1.records.ItemDto;
import obssolution.task1.records.ItemRequestDto;
import obssolution.task1.repository.ItemRepository;
import obssolution.task1.service.interfaces.IItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService implements IItemService{

    private final ItemRepository itemRepository;

    @Override
    public ItemDto getByID(Long id, boolean includeStock) throws BusinessException {
        return includeStock ?
                itemRepository.findByIdIncludeStock(id).orElseThrow(() -> new BusinessException(String.format("Item Stock with id %s not found", id))) :
                itemRepository.findById(id).map(item -> new ItemDto(item.getId(), item.getName(), item.getPrice(), null)).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", id)));
    }

    @Override
    public Page<ItemDto> findAll(boolean includeStock, Pageable pageable) {
        return includeStock ? itemRepository.findAllWithStock(pageable) : itemRepository.findAll(pageable).map(item -> new ItemDto(item.getId(), item.getName(), item.getPrice(), null));
    }

    @Override
    public Item save(ItemRequestDto dto) {
        Item item = new Item();
        item.setName(dto.name());
        item.setPrice(dto.price());
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item update(Long id, ItemRequestDto dto) throws BusinessException {
        Item dbItem = itemRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Item with id %s not found", id)));
        dbItem.setId(id);
        dbItem.setName(dto.name());
        dbItem.setPrice(dto.price());
        return itemRepository.save(dbItem);
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
