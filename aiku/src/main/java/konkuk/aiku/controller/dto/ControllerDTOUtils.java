package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Location;
import konkuk.aiku.service.dto.GroupDetailServiceDTO;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleDetailServiceDTO;
import konkuk.aiku.service.dto.UserSimpleServiceDTO;

import java.util.ArrayList;
import java.util.List;

public class ControllerDTOUtils {
    public static ScheduleResponseDTO createScheduleResponseDTO(ScheduleDetailServiceDTO serviceDTO){
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

    public static List<UserSimpleResponseDTO> createUserSimpleResponseDTO(List<UserSimpleServiceDTO> serviceDTOs){
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

    public static GroupResponseDTO createGroupResponseDTO(GroupDetailServiceDTO serviceDTO) {
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
    public static UserSimpleResponseDTO createUserSimpleServiceDTO(UserSimpleServiceDTO serviceDTO){
        UserSimpleResponseDTO responseDTO = UserSimpleResponseDTO.builder()
                .userId(serviceDTO.getUserKaKaoId())
                .personName(serviceDTO.getPersonName())
                .userImg(serviceDTO.getUserImg())
                .build();
        return responseDTO;
    }

    public static Location createLocation(LocationServiceDTO serviceDTO){
        return new Location(serviceDTO.getLatitude(), serviceDTO.getLongitude(), serviceDTO.getLocationName());
    }
}
