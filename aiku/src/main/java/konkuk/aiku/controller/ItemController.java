package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.ItemListResponseDto;
import konkuk.aiku.controller.dto.ItemResponseDto;
import konkuk.aiku.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ItemListResponseDto> getItems(@RequestParam String itemType) {
        List<ItemResponseDto> itemList = itemService.getItemList(itemType);

        return new ResponseEntity<>(new ItemListResponseDto(itemList), HttpStatus.OK);
    }

    @GetMapping("/sale")
    public ResponseEntity<ItemListResponseDto> getSaleItems() {
        List<ItemResponseDto> itemList = itemService.getSaleItem();

        return new ResponseEntity<>(new ItemListResponseDto(itemList), HttpStatus.OK);
    }

}
