package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Users extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    @Setter(value = AccessLevel.NONE)
    private Long id;
    private String username;
    private String phoneNumber;
    private String userImg;

    @Embedded
    private Setting setting;

    @OneToMany(mappedBy = "user")
    private List<UserTitle> userTitles;
    private int point;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}
