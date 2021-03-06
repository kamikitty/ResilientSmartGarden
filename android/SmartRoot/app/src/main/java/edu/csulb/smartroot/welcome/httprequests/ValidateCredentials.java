package edu.csulb.smartroot.welcome.httprequests;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.GardenView;

/**
 * An inner class that will handle validating the user's credentials, in a separate
 * thread.
 */
public class ValidateCredentials extends AsyncTask<String, Void, JSONObject> {

    private String userName;
    private String password;

    private Dialog dialog;
    private Dialog dialogProgress;
    private View view;

    private Resources resources;
    private int responseCode;

    /**
     * Constructor that references the username and password
     *
     * @param eUserName The EditText of username.
     * @param ePassword The EditText of password.
     * @param view References the login button
     */
    public ValidateCredentials(EditText eUserName, EditText ePassword, View view, Dialog dialog) {
        this.userName = eUserName.getText().toString();
        this.password = ePassword.getText().toString();

        this.dialog = dialog;
        this.view = view;
        this.resources = view.getContext().getResources();
        this.responseCode = 0;

        // Create dialog for server connection
        dialogProgress = new Dialog(view.getContext());

        // Apply the layout
        View viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_progress, null);
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
     * An implementation of AsyncTask. This will get the user's credentials from the server in a
     * separate thread.
     * @param args The API address to send a POST request.
     * @return A JSONObject containing username and password.
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
            data.put("password", password);

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

            Log.d("LOGIN", "Data: " + data.toString());

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
            Log.d("LOGIN", "URL is not in the correct format");
            return null;
        } catch (ConnectException e) {
            responseCode = HttpURLConnection.HTTP_NOT_FOUND;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("LOGIN", "JSON format is incorrect");
        }  finally {
            // Disconnect from the server
            if (http != null)
                http.disconnect();
        }

        Log.d("REGISTER", "Results: " + result);

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
     * An implementation of AsyncTask. This will verify the user's credentials.
     *
     * @param jsonObject JSON object containing the user's credentials.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        dialogProgress.dismiss();

        Toast toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        // Process response code and execute appropriate action
        switch (responseCode) {
            case HttpURLConnection.HTTP_OK:
                // If the server response is valid...
                if (jsonObject != null) {
                    try {
                        // Get credentials from JSONObject
                        boolean response = jsonObject.getBoolean("success");

                        // If the user credentials are validated...
                        if (response) {
                            //... create an intent to go to the GardenView

                            // Create toast to notify user of successful login
                            toast.setText(R.string.login_success);
                            toast.show();

                            // Dismiss the dialog
                            dialog.dismiss();

                            // Create new intent and store username to display in GardenView
                            Intent intent = new Intent(view.getContext(), GardenView.class);
                            intent.putExtra("username", userName);

                            view.getContext().startActivity(intent);
                        } else {
                            //... otherwise notify the user the credentials are incorrect
                            toast.setText(R.string.login_failed);
                            toast.show();

                            // Show dialog again
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        // The JSON format is incorrect. Notify user an error has occurred.
                        toast.setText(R.string.login_invalid_data);
                        toast.show();

                        // Show dialog again
                        dialog.show();
                        e.printStackTrace();
                    }
                } else {
                    // The server did not respond with a JSON format. Notify user an error has occurred.
                    toast.setText(R.string.login_invalid_data);
                    toast.show();

                    // Show dialog again
                    dialog.show();
                }
                break;

            case HttpURLConnection.HTTP_NOT_FOUND:
                // Notify user the server cannot be found
                toast.setText(R.string.login_404);
                toast.show();

                // Show dialog again
                dialog.show();
                break;

            case HttpURLConnection.HTTP_FORBIDDEN:
                // Notify user the server is forbidden
                toast.setText(R.string.login_403);
                toast.show();

                // Show dialog again
                dialog.show();
                break;

            default:
                // Notify user an unexpected server response was received
                toast.setText(R.string.login_unknown);
                toast.show();

                // Show dialog again
                dialog.show();
                break;
        }
    }
}