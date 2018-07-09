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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "question_bank")
@JsonIgnoreProperties(value = {"priority"}, allowSetters = true)
public class QuestionBank implements Comparable<QuestionBank> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(max = 512)
  private String question;
  @NotNull
  @Size(max = 512)
  private String answer1;
  @NotNull
  @Size(max = 512)
  private String answer2;
  @Size(max = 512)
  private String answer3;
  @Size(max = 512)
  private String answer4;
  @Column(nullable = false)
  private int priority;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "questions")
  @JsonIgnoreProperties("questions")
  private List<FailureMode> failureModes = new ArrayList<>();

  public QuestionBank() {
  }

  public QuestionBank(@NotNull @Size(max = 512) String question, @NotNull @Size(max = 512) String answer1, @NotNull @Size(max = 512) String answer2,
                      @Size(max = 512) String answer3, @Size(max = 512) String answer4, int priority, List<FailureMode> failureModes) {
    this.question = question;
    this.answer1 = answer1;
    this.answer2 = answer2;
    this.answer3 = answer3;
    this.answer4 = answer4;
    this.priority = priority;
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

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public List<FailureMode> getFailureModes() {
    return failureModes;
  }

  public void setFailureModes(List<FailureMode> failureModes) {
    this.failureModes = failureModes;
  }


  @Override
  public int compareTo(QuestionBank o) {
    if(this.priority == 0){
      if(o.priority == 0){
        return 0;
      } else {
        return 1;
      }
    }
    return this.priority - o.priority;
  }

}
