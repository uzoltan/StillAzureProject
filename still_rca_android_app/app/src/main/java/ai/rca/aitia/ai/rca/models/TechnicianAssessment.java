package ai.rca.aitia.ai.rca.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class TechnicianAssessment {

    private Long id;

    @JsonIgnoreProperties("techAssessment")
    private FaultPrediction faultPrediction;

    private String rootCause;

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
