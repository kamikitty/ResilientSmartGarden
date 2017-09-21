package edu.csulb.smartroot.welcome.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
 * A button listener for Login. This will attempt to login the user into the database.
 */
public class LoginButton implements Button.OnClickListener {

    private AlertDialog dialog;
    private Context context;

    /**
     * Constructor that will pass the reference to the Login Dialog.
     *
     * @param dialog References the Login Dialog.
     */
    public LoginButton(AlertDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    /**
     * An implementation of Button.OnClickListener. This will validate the user input and
     * attempt to login the user into the database.
     *
     * @param view References the Login button.
     */
    @Override
    public void onClick(View view) {
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        // Get the references to EditText from dialog_login.xml layout.
        EditText eUserName = (EditText) dialog.findViewById(R.id.username);
        EditText ePassword = (EditText) dialog.findViewById(R.id.password);

        // Validate user input for username and password
        if (eUserName.length() <= 0) {
            toast.setText(R.string.username_empty);
            toast.show();
            eUserName.requestFocus();
            return;
        }
        if (ePassword.length() <= 0) {
            toast.setText(R.string.password_empty);
            toast.show();
            ePassword.requestFocus();
            return;
        }

        // TODO: Implement SmartRoots API GET request to validate credentials
        // Disable TextView and Button to prevent further user input
        inputOff(eUserName, ePassword, view);

        // Create task to connect to server

        ValidateCredentials validateCredentials = new ValidateCredentials(eUserName, ePassword, view);
        validateCredentials.execute(context.getString(R.string.login_api));
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will handle validating the user's credentials, in a separate
     * thread.
     */
    private class ValidateCredentials extends AsyncTask<String, Void, JSONObject> {
        EditText eUserName;
        EditText ePassword;

        String userName;
        String password;

        View view;
        int responseCode;

        /**
         * Constructor that references the username and password
         *
         * @param eUserName The EditText of username.
         * @param ePassword The EditText of password.
         * @param view References the login button
         */
        public ValidateCredentials(EditText eUserName, EditText ePassword, View view) {
            this.eUserName = eUserName;
            this.ePassword = ePassword;

            this.view = view;
            this.responseCode = 0;

            // Extract the text from EditText. This needs to be done before AsyncTasks since
            // getting any text from a UI element cannot be done in a thread
            userName = eUserName.getText().toString();
            password = ePassword.getText().toString();
        }

        /**
         * An implementation of AsyncTask. This will get the user's credentials from the server in a
         * separate thread.
         *
         * @param args The API address to send a GET request.
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

                // Open a connection to send a GET request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
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
                System.out.println("URL is not in the correct format");
                return null;
            } catch (ConnectException e) {
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
            Log.d("REGISTER", "JSON format is incorrect");
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
            Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);

            // Process response code and execute appropriate action
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // If the server response is valid...
                    if (jsonObject != null) {
                        try {
                            // TODO: Adjust to server specifications when it comes online
                            // Get credentials from JSONObject
                            boolean response = jsonObject.getBoolean("success");

                            // If the user credentials are validated...
                            if (response) {
                                //... create an intent to go to the GardenView
                                dialog.dismiss();

                                // Create toast to notify user of successful login
                                toast.setText(R.string.login_success);
                                toast.show();

                                // Create new intent and store username to display in GardenView
                                Intent intent = new Intent(context, GardenView.class);
                                intent.putExtra("username", userName);

                                context.startActivity(intent);
                            } else {
                                //... otherwise notify the user the credentials are incorrect
                                toast.setText(R.string.login_failed);
                                toast.show();

                                inputOn(eUserName, ePassword, view);
                            }
                        } catch (JSONException e) {
                            // The JSON format is incorrect. Notify user an error has occurred.
                            toast.setText(R.string.login_invalid_data);
                            toast.show();

                            inputOn(eUserName, ePassword, view);
                            e.printStackTrace();
                        }
                    } else {
                        // The server did not respond with a JSON format. Notify user an error has occurred.
                        toast.setText(R.string.login_invalid_data);
                        toast.show();

                        inputOn(eUserName, ePassword, view);
                    }
                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user the server cannot be found
                    toast.setText(R.string.login_404);
                    toast.show();

                    inputOn(eUserName, ePassword, view);
                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    toast.setText(R.string.login_403);
                    toast.show();

                    inputOn(eUserName, ePassword, view);
                    break;

                default:
                    // Notify user an unexpected server response was received
                    toast.setText(R.string.login_unknown);
                    toast.show();

                    inputOn(eUserName, ePassword, view);
                    break;
            }
        }
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * A helper method that will prevent further user input by disabling UI elements
     * @param eUserName The EditText of the username.
     * @param ePassword The EditText of the password.
     * @param view The reference to the login button.
     */
    private void inputOff(EditText eUserName, EditText ePassword, View view) {
        eUserName.setEnabled(false);
        ePassword.setEnabled(false);
        view.setEnabled(false);
    }

    /**
     * A helper method that will accept user input by enabling UI elements
     * @param eUserName The EditText of the username.
     * @param ePassword The EditText of the password.
     * @param view The reference to the login button.
     */
    private void inputOn(EditText eUserName, EditText ePassword, View view) {
        eUserName.setEnabled(true);
        ePassword.setEnabled(true);
        view.setEnabled(true);
    }
}