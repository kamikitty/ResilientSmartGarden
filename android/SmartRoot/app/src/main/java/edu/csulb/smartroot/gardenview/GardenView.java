package edu.csulb.smartroot.gardenview;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.listeners.ScanButton;
import edu.csulb.smartroot.welcome.Welcome;

/**
 * An activity that will display a list of gardens as cards.
 */
public class GardenView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private String userName;

    /**
     * Gets a reference to the RecyclerView and Adapter upon creation of this activity.
     * @param savedInstanceState The state of this activity when previously launched.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gardenview);

        // Set the title as the user name
        userName = getIntent().getExtras().getString("username");
        setTitle(userName);

        // Get reference to recycler view and set layout
        recyclerView = (RecyclerView) findViewById(R.id.garden_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter and set it to recycler view
        adapter = new GardenHolder(new ArrayList<Garden>());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Creates the action bar menu using menu_actionbar.xml layout.
     * @param menu The menu to create the action bar.
     * @return True when the action bar menu is created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    /////////////////////
    // ONCLICK METHODS //
    /////////////////////

    /**
     * A button listener that is referenced in menu_actionbar.xml. This will attempt to logout the
     * user.
     * @param menuItem References the Logout button the action bar.
     */
    public void logout(MenuItem menuItem) {
        Toast toast = Toast.makeText(this, getString(R.string.logout, userName), Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        toast.show();

        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }

    /**
     * A floating action button that is referenced in activity_gardenview.xml. This will
     * first scan the WiFi network for a garden, then display the gardens in a list for the user
     * to decide which to add.
     * @param view References the floating action button.
     */
    public void addGarden(View view) {
        // Create dialog
        Dialog dialog = new Dialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_garden_scan, null);

        // Set layout
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

        // Setup button listener for cancel and scan
        Button button = (Button) dialogView.findViewById(R.id.button_scan_cancel);
        button.setOnClickListener(new CancelButton(dialog));

        button = (Button) dialogView.findViewById(R.id.button_scan);
        button.setOnClickListener(new ScanButton(dialog));

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
         * An implementation of button.OnClickListener. This will dismiss the dialog.
         * @param view References the cancel button.
         */
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }

    /**
     * Reimplement the back button method. This will disable the back button, preventing the user
     * from going back to the Welcome activity.
     */
    @Override
    public void onBackPressed() {
    }
}
