package konkuk.aiku.service.dto;

import konkuk.aiku.domain.*;

public class ServiceDtoUtils {
    public static Location createLocation(LocationServiceDto locationServiceDTO){
        return new Location(locationServiceDTO.getLatitude(), locationServiceDTO.getLongitude(), locationServiceDTO.getLocationName());
    }
}
