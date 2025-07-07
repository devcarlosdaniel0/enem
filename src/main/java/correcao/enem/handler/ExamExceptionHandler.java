package correcao.enem.handler;

import correcao.enem.exceptions.InvalidAnswerRequest;
import correcao.enem.exceptions.QuestionNumberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExamExceptionHandler {
    @ExceptionHandler(InvalidAnswerRequest.class)
    public ResponseEntity<ProblemDetail> handlerInvalidAnswerRequest(
            InvalidAnswerRequest e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Your answers in request must be only [A,B,C,D,E].");
        problemDetail.setProperty("timeStamp", timeFormatted());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(QuestionNumberNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerQuestionNumberNotFoundException(
            QuestionNumberNotFoundException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("That question could not be found.");
        problemDetail.setProperty("timeStamp", timeFormatted());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    private String timeFormatted() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }
}