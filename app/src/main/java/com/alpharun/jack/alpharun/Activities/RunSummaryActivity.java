package com.alpharun.jack.alpharun.Activities;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alpharun.jack.alpharun.R;

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

        //Assign UI elements to variables
        distanceSummary = (TextView) findViewById(R.id.summary_distance_text);
        timeSummary = (TextView) findViewById(R.id.summary_time_text);
        int runDistance = extras.getInt("RUN_DISTANCE");


        ArrayList<Location> l = (ArrayList<Location>) extras.getSerializable("COORD LIST");
        distanceSummary.setText("Distance: " + runDistance);
        timeSummary.setText(String.valueOf(l.get(0).getLatitude()));

    }

    public class DatabaseSynchronize extends AsyncTask<ArrayList<Location>, Void, Void>{

        @Override
        protected void onPreExecute(){
            //Can assume that when the run is done there will be some stuff entered into the database so we have to put all the stuff into the database
            ProgressDialog dialog = new ProgressDialog(RunSummaryActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(ArrayList<Location>... locations){
            //For each location in the ArrayList passed into the database insert it into the database




            return null;
        }


        protected void onPostExecute(){
            //Potentially update the UI here
            //Close the connection to the database


        }
    }
}
