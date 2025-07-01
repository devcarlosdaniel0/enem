package correcao.enem.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class PdfService {

    public String extractContentFromResource(String fileName) {
        String text;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);

        } catch (Exception ex) {
            log.error("Error to read pdf from resources", ex);
            return "Error while trying to read PDF file";
        }

        return text;
    }
}
