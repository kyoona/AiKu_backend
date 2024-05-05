package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.LocationDTO;
import konkuk.aiku.controller.dto.ScheduleDTO;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller(value = "/groups/{groupId}/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public void scheduleAdd(@PathVariable Long groupId,
                            @RequestBody @Valid ScheduleDTO scheduleDTO,
                            @AuthenticationPrincipal UserDetails userDetails){
        String kakaoId = userDetails.getPassword();

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName(scheduleDTO.getScheduleName())
                .location(createLocationServiceDTO(scheduleDTO.getLocation()))
                .scheduleTime(scheduleDTO.getScheduleTime())
                .build();

        scheduleService.addSchedule(kakaoId, groupId, scheduleServiceDTO);
    }

    @PatchMapping("/{scheduleId}")
    public void scheduleModify(@PathVariable Long groupId,
                                @PathVariable Long scheduleId,
                                @RequestBody @Valid ScheduleDTO scheduleDTO,
                                @AuthenticationPrincipal UserDetails userDetails){
        String kakaoId = userDetails.getPassword();

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName(scheduleDTO.getScheduleName())
                .location(createLocationServiceDTO(scheduleDTO.getLocation()))
                .scheduleTime(scheduleDTO.getScheduleTime())
                .build();

        scheduleService.modifySchedule(kakaoId, groupId, scheduleId, scheduleServiceDTO);
    }

    @DeleteMapping("/{scheduleId}")
    public void scheduleDelete(@PathVariable Long groupId,
                               @PathVariable Long scheduleId,
                               @AuthenticationPrincipal UserDetails userDetails){
        String kakaoId = userDetails.getPassword();
        scheduleService.deleteSchedule(kakaoId, groupId, scheduleId);
    }

    private LocationServiceDTO createLocationServiceDTO(LocationDTO location){
        return new LocationServiceDTO(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }
}
