package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserTitle;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UserTitleServiceDto {
    private Long id;
    private UserServiceDto user;
    private TitleServiceDto title;

    public static UserTitleServiceDto toServiceDto(UserTitle userTitle) {
        UserServiceDto user = UserServiceDto.toServiceDto(userTitle.getUser());
        TitleServiceDto title = TitleServiceDto.toDto(userTitle);

        return UserTitleServiceDto.builder()
                .id(userTitle.getId())
                .user(user)
                .title(title)
                .build();
    }

    public UserTitle toEntity() {
        return UserTitle.builder()
                .id(id)
                .title(title.toEntity())
                .user(user.toEntity())
                .build();
    }
}
