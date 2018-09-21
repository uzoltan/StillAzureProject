package ai.rca.aitia.ai.rca.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FailureMode implements Serializable {


    private Long id;

    private String failureName;

    @JsonIgnoreProperties("failureModes")
    private List<QuestionBank> questions = new ArrayList<>();

    public FailureMode() {
    }

    public FailureMode(String failureName, List<QuestionBank> questions) {
        this.failureName = failureName;
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFailureName() {
        return failureName;
    }

    public void setFailureName(String failureName) {
        this.failureName = failureName;
    }

    public List<QuestionBank> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionBank> questions) {
        this.questions = questions;
    }

}
