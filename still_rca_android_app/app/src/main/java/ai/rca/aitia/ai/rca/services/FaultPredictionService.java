package ai.rca.aitia.ai.rca.services;

import android.os.AsyncTask;
import android.util.Log;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ai.rca.aitia.ai.rca.MainActivity;
import ai.rca.aitia.ai.rca.models.FaultPrediction;


public class FaultPredictionService extends AsyncTask<String, Void, FaultPrediction[]> {

    private static final String TAG = "FaultPredictionService";
    private static final String GET_FAULT_PREDICTION_PREFIX = "https://mantis-rca-webapp.azurewebsites.net/predictions/";
    private static final String GET_FAULT_PREDICTION_SUFFIX = "?status=new";
    private static final String SUCCESS = "Success";
    private static final String ERROR = "HTTP Error";

    private MainActivity activity;

    public FaultPredictionService(final MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected FaultPrediction[] doInBackground(String... strings) {
        final String requestString = GET_FAULT_PREDICTION_PREFIX + strings[0] + GET_FAULT_PREDICTION_SUFFIX;
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(requestString).openConnection();
            conn.setReadTimeout(25000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Authorization","Basic bWFudGlzOnBTTkVONE5xWFlDV005OWRjaUtv");

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final JsonParser jp = new JsonFactory().createParser(conn.getInputStream());
                final ObjectMapper mapper = new ObjectMapper();
                final FaultPrediction[] obj = mapper
                        .readValue(jp, FaultPrediction[].class);
                Log.i(TAG, SUCCESS);
                return obj;
            } else {
                Log.e(TAG, ERROR + " (" + conn.getResponseCode() + "): " + conn
                        .getResponseMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return new FaultPrediction[]{};
    }

    @Override
    protected void onPostExecute(FaultPrediction[] faultPredictions) {
        try {
            this.activity.onFaultPredictionsReceived(faultPredictions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
