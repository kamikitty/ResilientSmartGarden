package edu.csulb.smartroot.welcome.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        // Get the references to EditText from dialog_login.xml layout.
        EditText eUserName = (EditText) dialog.findViewById(R.id.username);
        EditText ePassword = (EditText) dialog.findViewById(R.id.password);

        // Get the credentials from the EditText
        String userName = eUserName.getText().toString();
        String password = ePassword.getText().toString();

        // Validate user input for username and password
        if (userName.equals("")) {
            Toast.makeText(context, R.string.username_empty, Toast.LENGTH_SHORT).show();
            eUserName.requestFocus();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_SHORT).show();
            ePassword.requestFocus();
            return;
        }

        // TODO: Implement SmartRoots API GET request to validate credentials
        view.setEnabled(false);

        // Create task to connect to server
        ValidateCredentials validateCredentials = new ValidateCredentials(userName, password, view);
        validateCredentials.execute(context.getString(R.string.login_api));
    }

    /**
     * An inner class that will handle validating the user's credentials, in a separate
     * thread.
     */
    private class ValidateCredentials extends AsyncTask<String, Void, JSONObject> {
        String userName;
        String password;
        View view;
        int responseCode;

        /**
         * Constructor that references the username and password
         *
         * @param userName The username.
         * @param password The password of the user
         * @param view References the login button
         */
        public ValidateCredentials(String userName, String password, View view) {
            this.userName = userName;
            this.password = password;
            this.view = view;
            this.responseCode = 0;
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
                // Open a connection to send a GET request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
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
            } catch (MalformedURLException e) {
                System.out.println("URL is not in the correct format");
                return null;
            } catch (ConnectException e) {
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect from the server
                if (http != null)
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
         * An implementation of AsyncTask. This will verify the user's credentials.
         *
         * @param jsonObject JSON object containing the user's credentials.
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            // Process response code and execute appropriate action
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // If the server response is valid...
                    if (jsonObject != null) {
                        try {
                            // TODO: Adjust to server specifications when it comes online
                            // Get credentials from JSONObject
                            String vUserName = jsonObject.getString("username");
                            String vPassword = jsonObject.getString("password");

                            // If the user credentials are validated...
                            if (userName.equals(vUserName) && password.equals(vPassword)) {
                                //... create an intent to go to the GardenView
                                dialog.dismiss();
                                Toast.makeText(
                                        context, context.getString(R.string.login_success), Toast.LENGTH_SHORT).show();

                                // Create new intent and store username to display in GardenView
                                Intent intent = new Intent(context, GardenView.class);
                                intent.putExtra("username", userName);

                                context.startActivity(intent);
                            } else {
                                //... otherwise notify the user the credentials are incorrect
                                Toast.makeText(
                                        context, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                view.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            // The JSON format is incorrect. Notify user an error has occurred.
                            e.printStackTrace();
                            Toast.makeText(
                                    context, R.string.login_invalid_data, Toast.LENGTH_SHORT).show();
                            view.setEnabled(true);
                        }
                    } else {
                        // The server did not respond with a JSON format. Notify user an error has occurred.
                        Toast.makeText(
                                context, R.string.login_invalid_data, Toast.LENGTH_SHORT).show();
                        view.setEnabled(true);
                    }
                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user the server cannot be found
                    Toast.makeText(
                            context, R.string.login_404, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    Toast.makeText(
                            context, R.string.login_403, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;

                default:
                    // Notify user an unexpected server response was received
                    Toast.makeText(
                            context, R.string.login_unknown, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;
            }
        }
    }
}