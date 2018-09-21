package ai.rca.aitia.ai.rca;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import ai.rca.aitia.ai.rca.models.TechnicalAssessment;
import ai.rca.aitia.ai.rca.services.TechnicalAssessmentService;

public class TechnicalAssessmentActivity extends AppCompatActivity {

    private static final String FAULTPREDICTIONID = "faultPredictionId";

    private long faultPredictionId;
    private EditText rootCauseEditText;
    private EditText commentsEditText;
    private Button sendButton;
    private ProgressBar progressBar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_assessment);

        Bundle b = getIntent().getExtras();
        faultPredictionId = b.getLong(FAULTPREDICTIONID);

        //rootCauseEditText = (EditText) findViewById(R.id.rootCauseEditText);
        commentsEditText = (EditText) findViewById(R.id.commentsEditText);

        commentsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        spinner = (Spinner) findViewById(R.id.spinner);
        sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentsEditText.getText().toString().trim().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    final TechnicalAssessmentService service = new TechnicalAssessmentService(TechnicalAssessmentActivity.this, faultPredictionId);

                    final TechnicalAssessment assessment = new TechnicalAssessment();
                    assessment.setRootCause(spinner.getSelectedItem().toString());
                    assessment.setComments(commentsEditText.getText().toString());

                    service.execute(assessment);
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }
    
    public void onTechnicalAssessmentSent() {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getText(R.string.success), Toast.LENGTH_LONG).show();

        final Intent intent = new Intent(this, MainActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);

    }
}
