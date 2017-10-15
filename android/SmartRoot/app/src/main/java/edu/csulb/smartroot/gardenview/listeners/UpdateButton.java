package edu.csulb.smartroot.gardenview.listeners;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.Garden;
import edu.csulb.smartroot.gardenview.GardenHolder;

/**
 * A button listener for Update. This will update the sensor readings of the garden.
 */
public class UpdateButton implements Button.OnClickListener {
    GardenHolder.ViewHolder viewHolder;
    ArrayList<Garden> gardens;
    Context context;

    /**
     * Constructor that passes a reference to ViewHolder, ArrayList of gardens, and the parent context.
     * @param viewHolder The view holder of the garden card.
     * @param gardens An ArrayList of all the user's gardens.
     * @param context The context that will contain the RecyclerView of gardens. In this case,
     *                the activity in GardenView.java.
     */
    public UpdateButton(GardenHolder.ViewHolder viewHolder, ArrayList<Garden> gardens, Context context){
        this.viewHolder = viewHolder;
        this.gardens = gardens;
        this.context = context;
    }

    /**
     * An implementation of Button.OnClickListener. This will retrieve the current
     * sensor readings from the garden.
     * @param view References the Update button.
     */
    @Override
    public void onClick(View view){

        GetSensorReadings getSensorReadings = new  GetSensorReadings(view);
        getSensorReadings.execute(context.getString(R.string.sensor_api));
    }

    /**
     * An inner class that will handle getting the sensor readings from the server, in a separate
     * thread.
     */
    private class GetSensorReadings extends AsyncTask<String, Void, JSONObject> {
        View view;

        /**
         * Constructor that gets a reference to the card view.
         * @param view The Update Button view.
         */
        public GetSensorReadings(View view) {
            this.view = view.getRootView();
        }

        /**
         * An implementation of AsyncTask. This will get the sensor readings from the server in a
         * separate thread.
         * @param args The address to the API to send a GET request.
         * @return A JSONObject containing temperature, moisture, and humidity sensor readings.
         */
        @Override
        protected JSONObject doInBackground(String...args){
            StringBuilder result = new StringBuilder();
            HttpURLConnection http = null;

            try{
                URL url = new URL(args[0]);

                // Open a connect to send a GET request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);

                // If the connection to the server is a success...
                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //... begin to read the server response
                    InputStream in = new BufferedInputStream(http.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while((buffer = reader.readLine()) != null){
                        result.append(buffer);
                    }
                }
            } catch(MalformedURLException e) {
                System.out.println("URL is not in the correct format");
                return null;
            } catch(IOException e){
                e.printStackTrace();
            } finally {
                http.disconnect();
            }

            // Convert the response from the server into a JSONObject
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(result.toString());
            } catch(JSONException e){
                e.printStackTrace();
            }

            return jsonObject;
        }

        /**
         * An implementation of AsyncTask. This will update the sensor readings in this application,
         * client side.
         * @param jsonObject JSON object containing the sensor readings
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            try {
                // TODO: Get data from JSON object and store it in current garden
                int position = viewHolder.getAdapterPosition();

                // Get readings from JSON object
                double temperature = jsonObject.getDouble("temperature");
                double moisture = jsonObject.getDouble("moisture");
                double humidity = jsonObject.getDouble("humidity");

                // Update readings on card
                viewHolder.name.setText(
                        context.getString(R.string.label_temperature, temperature));
                viewHolder.moisture.setText(
                        context.getString(R.string.label_moisture, moisture));
                viewHolder.humidity.setText(
                        context.getString(R.string.label_humidity, humidity));

                // Store readings in garden class
                gardens.get(position).setTemperature(temperature);
                gardens.get(position).setMoisture(moisture);
                gardens.get(position).setHumidity(humidity);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}