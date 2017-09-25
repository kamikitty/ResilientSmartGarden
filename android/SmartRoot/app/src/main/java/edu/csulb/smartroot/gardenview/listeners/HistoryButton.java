package edu.csulb.smartroot.gardenview.listeners;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;

import edu.csulb.smartroot.R;

/**
 * A button listener for View History. This will display the sensor reading history of the garden.
 */
public class HistoryButton implements Button.OnClickListener {

    /**
     * An implementation of Button.OnClickListener. This will retrieve the sensor reading history
     * of the garden and display the results in a dialog.
     * @param view References the View History button.
     */
    @Override
    public void onClick(View view){
        // TODO: Implement retrieval of sensor reading history of garden and display in dialog.
        // Create dialog
        Dialog dialog = new Dialog(view.getContext());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_history, null);

        // Set layout
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }
}