package eu.mantis.still_rca_app.controller;

import eu.mantis.still_rca_app.exception.ResourceNotFoundException;
import eu.mantis.still_rca_app.model.FailureMode;
import eu.mantis.still_rca_app.repository.FailureModeRepository;
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
public class FailureModeController {

  public final FailureModeRepository failureRepo;

  @Autowired
  public FailureModeController(FailureModeRepository failureRepo) {
    this.failureRepo = failureRepo;
  }

  @GetMapping("failure_modes")
  public Page<FailureMode> getAllFailureModes(Pageable pageable) {
    return failureRepo.findAll(pageable);
  }

  @PostMapping("failure_modes")
  public FailureMode createFailureMode(@Valid @RequestBody FailureMode failureMode) {
    return failureRepo.save(failureMode);
  }

  @PostMapping("failure_modes/bulk")
  public List<FailureMode> createFailureModes(@Valid @RequestBody List<FailureMode> failureModes) {
    return failureRepo.saveAll(failureModes);
  }

  @DeleteMapping("failure_modes/{id}")
  public ResponseEntity<?> deleteFailureMode(@PathVariable(value = "id") Long failureModeId) {
    return failureRepo.findById(failureModeId).map(failureMode -> {
      failureRepo.delete(failureMode);
      return ResponseEntity.ok().build();
    }).orElseThrow(() -> new ResourceNotFoundException("FailureMode resource with id " + failureModeId + " not found"));
  }

}
