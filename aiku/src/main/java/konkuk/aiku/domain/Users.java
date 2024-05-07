package konkuk.aiku.domain;

import jakarta.persistence.*;
import konkuk.aiku.controller.dto.UserUpdateDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
public class Users extends TimeEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    @Setter(value = AccessLevel.NONE)
    private Long id;
    private String personName;
    private String phoneNumber;
    private String userImg;

    private String kakaoId;

    @Setter
    private String password;

    @Embedded
    private Setting setting;

    @OneToMany(mappedBy = "user")
    private List<UserTitle> userTitles;
    private int point;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
//        return this.roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
    }

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUser(UserUpdateDTO userUpdateDTO) {
        personName = userUpdateDTO.getPersonName();
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

    @Override
    public String getUsername() {
        return kakaoId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
