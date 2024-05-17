package konkuk.aiku.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupListServiceDto {
    private Long userId;
    private List<GroupSimpleServiceDto> data = new ArrayList<>();

    public static GroupListServiceDto toDto(Long userId, List<GroupSimpleServiceDto> data) {
        return new GroupListServiceDto(userId, data);
    }
}
