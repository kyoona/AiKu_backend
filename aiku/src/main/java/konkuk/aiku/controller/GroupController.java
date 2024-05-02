package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.GroupDTO;
import konkuk.aiku.service.GroupService;
import konkuk.aiku.service.dto.GroupServiceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/{userId}/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public void groupAdd(@PathVariable Long userId,
                           @RequestBody @Valid GroupDTO groupDTO){
        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName(groupDTO.getGroupName());
        groupServiceDTO.setDescription(groupDTO.getDescription());
        groupServiceDTO.setGroupImg(null);

        groupService.addGroup(userId, groupServiceDTO);
    }

    @PatchMapping("/{groupId}")
    public void groupModify(@PathVariable Long userId,
                            @PathVariable Long groupId,
                            @RequestBody @Valid GroupDTO groupDTO){
        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setGroupName(groupDTO.getGroupName());
        groupServiceDTO.setDescription(groupDTO.getDescription());
        groupServiceDTO.setGroupImg(null);
    }

    @DeleteMapping("/{groupId}")
    public void groupDelete(@PathVariable Long userId,
                            @PathVariable Long groupId){
        groupService.deleteGroup(userId, groupId);
    }
}
