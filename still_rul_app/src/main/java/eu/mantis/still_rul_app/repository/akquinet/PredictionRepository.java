package eu.mantis.still_rul_app.repository.akquinet;

import eu.mantis.still_rul_app.model.akquinet.Prediction;
import eu.mantis.still_rul_app.model.akquinet.PredictionKey;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, PredictionKey> {

  List<Prediction> findById_DatePredictionAfter(LocalDateTime past);

  List<Prediction> findById_MachineIdAndId_DatePredictionAfter(String machineId, LocalDateTime past);

  List<Prediction> findById_MachineIdNotIn(Collection<String> machineId);
}
