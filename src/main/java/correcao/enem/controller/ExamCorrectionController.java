package correcao.enem.controller;

import correcao.enem.dto.ResultResponse;
import correcao.enem.dto.UserAnswersRequest;
import correcao.enem.service.ExamCorrectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/correct-exam")
@RequiredArgsConstructor
@Slf4j
public class ExamCorrectionController {

    private final ExamCorrectionService examCorrectionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> correctExam(
            @RequestPart("file") MultipartFile file,
            @RequestPart("userAnswers") @Valid UserAnswersRequest userAnswersRequest) {

        log.info(String.valueOf(userAnswersRequest));

        return ResponseEntity.ok(examCorrectionService.correctExam(file, userAnswersRequest));
    }

}