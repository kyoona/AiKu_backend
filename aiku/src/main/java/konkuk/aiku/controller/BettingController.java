package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.BettingAddDto;
import konkuk.aiku.controller.dto.BettingModifyDto;
import konkuk.aiku.controller.dto.BettingResponseDto;
import konkuk.aiku.controller.dto.SuccessResponseDto;
import konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.dto.BettingServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{scheduleId}/bettings")
public class BettingController {

    private final BettingService bettingService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> addBetting(
            @AuthenticationPrincipal UserAdaptor userAdaptor,
            @PathVariable Long scheduleId, @RequestBody BettingAddDto bettingAddDto
    ) {
        BettingServiceDto bettingServiceDto = bettingAddDto.toServiceDto();

        Long bettingId = bettingService.addBetting(userAdaptor.getUsers(), scheduleId, bettingServiceDto);

        return SuccessResponseDto.getResponseEntity(bettingId, SuccessMessage.ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/{bettingId}")
    public ResponseEntity<SuccessResponseDto> acceptBetting(@PathVariable Long bettingId) {
        Long updateBettingId = bettingService.acceptBetting(bettingId);

        return SuccessResponseDto.getResponseEntity(updateBettingId, SuccessMessage.MODIFY_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{bettingId}")
    public ResponseEntity<BettingResponseDto> getBetting(@PathVariable Long bettingId) {

        BettingServiceDto bettingServiceDto = bettingService.findBetting(bettingId);
        BettingResponseDto bettingResponseDto = BettingResponseDto.toDto(bettingServiceDto);

        return new ResponseEntity(bettingResponseDto, HttpStatus.OK);
    }

    // 베팅 업데이트 미사용(Deprecated)
//    @PatchMapping("/{bettingId}")
//    public ResponseEntity<SuccessResponseDto> modifyBetting(
//            @AuthenticationPrincipal UserAdaptor userAdaptor,
//            @PathVariable Long scheduleId, @PathVariable Long bettingId,
//            @RequestBody BettingModifyDto bettingModifyDto) {
//        BettingServiceDto bettingServiceDto = bettingModifyDto.toServiceDto();
//
//        Long updateId = bettingService.updateBetting(userAdaptor.getUsers(), scheduleId, bettingId, bettingServiceDto);
//
//        return SuccessResponseDto.getResponseEntity(updateId, SuccessMessage.MODIFY_SUCCESS, HttpStatus.OK);
//
//    }

    @DeleteMapping("/{bettingId}")
    public ResponseEntity<SuccessResponseDto> deleteBetting(
            @AuthenticationPrincipal UserAdaptor userAdaptor,
            @PathVariable Long scheduleId, @PathVariable Long bettingId) {
        Long deleteId = bettingService.deleteBetting(userAdaptor.getUsers(), scheduleId, bettingId);

        return SuccessResponseDto.getResponseEntity(deleteId, SuccessMessage.DELETE_SUCCESS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BettingResponseDto>> getBettings(@PathVariable Long scheduleId, @RequestParam String bettingType) {
        List<BettingServiceDto> bettingList = bettingService.getBettingsByType(scheduleId, bettingType);

        List<BettingResponseDto> responseDtoList = bettingList.stream().map(BettingResponseDto::toDto).collect(Collectors.toList());

        return new ResponseEntity(responseDtoList, HttpStatus.OK);
    }

}
