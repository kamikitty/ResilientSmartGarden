package edu.csulb.smartroot;

import android.os.AsyncTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * An Instrumented Test that will test GET and POST requests to a server.
 */
@RunWith(AndroidJUnit4.class)
public class ServerTest {
    static final String POST_ENDPOINT = "Enter POST endpoint";
    static final String GET_ENDPOINT = "Enter GET endpoint";

    static final String POST_KEY = "Enter JSON key for POST";
    static final String POST_VALUE = "ENTER JSON value for POST";

    static final int MAX_RESPONSE_TIME = 5000; // Enter maximum wait time for server response

    /////////////////////////////
    // INSTRUMENTED TEST UNITS //
    /////////////////////////////

    /**
     * Conducts a POST request instrumented test.
     * @throws Exception
     */
    @Test
    public void serverPOSTTest() throws Exception {
        ServerPOSTConnect serverPOSTConnect = new ServerPOSTConnect();

        // Start the POST request thread and wait for the thread to finish.
        // If it does not finish within the defined time, continue.
        serverPOSTConnect.execute(POST_ENDPOINT);
        serverPOSTConnect.get(MAX_RESPONSE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * Conducts a GET request instrumented test.
     * @throws Exception
     */
    //@Test
    public void serverGETTest() throws Exception {
        ServerGETConnect serverGETConnect = new ServerGETConnect();

        // Start the GET request thread and wait for the thread to finish.
        // If it does not finish within the defined time, continue.
        serverGETConnect.execute(GET_ENDPOINT);
        serverGETConnect.get(MAX_RESPONSE_TIME, TimeUnit.MILLISECONDS);
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will send a POST request.
     */
    private class ServerPOSTConnect extends AsyncTask<String, Void, JSONObject> {

        boolean isConnected;

        /**
         * A constructor that initializes the connection flag.
         */
        public ServerPOSTConnect(){
            isConnected = false;
        }

        /**
         * An implementation of AsyncTask. This will send a POST request to the server and
         * get the response in a separate thread.
         *
         * @param args The endpoint to send a POST request.
         * @return A JSONObject containing the server response.
         */
        @Override
        protected JSONObject doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection http = null;

            try {
                URL url = new URL(args[0]);

                // Open a connect to send a POST request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
                http.setRequestMethod("POST");

                // Insert data for POST request
                StringBuilder sb = new StringBuilder();

                sb.append(POST_KEY);
                sb.append("=");
                sb.append(POST_VALUE);

                OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                out.write(sb.toString());
                out.flush();

                Log.d("TESTING - POST", "Connecting...");

                int responseCode = http.getResponseCode();

                // Process HTTP response code

                // If the connection to the server is a success...
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //... begin to read the server response
                    InputStream in = new BufferedInputStream(http.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while ((buffer = reader.readLine()) != null) {
                        result.append(buffer);
                    }

                    isConnected = true;
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    Log.d("TESTING - POST", "Access is forbidden");
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d("TESTING - POST", "Server not found");
                } else {
                    Log.d("TESTING - POST", "Unknown error: " + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.d("TESTING - POST", "URL is not in the correct format");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                http.disconnect();
            }

            // Convert the response from the server into a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        /**
         * An implementation of AsyncTask. This will verify the POST request was accepted.
         *
         * @param jsonObject JSON object containing the server response.
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            assertTrue("Unable to connect to server" , isConnected);
            Log.d("TESTING - POST", "Printing JSON Object...");
            Log.d("TESTING - POST", jsonObject.toString());
            try {
                assertEquals(POST_VALUE, jsonObject.getString("test"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will send a GET request.
     */
    private class ServerGETConnect extends AsyncTask<String, Void, JSONObject>{

        boolean isConnected;

        /**
         * A constructor that initializes the connection flag.
         */
        public ServerGETConnect(){
            isConnected = false;
        }

        /**
         * An implementation of AsyncTask. This will send a GET request to the server and get
         * the response in a separate thread.
         *
         * @param args The endpoint to send the GET request.
         * @return A JSONObject containing the server response.
         */
        @Override
        protected JSONObject doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection http = null;

            try {
                URL url = new URL(args[0]);

                // Open a connect to send a GET request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
                http.setRequestMethod("GET");

                Log.d("TESTING - GET", "Connecting...");

                int responseCode = http.getResponseCode();

                // Process HTTP response code

                // If the connection to the server is a success...
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //... begin to read the server response
                    InputStream in = new BufferedInputStream(http.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while ((buffer = reader.readLine()) != null) {
                        result.append(buffer);
                    }

                    isConnected = true;
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    Log.d("TESTING - GET", "Access is forbidden");
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.d("TESTING - GET", "Server not found");
                } else {
                    Log.d("TESTING - GET", "Unknown error: " + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.d("TESTING - GET", "URL is not in the correct format");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                http.disconnect();
            }

            // Convert the response from the server into a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        /**
         * An implementation of AsyncTask. This will verify that the GET request is accepted
         * and the server has responded.
         *
         * @param jsonObject JSON object containing the user's credentials
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            assertTrue("Unable to connect to server", isConnected);
            if (jsonObject != null) {
                Log.d("TESTING - GET", "Printing JSON Object...");
                Log.d("TESTING - GET", jsonObject.toString());
            } else {
                Log.d("TESTING - GET", "JSON Object is null");
            }
            assertNotNull("JSON Object is null", jsonObject);
        }
    }
}