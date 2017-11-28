package edu.csulb.smartroot.welcome.httprequests;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.companion.Companion;

/**
 * An AsyncTask that will get a list of all plants in the Companion Planting database
 */
public class GetPlantNames extends AsyncTask<String, Void, JSONObject> {

    private Dialog dialogProgress;
    private Context context;

    private Resources resources;
    private int responseCode;

    /**
     * Constructor that references the context of the calling activity.
     * @param context Context of the activity.
     */
    public GetPlantNames(Context context) {
        this.context = context;

        this.resources = context.getApplicationContext().getResources();
        this.responseCode = 0;

        // Create dialog for server connection
        dialogProgress = new Dialog(context);

        // Apply the layout
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);

        TextView textView = (TextView) viewDialog.findViewById(R.id.progress_message);
        textView.setText(R.string.progress_plant);

        dialogProgress.setContentView(viewDialog);

        // Make it so the dialog cannot be dismissed on click
        dialogProgress.setCancelable(false);
        dialogProgress.setCanceledOnTouchOutside(false);
    }

    /**
     * An implementation of AsyncTask. This will display the dialog process on the UI thread,
     * which is separate from the doInBackground thread.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogProgress.show();
    }

    /**
     * An implementation of AsyncTask. This will get an array of all the plant names in the
     * Companion Planting database in a separate thread.
     * @param args The API address to send a GET request.
     * @return A JSON object containing an array of all the plant names.
     */
    @Override
    protected JSONObject doInBackground(String...args) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection http = null;

        try {
            URL url = new URL(args[0]);

            // Open a connection to send a GET request to the server
            http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            http.setConnectTimeout(resources.getInteger(R.integer.connection_timeout));
            http.setReadTimeout(resources.getInteger(R.integer.connection_timeout));
            http.setRequestMethod("GET");

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
        } catch(MalformedURLException e) {
            Log.d("PLANT RETRIEVE", "URL not in correct format");
            e.printStackTrace();
            return null;
        } catch(ConnectException e) {
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("PLANT RETRIEVE", "IO exception");
            e.printStackTrace();
        } finally {
            // Disconnect from the server
            if (http != null)
                http.disconnect();
        }

        Log.d("PLANT RETRIEVE", "Response: " + responseCode);
        Log.d("PLANT RETRIEVE", "Results: " + result);

        // Convert the response from the server int oa JSONObject
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * An implementation of AsyncTask. This will process the server response code to notify
     * the user of the server connection attempt.
     * @param jsonObject JSONObject containing the list of plant names.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        dialogProgress.dismiss();

        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        // Process response code and execute appropriate action
        switch(responseCode) {
            case HttpURLConnection.HTTP_OK:
                if (jsonObject != null) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<String> names = new ArrayList<String>();

                        for (int i = 0; i < jsonArray.length(); i++)
                            names.add(jsonArray.getJSONObject(i).getString("_id"));

                        Intent intent = new Intent(context, Companion.class);
                        intent.putExtra("plants", names);

                        context.startActivity(intent);
                    } catch (JSONException e) {
                        toast.setText((R.string.plant_invalid_data));
                        toast.show();

                        e.printStackTrace();
                    }
                }
                break;

            case HttpURLConnection.HTTP_NOT_FOUND:
                toast.setText(R.string.plant_404);
                toast.show();

                break;

            case HttpURLConnection.HTTP_FORBIDDEN:
                toast.setText(R.string.plant_403);
                toast.show();

                break;

            default:
                toast.setText(R.string.plant_unknown);
                toast.show();

                break;
        }
    }
}
