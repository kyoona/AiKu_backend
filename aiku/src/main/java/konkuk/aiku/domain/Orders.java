package konkuk.aiku.domain;

import jakarta.persistence.*;
import konkuk.aiku.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Orders extends TimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private int price;
    private int eventPrice;
    private String eventDescription;
    int count;
}
