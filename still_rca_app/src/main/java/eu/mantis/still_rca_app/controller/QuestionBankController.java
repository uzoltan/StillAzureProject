package eu.mantis.still_rca_app.controller;

import eu.mantis.still_rca_app.exception.ResourceNotFoundException;
import eu.mantis.still_rca_app.model.QuestionBank;
import eu.mantis.still_rca_app.repository.QuestionBankRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionBankController {

  private final QuestionBankRepository questionRepo;

  @Autowired
  public QuestionBankController(QuestionBankRepository questionRepo) {
    this.questionRepo = questionRepo;
  }

  @GetMapping("questions")
  public Page<QuestionBank> getAllQuestions(Pageable pageable) {
    return questionRepo.findAll(pageable);
  }

  @PostMapping("questions")
  public QuestionBank createQuestion(@Valid @RequestBody QuestionBank question) {
    return questionRepo.save(question);
  }

  @PostMapping("questions/bulk")
  public List<QuestionBank> createQuestions(@Valid @RequestBody List<QuestionBank> questions) {
    return questionRepo.saveAll(questions);
  }

  @DeleteMapping("questions/{id}")
  public ResponseEntity<?> deleteQuestion(@PathVariable(value = "id") Long questionId) {
    return questionRepo.findById(questionId).map(question -> {
      questionRepo.delete(question);
      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("FailureMode resource with id " + questionId + " not found"));
  }

}
