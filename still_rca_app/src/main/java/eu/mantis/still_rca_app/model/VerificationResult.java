package eu.mantis.still_rca_app.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VerificationResult {

  @NotNull
  @Size(max = 512)
  private String question;

  @NotNull
  @Size(max = 512)
  private String answer;

  public VerificationResult() {
  }

  public VerificationResult(@NotNull @Size(max = 512) String question, @NotNull @Size(max = 512) String answer) {
    this.question = question;
    this.answer = answer;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public static List<VerificationResult> convertStepsToResult(List<VerificationStep> steps) {
    List<VerificationResult> results = new ArrayList<>();
    for (VerificationStep step : steps) {
      results.add(new VerificationResult(step.getQuestion().getQuestion(), step.getAnswer()));
    }
    return results;
  }

}
