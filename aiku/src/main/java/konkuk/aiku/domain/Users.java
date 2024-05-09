package konkuk.aiku.domain;

import jakarta.persistence.*;
import konkuk.aiku.controller.dto.UserUpdateDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
public class Users extends TimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    @Setter(value = AccessLevel.NONE)
    private Long id;
    private String username;
    private String phoneNumber;
    private String userImg;

    private Long kakaoId;

    @Setter
    private String password;

    @Embedded
    private Setting setting;

    @OneToMany(mappedBy = "user")
    private List<UserTitle> userTitles;
    private int point;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    private String fcmToken;
    private LocalDateTime fcmTokenCreateAt;

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUser(UserUpdateDto userUpdateDTO) {
        username = userUpdateDTO.getUsername();
        Long userTitleId = userUpdateDTO.getUserTitleId();

        for (UserTitle userTitle : userTitles) {
            // 원래 사용 중이던 타이틀 사용 제거
            if (userTitle.isUsed()) {
                userTitle.setUsed(false);
            }

            // 새로운 타이틀 사용
            if (userTitle.getId() == userTitleId) {
                userTitle.setUsed(true);
            }
        }
    }

    public void updateSetting(Setting setting) {
        this.setting = setting;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        this.fcmTokenCreateAt = LocalDateTime.now();
    }
}
