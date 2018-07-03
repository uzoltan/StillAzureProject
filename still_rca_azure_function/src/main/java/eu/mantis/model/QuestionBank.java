package eu.mantis.model;

import java.util.HashSet;
import java.util.Set;

public class QuestionBank {

  private Long id;
  private String question;
  private String answer1;
  private String answer2;
  private String answer3;
  private String answer4;
  private Set<FailureMode> failureModes = new HashSet<>();

  public QuestionBank() {
  }

  public QuestionBank(Long id, String question, String answer1, String answer2, String answer3, String answer4, Set<FailureMode> failureModes) {
    this.id = id;
    this.question = question;
    this.answer1 = answer1;
    this.answer2 = answer2;
    this.answer3 = answer3;
    this.answer4 = answer4;
    this.failureModes = failureModes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer1() {
    return answer1;
  }

  public void setAnswer1(String answer1) {
    this.answer1 = answer1;
  }

  public String getAnswer2() {
    return answer2;
  }

  public void setAnswer2(String answer2) {
    this.answer2 = answer2;
  }

  public String getAnswer3() {
    return answer3;
  }

  public void setAnswer3(String answer3) {
    this.answer3 = answer3;
  }

  public String getAnswer4() {
    return answer4;
  }

  public void setAnswer4(String answer4) {
    this.answer4 = answer4;
  }

  public Set<FailureMode> getFailureModes() {
    return failureModes;
  }

  public void setFailureModes(Set<FailureMode> failureModes) {
    this.failureModes = failureModes;
  }

}
