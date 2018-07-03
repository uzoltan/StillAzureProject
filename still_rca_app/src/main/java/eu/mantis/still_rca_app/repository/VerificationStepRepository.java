package eu.mantis.still_rca_app.repository;

import eu.mantis.still_rca_app.model.FaultPrediction;
import eu.mantis.still_rca_app.model.VerificationStep;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationStepRepository extends JpaRepository<VerificationStep, Long> {

  List<VerificationStep> findByFaultPrediction(FaultPrediction prediction);

  List<VerificationStep> findByFaultPredictionAndAnswerIsNull(FaultPrediction prediction);
}
