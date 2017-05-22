package edu.csulb.smartroot.gardenview.listeners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;

import edu.csulb.smartroot.R;

/**
 * A button listener for View History. This will display the sensor reading history of the garden.
 */
public class HistoryButton implements Button.OnClickListener {

    AlertDialog dialog;
    /**
     * An implementation of Button.OnClickListener. This will retrieve the sensor reading history
     * of the garden and display the results in a dialog.
     * @param view References the View History button.
     */
    @Override
    public void onClick(View view){
        // TODO: Implement retrieval of sensor reading history of garden and display in dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        LayoutInflater inflater = LayoutInflater.from(view.getRootView().getContext());
        View dialogView = inflater.inflate(R.layout.dialog_history, null);

        builder.setView(dialogView);
        builder.setPositiveButton(R.string.button_done, null);

        dialog = builder.create();
        dialog.show();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new DoneButton());
        System.out.println("View History button pressed.");
    }

    private class DoneButton implements Button.OnClickListener {
        @Override
        public void onClick(View view){
            dialog.dismiss();
        }
    }
}