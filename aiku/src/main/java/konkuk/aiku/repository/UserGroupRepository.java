package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom{
    List<UserGroup> findByGroupId(Long groupId);

    @Query("select (count(u) > 0) from UserGroup u where u.user.id = ?1 and u.group.id = ?2")
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
}
