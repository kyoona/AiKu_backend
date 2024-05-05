package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UsersRepository usersRepository;

    @Test
    public void scheduleAdd() throws Exception{
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
                .build();
        usersRepository.save(user);

//        Groups group =
        
        //when

        //then

    }
}