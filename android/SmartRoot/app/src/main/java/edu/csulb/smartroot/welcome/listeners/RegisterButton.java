package edu.csulb.smartroot.welcome.listeners;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
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

        // Hide dialog to prevent further user input
        dialog.hide();

        // Create task to connect to server
        CreateCredentials createCredentials = new CreateCredentials(eUserName, eEmail, ePassword, view);
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

        String userName;
        String eMail;
        String password;

        Dialog dialogProgress;
        View view;
        int responseCode;

        /**
         * Constructor that references the username, email, and password.
         * @param eUserName The EditText of username.
         * @param eEmail The EditText of email.
         * @param ePassword The EditText of password.
         * @param view References the register button.
         */
        public CreateCredentials(EditText eUserName, EditText eEmail, EditText ePassword, View view) {
            this.userName = eUserName.getText().toString();
            this.eMail = eEmail.getText().toString();
            this.password = ePassword.getText().toString();

            this.view = view;
            responseCode = 0;

            // Create dialog for server connection
            dialogProgress = new Dialog(context);

            // Apply the layout
            View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
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

                    in.close();
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

            Log.d("REGISTER", "Results: " + result);

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
            dialogProgress.dismiss();

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
                            // Get server response from JSONObject
                            boolean response = jsonObject.getBoolean("success");

                            Log.d("REGISTER" , "Response: " + response);

                            // Handle server response and display message to user
                            if (response) {
                                toast.setText(R.string.register_success);
                                toast.show();

                                // Discard the dialog
                                dialog.dismiss();
                            } else {
                                toast.setText(R.string.register_failed);
                                toast.show();

                                // Show dialog again
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            // The JSON format is incorrect. Notify user an error has occurred.
                            toast.setText(R.string.register_invalid_data);
                            toast.show();

                            // Show dialog again
                            dialog.show();

                            e.printStackTrace();
                        }
                    }

                    // Display dialog to user
                    dialog.show();
                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user the server cannot be found
                    toast.setText(R.string.register_404);
                    toast.show();

                    // Show dialog again
                    dialog.show();
                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    toast.setText(R.string.register_403);
                    toast.show();

                    // Show dialog again
                    dialog.show();
                    break;

                default:
                    // Notify user an unexpected server response was received
                    toast.setText(R.string.register_unknown);
                    toast.show();

                    // Show dialog again
                    dialog.show();
                    break;
            }
        }
    }
}