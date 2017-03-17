package com.alpharun.jack.alpharun.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alpharun.jack.alpharun.R;

import java.util.List;

/**
 * Created by Jack on 16/03/2017.
 */

public class RunSummaryAdapter extends ArrayAdapter<RunningEntry> {
    private int layoutResource;

    //Constructor
    public RunSummaryAdapter(Context context, int layoutResource, List<RunningEntry> listOfRuns){
        super(context, layoutResource, listOfRuns);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        //If the view is null then inflate it
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        //For each item in the listOfEvents object set the content of the view
        RunningEntry run = getItem(position);

        if (run != null) {
            //Get the various views from the layout

            TextView runDistance = (TextView) view.findViewById(R.id.run_summary_distance);
            TextView runPace = (TextView) view.findViewById(R.id.run_summary_pace);
            TextView runTime = (TextView) view.findViewById(R.id.run_summary_time);
            TextView runTemp = (TextView) view.findViewById(R.id.run_summary_temp);


            //Populate them based on information stored in the currevent EventListing object
            runDistance.setText(Double.toString(run.getDistance()) + "Km");

            runPace.setText(Double.toString(run.getRunPace()) + "Km/s");

            runTime.setText(run.getRunTime());

            runTemp.setText(run.getTemp());
        }

        return view;
    }
}
