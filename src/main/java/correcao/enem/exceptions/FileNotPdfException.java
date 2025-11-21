package correcao.enem.exceptions;

public class FileNotPdfException extends BaseException {
    public FileNotPdfException(String message) {
        super(ErrorCode.FILE_NOT_PDF, message);
    }
}
