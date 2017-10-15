package edu.csulb.smartroot.gardenview.listeners;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.csulb.smartroot.R;

/**
 * A button listener for Scan. This will scan the WiFi network for gardens and send the MAC address
 * of the garden to the user's account if they want to add it.
 */
public class ScanButton implements Button.OnClickListener {
    private Dialog dialog;
    private WifiManager wifiManager;

    /**
     * Constructor that will pass the reference to the Scan dialog and WiFi manager.
     * @param dialog References the Scan dialog.
     */
    public ScanButton (Dialog dialog) {
        this.dialog = dialog;

        wifiManager = (WifiManager) dialog.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * An implementation of Button.OnClickListener. This will check if the device is connected to
     * a WiFi network, scan the network for reachable IP addresses, and find gardens in the network
     * to add it to the user's account.
     * @param view References the Scan button.
     */
    @Override
    public void onClick(View view) {

        // Check to see if WiFi is enabled
        if (!wifiManager.isWifiEnabled()) {
            Toast toast = Toast.makeText(dialog.getContext(), R.string.scan_wifi_disabled, Toast.LENGTH_SHORT);

            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);

            toast.show();
            return;
        }

        // Check to see if WiFi is connected to a network
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getNetworkId() == -1) {
            Toast toast = Toast.makeText(dialog.getContext(), R.string.scan_wifi_disconnected, Toast.LENGTH_SHORT);

            TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);

