package correcao.enem.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfValidator {
    private static final byte[] PDF_MAGIC_NUMBER = new byte[] {0x25, 0x50, 0x44, 0x46, 0x2D}; // "%PDF-" em ASCII

    public boolean validatePDF(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        if (file.isEmpty() || contentType == null || !contentType.equals("application/pdf") ||
                fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            return false;
        }

        try (InputStream is = file.getInputStream()) {
            byte[] magicBytes = new byte[5];
            int bytesRead = is.read(magicBytes);

            if (bytesRead < 5) {
                return false;
            }

            for (int i = 0; i < 5; i++) {
                if (magicBytes[i] != PDF_MAGIC_NUMBER[i]) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
