package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userGroupId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    private Groups group;

    protected UserGroup(Users user, Groups group) {
        this.user = user;
        this.group = group;
    }

    //==연관 관계 편의 메서드==
    public void setGroup(Groups group) {
        this.group = group;
    }
}
