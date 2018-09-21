package ai.rca.aitia.ai.rca.models;

import java.util.ArrayList;
import java.util.List;

public class VerificationResult {

    private String question;
    private String answer;

    public VerificationResult() {
    }

    public VerificationResult(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static List<VerificationResult> convertStepsToResult(List<VerificationStep> steps){
        List<VerificationResult> results = new ArrayList<>();
        for(VerificationStep step : steps){
            results.add(new VerificationResult(step.getQuestion().getQuestion(), step.getAnswer()));
        }
        return results;
    }

}
