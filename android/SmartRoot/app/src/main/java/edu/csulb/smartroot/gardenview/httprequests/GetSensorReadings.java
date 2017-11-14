package edu.csulb.smartroot.gardenview.httprequests;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.Garden;
import edu.csulb.smartroot.gardenview.GardenHolder;

/**
 * An inner class that will handle getting the sensor readings from the server, in a separate
 * thread.
 */
public class GetSensorReadings extends AsyncTask<String, Void, JSONObject> {
    GardenHolder.ViewHolder viewHolder;
    ArrayList<Garden> gardens;
    Context context;

    View view;
    int responseCode;

    /**
     * Constructor that gets a reference to the card view.
     *
     * @param view The of the Garden Activity view.
     * @param viewHolder The Garden card ViewHolder.
     * @param gardens The ArrayList of gardens that will be used to display their information in the
     *                recycler view.
     * @param context The context that will contain the RecyclerView of gardens. In this case,
     *                the activity in the GardenView.java.
     */
    public GetSensorReadings(View view, GardenHolder.ViewHolder viewHolder, ArrayList<Garden> gardens, Context context) {
        this.viewHolder = viewHolder;
        this.gardens = gardens;
        this.context = context;

        this.view = view.getRootView();
        this.responseCode = 0;
    }

    /**
     * An implementation of AsyncTask. This will get the sensor readings from the server in a
     * separate thread.
     *
     * @param args The address to the API to send a GET request.
     * @return A JSONObject containing temperature, moisture, and humidity sensor readings.
     */
    @Override
    protected JSONObject doInBackground(String... args) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection http = null;

        try {
            URL url = new URL(args[0]);

            // Create JSON object to send to server
            JSONObject data = new JSONObject();

            data.put("mac", gardens.get(viewHolder.getAdapterPosition()).getMacAddress());

            // Open a connect to send a POST request to the server
            http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            http.setConnectTimeout(context.getResources().getInteger(R.integer.connection_timeout));
            http.setReadTimeout(context.getResources().getInteger(R.integer.connection_timeout));
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestMethod("POST");

            // Insert data for POST request
            StringBuilder sb = new StringBuilder();

            sb.append(data.toString());

            Log.d("UPDATE", "Data: " + sb.toString());

            OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
            out.write(sb.toString());
            out.flush();

            // Attempt connection and get server response code
            responseCode = http.getResponseCode();

            // If the connection to the server is a success...
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //... begin to read the server response
                InputStream in = new BufferedInputStream(http.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String buffer = null;
                while ((buffer = reader.readLine()) != null) {
                    result.append(buffer);
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("URL is not in the correct format");
            return null;
        } catch (ConnectException e) {
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("UPDATE", "JSON format is incorrect");
        } finally {
            // Disconnect from the server
            if (http != null)
                http.disconnect();
        }

        Log.d("UPDATE", "Results: " + result);

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
     * An implementation of AsyncTask. This will update the sensor readings in this application,
     * client side.
     *
     * @param jsonObject JSON object containing the sensor readings
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        Snackbar snackBar = Snackbar.make(view.findViewById(R.id.fab_add_garden), "", Snackbar.LENGTH_SHORT);
        int position = viewHolder.getAdapterPosition();

        double temperature = gardens.get(position).getTemperature();
        double moisture = gardens.get(position).getMoisture();
        double humidity = gardens.get(position).getHumidity();

        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                // If the server response is valid...
                if (jsonObject != null) {
                    try {
                        // Get readings from JSON object
                        temperature = jsonObject.getDouble("temperature");
                        moisture = jsonObject.getDouble("moisture");
                        humidity = jsonObject.getDouble("humidity");

                        // Store readings in garden class
                        gardens.get(position).setTemperature(temperature);
                        gardens.get(position).setMoisture(moisture);
                        gardens.get(position).setHumidity(humidity);

                    } catch (JSONException e) {
                        // The JSON format is incorrect. Notify user an error has occurred.
                        snackBar.setText(
                                context.getResources().getString(R.string.update_garden_invalid, gardens.get(position).getGardenName()));
                        snackBar.show();
                        e.printStackTrace();
                    }
                }
                break;

            case HttpURLConnection.HTTP_NOT_FOUND:
                // Notify user the server cannot be found
                snackBar.setText(
                        context.getResources().getString(R.string.update_garden_404, gardens.get(position).getGardenName()));
                snackBar.show();
                break;

            case HttpURLConnection.HTTP_FORBIDDEN:
                // Notify user the server is forbidden
                snackBar.setText(
                        context.getResources().getString(R.string.update_garden_403, gardens.get(position).getGardenName()));
                snackBar.show();
                break;

            default:
                // Notify user an unexpected server response was received
                snackBar.setText(
                        context.getResources().getString(R.string.update_garden_unknown, gardens.get(position).getGardenName()));
                snackBar.show();
                break;
        }

        // Update readings on card
        viewHolder.temperature.setText(
                context.getString(R.string.label_temperature, temperature));
        viewHolder.moisture.setText(
                context.getString(R.string.label_moisture, moisture));
        viewHolder.humidity.setText(
                context.getString(R.string.label_humidity, humidity));
    }
}