package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Title extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "titleId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private String titleName;
    private String Description;
    private String titleImg;
}
