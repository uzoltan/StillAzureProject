package ai.rca.aitia.ai.rca.models;

import java.util.List;

public class VerificationStep {

    private Long id;
    private FaultPrediction faultPrediction;
    private QuestionBank question;
    private String answer;

    public VerificationStep() {
    }

    public VerificationStep(FaultPrediction faultPrediction, QuestionBank question, String answer) {
        this.faultPrediction = faultPrediction;
        this.question = question;
        this.answer = answer;
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

    public QuestionBank getQuestion() {
        return question;
    }

    public void setQuestion(QuestionBank question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static boolean allQuestionsAnswered(List<VerificationStep> steps) {
        String answer;
        for (VerificationStep step : steps) {
            if (step.getAnswer() == null) {
                return false;
            }
            answer = step.getAnswer().trim();
            if (answer.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
