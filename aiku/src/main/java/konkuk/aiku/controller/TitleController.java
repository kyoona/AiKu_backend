package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.TitleListResponseDto;
import konkuk.aiku.controller.dto.TitleResponseDto;
import konkuk.aiku.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/titles")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @GetMapping
    public ResponseEntity<TitleListResponseDto> getTitles() {
        List<TitleResponseDto> titleList = titleService.getTitles();

        return new ResponseEntity(new TitleListResponseDto(titleList), HttpStatus.OK);
    }
}
