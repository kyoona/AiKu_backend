package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.VoiceResponseDto;
import konkuk.aiku.service.VoiceApiService;
import konkuk.aiku.service.dto.VoiceApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VoiceApiController {
    private final VoiceApiService voiceApiService;

    @GetMapping("/voice")
    public ResponseEntity<VoiceResponseDto> voiceToData(@RequestPart MultipartFile file) throws IOException {
        VoiceApiResponseDto voiceApiResponseDto = voiceApiService.voiceToData(file);
        return new ResponseEntity<>(voiceApiResponseDto.toResponseDto(), HttpStatus.OK);
    }

    @GetMapping("/ai-test")
    public String aiTest(){
        String test = voiceApiService.test();
        return test + ": test ok";
    }
}
