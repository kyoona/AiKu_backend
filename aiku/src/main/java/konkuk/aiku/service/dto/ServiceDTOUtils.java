package konkuk.aiku.service.dto;

import konkuk.aiku.domain.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceDTOUtils {
    public static UserSimpleServiceDTO createUserSimpleServiceDTO(Users user){
        UserSimpleServiceDTO userSimpleServiceDTO = UserSimpleServiceDTO.builder()
                .userId(user.getKakaoId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .userImg(user.getUserImg())
                .setting(createSettingServiceDTO(user))
                .point(user.getPoint())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
        return userSimpleServiceDTO;
    }

    public static SettingServiceDTO createSettingServiceDTO(Users user){
        Setting setting = user.getSetting();
        SettingServiceDTO settingServiceDTO = SettingServiceDTO.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
        return settingServiceDTO;
    }

    public static ScheduleResultServiceDTO createScheduleResultServiceDTO(Schedule scedule, List<UserArrivalData> userArrivalDatas){
        ScheduleResultServiceDTO serviceDTO = ScheduleResultServiceDTO.builder()
                .schedule(createScheduleServiceDTO(scedule))
                .dataSize(userArrivalDatas.size())
                .data(createUserArrivalDataServiceDTOs(userArrivalDatas))
                .build();
        return serviceDTO;
    }

    public static ScheduleServiceDTO createScheduleServiceDTO(Schedule schedule){
        ScheduleServiceDTO serviceDTO = ScheduleServiceDTO.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(createLocationServiceDTO(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return serviceDTO;
    }

    public static List<UserArrivalDataServiceDTO> createUserArrivalDataServiceDTOs(List<UserArrivalData> userArrivalDatas){
        List<UserArrivalDataServiceDTO> serviceDTOs = new ArrayList<>();
        for (UserArrivalData userArrivalData : userArrivalDatas) {
            UserArrivalDataServiceDTO serviceDTO = UserArrivalDataServiceDTO.builder()
                    .userArrivalDataId(userArrivalData.getId())
                    .user(createUserSimpleServiceDTO(userArrivalData.getUser()))
                    .arrivalTime(userArrivalData.getArrivalTime())
                    .timeDifference(userArrivalData.getTimeDifference())
                    .startLocation(userArrivalData.getStartLocation())
                    .endLocation(userArrivalData.getEndLocation())
                    .build();
            serviceDTOs.add(serviceDTO);
        }
        return serviceDTOs;
    }

    public static ScheduleDetailServiceDTO createScheduleDetailServiceDTO(Schedule schedule, List<Users> waitUsers){

        ScheduleDetailServiceDTO scheduleDetailServiceDTO = ScheduleDetailServiceDTO.builder()
                .id(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(createLocationServiceDTO(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .acceptUsers(createUserSimpleServiceDTOsByUserSchedule(schedule.getUsers()))
                .waitUsers(createUserSimpleServiceDTOsByUsers(waitUsers))
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return scheduleDetailServiceDTO;
    }

    public static List<UserSimpleServiceDTO> createUserSimpleServiceDTOsByUserSchedule(List<UserSchedule> users){
        List<UserSimpleServiceDTO> userSimpleServiceDTOs = new ArrayList<>();
        for (UserSchedule userSchedule : users) {
            Users user = userSchedule.getUser();
            UserSimpleServiceDTO userSimpleServiceDTO = UserSimpleServiceDTO.builder()
                    .userId(user.getKakaoId())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .userImg(user.getUserImg())
                    .setting(createSettingServiceDTO(user.getSetting()))
                    .point(user.getPoint())
                    .role(user.getRole())
                    .createdAt(user.getCreatedAt())
                    .modifiedAt(user.getModifiedAt())
                    .build();
            userSimpleServiceDTOs.add(userSimpleServiceDTO);
        }
        return userSimpleServiceDTOs;
    }

    public static List<UserSimpleServiceDTO> createUserSimpleServiceDTOsByUsers(List<Users> users){
        List<UserSimpleServiceDTO> userSimpleServiceDTOs = new ArrayList<>();
        for (Users user : users) {
            UserSimpleServiceDTO userSimpleServiceDTO = UserSimpleServiceDTO.builder()
                    .userId(user.getKakaoId())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .userImg(user.getUserImg())
                    .setting(createSettingServiceDTO(user.getSetting()))
                    .point(user.getPoint())
                    .role(user.getRole())
                    .createdAt(user.getCreatedAt())
                    .modifiedAt(user.getModifiedAt())
                    .build();
            userSimpleServiceDTOs.add(userSimpleServiceDTO);
        }
        return userSimpleServiceDTOs;
    }

    public static Location createLocation(LocationServiceDTO locationServiceDTO){
        return new Location(locationServiceDTO.getLatitude(), locationServiceDTO.getLongitude(), locationServiceDTO.getLocationName());
    }

    public static LocationServiceDTO createLocationServiceDTO(Location location){
        return new LocationServiceDTO(location.getLatitude(), location.getLongitude(), location.getLocationName());
    }

    public static SettingServiceDTO createSettingServiceDTO(Setting setting){
        SettingServiceDTO settingServiceDTO = SettingServiceDTO.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
        return settingServiceDTO;
    }
}
