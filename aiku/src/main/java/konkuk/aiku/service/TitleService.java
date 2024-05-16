package konkuk.aiku.service;

import konkuk.aiku.controller.dto.TitleResponseDto;
import konkuk.aiku.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TitleService {
    private final TitleRepository titleRepository;

    public List<TitleResponseDto> getTitles() {
        return titleRepository.findAll().stream()
                .map(TitleResponseDto::toTitleDto)
                .collect(Collectors.toList());
    }

}
