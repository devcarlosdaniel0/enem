package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ParsingTextFromPdfException extends RuntimeException {
    public ParsingTextFromPdfException(String message) {
        super(message);
    }
}
