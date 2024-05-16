package konkuk.aiku.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Groups엔티티 테스트")
class GroupsTest {

    @Test
    void createGroups() {
        //given
        Users user = createUser(1L, "user1");

        //given
        String groupName = "group1";
        String groupDescription = "createGroups test";
        Groups group = Groups.createGroups(user, groupName, groupDescription);

        //then
        UserGroup userGroup = group.getUserGroups().get(0);
        assertThat(userGroup.getGroup()).isEqualTo(group);
        assertThat(userGroup.getUser()).isEqualTo(user);
    }

    @Test
    void updateGroup() {
        //given
        Users user = createUser(2L, "user2");
        Groups group = Groups.createGroups(user, "group1", "group1입니다.");

        //when
        String updateName = "updateGroup";
        String updateDescription = "Group update!";
        group.updateGroup(updateName, updateDescription);

        //then
        assertThat(group.getGroupName()).isEqualTo(updateName);
        assertThat(group.getDescription()).isEqualTo(updateDescription);
    }

    @Test
    void addSchedule() {
        //given
        Users user = createUser(2L, "user2");
        Groups group = Groups.createGroups(user, "group1", "group1입니다.");

        //when
        group.addSchedule();

        //then

    }

    Users createUser(Long id, String username){
        return Users.builder()
                .id(id)
                .username(username)
                .build();
    }
}