package correcao.enem.controller;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import correcao.enem.service.ExamCorrectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/correct-exam")
@RequiredArgsConstructor
public class ExamCorrectionController {

    private final ExamCorrectionService examCorrectionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> correctExam(
            @RequestPart("file") MultipartFile filePath,
            @RequestPart("userAnswers") @Valid UserAnswersRequest userAnswersRequest) {

        return ResponseEntity.ok(examCorrectionService.correctExam(filePath, userAnswersRequest));
    }

}