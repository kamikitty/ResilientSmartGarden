package edu.csulb.smartroot.welcome;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

        // Setup welcome_logo animation
        View view = findViewById(R.id.logo);
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_logo);

        // Set animation listener to animate login and register button after logo is done
        logoAnimation.setAnimationListener(new ButtonAnimation(
                findViewById(R.id.welcome_login_button),
                findViewById(R.id.welcome_register_button)));


        // Start the animation
        view.startAnimation(logoAnimation);
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
                    new BackPressTimer(),
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
        // Create dialog
        Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.dialog_login, null);

        // Set layout
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        // Setup button listener for cancel and login
        Button button = (Button) dialogView.findViewById(R.id.button_login_cancel);
        button.setOnClickListener(new CancelButton(dialog));

        button = (Button) dialogView.findViewById(R.id.button_login);
        button.setOnClickListener(new LoginButton(dialog));

        // Display the dialog
        dialog.show();
    }

    /**
     * Creates a register dialog when the Register button is clicked/tapped.
     * @param view The button that this method is assigned to in dialog_register.xml layout.
     */
    public void register(View view){
        // Create dialog
        Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.dialog_register, null);

        // Set layout
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        // Setup button listeners for cancel and login
        Button button = (Button) dialogView.findViewById(R.id.button_register_cancel);
        button.setOnClickListener(new CancelButton(dialog));

        button = (Button) dialogView.findViewById(R.id.button_register);
        button.setOnClickListener(new RegisterButton(dialog));

        // Display the dialog
        dialog.show();
    }

    ///////////////
    // LISTENERS //
    ///////////////

    /**
     * A button listener for cancel button. This will dismiss the dialog.
     */
    private class CancelButton implements Button.OnClickListener {
        Dialog dialog;

        /**
         * A constructor that gets a reference to the dialog.
         * @param dialog The referenced dialog.
         */
        public CancelButton(Dialog dialog) {
            this.dialog = dialog;
        }

        /**
         * An implementation of Button.OnClickListener. This will dismiss the dialog.
         * @param view References the cancel button.
         */
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }

    /**
     * An animation listener for the welcome logo. This will start the welcome button animation
     * once the welcome logo is done animating.
     */
    private class ButtonAnimation implements Animation.AnimationListener {
        View login;
        View register;

        /**
         * A constructor that references the login and register button
         * @param login References the login button
         * @param register References the register button
         */
        public ButtonAnimation (View login, View register) {
            this.login = login;
            this.register = register;
        }

        /**
         * An implementation of Animation.AnimationListener. This will begin the login and register
         * button animations once the welcome animation is done.
         * @param animation References the animation used for the welcome lgo
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_button);

            // Make the login and register button visible
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);

            // Start the login and register button animation
            login.startAnimation(anim);
            register.startAnimation(anim);
        }

        /**
         * An implementation of Animation.AnimationListener. This will not be implemented for
         * this project.
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {}

        /**
         * An implementation of Animation.AnimationListener. This will not be implemented for
         * this project.
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {}
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * A TimerTask that will set the back button flag to false
     */
    private class BackPressTimer extends TimerTask {
        public void run() {
            isBackPressed = false;
        }
    }
}