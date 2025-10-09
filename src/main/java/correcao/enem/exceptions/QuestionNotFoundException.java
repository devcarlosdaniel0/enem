package correcao.enem.exceptions;

public class QuestionNotFoundException extends BaseException {
    public QuestionNotFoundException(String message) {
        super(ErrorCode.QUESTION_NOT_FOUND, message);
    }
}
