package konkuk.aiku.domain;

import jakarta.persistence.*;
import konkuk.aiku.domain.item.Item;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @ToString
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

    public void changeStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void setUserAndItem(Users user, Item item) {
        this.item = item;
        this.user = user;
        this.price = item.getPrice();
        this.eventPrice = item.getEventPrice();
        this.eventDescription = item.getEventDescription();
    }

    @Builder
    public Orders(Users user, Item item, OrderStatus status, int price, int eventPrice, String eventDescription, int count) {
        this.user = user;
        this.item = item;
        this.status = status;
        this.price = price;
        this.eventPrice = eventPrice;
        this.eventDescription = eventDescription;
        this.count = count;
    }
}
