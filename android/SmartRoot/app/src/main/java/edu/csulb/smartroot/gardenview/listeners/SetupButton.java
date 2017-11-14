package edu.csulb.smartroot.gardenview.listeners;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.csulb.smartroot.R;

/**
 * A button listener for Setup. This will display the automatic watering settings.
 */
public class SetupButton implements Button.OnClickListener {

    /**
     * An implementation of Button.OnClickListener. This will display the automatic watering
     * settings in a dialog.
     * @param view References the Setup button.
     */
    @Override
    public void onClick(View view) {

        // Create dialog
        Dialog dialog = new Dialog(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_setup_water, null);

        // Set layout
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        // Setup button listeners for cancel and done
        Button button = (Button) dialogView.findViewById(R.id.button_water_cancel);
        button.setOnClickListener(new CancelButton(dialog));

        button = (Button) dialogView.findViewById(R.id.button_done);
        button.setOnClickListener(new DoneButton(dialog));

        // display the dialog
        dialog.show();
    }

    /**
     * A button listener for Cancel. This will dismiss the dialog.
     */
    public static class CancelButton implements Button.OnClickListener {
        Dialog dialog;

        /**
         * A constructor that references the Automatic Watering Setup dialog.
         * @param dialog
         */
        public CancelButton(Dialog dialog) {
            this.dialog = dialog;
        }

        /**
         * An implementation of Button.OnClickListener. This will dismiss the Automatic Watering
         * Setup dialog.
         * @param view References the Cancel button.
         */
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }

    /**
     * A button listener for Done. This will get the values from the user.
     */
    public static class DoneButton implements Button.OnClickListener {
        Dialog dialog;

        /**
         * A constructor that references the Automatic Watering Setup dialog.
         * @param dialog References the Automatic Watering Setup dialog.
         */
        public DoneButton(Dialog dialog) {
            this.dialog = dialog;
        }

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