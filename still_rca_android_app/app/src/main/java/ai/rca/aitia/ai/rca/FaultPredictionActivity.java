package ai.rca.aitia.ai.rca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ai.rca.aitia.ai.rca.models.AnsweredQuestion;
import ai.rca.aitia.ai.rca.models.FaultPrediction;
import ai.rca.aitia.ai.rca.models.QuestionBank;
import ai.rca.aitia.ai.rca.services.VerificationService;

public class FaultPredictionActivity extends AppCompatActivity {

    private static final String PREDICTIONS = "predictions";
    private static final String FAULTPREDICTIONID = "faultPredictionId";

    private FaultPrediction[] predictions;
    private TextView questionTextView;
    private TextView questionNumberTextView;
    private TextView pleaseTextView;
    private TextView hintTextView;
    private RelativeLayout questionLayout;
    private ProgressBar progressBar;
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;
    private Button sendTechnicalAssessmentButton;
    private int predictionIdx = 0;
    private int questionIdx = 0;
    private long currentPredictionId;
    private List<AnsweredQuestion> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_prediction);

        Bundle b = getIntent().getExtras();
        try {
            createPredictions(b.getString(PREDICTIONS));
        } catch (IOException e) {
            e.printStackTrace();
        }

        hintTextView = (TextView) findViewById(R.id.hintTextView);
        pleaseTextView = (TextView) findViewById(R.id.pleaseTextView);

        questionLayout = (RelativeLayout) findViewById(R.id.questionLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);

        sendTechnicalAssessmentButton = (Button) findViewById(R.id.sendTechnicalAssessment);
        sendTechnicalAssessmentButton.setVisibility(View.INVISIBLE);
        sendTechnicalAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FaultPredictionActivity.this, TechnicalAssessmentActivity.class);
                i.putExtra(FAULTPREDICTIONID, currentPredictionId);
                startActivity(i);
            }
        });

        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);

        answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestion(view);
            }
        });

        answerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestion(view);
            }
        });

        answerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestion(view);
            }
        });

        answerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestion(view);
            }
        });


        showQuestions();
    }

    public void onPredictionVerified(final FaultPrediction prediction) {
        final String failureText = getString(R.string.prediction_update) + "\n" + prediction.getFailureMode().getFailureName();
        hintTextView.setText(failureText);
        predictionIdx++;

        if (predictionIdx == predictions.length) {
            outOfPredicitons();
            return;
        }

        questionIdx = 0;
        answers.clear();
        showQuestions();
    }

    private void createPredictions(final String predicitons) throws IOException {
        final JsonParser jp = new JsonFactory().createParser(predicitons);
        final ObjectMapper mapper = new ObjectMapper();
        predictions = mapper
                .readValue(jp, FaultPrediction[].class);
    }

    private void showQuestions() {
        progressBar.setVisibility(View.INVISIBLE);
        questionLayout.setVisibility(View.VISIBLE);
        answerA.setVisibility(View.VISIBLE);
        answerB.setVisibility(View.VISIBLE);
        answerC.setVisibility(View.VISIBLE);
        answerD.setVisibility(View.VISIBLE);
        final FaultPrediction prediction = predictions[predictionIdx];
        currentPredictionId =  prediction.getId();
        final String failureText = getString(R.string.predected_failure) + "\n" + prediction.getFailureMode().getFailureName();
        hintTextView.setText(failureText);

        final List<QuestionBank> questions = prediction.getFailureMode().getQuestions();
        setQuestion(questions.get(questionIdx));

        questionNumberTextView.setText(questionIdx + 1 + "/" + questions.size());
    }

    private void setQuestion(final QuestionBank q) {
        questionTextView.setText(q.getQuestion());
        if (q.getAnswer1() != null) {
            answerA.setText(q.getAnswer1().toString());
        } else {
            answerA.setVisibility(View.GONE);
        }
        if (q.getAnswer2() != null) {
            answerB.setText(q.getAnswer2().toString());
        }
        else {
            answerB.setVisibility(View.GONE);
        }
        if (q.getAnswer3() != null) {
            answerC.setText(q.getAnswer3().toString());
        }
        else {
            answerC.setVisibility(View.GONE);
        }
        if (q.getAnswer4() != null) {
            answerD.setText(q.getAnswer4().toString());
        }else {
            answerD.setVisibility(View.GONE);
        }
    }

    private void answerQuestion(final View v) {
        final Button b = (Button)v;
        final String buttonText = b.getText().toString();

        AnsweredQuestion aq = new AnsweredQuestion();
        aq.setQuestion(questionTextView.getText().toString());
        aq.setAnswer(buttonText);

        answers.add(aq);
        questionIdx++;

        if (questionIdx == predictions[predictionIdx].getFailureMode().getQuestions().size()) {
            sendVerificationAnswers();
        } else {
            showQuestions();
        }

    }

    private void sendVerificationAnswers() {
        questionLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final VerificationService verificationService = new VerificationService(this, currentPredictionId);
        verificationService.execute(answers);
    }

    private void outOfPredicitons() {
        questionLayout.setVisibility(View.VISIBLE);
        questionTextView.setText(getString(R.string.thanks));
        pleaseTextView.setVisibility(View.INVISIBLE);
        answerA.setVisibility(View.GONE);
        answerB.setVisibility(View.GONE);
        answerC.setVisibility(View.GONE);
        answerD.setVisibility(View.GONE);
        questionNumberTextView.setText("");
        progressBar.setVisibility(View.INVISIBLE);
        sendTechnicalAssessmentButton.setVisibility(View.VISIBLE);
    }

}
