package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidYearException extends RuntimeException {
    public InvalidYearException(String message) {
        super(message);
    }
}
