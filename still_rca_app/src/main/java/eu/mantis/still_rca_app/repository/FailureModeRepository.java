package eu.mantis.still_rca_app.repository;

import eu.mantis.still_rca_app.model.FailureMode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailureModeRepository extends JpaRepository<FailureMode, Long> {

  Optional<FailureMode> findByFailureNameIgnoreCase(String name);
}
