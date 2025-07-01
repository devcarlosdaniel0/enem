package correcao.enem.controller;

import correcao.enem.dto.GabaritoRequestDTO;
import correcao.enem.service.PdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
@Validated
public class PdfController {

    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<String> correct(@RequestBody @Valid GabaritoRequestDTO request) {
        String filePath = String.format("%d_%d_%s.pdf", request.ano(), request.dia(), request.cor().toUpperCase());
        String text = pdfService.extractContentFromResource(filePath);

        return ResponseEntity.ok(text);
    }


}
