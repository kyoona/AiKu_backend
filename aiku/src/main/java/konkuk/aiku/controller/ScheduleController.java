package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@Controller
@RequestMapping("/groups/{groupId}/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> scheduleAdd(@PathVariable Long groupId,
                                                          @RequestBody @Valid ScheduleDto scheduleDTO,
                                                          @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        ScheduleServiceDto serviceDto = scheduleDTO.toServiceDto();

        Long addId = scheduleService.addSchedule(user, groupId, serviceDto);
        return SuccessResponseDto.getResponseEntity(addId, ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponseDto> scheduleModify(@PathVariable Long groupId,
                                                             @PathVariable Long scheduleId,
                                                             @RequestBody @Valid ScheduleDto scheduleDTO,
                                                             @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        ScheduleServiceDto serviceDto = scheduleDTO.toServiceDto();

        Long modifyId = scheduleService.modifySchedule(user, groupId, scheduleId, serviceDto);
        return SuccessResponseDto.getResponseEntity(modifyId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponseDto> scheduleDelete(@PathVariable Long groupId,
                                                             @PathVariable Long scheduleId,
                                                             @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        Long deleteId = scheduleService.deleteSchedule(user, groupId, scheduleId);
        return SuccessResponseDto.getResponseEntity(deleteId, DELETE_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> scheduleDetails(@PathVariable Long groupId,
                                                               @PathVariable Long scheduleId,
                                                               @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        ScheduleDetailServiceDto serviceDto = scheduleService.findScheduleDetailById(user, groupId, scheduleId);

        ScheduleResponseDto responseDto = ScheduleResponseDto.toDto(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}/enter")
    public ResponseEntity<SuccessResponseDto> scheduleEnter(@PathVariable Long groupId,
                                                            @PathVariable Long scheduleId,
                                                            @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        Long enterId = scheduleService.enterSchedule(user, groupId, scheduleId);
        return SuccessResponseDto.getResponseEntity(enterId, ENTER_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}/exit")
    public ResponseEntity<SuccessResponseDto> scheduleExit(@PathVariable Long groupId,
                                                           @PathVariable Long scheduleId,
                                                           @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        Long exitId = scheduleService.exitSchedule(user, groupId, scheduleId);
        return SuccessResponseDto.getResponseEntity(exitId, EXIT_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/result")
    public ResponseEntity<ScheduleResultReponseDto> scheduleResult(@PathVariable Long groupId,
                                                                    @PathVariable Long scheduleId,
                                                                    @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        ScheduleResultServiceDto serviceDto = scheduleService.findScheduleResult(user, groupId, scheduleId);
        ScheduleResultReponseDto responseDto = ScheduleResultReponseDto.toDto(serviceDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
