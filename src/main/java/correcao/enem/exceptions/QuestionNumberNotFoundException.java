package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNumberNotFoundException extends RuntimeException {
    public QuestionNumberNotFoundException(String message) {
        super(message);
    }
}
