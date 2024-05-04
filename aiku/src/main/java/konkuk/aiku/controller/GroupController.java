package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.EntryDTO;
import konkuk.aiku.controller.dto.GroupDTO;
import konkuk.aiku.controller.dto.GroupResponseDTO;
import konkuk.aiku.controller.dto.UserSimpleResponseDTO;
import konkuk.aiku.service.GroupService;
import konkuk.aiku.service.dto.GroupDetailServiceDTO;
import konkuk.aiku.service.dto.GroupServiceDTO;
import konkuk.aiku.service.dto.UserSimpleServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users/{userId}/groups")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public void groupAdd(@PathVariable Long userId,
                         @RequestBody @Valid GroupDTO groupDTO){
        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName(groupDTO.getGroupName())
                .description(groupDTO.getDescription())
                .groupImg(null)
                .build();
        groupService.addGroup(userId, groupServiceDTO);
    }

    @PatchMapping("/{groupId}")
    public void groupModify(@PathVariable Long userId,
                            @PathVariable Long groupId,
                            @RequestBody @Valid GroupDTO groupDTO){
        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName(groupDTO.getGroupName())
                .description(groupDTO.getDescription())
                .groupImg(null)
                .build();
        groupService.modifyGroup(userId, groupId, groupServiceDTO);
    }

    @DeleteMapping("/{groupId}")
    public void groupDelete(@PathVariable Long userId,
                            @PathVariable Long groupId){
        groupService.deleteGroup(userId, groupId);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDTO> groupDetails(@PathVariable Long userId,
                                                         @PathVariable Long groupId){
        GroupDetailServiceDTO serviceDTO = groupService.findGroupDetailById(userId, groupId);

        GroupResponseDTO groupResponseDTO = createGroupResponseDTO(serviceDTO);
        return new ResponseEntity<GroupResponseDTO>(groupResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/entry")
    public void groupEntry(@PathVariable Long userId,
                           @PathVariable Long groupId,
                           @RequestBody EntryDTO entryDTO){
        if (entryDTO.getEnter()) {
            groupService.enterGroup(userId, groupId);
        } else {
            groupService.exitGroup(userId, groupId);
        }
    }

    private GroupResponseDTO createGroupResponseDTO(GroupDetailServiceDTO serviceDTO) {
        List<UserSimpleServiceDTO> userServiceDTOs = serviceDTO.getUsers();
        List<UserSimpleResponseDTO> userSimpleResponseDTOs = new ArrayList<>();
        for (UserSimpleServiceDTO userServiceDTO : userServiceDTOs) {
            userSimpleResponseDTOs.add(createUserSimpleServiceDTO(userServiceDTO));
        }

        GroupResponseDTO responseDTO = GroupResponseDTO.builder()
                .groupId(serviceDTO.getGroupId())
                .groupName(serviceDTO.getGroupName())
                .description(serviceDTO.getDescription())
                .users(userSimpleResponseDTOs)
                .build();

        return responseDTO;
    }
    private UserSimpleResponseDTO createUserSimpleServiceDTO(UserSimpleServiceDTO serviceDTO){
        UserSimpleResponseDTO responseDTO = new UserSimpleResponseDTO();
        responseDTO.setUserId(serviceDTO.getUserId());
        responseDTO.setUserImg(serviceDTO.getUserImg());
        responseDTO.setUsername(serviceDTO.getUsername());
        return responseDTO;
    }
}
