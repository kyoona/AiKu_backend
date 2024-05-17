package konkuk.aiku.repository;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupsRepository extends JpaRepository<Groups, Long> , GroupsRepositoryCustom{
    @Query("SELECT ug FROM UserGroup ug JOIN FETCH ug.user WHERE ug.group.id = :groupId")
    List<UserGroup> findUserGroupWithUser(@Param("groupId") Long groupId);
}
