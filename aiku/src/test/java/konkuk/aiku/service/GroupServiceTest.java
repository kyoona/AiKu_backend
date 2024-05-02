package konkuk.aiku.service;

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

    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        Users user2 = new Users();
        user2.setUsername("user1");
        Long userId2 = usersRepository.save(user2)
                .getId();

        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName("group1");
        groupServiceDTO.setGroupImg(null);
        groupServiceDTO.setDescription("test group1");
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        GroupServiceDTO findGroupDTO = groupService.findGroupById(groupId);
        assertThat(findGroupDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(findGroupDTO.getGroupImg()).isEqualTo(groupServiceDTO.getGroupImg());
        assertThat(findGroupDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() throws IllegalAccessException {
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

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

        GroupServiceDTO findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDTO.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(modifyGroupServiceDTO.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() throws IllegalAccessException {
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        Users user2 = new Users();
        user2.setUsername("user1");
        Long userId2 = usersRepository.save(user2)
                .getId();

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
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName("group1");
        groupServiceDTO.setGroupImg(null);
        groupServiceDTO.setDescription("test group1");
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        groupService.deleteGroup(userId1, groupId);

        assertThat(groupService.findGroupById(groupId)).isNull();
    }
}