package eu.mantis.still_rca_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "technical_assessments")
public class TechnicianAssessment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fault_pred_id", nullable = false)
  @JsonIgnoreProperties("techAssessment")
  private FaultPrediction faultPrediction;

  @NotNull
  private String rootCause;

  @Lob //large object
  private String comments;

  public TechnicianAssessment() {
  }

  public TechnicianAssessment(FaultPrediction faultPrediction, String rootCause, String comments) {
    this.faultPrediction = faultPrediction;
    this.rootCause = rootCause;
    this.comments = comments;
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

  public String getRootCause() {
    return rootCause;
  }

  public void setRootCause(String rootCause) {
    this.rootCause = rootCause;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

}
