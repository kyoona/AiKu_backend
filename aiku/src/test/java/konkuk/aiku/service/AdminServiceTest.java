package konkuk.aiku.service;

import konkuk.aiku.controller.dto.ItemDto;
import konkuk.aiku.controller.dto.TitleDto;
import konkuk.aiku.domain.EventStatus;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.Title;
import konkuk.aiku.domain.item.Item;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.service.dto.ItemServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    Long itemId;
    Long titleId;

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
        itemId = adminService.addItem(item1.toServiceDto());

        TitleDto titleDto = TitleDto.builder()
                .titleName("TitleA")
                .description("Title for Test1")
                .titleImg("abc")
                .build();

        titleId = adminService.addTitle(titleDto.toServiceDto());
    }

    @Test
    void addItem() {
        Item itemEntity = adminService.findItemById(itemId);
        Assertions.assertThat(itemEntity.getItemName()).isEqualTo("Item1");
    }

    @Test
    void updateItem() {
        ItemDto itemUpdate = ItemDto.builder()
                .itemName("Item2")
                .itemCategory(ItemCategory.ITEM)
                .eventStatus(EventStatus.EVENT)
                .price(450)
                .eventPrice(450)
                .eventDescription("이벤트 중이에요")
                .build();

        adminService.updateItem(itemId, itemUpdate.toServiceDto());
        Item itemEntity = adminService.findItemById(itemId);

        Assertions.assertThat(itemEntity.getItemName()).isEqualTo("Item2");
    }

    @Test
    void deleteItem() {
        adminService.deleteItem(itemId);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchEntityException.class, () -> adminService.findItemById(itemId));
    }

    @Test
    void addTitle() {
        Title title = adminService.findTitleById(titleId);

        Assertions.assertThat(title.getTitleName()).isEqualTo("TitleA");
    }

    @Test
    void updateTitle() {
        TitleDto titleDto = TitleDto.builder()
                .titleName("TitleB")
                .description("Title for Test2")
                .titleImg("abcd")
                .build();

        Long updateTitleId = adminService.updateTitle(titleId, titleDto.toServiceDto());

        Title title = adminService.findTitleById(updateTitleId);
        log.info(title.toString());
        Assertions.assertThat(title.getTitleName()).isEqualTo("TitleB");

    }

    @Test
    void deleteTitle() {
        adminService.deleteTitle(titleId);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchEntityException.class, () -> adminService.findTitleById(titleId));
    }
}