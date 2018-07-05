package eu.mantis.still_rca_app.repository;

import eu.mantis.still_rca_app.model.FaultPrediction;
import eu.mantis.still_rca_app.model.PredictionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaultPredictionRepository extends JpaRepository<FaultPrediction, Long> {

  List<FaultPrediction> findByTruckSerial(String truckSerial);

  List<FaultPrediction> findByTruckSerialAndPredictionStatus(String truckSerial, PredictionStatus status);
}
