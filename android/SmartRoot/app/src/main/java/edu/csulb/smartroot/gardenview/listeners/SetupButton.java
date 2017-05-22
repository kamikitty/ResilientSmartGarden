package edu.csulb.smartroot.gardenview.listeners;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.csulb.smartroot.R;

/**
 * A button listener for Setup. This will display the automatic watering settings.
 */
public class SetupButton implements Button.OnClickListener {

    AlertDialog dialog;

    /**
     * An implementation of Button.OnClickListener. This will display the automatic watering
     * settings in a dialog.
     * @param view References the Setup button.
     */
    @Override
    public void onClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        LayoutInflater inflater = LayoutInflater.from(view.getRootView().getContext());
        View dialogView = inflater.inflate(R.layout.dialog_setup_water, null);

        builder.setView(dialogView);
        builder.setPositiveButton(R.string.button_done, null);

        dialog = builder.create();
        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new DoneButton());

        // TODO: Implement dialog with automatic watering settings.
        System.out.println("Setup button pressed.");
    }

    /**
     * A button listener for Done. This will get the values from the user.
     */
    private class DoneButton implements Button.OnClickListener {
        /**
         * An implementation of Button.OnClickListener. This will get the moisture limit the user
         * entered in the Automatic Watering Setup dialog and dismiss the dialog.
         * @param view References the Done button.
         */
        @Override
        public void onClick(View view){
            EditText eMoistureLimit = (EditText) view.getRootView().findViewById(R.id.moisture_limit);

            String moistureLimit = eMoistureLimit.getText().toString();

            // TODO: Implement setting the moisture limit on the garden.
            // Also set validation range from 0 to 100.
            System.out.println(moistureLimit);
            dialog.dismiss();
        }
    }
}