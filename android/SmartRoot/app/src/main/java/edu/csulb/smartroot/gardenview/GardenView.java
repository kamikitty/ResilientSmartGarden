package edu.csulb.smartroot.gardenview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.httprequests.GetGardens;
import edu.csulb.smartroot.gardenview.listeners.ScanButton;
import edu.csulb.smartroot.welcome.Welcome;

/**
 * An activity that will display a list of gardens as cards.
 */
public class GardenView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private GardenHolder holder;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        holder = new GardenHolder(userName, this, findViewById(R.id.fab_add_garden), swipeRefreshLayout);
        adapter = holder;
        recyclerView.setAdapter(adapter);

        // Set up refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.garden_refresh);
        swipeRefreshLayout.setOnRefreshListener(new GardenRefresh(this));
    }

    /**
     * An implementation of the action bar menu. This will initialize the menu.
     * @param menu The menu to initialize.
     * @return
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
        button.setOnClickListener(new ScanButton(dialog, holder, userName, this));

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
     * A SwipeRefreshLayout listener. This will refresh the user's gardens.
     */
    private class GardenRefresh implements SwipeRefreshLayout.OnRefreshListener {
        private Context context;

        /**
         * Constructor that references the context.
         * @param context Context to reference.
         */
        public GardenRefresh(Context context) {
            this.context = context;
        }

        /**
         * An implementation of SwipeRefreshLayout.OnRefreshListener. This will get the user's
         * gardens.
         */
        @Override
        public void onRefresh() {
            new GetGardens(holder, userName, context).execute(getString(R.string.garden_retrieve_api));
            swipeRefreshLayout.setRefreshing(false);
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
