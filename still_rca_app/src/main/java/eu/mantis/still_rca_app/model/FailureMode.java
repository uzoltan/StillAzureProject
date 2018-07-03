package eu.mantis.still_rca_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "failure_modes")
public class FailureMode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  @Column(unique = true)
  private String failureName;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(name = "failure_questions", joinColumns = {@JoinColumn(name = "failure_mode_id")}, inverseJoinColumns = {
      @JoinColumn(name = "question_id")})
  @JsonIgnoreProperties("failureModes")
  private List<QuestionBank> questions = new ArrayList<>();

  public FailureMode() {
  }

  public FailureMode(@NotNull String failureName, List<QuestionBank> questions) {
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

  public List<QuestionBank> getQuestions() {
    return questions;
  }

  public void setQuestions(List<QuestionBank> questions) {
    this.questions = questions;
  }

}
