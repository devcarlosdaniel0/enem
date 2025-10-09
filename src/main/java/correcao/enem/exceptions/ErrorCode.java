package correcao.enem.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "That question could not be found."),
    PDF_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while trying to parse PDF content."),
    EXAM_YEAR_NOT_FOUND(HttpStatus.NOT_FOUND, "Exam year not found in answer key."),
    INVALID_EXAM_YEAR(HttpStatus.BAD_REQUEST, "The year of exam is lower than we accept."),
    INVALID_PARAMETERS(HttpStatus.BAD_REQUEST, "Your request parameters did not validate!"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return name();
    }
}