            toast.show();
            return;
        }

        dialog.hide();

        // Begin scanning the network for gardens
        Integer ipAddress = wifiInfo.getIpAddress();
        GardenScan gardenScan = new GardenScan(view);
        gardenScan.execute(ipAddress);
    }

    ///////////////////
    // INNER CLASSES //
    ///////////////////

    /**
     * An inner class that will handle scanning the WiFi network for gardens
     */
    private class GardenScan extends AsyncTask<Integer, Void, ArrayList<Map<String, String>>> {
        Dialog dialogProgress;
        View view;
        Resources resources;
        int responseCode;

        /**
         * Constructor that references the button view.
         * @param view References the button view.
         */
        public GardenScan(View view) {
            this.view = view;
            this.resources = view.getContext().getResources();
            this.responseCode = 0;

            // Create progress dialog for WiFi scanner
            dialogProgress = new Dialog(view.getContext());

            // Apply the layout
            View viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_progress, null);

            TextView textView = (TextView) viewDialog.findViewById(R.id.progress_message);
            textView.setText(R.string.progress_scanning);

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
         * An implementation of AsyncTask. This will scan the WiFi network for reachable IP addresses,
         * attempt a handshake with the reachable IP addresses, and send the MAC address of the
         * garden to the server if the user wants to add it.
         * @param args The IP address of the Android device, just as a reference for the WiFi scan.
         * @return An ArrayList that maps the MAC addresses.
         */
        @Override
        protected ArrayList<Map<String, String>> doInBackground(Integer...args) {

            ArrayList<String> reachableIp = new ArrayList<String>();
            ArrayList<Map<String, String>> gardenMacAddress = new ArrayList<Map<String, String>>();

            HttpURLConnection http = null;
            int ip = args[0];

            try {
                // Scan all host addresses for active IP address
                for (int i = 0; i < 256; i++) {
                    StringBuilder sb = new StringBuilder();

                    // Build IP address to ping
                    sb.append(ip & 0xFF);
                    sb.append(".");
                    sb.append((ip >> 8) & 0xFF);
                    sb.append(".");
                    sb.append((ip >> 16) & 0xFF);
                    sb.append(".");
                    sb.append(i);

                    String ipAddress = sb.toString();

                    Log.d("GARDEN SCAN", ipAddress);

                    // Check to see if IP address is reachable
                    InetAddress inetAddress = InetAddress.getByName(ipAddress);

                    // If the IP address is reachable, add it to the ArrayList
                    if (inetAddress.isReachable(resources.getInteger(R.integer.ping_timeout))) {
                        reachableIp.add(inetAddress.getHostAddress());
                        Log.d("GARDEN SCAN", "Reachable: " + reachableIp.toString());
                    }
                    Log.d("GARDEN SCAN", "Going...");
                }

                // Attempt handshake with reachable IP address
                for (String ipAddress : reachableIp) {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder results = new StringBuilder();

                    // Build endpoint for garden
                    sb.append("http://");
                    sb.append(ipAddress);
                    sb.append(":3001/handShake");

                    Log.d("GARDEN SCAN", sb.toString());

                    URL url = new URL(sb.toString());

                    // Create JSON object to send to garden
                    JSONObject data = new JSONObject();
                    data.put("handShake", resources.getString(R.string.handshake_key));

                    // Open a connection to send a POST request to the server
                    http = (HttpURLConnection) url.openConnection();
                    http.setDoInput(true);
                    http.setConnectTimeout(resources.getInteger(R.integer.connection_timeout));
                    http.setReadTimeout(resources.getInteger(R.integer.connection_timeout));
                    http.setRequestProperty("Content-Type", "application/json");
                    http.setRequestMethod("POST");

                    try {
                        // Attempt connection and get server response
                        OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
                        out.write(data.toString());
                        out.flush();

                        responseCode = http.getResponseCode();

                        // If the connection to the server is a success...
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            //... begin to read the server response
                            InputStream in = new BufferedInputStream(http.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String buffer = null;
                            while ((buffer = reader.readLine()) != null) {
                                results.append(buffer);
                            }
                        }
                        // Disconnect from server
                        http.disconnect();

                        // Parse the response of the JSON object
                        JSONObject jsonObject = new JSONObject(results.toString());
                        Log.d("GARDEN SCAN", jsonObject.toString());

                        // If the handshake was a success...
                        if (jsonObject.getBoolean("valid")) {
                            //... add the garden's MAC address into the ArrayList map
                            Log.d("GARDEN SCAN", jsonObject.getString("mac"));
                            gardenMacAddress.add(createGarden("mac", jsonObject.getString("mac")));
                        }
                    } catch (ConnectException e) {
                        Log.d("GARDEN SCAN", "Refused: " + ipAddress);
                        e.printStackTrace();
                    }
                }
            } catch (UnknownHostException e) {
                Log.d("GARDEN SCAN", "Unknown exception");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("GARDEN SCAN", "IO exception");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("GARDEN SCAN", "JSON exception");
                e.printStackTrace();
            } finally {
                if (http != null)
                    http.disconnect();
            }

            return gardenMacAddress;
        }

        /**
         * An implementation of AsyncTask. This will display a list of gardens to add.
         * @param gardenMacAddress An ArrayList map of the garden's MAC address.
         */
        @Override
        protected void onPostExecute(ArrayList<Map<String, String>> gardenMacAddress) {
            dialogProgress.dismiss();

            // If the ArrayList map is empty...
            if (gardenMacAddress.isEmpty()) {
                //... notify the user that no gardens were found on the network
                Toast toast = Toast.makeText(view.getContext(), R.string.scan_no_garden, Toast.LENGTH_SHORT);

                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                textView.setGravity(Gravity.CENTER);

                toast.show();
            } else {
                //... otherwise display a list of gardens for the user to select and add
                Log.d("GARDEN SCAN", gardenMacAddress.toString());
                dialog.dismiss();

                // Create dialog
                Dialog addDialog = new Dialog(view.getContext());
                View viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_garden_add, null);

                // Set the layout
                addDialog.setContentView(viewDialog);

                // Set up the list
                ListView listView = (ListView) viewDialog.findViewById(R.id.list_garden_add);

                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        view.getContext(),
                        gardenMacAddress,
                        android.R.layout.simple_list_item_1,
                        new String[] {"mac"},
                        new int[] {android.R.id.text1});

                listView.setAdapter(simpleAdapter);
                listView.setOnItemClickListener(new GardenSelect(addDialog));

                // Show dialog
                addDialog.show();
            }
        }
    }

    /**
     * A on item click listener for add garden. This will send the garden's MAC address to
     * the server.
     */
    private class GardenSelect implements AdapterView.OnItemClickListener {
        Dialog dialog;

        /**
         * A constructor that references the Add Dialog.
         * @param dialog
         */
        public GardenSelect(Dialog dialog) {
            this.dialog = dialog;
        }

        /**
         * An implementation of OnItemClickListener. This will send a POST request to the server
         * to add the garden to the user's account.
         * @param parentAdapter The adapter of the list.
         * @param view The view of the item clicked.
         * @param position The position of the item clicked.
         * @param id The ID of the item clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
            TextView textView = (TextView) view;

            Log.d("GARDEN SELECT", textView.getText() + " selected.");

            dialog.dismiss();

            // TODO: Send POST request to add garden
        }
    }

    /**
     * A helper method that adds a garden's MAC address into a map.
     * @param key The key of the garden.
     * @param value The MAC address of the garden.
     * @return The garden.
     */
    private HashMap<String, String> createGarden(String key, String value) {
        HashMap<String, String> garden = new HashMap<String, String>();
        garden.put(key, value);

        return garden;
    }
}
