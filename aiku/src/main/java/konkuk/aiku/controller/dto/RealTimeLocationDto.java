package konkuk.aiku.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RealTimeLocationDto {
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public RealTimeLocationDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

/*    public Double distance(double scheLat, double scheLon){
        double theta = longitude - scheLon;
        double dist = Math.sin(deg2rad(latitude))* Math.sin(deg2rad(scheLat)) + Math.cos(deg2rad(latitude))*Math.cos(deg2rad(scheLat))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60*1.1515*1609.344;

        return dist; //단위 meter
    }

    //10진수를 radian(라디안)으로 변환
    private double deg2rad(double deg){
        return (deg * Math.PI/180.0);
    }
    //radian(라디안)을 10진수로 변환
    private double rad2deg(double rad){
        return (rad * 180 / Math.PI);
    }*/
}
