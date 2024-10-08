package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.GroupService;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> groupAdd(@RequestBody @Valid GroupDto groupDTO,
                                                       @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupServiceDto groupServiceDTO = groupDTO.toServiceDto();

        Long addId = groupService.addGroup(user, groupServiceDTO);
        return SuccessResponseDto.getResponseEntity(addId, ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<SuccessResponseDto> groupModify(@PathVariable Long groupId,
                                                          @RequestBody @Valid GroupDto groupDTO,
                                                          @AuthenticationPrincipal UserAdaptor userAdapter){
        Users user = userAdapter.getUsers();

        GroupServiceDto groupServiceDTO = groupDTO.toServiceDto();

        Long modifyId = groupService.modifyGroup(user, groupId, groupServiceDTO);
        return SuccessResponseDto.getResponseEntity(modifyId, MODIFY_SUCCESS, HttpStatus.OK);
    }

/*    @DeleteMapping("/{groupId}")
    public ResponseEntity<SuccessResponseDto> groupDelete(@PathVariable Long groupId,
                                                          @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        Long deleteId = groupService.deleteGroup(user, groupId);
        return SuccessResponseDto.getResponseEntity(deleteId, DELETE_SUCCESS, HttpStatus.OK);
    }*/

    @GetMapping
    public ResponseEntity<GroupListResponseDto> groupsList(@AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupListServiceDto serviceDto = groupService.findGroupList(user);

        GroupListResponseDto responseDto = GroupListResponseDto.toDto(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDto> groupDetails(@PathVariable Long groupId,
                                                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        GroupDetailServiceDto serviceDTO = groupService.findGroupDetail(user, groupId);

        GroupResponseDto responseDto = GroupResponseDto.toDto(serviceDTO);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/enter")
    public ResponseEntity<SuccessResponseDto> groupEnter(@PathVariable Long groupId,
                                                         @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        Long enterId = groupService.enterGroup(user, groupId);
        return SuccessResponseDto.getResponseEntity(enterId, ENTER_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/exit")
    public ResponseEntity<SuccessResponseDto> groupExit(@PathVariable Long groupId,
                                                        @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        Long exitId = groupService.exitGroup(user, groupId);
        return SuccessResponseDto.getResponseEntity(exitId, EXIT_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/analytics/late")
    public ResponseEntity<AnalyticsLateRatingResponseDto> analyticsLateRanking(@PathVariable Long groupId,
                                     @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        AnalyticsLateRatingServiceDto serviceDto = groupService.getLateAnalytics(user, groupId);

        AnalyticsLateRatingResponseDto responseDto = AnalyticsLateRatingResponseDto.toDto(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/analytics/betting")
    public ResponseEntity<AnalyticsBettingRatingResponseDto> analyticsBettingRanking(@PathVariable Long groupId,
                                                                               @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        List<AnalyticsBettingServiceDto> bettingAnalytics = groupService.getBettingAnalytics(user, groupId);

        AnalyticsBettingRatingResponseDto responseDto = AnalyticsBettingRatingResponseDto.toDto(bettingAnalytics);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
