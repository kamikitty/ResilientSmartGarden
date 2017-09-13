package edu.csulb.smartroot.welcome.listeners;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.csulb.smartroot.R;

/**
 * A button listener for Register. This will attempt to register a new user on the database.
 */
public class RegisterButton implements Button.OnClickListener{

    private AlertDialog dialog;
    private Context context;

    /**
     * Constructor that will get a reference to the AlertDialog to build the login dialog.
     * @param dialog The reference to AlertDialog.
     */
    public RegisterButton(AlertDialog dialog, Context context){
        this.dialog = dialog;
        this.context = context;
    }

    /**
     * An implementation of Button.OnClickListener. This will validate the user input and
     * attempt to register a new user into the database.
     * @param view References the Register button.
     */
    @Override
    public void onClick(View view){
        // Get the references to EditText from dialog_register.xml layout.
        EditText eUserName = (EditText) dialog.findViewById(R.id.username);
        EditText eEmail = (EditText) dialog.findViewById(R.id.email);
        EditText ePassword = (EditText) dialog.findViewById(R.id.password);
        EditText eConfirmPassword = (EditText) dialog.findViewById(R.id.confirm_password);

        // Get the credentials from the EditText
        String userName = eUserName.getText().toString();
        String email = eEmail.getText().toString();
        String password = ePassword.getText().toString();
        String confirmPassword = eConfirmPassword.getText().toString();

        // Validate user input for username, email, password, and confirm password
        if (userName.equals("")) {
            Toast.makeText(context, R.string.username_empty, Toast.LENGTH_SHORT).show();
            eUserName.requestFocus();
            return;
        }
        if (email.equals("")) {
            Toast.makeText(context, R.string.email_empty, Toast.LENGTH_SHORT).show();
            eEmail.requestFocus();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(context, R.string.password_empty, Toast.LENGTH_SHORT).show();
            ePassword.requestFocus();
            return;
        }
        if (confirmPassword.equals("")) {
            Toast.makeText(context, R.string.confirm_password_empty, Toast.LENGTH_SHORT).show();
            eConfirmPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(context, R.string.password_mismatch, Toast.LENGTH_SHORT).show();
            eConfirmPassword.requestFocus();
            return;
        }

        // TODO: Implement registering new user to database.
        view.setEnabled(false);

        // Create task to connect to server
        CreateCredentials createCredentials = new CreateCredentials(userName, email, password, view);
        createCredentials.execute(context.getString(R.string.register_api));
    }

    /**
     * An inner class that will handle creating the user's credentials on the database, in a
     * separate thread.
     */
    private class CreateCredentials extends AsyncTask<String, Void, JSONObject> {
        String userName;
        String eMail;
        String password;
        View view;
        int responseCode;

        /**
         * Constructor that references the username, email, and password.
         * @param userName The username.
         * @param eMail The email of the user.
         * @param password The password of the user.
         * @param view References the register button.
         */
        public CreateCredentials(String userName, String eMail, String password, View view) {
            this.userName = userName;
            this.eMail = eMail;
            this.password = password;
            this.view = view;
            responseCode = 0;
        }

        /**
         * An implementation of AsyncTask. This will send the new user's credentials to the server's
         * database in a separate thread.
         * @param args The API address to send a POST request.
         * @return A JSONObject containing the server response.
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
                data.put("email", eMail);
                data.put("password", password);

                // Open a connection to send a POST request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(R.integer.connection_timeout);
                http.setReadTimeout(R.integer.connection_timeout);
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestMethod("POST");

                // Insert data for POST request
                StringBuilder sb = new StringBuilder();

                sb.append(data.toString());

                OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                out.write(sb.toString());
                out.flush();

                // Attempt connection and get server response code
                responseCode = http.getResponseCode();

                Log.d("REGISTER", "Response Code " + responseCode);

                // If the connection to the server is a success...
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //... begin to read the serve response
                    InputStream in = new BufferedInputStream(http.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while ((buffer = reader.readLine()) != null) {
                        result.append(buffer);
                    }
                }
            } catch (MalformedURLException e) {
                Log.d("REGISTER", "URL is not in the correct format");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("REGISTER", "JSON format is incorrect");
            } finally {
                if (http != null) {
                    http.disconnect();
                }
            }

            // Convert the response from the server into a JSON Object
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        /**
         * An implementation of AsyncTask. This will verify that the user's account was created
         * successfully.
         * @param jsonObject JSON object containing server response
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            // Process response code and execute appropriate action
            Log.d("REGISTER", "onPostExecute: Response Code " + responseCode);
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // If the server response is valid...
                    if (jsonObject != null) {
                        try {
                            // TODO: Adjust to server specifications when it comes online
                            // Get server response from JSONObject
                            String response = jsonObject.getString("data");

                            Log.d("REGISTER" , response);

                            // Handle server response and display message to user
                            Toast.makeText(
                                    context, R.string.register_success, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // The JSON format is incorrect. Notify user an error has occurred.
                            e.printStackTrace();
                            Toast.makeText(
                                    context, R.string.register_invalid_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                    view.setEnabled(true);
                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user the server cannot be found
                    Toast.makeText(
                            context, R.string.register_404, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    Toast.makeText(
                            context, R.string.register_403, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;

                default:
                    // Notify user an unexpected server response was received
                    Toast.makeText(
                            context, R.string.register_unknown, Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                    break;
            }
        }
    }
}