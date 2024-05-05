package konkuk.aiku.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller(value = "/groups/{groupId}/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public void scheduleAdd(@PathVariable Long groupId,
                            @RequestBody ScheduleDTO scheduleDTO,
                            @AuthenticationPrincipal UserDetails userDetails){
        String kakaoId = userDetails.getPassword();

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName(scheduleDTO.getScheduleName())
                .location(createLocationServiceDTO(scheduleDTO.getLocation()))
                .scheduleTime(scheduleDTO.getScheduleTime())
                .build();

        scheduleService.scheduleAdd(kakaoId, groupId, scheduleServiceDTO);
    }

    private LocationServiceDTO createLocationServiceDTO(LocationDTO location){
        return new LocationServiceDTO(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }
}
