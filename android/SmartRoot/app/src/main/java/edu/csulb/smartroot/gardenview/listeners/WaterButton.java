package edu.csulb.smartroot.gardenview.listeners;

import android.view.View;
import android.widget.Button;

/**
 * A button listener for Water Now. This will water the garden.
 */
public class WaterButton implements Button.OnClickListener {
    /**
     * An implementation of Button.OnClickListener. This will send a message to the garden
     * to begin watering.
     * @param view References the Water Now button.
     */
    @Override
    public void onClick(View view){
        // TODO: Implement sending message to garden to start watering.
        System.out.println("Water Now button pressed.");
    }
}