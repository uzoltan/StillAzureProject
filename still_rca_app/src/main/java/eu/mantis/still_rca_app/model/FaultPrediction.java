package eu.mantis.still_rca_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "fault_predictions")
@JsonIgnoreProperties(value = {"verified", "techAssessment"}, allowGetters = true)
public class FaultPrediction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String truckSerial;
  private LocalDateTime predictionTime;
  private LocalDateTime predictedTime;

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "failure_mode_id", nullable = false)
  private FailureMode failureMode;


  private Integer failureSeverity;
  @Min(0)
  @Max(1)
  private Double failureProbability;

  @Type(type = "yes_no")
  private Boolean verified = false;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "faultPrediction")
  @JsonIgnoreProperties("faultPrediction")
  private TechnicianAssessment techAssessment;

  public FaultPrediction() {
  }

  public FaultPrediction(String truckSerial, LocalDateTime predictionTime, LocalDateTime predictedTime, FailureMode failureMode,
                         Integer failureSeverity, Double failureProbability, Boolean verified, TechnicianAssessment techAssessment) {
    this.truckSerial = truckSerial;
    this.predictionTime = predictionTime;
    this.predictedTime = predictedTime;
    this.failureMode = failureMode;
    this.failureSeverity = failureSeverity;
    this.failureProbability = failureProbability;
    this.verified = verified;
    this.techAssessment = techAssessment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTruckSerial() {
    return truckSerial;
  }

  public void setTruckSerial(String truckSerial) {
    this.truckSerial = truckSerial;
  }

  public LocalDateTime getPredictionTime() {
    return predictionTime;
  }

  public void setPredictionTime(LocalDateTime predictionTime) {
    this.predictionTime = predictionTime;
  }

  public LocalDateTime getPredictedTime() {
    return predictedTime;
  }

  public void setPredictedTime(LocalDateTime predictedTime) {
    this.predictedTime = predictedTime;
  }

  public FailureMode getFailureMode() {
    return failureMode;
  }

  public void setFailureMode(FailureMode failureMode) {
    this.failureMode = failureMode;
  }

  public Integer getFailureSeverity() {
    return failureSeverity;
  }

  public void setFailureSeverity(Integer failureSeverity) {
    this.failureSeverity = failureSeverity;
  }

  public Double getFailureProbability() {
    return failureProbability;
  }

  public void setFailureProbability(Double failureProbability) {
    this.failureProbability = failureProbability;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public TechnicianAssessment getTechAssessment() {
    return techAssessment;
  }

  public void setTechAssessment(TechnicianAssessment techAssessment) {
    this.techAssessment = techAssessment;
  }

}
