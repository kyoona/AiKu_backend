package konkuk.aiku.service;

import konkuk.aiku.controller.dto.ItemResponseDto;
import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public List<ItemResponseDto> getItemList(String itemType) {
        ItemCategory category = ItemCategory.valueOf(itemType);

        return itemRepository.findItemsByItemCategory(category)
                .stream().map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDto> getSaleItem() {
        return itemRepository.findItemsByEventStatus(EventStatus.EVENT)
                .stream().map(ItemResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
