package edu.csulb.smartroot.companion.httprequest;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.csulb.smartroot.R;

public class GetCompanion extends AsyncTask<String, Void, JSONObject> {
    private Dialog dialogProgress;

    private Resources resources;
    private int responseCode;

    Context context;

    public GetCompanion(View view, Context context) {
        this.resources = context.getResources();
        this.responseCode = 0;

        this.context = context;

        // Create dialog for server connection
        dialogProgress = new Dialog(context);

        // Apply the layout
        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        dialogProgress.setContentView(viewDialog);

        TextView textView = (TextView) viewDialog.findViewById(R.id.progress_message);
        textView.setText(R.string.progress_companion);

        // Make it so the dialog cannot be dismissed on click
        dialogProgress.setCancelable(false);
        dialogProgress.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //dialogProgress.show();
    }

    @Override
    protected JSONObject doInBackground(String...args) {
        HttpURLConnection http = null;

        ArrayList <JSONArray> compatibility = new ArrayList<JSONArray>();

        for (int i = 1; i < args.length; i++) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(args[0]);

                // Create JSON object to send to server
                JSONObject data = new JSONObject();

                data.put("plant", args[i]);

                // Open a connection to send a POST request to teh server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(resources.getInteger(R.integer.connection_timeout));
                http.setReadTimeout(resources.getInteger(R.integer.connection_timeout));
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestMethod("POST");

                // Insert data for POST request
                StringBuilder sb = new StringBuilder();

                sb.append(data.toString());

                Log.d("COMPANION", "Data: " + data.toString());

                OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                out.write(sb.toString());
                out.flush();

                // Attempt connect and get server response code
                responseCode = http.getResponseCode();

                // If the connect to the server is a success...
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
                Log.d("COMPANION", "URL is not in the correct format");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException e) {
                Log.d("COMPANION", "JSON format is incorrect");
            } finally {
                if (http != null)
                    http.disconnect();
            }

            Log.d("COMPANION" , "Results: " + result);
            // Covert the response from the server into a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                compatibility.add(jsonObject.getJSONArray("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayList<String> enemy1 = new ArrayList<String>();
        ArrayList<String> enemy2 = new ArrayList<String>();

        try {
            enemy1.add(args[1]);
            for (int i = 0; i < compatibility.get(0).length(); i++)
                enemy1.add(compatibility.get(0).getString(i));

            enemy2.add(args[2]);
            for (int i = 0; i < compatibility.get(1).length(); i++) {
                enemy2.add(compatibility.get(1).getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (enemy1.contains(enemy2.get(0))) {
            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("compatible", "false");

                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (enemy2.contains(enemy1.get(0))) {
            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("compatible", "false");

                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("compatible", "true");

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        dialogProgress.dismiss();

        Log.d("POST", "DATA: " + jsonObject.toString());

        try {
            if (jsonObject.getBoolean("compatible")) {
                Toast toast = Toast.makeText(context, "Plants are compatible!", Toast.LENGTH_LONG);

                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                textView.setGravity(Gravity.CENTER);

                toast.show();
            } else {
                Toast toast = Toast.makeText(context, "Plants are not compatible!", Toast.LENGTH_LONG);

                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                textView.setGravity(Gravity.CENTER);

                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
