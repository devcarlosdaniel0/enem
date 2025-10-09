package correcao.enem.exceptions;

public class InvalidYearException extends BaseException {
    public InvalidYearException(String message) {
        super(ErrorCode.INVALID_EXAM_YEAR, message);
    }
}
