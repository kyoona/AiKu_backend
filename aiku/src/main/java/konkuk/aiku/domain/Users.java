package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
public class Users extends TimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Setter
    private String username;

    private String phoneNumber;
    private String userImg;

    @Embedded
    private UserImgData userImgData;

    private Long kakaoId;

    @Setter
    private String password;

    @Embedded
    private Setting setting;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserTitle> userTitles;

    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    private UserTitle mainTitle; // 기본으로 설정한 타이틀

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

    public void updateSetting(Setting setting) {
        this.setting = setting;
    }

    public void addTitle(UserTitle userTitle) {
        this.userTitles.add(userTitle);
    }

    public void updateUserImg(String userImg, UserImgData userImgData) {
        this.userImg = userImg;
        this.userImgData = userImgData;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        this.fcmTokenCreateAt = LocalDateTime.now();
    }

    public void plusPoint(int point) {
        this.point += point;
    }

    public void minusPoint(int point) {
        this.point -= point;
    }

    public boolean hasTitle(String titleName) {
        return userTitles.stream()
                .filter(userTitle ->
                        userTitle.getTitle().getTitleName().equals(titleName)
                )
                .findAny().isPresent();
    }
}
