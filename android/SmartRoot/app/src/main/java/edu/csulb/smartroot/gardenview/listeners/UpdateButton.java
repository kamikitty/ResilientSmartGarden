package edu.csulb.smartroot.gardenview.listeners;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import edu.csulb.smartroot.R;
import edu.csulb.smartroot.gardenview.Garden;
import edu.csulb.smartroot.gardenview.GardenHolder;
import edu.csulb.smartroot.gardenview.httprequests.GetSensorReadings;

/**
 * A button listener for Update. This will update the sensor readings of the garden.
 */
public class UpdateButton implements Button.OnClickListener {
    GardenHolder.ViewHolder viewHolder;
    ArrayList<Garden> gardens;
    Context context;

    /**
     * Constructor that passes a reference to ViewHolder, ArrayList of gardens, and the parent context.
     * @param viewHolder The view holder of the garden card.
     * @param gardens An ArrayList of all the user's gardens.
     * @param context The context that will contain the RecyclerView of gardens. In this case,
     *                the activity in GardenView.java.
     */
    public UpdateButton(GardenHolder.ViewHolder viewHolder, ArrayList<Garden> gardens, Context context){
        this.viewHolder = viewHolder;
        this.gardens = gardens;
        this.context = context;
    }

    /**
     * An implementation of Button.OnClickListener. This will retrieve the current
     * sensor readings from the garden.
     * @param view References the Update button.
     */
    @Override
    public void onClick(View view){

        GetSensorReadings getSensorReadings = new GetSensorReadings(view, viewHolder, gardens, context);
        getSensorReadings.execute(context.getString(R.string.sensor_api));
    }
}