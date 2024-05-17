package konkuk.aiku.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "titleId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    private String titleName;
    private String description;
    private String titleImg;

    @Builder
    public Title(String titleName, String description, String titleImg) {
        this.titleName = titleName;
        this.description = description;
        this.titleImg = titleImg;
    }

    public void updateTitle(String titleName, String description, String titleImg) {
        this.titleName = titleName;
        this.description = description;
        this.titleImg = titleImg;
    }
}
