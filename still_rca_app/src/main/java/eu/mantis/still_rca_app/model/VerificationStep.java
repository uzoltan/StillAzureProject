package eu.mantis.still_rca_app.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "verification_steps")
public class VerificationStep {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "prediction_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private FaultPrediction faultPrediction;

  @JoinColumn(name = "question_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.NO_ACTION)
  private QuestionBank question;

  @Size(max = 512)
  private String answer;

  public VerificationStep() {
  }

  public VerificationStep(FaultPrediction faultPrediction, QuestionBank question, @Size(max = 512) String answer) {
    this.faultPrediction = faultPrediction;
    this.question = question;
    this.answer = answer;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public FaultPrediction getFaultPrediction() {
    return faultPrediction;
  }

  public void setFaultPrediction(FaultPrediction faultPrediction) {
    this.faultPrediction = faultPrediction;
  }

  public QuestionBank getQuestion() {
    return question;
  }

  public void setQuestion(QuestionBank question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public static boolean allQuestionsAnswered(List<VerificationStep> steps) {
    String answer;
    for (VerificationStep step : steps) {
      if (step.getAnswer() == null) {
        return false;
      }
      answer = step.getAnswer().trim();
      if (answer.isEmpty()) {
        return false;
      }
    }
    return true;
  }

}
