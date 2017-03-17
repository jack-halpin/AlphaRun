package com.alpharun.jack.alpharun.Activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.alpharun.jack.alpharun.Database.RunDbHelper;
import com.alpharun.jack.alpharun.Database.RunTrackerContract;
import com.alpharun.jack.alpharun.R;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class RunSummaryActivity extends AppCompatActivity {

    protected TextView distanceSummary;
    protected TextView timeSummary;

    private int runDistance;
    private SQLiteDatabase db;

    private int runId;
    private long startTime;
    private long endTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_summary);
        Stetho.initializeWithDefaults(this);
        //Get the information passed from the RunActivity intent
        Bundle extras = getIntent().getExtras();

        //Assign UI elements to variables
        distanceSummary = (TextView) findViewById(R.id.summary_distance_text);
        timeSummary = (TextView) findViewById(R.id.summary_time_text);
        runDistance = extras.getInt("RUN_DISTANCE");
        startTime = extras.getLong("START_TIME");
        endTime = extras.getLong("END_TIME");


        ArrayList<Location> l = (ArrayList<Location>) extras.getSerializable("COORD_LIST");

        distanceSummary.setText("Distance: " + runDistance);
        timeSummary.setText(String.valueOf(l.get(0).getLatitude()));


        RunDbHelper runDbHelper = new RunDbHelper(this);

        db = runDbHelper.getWritableDatabase();
        DatabaseSynchronize dbSync = new DatabaseSynchronize();
        dbSync.execute(l);

    }

    public class DatabaseSynchronize extends AsyncTask<ArrayList<Location>, Void, Void>{

        @Override
        protected void onPreExecute(){
            //Can assume that when the run is done there will be some stuff entered into the database so we have to put all the stuff into the database
//            ProgressDialog dialog = new ProgressDialog(RunSummaryActivity.this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.setMessage("Loading. Please wait...");
//            dialog.setIndeterminate(true);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();

            //Get the id of the current run
            String[] projection = { RunTrackerContract.RunEntry._ID };

            Cursor cursor = db.query(
                    RunTrackerContract.RunEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            //Get the id of the current run. This will be equal to the number of current runs in the database + 1
            runId = cursor.getCount() + 1;

            Log.e("ID of last run: ", Long.toString(runId));
        }

        @Override
        protected Void doInBackground(ArrayList<Location>... params){
            //For each location in the ArrayList passed into the database insert it into the database
            ArrayList<Location> locations = params[0];

            //Insert the summary for the current run
            ContentValues values = new ContentValues();
            values.put(RunTrackerContract.RunEntry.DISTANCE_COLUMN, runDistance);
            values.put(RunTrackerContract.RunEntry.TIME_COLUMN, endTime - startTime);
            db.insert(RunTrackerContract.RunEntry.TABLE_NAME, null, values);

            for(Location loc : locations){
                values = new ContentValues();
                values.put(RunTrackerContract.LocationEntry.LATITUDE_COLUMN, loc.getLatitude());
                values.put(RunTrackerContract.LocationEntry.LONGTITUDE_COLUMN, loc.getLongitude());
                values.put(RunTrackerContract.LocationEntry.TIMESTAMP_SEC, loc.getTime()/1000);
                values.put(RunTrackerContract.LocationEntry.RUN_ID_COLUMN, runId);
                db.insert(RunTrackerContract.LocationEntry.TABLE_NAME, null, values);
            }


            return null;
        }


        protected void onPostExecute(){
            //Potentially update the UI here
            //Close the connection to the database
            db.close();

        }
    }
}
