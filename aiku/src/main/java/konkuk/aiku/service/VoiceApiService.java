package konkuk.aiku.service;

import konkuk.aiku.controller.dto.VoiceApiStatus;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.VoiceApiException;
import konkuk.aiku.service.dto.VoiceApiResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class VoiceApiService {
    private static final String URL = "http://54.180.117.190:5000/";

    public String test() {
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(URL, String.class);

        return result;
    }

    public VoiceApiResponseDto voiceToData(MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        ResponseEntity<VoiceApiResponseDto> response = restTemplate
                .postForEntity(URL + "/transcribe/", requestEntity, VoiceApiResponseDto.class);

        VoiceApiResponseDto responseBody = response.getBody();
        String status = responseBody.getStatus();

        if (status.equals(VoiceApiStatus.PAST_DATE))
            throw new VoiceApiException(ErrorCode.PAST_DATE);
        else if (status.equals(VoiceApiStatus.NOT_AVAILABLE_DATE))
            throw new VoiceApiException(ErrorCode.NOT_AVAILABLE_DATE);

        return responseBody;
    }

}
