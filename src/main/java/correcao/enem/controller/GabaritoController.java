package correcao.enem.controller;

import correcao.enem.service.GabaritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/extract")
@RequiredArgsConstructor
public class GabaritoController {

    private final GabaritoService gabaritoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> extract(
            @RequestPart("file") MultipartFile filePath,
            @RequestPart("data") String data) {

        return ResponseEntity.ok(gabaritoService.correctExam(filePath, data));
    }

}