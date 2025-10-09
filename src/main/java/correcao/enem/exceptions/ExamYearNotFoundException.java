package correcao.enem.exceptions;

public class ExamYearNotFoundException extends BaseException {
    public ExamYearNotFoundException(String message) {
        super(ErrorCode.EXAM_YEAR_NOT_FOUND, message);
    }
}
