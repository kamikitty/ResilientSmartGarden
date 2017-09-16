package edu.csulb.smartroot.welcome.listeners;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
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
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        // Get the references to EditText from dialog_register.xml layout.
        EditText eUserName = (EditText) dialog.findViewById(R.id.username);
        EditText eEmail = (EditText) dialog.findViewById(R.id.email);
        EditText ePassword = (EditText) dialog.findViewById(R.id.password);
        EditText eConfirm = (EditText) dialog.findViewById(R.id.confirm_password);

        // Validate user input for username, email, password, and confirm password
        if (eUserName.length() <= 0) {
            toast.setText(R.string.username_empty);
            toast.show();
            eUserName.requestFocus();
            return;
        }
        if (eEmail.length() <= 0) {
            toast.setText(R.string.email_empty);
            toast.show();
            eEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(eEmail.getText().toString()).matches()) {
            toast.setText(R.string.email_invalid);
            toast.show();
            eEmail.requestFocus();
            return;
        }
        if (ePassword.length() <= 0) {
            toast.setText(R.string.password_empty);
            toast.show();
            ePassword.requestFocus();
            return;
        }
        if (eConfirm.length() <= 0) {
            toast.setText(R.string.confirm_password_empty);
            toast.show();
            eConfirm.requestFocus();
            return;
        }
        if (!ePassword.getText().toString().equals(eConfirm.getText().toString())){
            toast.setText(R.string.password_mismatch);
            toast.show();
            eConfirm.requestFocus();
            return;
        }

        // TODO: Implement registering new user to database.
        // Disable TextView and Button to prevent further user input
        inputOff(eUserName, eEmail, ePassword, eConfirm, view);

        // Create task to connect to server
        CreateCredentials createCredentials = new CreateCredentials(eUserName, eEmail, ePassword, eConfirm, view);
        createCredentials.execute(context.getString(R.string.register_api));
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will handle creating the user's credentials on the database, in a
     * separate thread.
     */
    private class CreateCredentials extends AsyncTask<String, Void, JSONObject> {
        EditText eUserName;
        EditText eEmail;
        EditText ePassword;
        EditText eConfirm;

        String userName;
        String eMail;
        String password;

        View view;
        int responseCode;

        /**
         * Constructor that references the username, email, and password.
         * @param eUserName The EditText of username.
         * @param eEmail The EditText of email.
         * @param ePassword The EditText of password.
         * @param view References the register button.
         */
        public CreateCredentials(EditText eUserName, EditText eEmail, EditText ePassword, EditText eConfirm, View view) {
            this.eUserName = eUserName;
            this.eEmail = eEmail;
            this.ePassword = ePassword;
            this.eConfirm = eConfirm;
            this.view = view;
            responseCode = 0;

            // Extract the text from EditText. This needs to be done before AsyncTasks since
            // getting any text from a UI element cannot be done in a thread
            userName = eUserName.getText().toString();
            eMail = eEmail.getText().toString();
            password = ePassword.getText().toString();
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
            } catch (ConnectException e) {
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("REGISTER", "JSON format is incorrect");
            }  finally {
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
            Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);

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
                            toast.setText(R.string.register_success);
                            toast.show();
                        } catch (JSONException e) {
                            // The JSON format is incorrect. Notify user an error has occurred.
                            toast.setText(R.string.register_invalid_data);
                            toast.show();

                            e.printStackTrace();
                        }
                    }
                    view.setEnabled(true);
                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user the server cannot be found
                    toast.setText(R.string.register_404);
                    toast.show();

                    // Re-enable UI elements
                    inputOn(eUserName, eEmail, ePassword, eConfirm, view);
                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    toast.setText(R.string.register_403);
                    toast.show();

                    inputOn(eUserName, eEmail, ePassword, eConfirm, view);
                    break;

                default:
                    // Notify user an unexpected server response was received
                    toast.setText(R.string.register_unknown);
                    toast.show();

                    inputOn(eUserName, eEmail, ePassword, eConfirm, view);
                    break;
            }
        }
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * A helper method that will prevent further user input by disabling UI elements.
     * @param eUserName The EditText of the username.
     * @param eEmail The EditText of the email.
     * @param ePassword The EditText of the password.
     * @param eConfirm The EditText of the password confirmation.
     * @param view The references to the register button.
     */
    private void inputOff(EditText eUserName, EditText eEmail, EditText ePassword, EditText eConfirm, View view) {
        eUserName.setEnabled(false);
        eEmail.setEnabled(false);
        ePassword.setEnabled(false);
        eConfirm.setEnabled(false);
        view.setEnabled(false);
    }

    /**
     * A helper method that will accept user input by enabling UI elements.
     * @param eUserName The EditText of the username.
     * @param eEmail The EditText of the email.
     * @param ePassword The EditText of the password.
     * @param eConfirm The EditText of the password confirmation.
     * @param view The references to the register button.
     */
    private void inputOn(EditText eUserName, EditText eEmail, EditText ePassword, EditText eConfirm, View view) {
        eUserName.setEnabled(true);
        eEmail.setEnabled(true);
        ePassword.setEnabled(true);
        eConfirm.setEnabled(true);
        view.setEnabled(true);
    }
}