package ai.rca.aitia.ai.rca.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(value = {"verified", "techAssessment"}, allowGetters = true)
public class FaultPrediction implements Serializable {

    private Long id;
    private String truckSerial;
    private Date predictionTime;
    private Date predictedTime;

    private FailureMode failureMode;


    private Integer failureSeverity;
    private Double failureProbability;
    private PredictionStatus predictionStatus;

    @JsonIgnoreProperties("faultPrediction")
    private TechnicianAssessment techAssessment;

    public FaultPrediction() {
    }

    public FaultPrediction(Long id, String truckSerial, Date predictionTime, Date predictedTime, FailureMode failureMode, Integer failureSeverity, Double failureProbability, PredictionStatus predictionStatus, TechnicianAssessment techAssessment) {
        this.id = id;
        this.truckSerial = truckSerial;
        this.predictionTime = predictionTime;
        this.predictedTime = predictedTime;
        this.failureMode = failureMode;
        this.failureSeverity = failureSeverity;
        this.failureProbability = failureProbability;
        this.predictionStatus = predictionStatus;
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

    public Date getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(Date predictionTime) {
        this.predictionTime = predictionTime;
    }

    public Date getPredictedTime() {
        return predictedTime;
    }

    public void setPredictedTime(Date predictedTime) {
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

    public TechnicianAssessment getTechAssessment() {
        return techAssessment;
    }

    public void setTechAssessment(TechnicianAssessment techAssessment) {
        this.techAssessment = techAssessment;
    }

    public PredictionStatus getPredictionStatus() {
        return predictionStatus;
    }

    public void setPredictionStatus(PredictionStatus predictionStatus) {
        this.predictionStatus = predictionStatus;
    }
}
