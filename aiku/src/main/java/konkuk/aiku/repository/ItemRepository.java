package konkuk.aiku.repository;

import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByItemCategory(ItemCategory itemCategory);

    List<Item> findItemsByEventStatus(EventStatus eventStatus);
}
