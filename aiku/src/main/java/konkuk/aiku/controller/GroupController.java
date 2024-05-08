package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.EntryDto;
import konkuk.aiku.controller.dto.GroupDto;
import konkuk.aiku.controller.dto.GroupResponseDto;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.GroupService;
import konkuk.aiku.service.dto.GroupDetailServiceDto;
import konkuk.aiku.service.dto.GroupServiceDto;
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
    public void groupAdd(@RequestBody @Valid GroupDto groupDTO,
                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName(groupDTO.getGroupName())
                .description(groupDTO.getDescription())
                .groupImg(null)
                .build();
        groupService.addGroup(user, groupServiceDTO);
    }

    @PatchMapping("/{groupId}")
    public void groupModify(@PathVariable Long groupId,
                            @RequestBody @Valid GroupDto groupDTO,
                            @AuthenticationPrincipal UserAdaptor userAdapter){
        Users user = userAdapter.getUsers();

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
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
    public ResponseEntity<GroupResponseDto> groupDetails(@PathVariable Long userId,
                                                         @PathVariable Long groupId,
                                                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupDetailServiceDto serviceDTO = groupService.findGroupDetailById(user, groupId);

        GroupResponseDto responseDto = GroupResponseDto.toDto(serviceDTO);
        return new ResponseEntity<GroupResponseDto>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/entry")
    public void groupEntry(@PathVariable Long groupId,
                           @RequestBody EntryDto entryDTO,
                           @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        if (entryDTO.getEnter()) {
            groupService.enterGroup(user, groupId);
        } else {
            groupService.exitGroup(user, groupId);
        }
    }

}
