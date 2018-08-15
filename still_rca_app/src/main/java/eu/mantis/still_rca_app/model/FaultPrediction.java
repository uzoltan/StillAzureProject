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

@Entity
@Table(name = "fault_predictions")
@JsonIgnoreProperties(value = {"status", "techAssessment"}, allowGetters = true)
public class FaultPrediction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String truckSerial;
  private LocalDateTime predictionTime;
  private LocalDateTime predictedTime;
  //@Enumerated(EnumType.STRING)
  private PredictionStatus predictionStatus;

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "failure_mode_id", nullable = false)
  private FailureMode failureMode;

  private Integer failureSeverity;
  @Min(0)
  @Max(1)
  private Double failureProbability;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "faultPrediction")
  @JsonIgnoreProperties("faultPrediction")
  private TechnicianAssessment techAssessment;

  public FaultPrediction() {
  }

  public FaultPrediction(@NotNull String truckSerial, LocalDateTime predictionTime, LocalDateTime predictedTime, PredictionStatus predictionStatus,
                         @NotNull FailureMode failureMode, Integer failureSeverity, @Min(0) @Max(1) Double failureProbability,
                         TechnicianAssessment techAssessment) {
    this.truckSerial = truckSerial;
    this.predictionTime = predictionTime;
    this.predictedTime = predictedTime;
    this.predictionStatus = predictionStatus;
    this.failureMode = failureMode;
    this.failureSeverity = failureSeverity;
    this.failureProbability = failureProbability;
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

  public PredictionStatus getPredictionStatus() {
    return predictionStatus;
  }

  public void setPredictionStatus(PredictionStatus predictionStatus) {
    this.predictionStatus = predictionStatus;
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

  public TechnicianAssessment getTechAssessment() {
    return techAssessment;
  }

  public void setTechAssessment(TechnicianAssessment techAssessment) {
    this.techAssessment = techAssessment;
  }

}
