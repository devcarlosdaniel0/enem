package correcao.enem.handler;

import correcao.enem.exceptions.*;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseException e) {
        var code = e.getErrorCode();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                code.getStatus(), e.getMessage()
        );

        problem.setTitle(code.getMessage());
        problem.setProperty("errorCode", code.getCode());
        problem.setProperty("timeStamp", timeFormatted());

        return ResponseEntity.status(code.getStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        var fieldErrors = e.getFieldErrors().stream()
                .map(f -> new InvalidParam(f.getField(), f.getDefaultMessage()))
                .toList();

        var code = ErrorCode.INVALID_PARAMETERS;
        var pb = ProblemDetail.forStatus(code.getStatus());

        pb.setTitle(code.getMessage());
        pb.setProperty("errorCode", code.getCode());
        pb.setProperty("Invalid-params", fieldErrors);
        pb.setProperty("timeStamp", timeFormatted());

        return pb;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnknown(Exception e) {
        var code = ErrorCode.UNKNOWN_ERROR;

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(code.getStatus(), e.getMessage());
        pb.setTitle(code.getMessage());
        pb.setProperty("errorCode", code.getCode());
        pb.setProperty("timeStamp", timeFormatted());

        return ResponseEntity.status(code.getStatus()).body(pb);
    }

    private record InvalidParam(String name, String reason) {
    }

    private String timeFormatted() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }
}
