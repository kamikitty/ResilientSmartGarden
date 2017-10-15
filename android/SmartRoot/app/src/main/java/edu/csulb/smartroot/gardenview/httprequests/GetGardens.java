package edu.csulb.smartroot.gardenview.httprequests;

import android.app.Dialog;
import android.content.Context;
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
 * An AsyncTask that will get the user's garden from the server.
 */
public class GetGardens extends AsyncTask<String, Void, JSONObject> {

    private GardenHolder holder;
    private ArrayList<Garden> gardens;
    private Dialog dialogProgress;
    private Context context;

    private String userName;

    private Resources resources;
    private int responseCode;

    /**
     * Constructor that references the GardenHolder and ArrayList of gardens
     *
     * @param holder The referenced GardenHolder.
     * @param userName The username.
*      @param context Context of activity.
     */
    public GetGardens(GardenHolder holder, String userName, Context context) {
        this.holder = holder;
        this.gardens = holder.getGardens();
        this.context = context;
        this.userName = userName;

        this.resources = context.getApplicationContext().getResources();
        this.responseCode = 0;

        // Create dialog for server connection
        dialogProgress = new Dialog(context);

        // Apply the layout
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);

        TextView textView = (TextView) viewDialog.findViewById(R.id.progress_message);
        textView.setText(R.string.progress_garden);

        dialogProgress.setContentView(viewDialog);

        // Make it so the dialog cannot be dismissed on click
        dialogProgress.setCancelable(false);
        dialogProgress.setCanceledOnTouchOutside(false);
    }

    /**
     * An implementation of AsyncTask. This will display the dialog progress on the UI thread,
     * which is separate from the doInBackground thread.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogProgress.show();
    }

    /**
     * An implementation of AsyncTask. This will get the user's garden from the server in a
     * separate thread.
     * @param args The API address to send a POST request.
     * @return A JSONObject containing the user's garden.
     */
    @Override
    protected JSONObject doInBackground(String...args) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection http = null;

        try {
            URL url = new URL(args[0]);

            // Create JSON object to send to server
            JSONObject data = new JSONObject();
            data.put("username", userName);

            // Open a connection to send a POST request to the server
            http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            http.setConnectTimeout(resources.getInteger(R.integer.connection_timeout));
            http.setReadTimeout(resources.getInteger(R.integer.connection_timeout));
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestMethod("POST");

            // Insert data for POST request
            StringBuilder sb = new StringBuilder();

            sb.append(data.toString());

            Log.d("GARDEN RETRIEVE", "Data: " + sb.toString());

            OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
            out.write(sb.toString());
            out.flush();

            // Attempt connection and get server response code
            responseCode = http.getResponseCode();

            // If the connection to the server is a success...
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //... begin to read the server response
                InputStream in = new BufferedInputStream((http.getInputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String buffer = null;
                while ((buffer = reader.readLine()) != null) {
                    result.append(buffer);
                }
            }
        } catch(MalformedURLException e) {
            Log.d("GARDEN RETRIEVE", "URL not in correct format");
            e.printStackTrace();
            return null;
        } catch (ConnectException e) {
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            e.printStackTrace();
        } catch(IOException e) {
            Log.d("GARDEN RETRIEVE", "IO exception");
            e.printStackTrace();
        }
        catch (JSONException e) {
            Log.d("GARDEN RETRIEVE", "JSON exception");
            e.printStackTrace();
        } finally {
            // Disconnect from the server
            if (http != null)
                http.disconnect();
        }

        Log.d("GARDEN RETRIEVE", "Response: " + responseCode);
        Log.d("GARDEN RETRIEVE", "Results: " + result);

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
     * An implementation of AsyncTask. This will retrieve the user's garden and pass it
     * to the garden ArrayList.
     *
     * @param jsonObject JSON object containing the user's garden.
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
                // If the server response is valid...
                if (jsonObject != null) {
                    try {
                        // Get user's garden from JSONObject
                        JSONArray jsonGardens = jsonObject.getJSONArray("gardens");

                        gardens.clear();

                        // Separate the JSONObject gardens into arrays
                        for (int i = 0; i < jsonGardens.length(); i++) {
                            JSONObject garden = jsonGardens.getJSONObject(i);
                            gardens.add(new Garden(
                                    garden.getString("name"),
                                    garden.getString("mac")));
                        }

                        // Update recycler view
                        holder.notifyDataSetChanged();

                    } catch (JSONException e) {
                        toast.setText(R.string.garden_invalid_data);
                        toast.show();

                        e.printStackTrace();
                    }
                }

                break;

            case HttpURLConnection.HTTP_NOT_FOUND:
                // Notify user server cannot be found
                toast.setText(R.string.garden_404);
                toast.show();

                break;

            case HttpURLConnection.HTTP_FORBIDDEN:
                // Notify user the server is forbidden
                toast.setText(R.string.garden_403);
                toast.show();

                break;

            default:
                // Notify user an unexpected server response was received
                toast.setText(R.string.garden_unknown);
                toast.show();

                break;
        }
    }
}