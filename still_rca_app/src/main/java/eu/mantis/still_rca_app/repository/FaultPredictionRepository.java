package eu.mantis.still_rca_app.repository;

import eu.mantis.still_rca_app.model.FaultPrediction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaultPredictionRepository extends JpaRepository<FaultPrediction, Long> {

  List<FaultPrediction> findByTruckSerial(String truckSerial);

  List<FaultPrediction> findByTruckSerialAndVerified(String truckSerial, Boolean verified);

  List<FaultPrediction> findByTruckSerialAndTechAssessmentIsNull(String truckSerial);

  List<FaultPrediction> findByTruckSerialAndTechAssessmentIsNotNull(String truckSerial);

  List<FaultPrediction> findByTruckSerialAndVerifiedAndTechAssessmentIsNull(String truckSerial, Boolean verified);

  List<FaultPrediction> findByTruckSerialAndVerifiedAndTechAssessmentIsNotNull(String truckSerial, Boolean verified);
}
