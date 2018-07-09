package eu.mantis.model;

public class FaultPrediction {

  private Long id;
  private String truckSerial;
  private String predictionTime;
  private String predictedTime;
  private FailureMode failureMode;
  private Integer failureSeverity;
  private Double failureProbability;
  private PredictionStatus predictionStatus;

  public FaultPrediction() {
  }

  public FaultPrediction(String truckSerial, String predictionTime, String predictedTime, FailureMode failureMode, Integer failureSeverity,
                         Double failureProbability) {
    this.truckSerial = truckSerial;
    this.predictionTime = predictionTime;
    this.predictedTime = predictedTime;
    this.failureMode = failureMode;
    this.failureSeverity = failureSeverity;
    this.failureProbability = failureProbability;
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

  public String getPredictionTime() {
    return predictionTime;
  }

  public void setPredictionTime(String predictionTime) {
    this.predictionTime = predictionTime;
  }

  public String getPredictedTime() {
    return predictedTime;
  }

  public void setPredictedTime(String predictedTime) {
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

  public PredictionStatus getPredictionStatus() {
    return predictionStatus;
  }

  public void setPredictionStatus(PredictionStatus predictionStatus) {
    this.predictionStatus = predictionStatus;
  }
}
