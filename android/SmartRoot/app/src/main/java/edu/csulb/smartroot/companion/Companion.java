package edu.csulb.smartroot.companion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.companion.httprequest.GetCompanion;

public class Companion extends AppCompatActivity {
    private AutoCompleteTextView firstPlant;
    private AutoCompleteTextView secondPlant;

    private ArrayList<String> plants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        plants = getIntent().getExtras().getStringArrayList("plants");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                plants.toArray(new String[plants.size()]));

        // Initialize autocomplete text views
        firstPlant = (AutoCompleteTextView) findViewById(R.id.autoPlantFirst);
        firstPlant.setAdapter(adapter);

        secondPlant = (AutoCompleteTextView) findViewById(R.id.autoPlantSecond);
        secondPlant.setAdapter(adapter);
    }

    public void checkCompatibility(View view) {
        String firstPlantName = firstPlant.getText().toString();
        String secondPlantName = secondPlant.getText().toString();

        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setGravity(Gravity.CENTER);

        if (!plants.contains(firstPlantName)) {
            toast.setText(getResources().getString(R.string.plant_error, 1));
            toast.show();

            return;
        }


        if (!plants.contains(secondPlantName)) {
            toast.setText(getResources().getString(R.string.plant_error, 2));
            toast.show();

            return;
        }

        GetCompanion getCompanion = new GetCompanion(view, this.getApplicationContext());
        getCompanion.execute("http://192.168.12.243:3001/get_enemy", firstPlantName, secondPlantName);
    }
}
