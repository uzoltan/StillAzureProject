package ai.rca.aitia.ai.rca.models;

public class TechnicalAssessment {

    private String rootCause;
    private String comments;

    public TechnicalAssessment() {}

    public String getRootCause() {
        return rootCause;
    }

    public String getComments() {
        return comments;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
