package correcao.enem.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import correcao.enem.dto.UserAnswers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateJson {
    private final ObjectMapper objectMapper;

    public UserAnswers validateJsonFromString(String data) {
        try {
            return objectMapper.readValue(data, UserAnswers.class);
        } catch (JsonProcessingException ex) {
            log.error("Error parsing JSON", ex);
            throw new RuntimeException("Invalid JSON");
        }
    }
}
