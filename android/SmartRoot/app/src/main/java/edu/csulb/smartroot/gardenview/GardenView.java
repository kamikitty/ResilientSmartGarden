package edu.csulb.smartroot.gardenview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import edu.csulb.smartroot.R;
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

    /**
     * A button listener that is referenced in menu_actionbar.xml. This will attempt to logout the
     * user.
     * @param menuItem References the Logout button the action bar.
     */
    public void logout(MenuItem menuItem) {
        Toast.makeText(
                this, getString(R.string.logout, userName), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }

    /**
     * Reimplement the back button method. This will disable the back button, preventing the user
     * from going back to the Welcome activity.
     */
    @Override
    public void onBackPressed() {
    }
}
