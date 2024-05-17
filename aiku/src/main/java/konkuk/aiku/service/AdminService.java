package konkuk.aiku.service;

import konkuk.aiku.domain.Title;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.ItemRepository;
import konkuk.aiku.repository.TitleRepository;
import konkuk.aiku.service.dto.ItemServiceDto;
import konkuk.aiku.service.dto.TitleServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final ItemRepository itemRepository;
    private final TitleRepository titleRepository;

    public Long addItem(ItemServiceDto itemDto) {
        Item item = itemDto.toEntity();
        itemRepository.save(item);

        return item.getId();
    }

    public Long updateItem(Long itemId, ItemServiceDto itemDto) {
        Item item = findItemById(itemId);
        item.updateItem(itemDto.getItemName(), itemDto.getItemCategory(), itemDto.getPrice(), itemDto.getEventPrice(), itemDto.getEventStatus(), itemDto.getEventDescription());

        return item.getId();
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_ITEM));
    }

    public Long deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);

        return itemId;
    }

    public Long addTitle(TitleServiceDto titleServiceDto) {
        Title title = titleServiceDto.toEntity();
        titleRepository.save(title);

        return title.getId();
    }

    public Long updateTitle(Long titleId, TitleServiceDto titleServiceDto) {
        Title title = findTitleById(titleId);
        title.updateTitle(titleServiceDto.getTitleName(), titleServiceDto.getDescription(), titleServiceDto.getTitleImg());

        return title.getId();
    }

    public Title findTitleById(Long titleId) {
        return titleRepository.findById(titleId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_TITLE));
    }

    public Long deleteTitle(Long titleId) {
        titleRepository.deleteById(titleId);

        return titleId;
    }
}
