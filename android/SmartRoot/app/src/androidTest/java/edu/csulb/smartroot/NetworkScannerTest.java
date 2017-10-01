package edu.csulb.smartroot;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;

import edu.csulb.smartroot.welcome.Welcome;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * An instrumented test that will test scanning a network.
 */
@RunWith(AndroidJUnit4.class)
public class NetworkScannerTest {

    @Rule
    public ActivityTestRule<Welcome> activityTestRule =
            new ActivityTestRule<>(Welcome.class);

    /////////////////////////////
    // INSTRUMENTED TEST UNITS //
    /////////////////////////////

    /**
     * Conducts an ip scan in the WiFi network
     */
    //@Test
    public void WiFiTest() {
        WifiManager wifiManager;

        Log.d("WIFI TEST", "Getting WIFI");
        wifiManager = (WifiManager) activityTestRule.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // Get IP address of Android device
        int ip = wifiInfo.getIpAddress();

        // Log state of WiFi
        Log.d("WIFI TEST", "State: " + wifiManager.isWifiEnabled());

        // Go through all host addresses to scan active IP addresses
        try {
            for (int i = 0; i < 256; i++) {
                StringBuilder sb = new StringBuilder();

                // Build IP address to ping
                sb.append(ip & 0xFF);
                sb.append(".");
                sb.append((ip >> 8) & 0xFF);
                sb.append(".");
                sb.append((ip >> 16) & 0xFF);
                sb.append(".");
                sb.append(i);

                String ipAddress = sb.toString();

                // Check to see if IP address is reachable
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                if (inetAddress.isReachable(100)) {
                    Log.d("WIFI TEST", inetAddress.toString() + " is reachable");
                    Log.d("WIFI TEST", "Name: " + inetAddress.getHostName());
                }
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conducts a POST request to handshake the garden
     */
    //@Test
    public void HandShakeTest() {
        HandShake handShake = new HandShake();

        handShake.execute("http://192.168.0.15:3001/handShake");
    }

    /**
     * Conducts a combination of WiFi Scan and Handshake
     */
    @Test
    public void CombinedTest() {
        WifiManager wifiManager;

        Log.d("WIFI TEST", "Getting WIFI");
        wifiManager = (WifiManager) activityTestRule.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // Get IP address of Android device
        int ip = wifiInfo.getIpAddress();

        // Log state of WiFi
        Log.d("WIFI TEST", "State: " + wifiManager.isWifiEnabled());

        ArrayList<String> reachableIp = new ArrayList<String>();

        // Go through all host addresses to scan active IP addresses
        try {
            for (int i = 0; i < 256; i++) {
                StringBuilder sb = new StringBuilder();

                // Build IP address to ping
                sb.append(ip & 0xFF);
                sb.append(".");
                sb.append((ip >> 8) & 0xFF);
                sb.append(".");
                sb.append((ip >> 16) & 0xFF);
                sb.append(".");
                sb.append(i);

                String ipAddress = sb.toString();

                // Check to see if IP address is reachable
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                if (inetAddress.isReachable(100)) {
                    Log.d("WIFI TEST", inetAddress.toString() + " is reachable");
                    reachableIp.add(inetAddress.getHostAddress());
                }
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("WIFI TEST", reachableIp.toString());

        for (String ipAddress : reachableIp) {
            HandShake handShake = new HandShake();

            StringBuilder sb = new StringBuilder();

            sb.append("http://");
            sb.append(ipAddress);
            sb.append(":3001/handShake");


            Log.d("WIFI TEST", "Reaching " + sb.toString());

            handShake.execute(sb.toString());
        }

    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will send a GET request.
     */
    private class HandShake extends AsyncTask<String, Void, JSONObject> {

        boolean isConnected;

        /**
         * A constructor that initializes the connection flag.
         */
        public HandShake() {
            isConnected = false;
        }

        /**
         * An implementation of AsyncTask. This will send a POST request to the server and get
         * the response in a separate thread.
         *
         * @param args The endpoint to send the POST request.
         * @return A JSONObject containing the server response.
         */
        @Override
        protected JSONObject doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection http = null;

            try {
                URL url = new URL(args[0]);

                JSONObject data = new JSONObject();

                data.put("handShake", "SmartRoots");

                // Open a connect to send a GET request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestMethod("POST");

                StringBuilder sb = new StringBuilder();

                sb.append(data.toString());

                Log.d("TESTING - GET", "Connecting...");

                OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                out.write(sb.toString());
                out.flush();

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
            } catch (JSONException e) {
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
         * An implementation of AsyncTask. This will verify that the POST request is accepted
         * and the server has responded.
         *
         * @param jsonObject JSON object containing the user's credentials
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            // Comment out the asserts when testing combined.

            //assertTrue("Unable to connect to server", isConnected);
            if (jsonObject != null) {
                Log.d("TESTING - GET", "Printing JSON Object...");
                Log.d("TESTING - GET", jsonObject.toString());
            } else {
                Log.d("TESTING - GET", "JSON Object is null");
            }
            //assertNotNull("JSON Object is null", jsonObject);
        }
    }
}