package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAnswerRequest extends RuntimeException {
    public InvalidAnswerRequest(String message) {
        super(message);
    }
}
