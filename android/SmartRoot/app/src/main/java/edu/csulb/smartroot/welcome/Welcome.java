package edu.csulb.smartroot.welcome;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.welcome.listeners.*;

/**
 * The starting activity. This will prompt the user to either login or register a new account.
 */
public class Welcome extends AppCompatActivity {

    private LayoutInflater inflater;

    /**
     * Gets references to inflater for button listeners upon creation of this activity.
     * @param savedInstanceState The state of this activity when previously launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setTitle(R.string.activity_label_welcome);

        // Get reference to inflater to use in button listeners
        inflater = getLayoutInflater();
    }

    /////////////////////
    // ONCLICK METHODS //
    /////////////////////

    /**
     * Creates a login dialog when the Login button is clicked/tapped.
     * @param view The button that this method is assigned to in dialog_login.xml layout.
     */
    public void login(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.dialog_login, null);

        // Set the characteristics of the dialog
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.login, null);
        builder.setNegativeButton(R.string.cancel, null);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Override login button listeners on dialog. This is so the dialog will remain
        // open when credentials are not valid.
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new LoginButton(dialog, this));
    }

    /**
     * Creates a register dialog when the Register button is clicked/tapped.
     * @param view The button that this method is assigned to in dialog_register.xml layout.
     */
    public void register(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.dialog_register, null);

        // Set the characteristics of the dialog
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.create, null);
        builder.setNegativeButton(R.string.cancel, null);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Override register button listeners on dialog. This is so the dialog will remain
        // open when credentials are not valid.
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new RegisterButton(dialog, this));
    }
}