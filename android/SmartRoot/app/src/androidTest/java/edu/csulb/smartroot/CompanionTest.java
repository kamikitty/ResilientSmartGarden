package edu.csulb.smartroot;

import android.os.AsyncTask;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * An Instrumented Test that will test GET and POST requests to a server.
 */
@RunWith(AndroidJUnit4.class)
public class CompanionTest {
    static final int MAX_RESPONSE_TIME = 5000; // Enter maximum wait time for server response

    /////////////////////////////
    // INSTRUMENTED TEST UNITS //
    /////////////////////////////

    /**
     * Conducts a POST request instrumented test.
     *
     * @throws Exception
     */
    @Test
    public void getAllPlants() throws Exception {
        GetAllPlants getAllPlants = new GetAllPlants();
        getAllPlants.execute("API to Companion Planting Database");
        getAllPlants.get(MAX_RESPONSE_TIME, TimeUnit.MILLISECONDS);
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    private class GetAllPlants extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String...args) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection http = null;

            try {
                URL url = new URL(args[0]);

                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(5000);
                http.setReadTimeout(5000);
                http.setRequestMethod("GET");

                int responseCode = http.getResponseCode();
                Log.d("TESTING - ALL PLANTS", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(http.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while ((buffer = reader.readLine()) != null) {
                        result.append(buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                http.disconnect();
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                Log.d("TESTING - ALL PLANTS", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("plants");
                    Log.d("TESTING - ALL PLANTS ARRAY", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("TESTING - PLANT C/E", "Name: " + jsonArray.getJSONObject(i).get("_id"));
                        Log.d("TESTING - PLANT C/E", "Companion: " + jsonArray.getJSONObject(i).get("companion"));
                        Log.d("TESTING - PLANT C/E", "Enemy: " + jsonArray.getJSONObject(i).get("enemy"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("TESTING - ALL PLANTS", "JSON Object is null");
            }
        }
    }

}