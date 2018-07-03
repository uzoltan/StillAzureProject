package eu.mantis.model;

import java.util.HashSet;
import java.util.Set;

public class FailureMode {

  private Long id;
  private String failureName;
  private Set<QuestionBank> questions = new HashSet<>();

  public FailureMode() {
  }

  public FailureMode(String failureName, Set<QuestionBank> questions) {
    this.failureName = failureName;
    this.questions = questions;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFailureName() {
    return failureName;
  }

  public void setFailureName(String failureName) {
    this.failureName = failureName;
  }

  public Set<QuestionBank> getQuestions() {
    return questions;
  }

  public void setQuestions(Set<QuestionBank> questions) {
    this.questions = questions;
  }

}
