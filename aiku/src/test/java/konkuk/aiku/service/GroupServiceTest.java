package konkuk.aiku.service;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.GroupServiceDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class GroupServiceTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersRepository usersRepository;

    private Long userId1, userId2;

    @BeforeEach
    void beforeEach(){
        Users user = new Users();
        user.setUsername("user1");
        userId1 = usersRepository.save(user)
                .getId();

        Users user2 = new Users();
        user2.setUsername("user2");
        userId2 = usersRepository.save(user2)
                .getId();
    }

    @AfterEach
    void afterEach(){
        usersRepository.deleteAll();
    }
    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName("group1");
        groupServiceDTO.setGroupImg(null);
        groupServiceDTO.setDescription("test group1");
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(groupServiceDTO.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() throws IllegalAccessException {
        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName("group1");
        groupServiceDTO.setGroupImg(null);
        groupServiceDTO.setDescription("test group1");
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        GroupServiceDTO modifyGroupServiceDTO = new GroupServiceDTO();
        modifyGroupServiceDTO.setGroupName("changeName");
        modifyGroupServiceDTO.setGroupImg(null);
        modifyGroupServiceDTO.setDescription("change the group name");
        groupService.modifyGroup(userId1, groupId, modifyGroupServiceDTO);

        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDTO.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(modifyGroupServiceDTO.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() throws IllegalAccessException {
        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName("group1");
        groupServiceDTO.setGroupImg(null);
        groupServiceDTO.setDescription("test group1");
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        GroupServiceDTO modifyGroupServiceDTO2 = new GroupServiceDTO();
        modifyGroupServiceDTO2.setGroupName("error");
        modifyGroupServiceDTO2.setGroupImg(null);
        modifyGroupServiceDTO2.setDescription("can't access");
        assertThatThrownBy(()-> groupService.modifyGroup(userId2, groupId, modifyGroupServiceDTO2))
                .isExactlyInstanceOf(IllegalAccessException.class);
    }
}