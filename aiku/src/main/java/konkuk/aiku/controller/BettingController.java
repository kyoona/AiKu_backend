package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.BettingAddDto;
import konkuk.aiku.controller.dto.BettingModifyDto;
import konkuk.aiku.controller.dto.BettingResponseDto;
import konkuk.aiku.controller.dto.SuccessResponseDto;
import konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage;
import konkuk.aiku.domain.BettingResult;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.BettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        Long bettingId = bettingService.addBetting(userAdaptor.getUsers(), scheduleId, bettingAddDto);

        return SuccessResponseDto.getResponseEntity(bettingId, SuccessMessage.ADD_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/{bettingId}")
    public ResponseEntity<BettingResponseDto> getBetting(@PathVariable Long scheduleId, @PathVariable Long bettingId) {

        BettingResponseDto bettingResponseDto = bettingService.findBetting(scheduleId, bettingId);

        return new ResponseEntity(bettingResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{bettingId}")
    public ResponseEntity<SuccessResponseDto> modifyBetting(@PathVariable Long scheduleId, @PathVariable Long bettingId,
                                                            @RequestBody BettingModifyDto bettingModifyDto) {
        Long updateId = bettingService.updateBetting(scheduleId, bettingId, bettingModifyDto);

        return SuccessResponseDto.getResponseEntity(updateId, SuccessMessage.MODIFY_SUCCESS, HttpStatus.OK);

    }

    @DeleteMapping("/{bettingId}")
    public ResponseEntity<SuccessResponseDto> deleteBetting(@PathVariable Long scheduleId, @PathVariable Long bettingId) {
        Long deleteId = bettingService.deleteBetting(bettingId);

        return SuccessResponseDto.getResponseEntity(deleteId, SuccessMessage.DELETE_SUCCESS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BettingResponseDto>> getBettings(@PathVariable Long scheduleId, @RequestParam String bettingType) {
        List<BettingResponseDto> bettingList = bettingService.getBettingsByType(bettingType);

        return new ResponseEntity(bettingList, HttpStatus.OK);
    }

}
