package ai.rca.aitia.ai.rca.services;

import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;

import ai.rca.aitia.ai.rca.TechnicalAssessmentActivity;
import ai.rca.aitia.ai.rca.models.TechnicalAssessment;

public class TechnicalAssessmentService extends AsyncTask<TechnicalAssessment, Void, Void> {

    private static final String TAG = "AssessmentService";
    private static final String SEND_VERIFICATION_PREFIX = "https://mantis-rca-webapp.azurewebsites.net/predictions/";
    private static final String SEND_VERIFICATION_SUFFIX = "/assessment";
    private static final String SUCCESS = "Success";
    private static final String ERROR = "HTTP Error";

    private TechnicalAssessmentActivity activity;
    private long faultPredictionId;

    public TechnicalAssessmentService(final TechnicalAssessmentActivity activity, final long faultPredictionId) {
        this.activity = activity;
        this.faultPredictionId = faultPredictionId;
    }

    @Override
    protected Void doInBackground(TechnicalAssessment... technicalAssessments) {
        final String requestString = SEND_VERIFICATION_PREFIX + faultPredictionId + SEND_VERIFICATION_SUFFIX;
        final TechnicalAssessment assessment = technicalAssessments[0];
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(requestString).openConnection();
            conn.setReadTimeout(25000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Basic bWFudGlzOnBTTkVONE5xWFlDV005OWRjaUtv");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(conn.getOutputStream(), assessment);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, SUCCESS);
            } else {
                Log.e(TAG, ERROR + " (" + conn.getResponseCode() + "): " + conn
                        .getResponseMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void nothing)
    {
        super.onPostExecute(nothing);
        activity.onTechnicalAssessmentSent();
    }
}
