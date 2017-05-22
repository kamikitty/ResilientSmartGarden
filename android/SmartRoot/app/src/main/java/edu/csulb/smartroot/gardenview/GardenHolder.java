package edu.csulb.smartroot.gardenview;

import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.listeners.*;

/**
 * An adapter that will generate a card for each garden. These cards will be displayed in a list
 * in GardenView.java.
 */
public class GardenHolder extends RecyclerView.Adapter<GardenHolder.ViewHolder> {

    private ArrayList<Garden> gardens;
    private ViewGroup viewGroup;
    private AlertDialog dialog;
    private Context context;

    /**
     * Constructor that will get a reference to an ArrayList of gardens to create each individual
     * card.
     * @param gardens An ArrayList of gardens.
     */
    public GardenHolder(ArrayList<Garden> gardens) {

        this.gardens = gardens;
        this.viewGroup = null;
        this.dialog = null;

        // TODO: Get gardens from database to populate list
        // Adding gardens for testing purposes
        for (int i = 0; i < 10; i ++){
            gardens.add(new Garden(i + 1));
        }
    }

    /**
     * An implementation of RecyclerView.Adapter. It will create the card view for each individual
     * garden using card_garden.xml layout.
     * @param parent The ViewGroup which the card view will be added to. In this case, it will be
     *               added to the activity in GardenView.java.
     * @param viewType The view type of the generated card.
     * @return A ViewHolder containing the garden card view.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewGroup = parent;
        context = viewGroup.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.card_garden, viewGroup, false);

         return new ViewHolder(v);
    }

    /**
     * An implementation of RecyclerView.Adapter. This will update the garden card that is visible
     * in the RecyclerView.
     * @param holder The ViewHolder to set up the Views it contains. In this case, a garden card view.
     * @param position The position of the ViewHolder in the adapter.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(
                gardens.get(position).getGardenName());
        holder.temperature.setText(
                context.getString(R.string.label_temperature, gardens.get(position).getTemperature()));
        holder.moisture.setText(
                context.getString(R.string.label_moisture, gardens.get(position).getMoisture()));
        holder.humidity.setText(
                context.getString(R.string.label_humidity, gardens.get(position).getHumidity()));
        holder.lastUpdated.setText(
                context.getString(R.string.label_updated, gardens.get(position).getLastUpdated()));
    }

    /**
     * An implementation of RecyclerView.Adapter. It will return the amount of garden card views
     * in the adapter.
     * @return The amount of garden card views in the adapter.
     */
    @Override
    public int getItemCount() {
        return gardens.size();
    }

    ///////////////
    // LISTENERS //
    ///////////////

    /**
     * A button listener for Done in the Push Notification Settings dialog. This will configure
     * the push notification settings according to the user's preference.
     */
    private class DoneButton implements Button.OnClickListener {

        /**
         * An implementation of Button.OnClickListener. This will get the push notification settings
         * specified by the user in the the Push Notification Settings Dialog.
         * @param view References the Done button in the Push Notification Settings Dialog.
         */
        @Override
        public void onClick(View view) {
            // TODO: Process the push notification settings
            dialog.dismiss();
        }
    }

