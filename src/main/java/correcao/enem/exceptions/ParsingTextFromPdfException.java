package correcao.enem.exceptions;

public class ParsingTextFromPdfException extends BaseException {
    public ParsingTextFromPdfException(String message) {
        super(ErrorCode.PDF_PARSE_ERROR, message);
    }
}
