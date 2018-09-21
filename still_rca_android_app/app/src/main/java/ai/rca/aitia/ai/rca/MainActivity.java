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
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai.rca.aitia.ai.rca.models.FaultPrediction;
import ai.rca.aitia.ai.rca.services.FaultPredictionService;

public class MainActivity extends AppCompatActivity {

    private static final String PREDICTIONS = "predictions";

    private MainActivity activity = this;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText truckSerialEditText = (EditText) findViewById(R.id.editText);

        truckSerialEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        final Button setButton = (Button) findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!truckSerialEditText.getText().toString().trim().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    final FaultPredictionService faultPredictionService = new FaultPredictionService(activity);
                    faultPredictionService.execute(truckSerialEditText.getText().toString());
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onFaultPredictionsReceived(final FaultPrediction[] predictions) throws JsonProcessingException {
        progressBar.setVisibility(View.INVISIBLE);
        if (predictions.length != 0) {
            Toast.makeText(this, predictions.length + " "+ getString(R.string.prediction_received), Toast.LENGTH_LONG).show();


            Intent i = new Intent(this, FaultPredictionActivity.class);
            ObjectMapper mapper = new ObjectMapper();
            i.putExtra(PREDICTIONS, mapper.writeValueAsString(predictions));
            startActivity(i);
        } else {
            Toast.makeText(this, getString(R.string.noprediction), Toast.LENGTH_LONG).show();
        }
    }
}
