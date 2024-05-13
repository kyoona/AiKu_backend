package konkuk.aiku.firebase.dto;

import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.MessagingException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Message {
    public Map<String, String> toStringMap(){
        Map<String, String> map = new HashMap<>();

        Arrays.stream(this.getClass().getDeclaredFields())
                .forEach((field) -> {
                    try {
                        map.put(field.getName(), String.valueOf(field.get(this)));
                    } catch (IllegalAccessException e) {
                        throw new MessagingException(ErrorCode.FAIL_TO_CONVERT_MESSAGE);
                    }
                });
        return map;
    }
}
