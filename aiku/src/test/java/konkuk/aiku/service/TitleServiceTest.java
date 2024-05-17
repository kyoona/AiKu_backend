package konkuk.aiku.service;

import konkuk.aiku.controller.dto.TitleDto;
import konkuk.aiku.controller.dto.TitleResponseDto;
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
@Transactional
@SpringBootTest
class TitleServiceTest {
    @Autowired
    AdminService adminService;

    @Autowired
    TitleService titleService;

    @BeforeEach
    void setUp() {
        TitleDto titleADto = TitleDto.builder()
                .titleName("TitleA")
                .description("Title for Test1")
                .titleImg("abc1")
                .build();

        TitleDto titleBDto = TitleDto.builder()
                .titleName("TitleB")
                .description("Title for Test2")
                .titleImg("abc2")
                .build();

        TitleDto titleCDto = TitleDto.builder()
                .titleName("TitleC")
                .description("Title for Test3")
                .titleImg("abc3")
                .build();

        adminService.addTitle(titleADto.toServiceDto());
        adminService.addTitle(titleBDto.toServiceDto());
        adminService.addTitle(titleCDto.toServiceDto());
    }

    @Test
    void getTitles() {
        List<TitleResponseDto> titles = titleService.getTitles();

        log.info("titles={}", titles);

        Assertions.assertThat(titles.size()).isEqualTo(3);
    }
}