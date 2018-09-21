package ai.rca.aitia.ai.rca.services;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ai.rca.aitia.ai.rca.FaultPredictionActivity;
import ai.rca.aitia.ai.rca.models.AnsweredQuestion;
import ai.rca.aitia.ai.rca.models.FaultPrediction;

public class VerificationService extends AsyncTask<List<AnsweredQuestion>, Void, FaultPrediction> {

    private static final String TAG = "VerificationService";
    private static final String SEND_VERIFICATION_PREFIX = "https://mantis-rca-webapp.azurewebsites.net/predictions/";
    private static final String SEND_VERIFICATION_SUFFIX = "/verification";
    private static final String SUCCESS = "Success";
    private static final String ERROR = "HTTP Error";

    private FaultPredictionActivity activity;
    private long faultPredictionId;

    public VerificationService(final FaultPredictionActivity activity, final long faultPredictionId) {
        this.activity = activity;
        this.faultPredictionId = faultPredictionId;
    }

    @Override
    protected void onPostExecute(final FaultPrediction prediction)
    {
        //super.onPostExecute();

        if (prediction != null) {
            activity.onPredictionVerified(prediction);
        }

    }

    @Override
    protected FaultPrediction doInBackground(List<AnsweredQuestion>... lists) {
        final String requestString = SEND_VERIFICATION_PREFIX + faultPredictionId + SEND_VERIFICATION_SUFFIX;
        final List<AnsweredQuestion> list = lists[0];
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(requestString).openConnection();
            conn.setReadTimeout(25000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Basic bWFudGlzOnBTTkVONE5xWFlDV005OWRjaUtv");

            ObjectMapper mapper = new ObjectMapper();
            final String debugString = mapper.writeValueAsString(list);
            Log.i(TAG, debugString);
            mapper.writeValue(conn.getOutputStream(), list);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, SUCCESS);
                final JsonParser jp = new JsonFactory().createParser(conn.getInputStream());
                final FaultPrediction obj = mapper
                        .readValue(jp, FaultPrediction.class);
                return obj;
            } else {
                Log.e(TAG, ERROR + " (" + conn.getResponseCode() + "): " + conn
                        .getResponseMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
