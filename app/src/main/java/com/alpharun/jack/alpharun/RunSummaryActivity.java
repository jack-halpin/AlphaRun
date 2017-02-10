package com.alpharun.jack.alpharun;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class RunSummaryActivity extends AppCompatActivity {

    protected TextView distanceSummary;
    protected TextView timeSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_summary);

        //Get the information passed from the RunActivity intent
        Bundle extras = getIntent().getExtras();

        distanceSummary = (TextView) findViewById(R.id.summary_distance_text);
        timeSummary = (TextView) findViewById(R.id.summary_time_text);
        int runDistance = extras.getInt("RUN_DISTANCE");
        ArrayList<Location> l = (ArrayList<Location>) extras.getSerializable("COORD LIST");
        distanceSummary.setText("Distance: " + runDistance);
        timeSummary.setText(String.valueOf(l.get(0).getLatitude()));
    }
}
