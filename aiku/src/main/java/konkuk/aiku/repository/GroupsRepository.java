package konkuk.aiku.repository;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupsRepository extends JpaRepository<Groups, Long> , GroupsRepositoryCustom{
    @Query("SELECT g FROM Groups g LEFT JOIN FETCH g.schedules s WHERE g.id = :groupId")
    Groups findGroupWithSchedule(@Param("groupId") Long groupId);

    @Query("SELECT ug FROM UserGroup ug JOIN FETCH ug.user WHERE ug.group.id = :groupId")
    List<UserGroup> findUserGroupWithUser(@Param("groupId") Long groupId);
    @Query("SELECT ug FROM UserGroup ug JOIN FETCH ug.group WHERE ug.user.id = :userId")
    List<UserGroup> findUserGroupWithGroup(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Groups g SET g.userCount = g.userCount + 1 WHERE g.id = :groupId")
    void upGroupUserCount(@Param("groupId") Long groupId);
    @Modifying
    @Query("UPDATE Groups g SET g.userCount = g.userCount - 1 WHERE g.id = :groupId")
    void downGroupUserCount(@Param("groupId") Long groupId);
}
