package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Location;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleDetailServiceDTO;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import konkuk.aiku.service.dto.UserSimpleServiceDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static konkuk.aiku.controller.dto.SuccessResponseDTO.SuccessMessage.*;

@Controller(value = "/groups/{groupId}/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<SuccessResponseDTO> scheduleAdd(@PathVariable Long groupId,
                            @RequestBody @Valid ScheduleDTO scheduleDTO,
                            @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName(scheduleDTO.getScheduleName())
                .location(createLocationServiceDTO(scheduleDTO.getLocation()))
                .scheduleTime(scheduleDTO.getScheduleTime())
                .build();

        scheduleService.addSchedule(user, groupId, scheduleServiceDTO);
        return SuccessResponseDTO.getResponseEntity(ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponseDTO> scheduleModify(@PathVariable Long groupId,
                                @PathVariable Long scheduleId,
                                @RequestBody @Valid ScheduleDTO scheduleDTO,
                                @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName(scheduleDTO.getScheduleName())
                .location(createLocationServiceDTO(scheduleDTO.getLocation()))
                .scheduleTime(scheduleDTO.getScheduleTime())
                .build();

        scheduleService.modifySchedule(user, groupId, scheduleId, scheduleServiceDTO);
        return SuccessResponseDTO.getResponseEntity(MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<SuccessResponseDTO> scheduleDelete(@PathVariable Long groupId,
                               @PathVariable Long scheduleId,
                               @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        scheduleService.deleteSchedule(user, groupId, scheduleId);
        return SuccessResponseDTO.getResponseEntity(DELETE_SUCCESS, HttpStatus.OK);
    }

    private LocationServiceDTO createLocationServiceDTO(LocationDTO location){
        return new LocationServiceDTO(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDTO> scheduleDetails(@PathVariable Long groupId,
                                          @PathVariable Long scheduleId,
                                          @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        ScheduleDetailServiceDTO scheduleDetailServiceDTO = scheduleService.findScheduleDetailById(user, groupId, scheduleId);

        ScheduleResponseDTO scheduleResponseDTO = createScheduleResponseDTO(scheduleDetailServiceDTO);
        return new ResponseEntity<>(scheduleResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}/enter")
    public ResponseEntity<SuccessResponseDTO> scheduleEnter(@PathVariable Long groupId,
                              @PathVariable Long scheduleId,
                              @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        scheduleService.enterSchedule(user, groupId, scheduleId);
        return SuccessResponseDTO.getResponseEntity(ENTER_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}/exit")
    public ResponseEntity<SuccessResponseDTO> scheduleExit(@PathVariable Long groupId,
                              @PathVariable Long scheduleId,
                              @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        scheduleService.exitSchedule(user, groupId, scheduleId);
        return SuccessResponseDTO.getResponseEntity(EXIT_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/result")
    private void scheduleResult(@PathVariable Long groupId,
                                @PathVariable Long scheduleId,
                                @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        scheduleService.findScheduleResult(user, groupId, scheduleId);
    }

    private ScheduleResponseDTO createScheduleResponseDTO(ScheduleDetailServiceDTO serviceDTO){
        ScheduleResponseDTO responseDTO = ScheduleResponseDTO.builder()
                .scheduleId(serviceDTO.getId())
                .scheduleName(serviceDTO.getScheduleName())
                .location(createLocation(serviceDTO.getLocation()))
                .scheduleTime(serviceDTO.getScheduleTime())
                .acceptUsers(createUserSimpleResponseDTO(serviceDTO.getAcceptUsers()))
                .waitUsers(createUserSimpleResponseDTO(serviceDTO.getWaitUsers()))
                .createdAt(serviceDTO.getCreatedAt())
                .build();
        return responseDTO;
    }

    private List<UserSimpleResponseDTO> createUserSimpleResponseDTO(List<UserSimpleServiceDTO> serviceDTOs){
        List<UserSimpleResponseDTO> responseDTOs = new ArrayList<>();
        for (UserSimpleServiceDTO serviceDTO : serviceDTOs) {
            UserSimpleResponseDTO responseDTO = UserSimpleResponseDTO.builder()
                    .userId(serviceDTO.getUserKaKaoId())
                    .personName(serviceDTO.getPersonName())
                    .userImg(serviceDTO.getUserImg())
                    .build();
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    private Location createLocation(LocationServiceDTO serviceDTO){
        return new Location(serviceDTO.getLatitude(), serviceDTO.getLongitude(), serviceDTO.getLocationName());
    }
}
