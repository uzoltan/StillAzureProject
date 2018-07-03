package eu.mantis.still_rca_app.repository;

import eu.mantis.still_rca_app.model.FailureMode;
import eu.mantis.still_rca_app.model.QuestionBank;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {

  List<QuestionBank> findByFailureModes(Set<FailureMode> failureModes);
}
