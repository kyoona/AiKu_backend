package konkuk.aiku.service;

import konkuk.aiku.controller.dto.ItemDto;
import konkuk.aiku.controller.dto.ItemResponseDto;
import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired
    AdminService adminService;

    @Autowired
    ItemService itemService;

    @BeforeEach
    void setUp() {
        ItemDto item1 = ItemDto.builder()
                .itemName("Item1")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.NORMAL)
                .price(400)
                .eventPrice(400)
                .eventDescription("이벤트 중이 아니에요")
                .build();

        ItemDto item2 = ItemDto.builder()
                .itemName("Item2")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(200)
                .eventDescription("이벤트 중이에요")
                .build();

        ItemDto item3 = ItemDto.builder()
                .itemName("Item3")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(400)
                .eventPrice(300)
                .eventDescription("이벤트 중이에요")
                .build();


        adminService.addItem(item1.toServiceDto());
        adminService.addItem(item2.toServiceDto());
        adminService.addItem(item3.toServiceDto());
    }

    @Test
    void getItemList() {
        List<ItemResponseDto> soundItems = itemService.getItemList("SOUND");
        List<ItemResponseDto> backgroundItems = itemService.getItemList("BACKGROUND");
        List<ItemResponseDto> defaultItems = itemService.getItemList("ITEM");

        log.info("soundItems={}", soundItems);
        log.info("backgroundItems={}", backgroundItems);
        log.info("defaultItems={}", defaultItems);

        Assertions.assertThat(soundItems.size()).isEqualTo(0);
        Assertions.assertThat(backgroundItems.size()).isEqualTo(0);
        Assertions.assertThat(defaultItems.size()).isEqualTo(3);
    }

    @Test
    void getSaleItem() {
        List<ItemResponseDto> saleItemList = itemService.getSaleItem();

        log.info("saleItemList={}", saleItemList);

        Assertions.assertThat(saleItemList.size()).isEqualTo(2);
    }
}