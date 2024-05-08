package konkuk.aiku.service.dto;

import konkuk.aiku.domain.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceDTOUtils {
    public static Location createLocation(LocationServiceDTO locationServiceDTO){
        return new Location(locationServiceDTO.getLatitude(), locationServiceDTO.getLongitude(), locationServiceDTO.getLocationName());
    }
}
