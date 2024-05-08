package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.*;

import java.util.ArrayList;
import java.util.List;

public class ControllerDTOUtils {
    public static ScheduleResponseDTO createScheduleResponseDTO(ScheduleDetailServiceDTO serviceDTO){
        ScheduleResponseDTO responseDTO = ScheduleResponseDTO.builder()
                .scheduleId(serviceDTO.getId())
                .scheduleName(serviceDTO.getScheduleName())
                .location(createLocationDTO(serviceDTO.getLocation()))
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
                    .username(serviceDTO.getUsername())
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
                .username(serviceDTO.getUsername())
                .userImg(serviceDTO.getUserImg())
                .build();
        return responseDTO;
    }

    public static LocationDTO createLocationDTO(LocationServiceDTO serviceDTO){
        return new LocationDTO(serviceDTO.getLatitude(), serviceDTO.getLongitude(), serviceDTO.getLocationName());
    }

    public static ScheduleResultReponseDTO createScheduleResultReponseDTO(ScheduleResultServiceDTO serviceDTO){
        ScheduleResultReponseDTO responseDTO = ScheduleResultReponseDTO.builder()
                .schedule(createScheduleDTO(serviceDTO.getSchedule()))
                .dataSize(serviceDTO.getDataSize())
                .data(createUserArrivalDataDTOs(serviceDTO.getData()))
                .build();
        return responseDTO;
    }

    public static ScheduleDTO createScheduleDTO(ScheduleServiceDTO schedule){
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleName(schedule.getScheduleName())
                .location(createLocationDTO(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .build();
        return scheduleDTO;
    }

    public static List<UserArrivalDataDTO> createUserArrivalDataDTOs(List<UserArrivalDataServiceDTO> datas){
        List<UserArrivalDataDTO> userArrivalDataDTOs = new ArrayList<>();
        for (UserArrivalDataServiceDTO data : datas) {
            UserArrivalDataDTO userArrivalDataDTO = UserArrivalDataDTO.builder()
                    .userArrivalDataId(data.getUserArrivalDataId())
                    .user(createUserSimpleServiceDTO(data.getUser()))
                    .timeDifference(data.getTimeDifference())
                    .build();
            userArrivalDataDTOs.add(userArrivalDataDTO);
        }
        return userArrivalDataDTOs;
    }
}
