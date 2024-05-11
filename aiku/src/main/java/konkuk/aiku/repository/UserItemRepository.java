package konkuk.aiku.repository;

import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.userItem.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    @Query("select u from UserItem u where u.user.id = ?1 and u.item.itemCategory = ?2")
    public List<UserItem> findUserItemsByUserIdAndItemItemCategory(Long userId, ItemCategory itemCategory);
}
