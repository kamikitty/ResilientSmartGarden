package edu.csulb.smartroot.welcome.listeners;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import edu.csulb.smartroot.welcome.httprequests.CreateCredentials;

/**
 * A button listener for Register. This will attempt to register a new user on the database.
 */
public class RegisterButton implements Button.OnClickListener {

    private Dialog dialog;

    /**
     * Constructor that will get a reference to the Dialog to build the login dialog.
     * @param dialog The reference to Dialog.
     */
    public RegisterButton(Dialog dialog){
        this.dialog = dialog;
    }

    /**
     * An implementation of Button.OnClickListener. This will validate the user input and
     * attempt to register a new user into the database.
     * @param view References the Register button.
     */
    @Override
    public void onClick(View view){
        Toast toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);

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
        CreateCredentials createCredentials = new CreateCredentials(eUserName, eEmail, ePassword, view, dialog);
        createCredentials.execute(view.getContext().getString(R.string.register_api));
    }
}