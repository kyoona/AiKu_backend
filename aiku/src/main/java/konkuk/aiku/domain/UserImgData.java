package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class UserImgData {
    private ImgType imgData;
    private String colorCode;

    public enum ImgType {
        CUSTOM, DEFAULT0, DEFAULT1, DEFAULT2
    }

    @Builder
    public UserImgData(ImgType imgData, String colorCode) {
        this.imgData = imgData;
        this.colorCode = colorCode;
    }
}
