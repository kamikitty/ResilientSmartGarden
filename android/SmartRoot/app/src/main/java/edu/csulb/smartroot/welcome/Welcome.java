package edu.csulb.smartroot.welcome;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.welcome.listeners.*;

/**
 * The starting activity. This will prompt the user to either login or register a new account.
 */
public class Welcome extends AppCompatActivity {

    private LayoutInflater inflater;
    boolean isBackPressed;

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
        isBackPressed = false;
    }

    /**
     * A reimplementation of the back button. The Activity will not exit unless the back button
     * is pressed twice with a certain amount of time.
     */
    @Override
    public void onBackPressed() {
        // If the back button has been previously pressed...
        if (isBackPressed) {
            //...exit the activity
            super.onBackPressed();
        } else {
            //...otherwise set the flag for the back button being pressed, display a Toast to the user
            // to press the back button again, and start the timer
            this.isBackPressed = true;

            // Create a toast, center align it, and show
            Toast toast = Toast.makeText(this, R.string.button_back_again, Toast.LENGTH_SHORT);
            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);

            toast.show();

            // Create a new timer that will set the button pressed flag back to false when
            // the time has elapsed
            new Timer().schedule(
                    new backPressTimer(),
                    getResources().getInteger(R.integer.back_button_timeout));
        }
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

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * A TimerTask that will set the back button flag to false
     */
    private class backPressTimer extends TimerTask {
        public void run() {
            isBackPressed = false;
        }
    }
}