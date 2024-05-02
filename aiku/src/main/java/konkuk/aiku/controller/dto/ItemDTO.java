package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDTO {
    private String itemName;
    private ItemCategory itemCategory;
    private int price;
    private int eventPrice;
    private EventStatus eventStatus;
}
