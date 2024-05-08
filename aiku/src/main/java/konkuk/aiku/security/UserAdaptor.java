package konkuk.aiku.security;

import konkuk.aiku.domain.Users;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

@Getter
@Slf4j
public class UserAdaptor extends User {
    private Users users;

    public UserAdaptor(Users users) {
        super(String.valueOf(users.getKakaoId()), users.getPassword(), users.getAuthorities());
        this.users = users;
    }
}
