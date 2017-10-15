package edu.csulb.smartroot.gardenview;

import android.app.Dialog;
import android.content.Context;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
    private Context context;

    private String userName;

    /**
     * Constructor that will send a POST request to the server to get user's garden, store it in
     * an ArrayList of gardens, and create each individual card.
     */
    public GardenHolder(String userName, Context context) {
        gardens = new ArrayList<Garden>();
        this.context = context;

        this.userName = userName;
        this.viewGroup = null;

        this.userName = userName;

        // Send a POST request to retrieve user gardens
        GetGardens getGardens = new GetGardens(this, gardens);
        getGardens.execute(context.getString(R.string.garden_retrieve_api));
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
        Dialog dialog;

        /**
         * A constructor that references Push Notification Settings dialog.
         * @param dialog References the Push Notification Setting dialog.
         */
        public DoneButton (Dialog dialog) {
            this.dialog = dialog;
        }

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
     * A button listener for Cancel in the Push Notification Settings dialog.This will dismiss the
     * push notification dialog.
     */
    private class CancelButton implements Button.OnClickListener {
        Dialog dialog;

        /**
         * A constructor that references Push Notification Settings dialog.
         * @param dialog References the Push Notification Settings dialog.
         */
        public CancelButton (Dialog dialog) {
            this.dialog = dialog;
        }

        /**
         * An implementation of Button.OnClickListener. This will dismiss the push notification
         * dialog.
         * @param view References the Cancel button in Push Notification Settings Dialog.
         */
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }

    /**
     * A switch listener for Push Notification in the Push Notification Settings dialog. This will
     * enabled and disable the Push Notification Settings.
     */
    private class PushSwitch implements CompoundButton.OnCheckedChangeListener {
        private View dialogView;

        /**
         * A constructor that references the Dialog View of Push Notification.
         * @param dialogView References Push Notification dialog.
         */
        public PushSwitch(View dialogView) {
            this.dialogView = dialogView;
        }

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
                        (Switch) dialogView.findViewById(R.id.switch_temperature_warning),
                        (EditText) dialogView.findViewById(R.id.temperature_min),
                        (EditText) dialogView.findViewById(R.id.temperature_max));

                // Set the state for moisture warning
                setState(
                        false,
                        (Switch) dialogView.findViewById(R.id.switch_moisture_warning),
                        (EditText) dialogView.findViewById(R.id.moisture_min),
                        (EditText) dialogView.findViewById(R.id.moisture_max));

                // Set the state for humidity
                setState(false,
                        (Switch) dialogView.findViewById(R.id.switch_humidity_warning),
                        (EditText) dialogView.findViewById(R.id.humidity_min),
                        (EditText) dialogView.findViewById(R.id.humidity_max));
            } else {
                // Set the state for temperature warning
                setState(true,
                        (Switch) dialogView.findViewById(R.id.switch_temperature_warning),
                        (EditText) dialogView.findViewById(R.id.temperature_min),
                        (EditText) dialogView.findViewById(R.id.temperature_max));

                // Set the state for moisture warning
                setState(
                        true,
                        (Switch) dialogView.findViewById(R.id.switch_moisture_warning),
                        (EditText) dialogView.findViewById(R.id.moisture_min),
                        (EditText) dialogView.findViewById(R.id.moisture_max));

                // Set the state for humidity
                setState(true,
                        (Switch) dialogView.findViewById(R.id.switch_humidity_warning),
                        (EditText) dialogView.findViewById(R.id.humidity_min),
                        (EditText) dialogView.findViewById(R.id.humidity_max));
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
            Button button = (Button) v.findViewById(R.id.button_update);
            button.setOnClickListener(new UpdateButton(this, gardens, context));

            button = (Button) v.findViewById(R.id.button_history);
            button.setOnClickListener(new HistoryButton());

            button = (Button) v.findViewById(R.id.button_water);
            button.setOnClickListener(new WaterButton());

            button = (Button) v.findViewById(R.id.button_setup);
            button.setOnClickListener(new SetupButton());
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
                // Create dialog
                Dialog dialog = new Dialog(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_push_notification, null);

                // Set layout
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);

                // Setup button listeners for cancel and done
                Button button = (Button) dialogView.findViewById(R.id.button_push_cancel);
                button.setOnClickListener(new CancelButton(dialog));

                button = (Button) dialogView.findViewById(R.id.button_done);
                button.setOnClickListener(new DoneButton(dialog));

                // Setup switch listener for push notification settings
                Switch pushNotification = (Switch) dialogView.findViewById(R.id.switch_push_notification);
                pushNotification.setOnCheckedChangeListener(new PushSwitch(dialogView));

                // Display dialog
                dialog.show();
            }
            if(menuItem.getItemId() == R.id.menu_shutdown) {
                // TODO: Implement sending shut down message garden.
                System.out.println("Shut down");
            }
            return true;
        }
    }

    private class GetGardens extends AsyncTask<String, Void, JSONObject> {

        GardenHolder holder;
        ArrayList<Garden> gardens;
        Dialog dialogProgress;

        Resources resources;
        int responseCode;

        public GetGardens(GardenHolder holder, ArrayList<Garden> gardens) {
            this.holder = holder;
            this.gardens = gardens;

            this.resources = context.getApplicationContext().getResources();
            this.responseCode = 0;

            // Create dialog for server connection
            dialogProgress = new Dialog(context);

            // Apply the layout
            View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);

            TextView textView = (TextView) viewDialog.findViewById(R.id.progress_message);
            textView.setText(R.string.progress_garden);

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
         * An implementation of AsyncTask. This will get the user's garden from the server in a
         * separate thread.
         * @param args The API address to send a POST request.
         * @return A JSONObject containing the user's garden.
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

                // Open a connection to send a POST request to the server
                http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                http.setConnectTimeout(resources.getInteger(R.integer.connection_timeout));
                http.setReadTimeout(resources.getInteger(R.integer.connection_timeout));
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestMethod("POST");

                // Insert data for POST request
                StringBuilder sb = new StringBuilder();

                sb.append(data.toString());

                Log.d("GARDEN RETRIEVE", "Data: " + sb.toString());

                OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                out.write(sb.toString());
                out.flush();

                // Attempt connection and get server response code
                responseCode = http.getResponseCode();

                // If the connection to the server is a success...
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //... begin to read the server response
                    InputStream in = new BufferedInputStream((http.getInputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String buffer = null;
                    while ((buffer = reader.readLine()) != null) {
                        result.append(buffer);
                    }
                }
            } catch(MalformedURLException e) {
                Log.d("GARDEN RETRIEVE", "URL not in correct format");
                e.printStackTrace();
                return null;
            } catch (ConnectException e) {
                responseCode = HttpURLConnection.HTTP_NOT_FOUND;
                e.printStackTrace();
            } catch(IOException e) {
                Log.d("GARDEN RETRIEVE", "IO exception");
                e.printStackTrace();
            }
            catch (JSONException e) {
                Log.d("GARDEN RETRIEVE", "JSON exception");
                e.printStackTrace();
            } finally {
                // Disconnect from the server
                if (http != null)
                    http.disconnect();
            }

            Log.d("GARDEN RETRIEVE", "Response: " + responseCode);
            Log.d("GARDEN RETRIEVE", "Results: " + result);

            // Convert the response from the server into a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        /**
         * An implementation of AsyncTask. This will retrieve the user's garden and pass it
         * to the garden ArrayList.
         *
         * @param jsonObject JSON object containing the user's garden.
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dialogProgress.dismiss();

            Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);

            // Process response code and execute appropriate action
            switch(responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // If the server response is valid...
                    if (jsonObject != null) {
                        try {
                            // Get user's garden from JSONObject
                            JSONArray jsonGardens = jsonObject.getJSONArray("gardens");

                            // Separate the JSONObject gardens into arrays
                            for (int i = 0; i < jsonGardens.length(); i++) {
                                JSONObject garden = jsonGardens.getJSONObject(i);
                                gardens.add(new Garden(
                                        garden.getString("name"),
                                        garden.getString("mac")));
                            }

                            // Update recycler view
                            holder.notifyDataSetChanged();

                        } catch (JSONException e) {
                            toast.setText(R.string.garden_invalid_data);
                            toast.show();

                            e.printStackTrace();
                        }
                    }

                    break;

                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Notify user server cannot be found
                    toast.setText(R.string.garden_404);
                    toast.show();

                    break;

                case HttpURLConnection.HTTP_FORBIDDEN:
                    // Notify user the server is forbidden
                    toast.setText(R.string.garden_403);
                    toast.show();

                    break;

                default:
                    // Notify user an unexpected server response was received
                    toast.setText(R.string.garden_unknown);
                    toast.show();

                    break;
            }
        }
    }
}
