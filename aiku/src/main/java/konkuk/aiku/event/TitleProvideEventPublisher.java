package konkuk.aiku.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TitleProvideEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void titleProvideEvent(Long userId, Long titleId){
        publisher.publishEvent(new TitleProvideEvent(userId, titleId));
    }

}