    /**
     * A switch listener for Push Notification in the Push Notification Settings dialog. This will
     * enabled and disable the Push Notification Settings.
     */
    private class PushSwitch implements CompoundButton.OnCheckedChangeListener {
        /**
         * An implementation of CompoundButton.OnCheckedChangeListener. This will update the state
         * of all the Push Notification Settings.
         * @param buttonView References to the Push Notification Switch.
         * @param isChecked The current state of the switch when pressed.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            System.out.println("Switch has changed");

            if (!isChecked) {
                // Set the state for temperature warning
                setState(false,
                        (Switch) dialog.findViewById(R.id.switch_temperature_warning),
                        (EditText) dialog.findViewById(R.id.temperature_min),
                        (EditText) dialog.findViewById(R.id.temperature_max));

                // Set the state for moisture warning
                setState(
                        false,
                        (Switch) dialog.findViewById(R.id.switch_moisture_warning),
                        (EditText) dialog.findViewById(R.id.moisture_min),
                        (EditText) dialog.findViewById(R.id.moisture_max));

                // Set the state for humidity
                setState(false,
                        (Switch) dialog.findViewById(R.id.switch_humidity_warning),
                        (EditText) dialog.findViewById(R.id.humidity_min),
                        (EditText) dialog.findViewById(R.id.humidity_max));
            } else {
                // Set the state for temperature warning
                setState(true,
                        (Switch) dialog.findViewById(R.id.switch_temperature_warning),
                        (EditText) dialog.findViewById(R.id.temperature_min),
                        (EditText) dialog.findViewById(R.id.temperature_max));

                // Set the state for moisture warning
                setState(
                        true,
                        (Switch) dialog.findViewById(R.id.switch_moisture_warning),
                        (EditText) dialog.findViewById(R.id.moisture_min),
                        (EditText) dialog.findViewById(R.id.moisture_max));

                // Set the state for humidity
                setState(true,
                        (Switch) dialog.findViewById(R.id.switch_humidity_warning),
                        (EditText) dialog.findViewById(R.id.humidity_min),
                        (EditText) dialog.findViewById(R.id.humidity_max));
            }
        }

        /**
         * A helper method that will assign the checked and enabled state of a push notification
         * settings in the dialog.
         * @param state The state of the setting.
         * @param rSwitch The Switch of the setting.
         * @param min The minimum limit of the setting.
         * @param max The maximum limit of the setting.
         */
        public void setState(boolean state, Switch rSwitch, EditText min, EditText max) {
            rSwitch.setChecked(false);
            rSwitch.setEnabled(state);

            min.setText("");
            min.setEnabled(state);

            max.setText("");
            max.setEnabled(state);
        }
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that references the card view. It inherits from ViewHolder and is used with
     * RecyclerView.Adapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements Toolbar.OnMenuItemClickListener {

        public TextView name;
        public TextView temperature;
        public TextView moisture;
        public TextView humidity;
        public TextView lastUpdated;

        private Button update;
        private Button history;
        private Button water;
        private Button setup;

        /**
         * Constructor that will initialize the garden card. It will set up all of the TextView,
         * Buttons, and Overflow Menu.
         * @param v The garden card view.
         */
        public ViewHolder(View v){
            super(v);

            // Initialize Overflow Menu
            Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.menu_overflow_garden);
            toolbar.setOnMenuItemClickListener(this);

            // Initialize TextView
            name = (TextView) v.findViewById(R.id.name);
            temperature = (TextView) v.findViewById(R.id.temperature);
            moisture = (TextView) v.findViewById(R.id.moisture);
            humidity = (TextView) v.findViewById(R.id.humidity);
            lastUpdated = (TextView) v.findViewById(R.id.updated);

            // Initialize buttons
            update = (Button) v.findViewById(R.id.button_update);
            update.setOnClickListener(new UpdateButton(this, gardens, context));

            history = (Button) v.findViewById(R.id.button_history);
            history.setOnClickListener(new HistoryButton());

            water = (Button) v.findViewById(R.id.button_water);
            water.setOnClickListener(new WaterButton());

            setup = (Button) v.findViewById(R.id.button_setup);
            setup.setOnClickListener(new SetupButton());
        }

        /**
         * Implementation of MenuItem.OnMenuItemClickListener. It will process the user's menu selections
         * and the appropriate actions.
         * @param menuItem The menu item that was selected by the user.
         * @return True, to consume the current click and prevent others from executing.
         */
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (menuItem.getItemId() == R.id.menu_push) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());

                final View dialogView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.dialog_push_notification, viewGroup, false);

                builder.setView(dialogView);
                builder.setPositiveButton(R.string.button_done, null);

                dialog = builder.create();
                dialog.show();

                Switch pushNotification = (Switch) dialogView.findViewById(R.id.switch_push_notification);
                pushNotification.setOnCheckedChangeListener(new PushSwitch());

                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new DoneButton());

                System.out.println("Push Notification");
            }
            if(menuItem.getItemId() == R.id.menu_shutdown) {
                // TODO: Implement sending shut down message garden.
                System.out.println("Shut down");
            }
            return true;
        }
    }
}
