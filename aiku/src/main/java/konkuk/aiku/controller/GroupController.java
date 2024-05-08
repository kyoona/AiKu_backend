package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.EntryDTO;
import konkuk.aiku.controller.dto.GroupDTO;
import konkuk.aiku.controller.dto.GroupResponseDTO;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.GroupService;
import konkuk.aiku.service.dto.GroupDetailServiceDTO;
import konkuk.aiku.service.dto.GroupServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public void groupAdd(@RequestBody @Valid GroupDTO groupDTO,
                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName(groupDTO.getGroupName())
                .description(groupDTO.getDescription())
                .groupImg(null)
                .build();
        groupService.addGroup(user, groupServiceDTO);
    }

    @PatchMapping("/{groupId}")
    public void groupModify(@PathVariable Long groupId,
                            @RequestBody @Valid GroupDTO groupDTO,
                            @AuthenticationPrincipal UserAdaptor userAdapter){
        Users user = userAdapter.getUsers();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName(groupDTO.getGroupName())
                .description(groupDTO.getDescription())
                .groupImg(null)
                .build();
        groupService.modifyGroup(user, groupId, groupServiceDTO);
    }

    @DeleteMapping("/{groupId}")
    public void groupDelete(@PathVariable Long groupId,
                            @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        groupService.deleteGroup(user, groupId);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDTO> groupDetails(@PathVariable Long userId,
                                                         @PathVariable Long groupId,
                                                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupDetailServiceDTO serviceDTO = groupService.findGroupDetailById(user, groupId);

        GroupResponseDTO responseDto = GroupResponseDTO.toDto(serviceDTO);
        return new ResponseEntity<GroupResponseDTO>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/entry")
    public void groupEntry(@PathVariable Long groupId,
                           @RequestBody EntryDTO entryDTO,
                           @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        if (entryDTO.getEnter()) {
            groupService.enterGroup(user, groupId);
        } else {
            groupService.exitGroup(user, groupId);
        }
    }

}
