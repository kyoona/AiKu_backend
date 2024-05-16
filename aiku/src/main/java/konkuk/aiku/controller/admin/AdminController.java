package konkuk.aiku.controller.admin;

import konkuk.aiku.controller.dto.ItemDto;
import konkuk.aiku.controller.dto.SuccessResponseDto;
import konkuk.aiku.controller.dto.TitleDto;
import konkuk.aiku.service.AdminService;
import konkuk.aiku.service.dto.ItemServiceDto;
import konkuk.aiku.service.dto.TitleServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/items")
    public ResponseEntity<SuccessResponseDto> addItem(ItemDto itemDto){
        ItemServiceDto itemServiceDto = itemDto.toServiceDto();

        Long itemId = adminService.addItem(itemServiceDto);

        return SuccessResponseDto.getResponseEntity(itemId, ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<SuccessResponseDto> updateItem(@PathVariable Long itemId, ItemDto itemDto){
        ItemServiceDto itemServiceDto = itemDto.toServiceDto();
        Long updateItemId = adminService.updateItem(itemId, itemServiceDto);

        return SuccessResponseDto.getResponseEntity(updateItemId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<SuccessResponseDto> deleteItem(@PathVariable Long itemId){
        Long deleteItemId = adminService.deleteItem(itemId);

        return SuccessResponseDto.getResponseEntity(deleteItemId, DELETE_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/titles")
    public ResponseEntity<SuccessResponseDto> addTitle(TitleDto titleDto){
        TitleServiceDto titleServiceDto = titleDto.toServiceDto();
        Long titleId = adminService.addTitle(titleServiceDto);

        return SuccessResponseDto.getResponseEntity(titleId, ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/titles/{titleId}")
    public ResponseEntity<SuccessResponseDto> updateTitle(@PathVariable Long titleId, TitleDto titleDto){
        TitleServiceDto titleServiceDto = titleDto.toServiceDto();
        Long updateTitleId = adminService.updateTitle(titleId, titleServiceDto);

        return SuccessResponseDto.getResponseEntity(updateTitleId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping("/titles/{titleId}")
    public ResponseEntity<SuccessResponseDto> deleteTitle(@PathVariable Long titleId){
        Long deleteTitleId = adminService.deleteTitle(titleId);

        return SuccessResponseDto.getResponseEntity(deleteTitleId, DELETE_SUCCESS, HttpStatus.OK);
    }




}

