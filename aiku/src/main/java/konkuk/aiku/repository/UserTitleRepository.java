package konkuk.aiku.repository;

import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.UserTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long>{
    @Query("select u from UserTitle u where u.id = :userTitleId")
    Optional<UserTitle> findByUserTitleId(Long userTitleId);

    @Query("select u from UserTitle u where u.user.id = ?1")
    List<UserTitle> findUserTitlesByUserId(Long userId);
}
