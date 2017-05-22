package edu.csulb.smartroot.welcome.listeners;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        System.out.println("Trying to register...");
        System.out.println(userName + "\n" +
                email + "\n" +
                password + "\n" +
                confirmPassword + "\n");

        dialog.dismiss();
    }
}