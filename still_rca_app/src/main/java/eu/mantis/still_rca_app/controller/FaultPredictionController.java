package eu.mantis.still_rca_app.controller;

import eu.mantis.still_rca_app.exception.BadRequetException;
import eu.mantis.still_rca_app.exception.ResourceNotFoundException;
import eu.mantis.still_rca_app.model.FailureMode;
import eu.mantis.still_rca_app.model.FaultPrediction;
import eu.mantis.still_rca_app.model.QuestionBank;
import eu.mantis.still_rca_app.model.TechnicianAssessment;
import eu.mantis.still_rca_app.model.VerificationResult;
import eu.mantis.still_rca_app.model.VerificationStep;
import eu.mantis.still_rca_app.repository.FailureModeRepository;
import eu.mantis.still_rca_app.repository.FaultPredictionRepository;
import eu.mantis.still_rca_app.repository.QuestionBankRepository;
import eu.mantis.still_rca_app.repository.TechnicianAssessmentRepository;
import eu.mantis.still_rca_app.repository.VerificationStepRepository;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaultPredictionController {

  private final FaultPredictionRepository predictionRepo;
  private final FailureModeRepository failureRepo;
  private final QuestionBankRepository questionRepo;
  private final TechnicianAssessmentRepository assessmentRepository;
  private final VerificationStepRepository verificationRepo;

  private final Logger logger = LoggerFactory.getLogger(FaultPredictionController.class);

  @Autowired
  public FaultPredictionController(FaultPredictionRepository predictionRepo, FailureModeRepository failureRepo, QuestionBankRepository questionRepo,
                                   TechnicianAssessmentRepository assessmentRepository, VerificationStepRepository verificationRepo) {
    this.predictionRepo = predictionRepo;
    this.failureRepo = failureRepo;
    this.questionRepo = questionRepo;
    this.assessmentRepository = assessmentRepository;
    this.verificationRepo = verificationRepo;
  }

  @GetMapping
  public String welcome() {
    return "This is the RCA web API.";
  }

  @GetMapping("predictions")
  public Page<FaultPrediction> getAllPredictions(Pageable pageable) {
    return predictionRepo.findAll(pageable);
  }

  @GetMapping("predictions/{truckSerial}")
  public List<FaultPrediction> getPredictionsByTruckSerial(@PathVariable String truckSerial,
                                                           @RequestParam(value = "verified", required = false) Boolean verified,
                                                           @RequestParam(value = "evaluated", required = false) Boolean evaluated) {
    if (verified == null) {
      if (evaluated != null) {
        if (evaluated) {
          return predictionRepo.findByTruckSerialAndTechAssessmentIsNotNull(truckSerial);
        } else {
          return predictionRepo.findByTruckSerialAndTechAssessmentIsNull(truckSerial);
        }
      } else {
        return predictionRepo.findByTruckSerial(truckSerial);
      }
    } else {
      if (evaluated != null) {
        if (evaluated) {
          return predictionRepo.findByTruckSerialAndVerifiedAndTechAssessmentIsNotNull(truckSerial, verified);
        } else {
          return predictionRepo.findByTruckSerialAndVerifiedAndTechAssessmentIsNull(truckSerial, verified);
        }
      } else {
        return predictionRepo.findByTruckSerialAndVerified(truckSerial, verified);
      }
    }
  }

  @PostMapping("predictions")
  @ResponseStatus(HttpStatus.CREATED)
  public FaultPrediction createFaultPrediction(@Valid @RequestBody FaultPrediction prediction) {
    logger.info("createFaultPrediction got the request!");
    String failureName = prediction.getFailureMode().getFailureName();
    FailureMode failure = failureRepo.findByFailureNameIgnoreCase(failureName)
                                     .orElseThrow(() -> new ResourceNotFoundException("Unknown failure mode: " + failureName));

    //Save the FaultPrediction
    prediction.setFailureMode(failure);
    prediction = predictionRepo.saveAndFlush(prediction);
    //Get the questions belonging to the failure mode
    List<QuestionBank> questions = questionRepo.findByFailureModes(Collections.singleton(failure));
    //Create the verification steps and save them
    for (QuestionBank question : questions) {
      VerificationStep step = new VerificationStep(prediction, question, null);
      verificationRepo.save(step);
    }

    verificationRepo.flush();
    prediction.setFailureMode(null);
    logger.info("createFaultPrediction processed the request!");
    return prediction;
  }

  @PutMapping("predictions/{id}/verification")
  public FaultPrediction saveVerificationAnswers(@PathVariable Long id, @Valid @RequestBody List<VerificationResult> verificationResults) {
    FaultPrediction prediction = predictionRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Fault prediction resource with id " + id + " not found"));
    if (prediction.getVerified()) {
      throw new BadRequetException("This fault prediction is already verified! (All questions have been answered)");
    }

    List<VerificationStep> steps = verificationRepo.findByFaultPredictionAndAnswerIsNull(prediction);
    for (VerificationStep step : steps) {
      for (VerificationResult result : verificationResults) {
        if (result.getQuestion().equals(step.getQuestion().getQuestion())) {
          step.setAnswer(result.getAnswer());
          verificationRepo.save(step);
          break;
        }
      }
    }

    //Here is the place for some logic to evaluate the answers, and maybe update the fault prediction properties based on them

    if (VerificationStep.allQuestionsAnswered(steps)) {
      prediction.setVerified(true);
      prediction = updateFailureModeFromAnswers(prediction, steps);
      return predictionRepo.save(prediction);
    } else {
      prediction.setFailureMode(null);
      return prediction;
    }
  }

  @GetMapping("predictions/{id}/verification")
  public List<VerificationResult> getVerificationResultsByPrediction(@PathVariable Long id) {
    FaultPrediction prediction = predictionRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Fault prediction resource with id " + id + " not found"));

    List<VerificationStep> steps = verificationRepo.findByFaultPrediction(prediction);
    return VerificationResult.convertStepsToResult(steps);
  }

  @PutMapping("predictions/{id}/assessment")
  public TechnicianAssessment saveTechnicalAssessment(@PathVariable Long id, @Valid @RequestBody TechnicianAssessment assessment) {
    FaultPrediction prediction = predictionRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Fault prediction resource with id " + id + " not found"));
    assessment.setFaultPrediction(prediction);
    return assessmentRepository.save(assessment);
  }

  //Some hardcoded stuff for now
  private FaultPrediction updateFailureModeFromAnswers(FaultPrediction prediction, List<VerificationStep> steps) {
    boolean shouldUpdate = true;
    if (prediction.getFailureMode().getFailureName().equalsIgnoreCase("CAN bus failure")) {
      for (VerificationStep step : steps) {
        String answer = step.getAnswer();
        switch (step.getQuestion().getQuestion()) {
          case "Are there proper communications on the CAN bus?":
            if (!answer.equalsIgnoreCase("Yes")) {
              shouldUpdate = false;
            }
            break;
          case "Is the MCU operational?":
            if (!answer.equalsIgnoreCase("MCU tests successful")) {
              shouldUpdate = false;
            }
            break;
          case "Does flashing the MCU solve the problem?":
            if (!answer.equalsIgnoreCase("No")) {
              shouldUpdate = false;
            }
            break;
          case "Are all GND connections solid?":
            if (!answer.equalsIgnoreCase("Yes")) {
              shouldUpdate = false;
            }
            break;
          default:
            break;
        }
      }

      if (shouldUpdate) {
        FailureMode failureMode = failureRepo.findByFailureNameIgnoreCase("Battery failure")
                                             .orElseGet(() -> failureRepo.save(new FailureMode("Battery failure", null)));
        prediction.setFailureMode(failureMode);
      }
    }
    return prediction;
  }

}
