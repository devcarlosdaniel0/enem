package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExamYearNotFoundException extends RuntimeException {
    public ExamYearNotFoundException(String message) {
        super(message);
    }
}
