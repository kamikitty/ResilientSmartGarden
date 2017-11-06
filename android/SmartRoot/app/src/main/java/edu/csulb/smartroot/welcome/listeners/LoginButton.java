package edu.csulb.smartroot.welcome.listeners;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
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
import edu.csulb.smartroot.gardenview.GardenView;
import edu.csulb.smartroot.welcome.httprequests.ValidateCredentials;

/**
 * A button listener for Login. This will attempt to login the user into the database.
 */
public class LoginButton implements Button.OnClickListener {

    private Dialog dialog;

    /**
     * Constructor that will pass the reference to the Login Dialog.
     *
     * @param dialog References the Login Dialog.
     */
    public LoginButton(Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     * An implementation of Button.OnClickListener. This will validate the user input and
     * attempt to login the user into the database.
     *
     * @param view References the Login button.
     */
    @Override
    public void onClick(View view) {
        Toast toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);

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

        // Disable TextView and Button to prevent further user input
        dialog.hide();

        // Create task to connect to server
        ValidateCredentials validateCredentials = new ValidateCredentials(eUserName, ePassword, view, dialog);
        validateCredentials.execute(view.getContext().getString(R.string.login_api));
    }
}