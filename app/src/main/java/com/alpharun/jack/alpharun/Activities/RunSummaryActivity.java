package com.alpharun.jack.alpharun.Activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    protected SQLiteDatabase db;
    protected long runId;
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
        int runDistance = extras.getInt("RUN_DISTANCE");


        ArrayList<Location> l = (ArrayList<Location>) extras.getSerializable("COORD LIST");

        distanceSummary.setText("Distance: " + runDistance);
        timeSummary.setText(String.valueOf(l.get(0).getLatitude()));


        RunDbHelper runDbHelper = new RunDbHelper(this);

        db = runDbHelper.getWritableDatabase();
        runDbHelper.onCreate(db);


        //Quick test for the databse
        ContentValues values = new ContentValues();
        values.put(RunTrackerContract.RunEntry.DISTANCE_COLUMN, 300);
        values.put(RunTrackerContract.RunEntry.TIME_COLUMN, 39);
        long newRowId = db.insert(RunTrackerContract.RunEntry.TABLE_NAME, null, values);

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

            //Move the cursor to the last item
            cursor.moveToLast();
            runId = cursor.getLong(cursor.getColumnIndexOrThrow(RunTrackerContract.RunEntry._ID));
            Log.e("ID of last run: ", Long.toString(runId));
        }

        @Override
        protected Void doInBackground(ArrayList<Location>... params){
            //For each location in the ArrayList passed into the database insert it into the database
            ArrayList<Location> locations = params[0];

            for(Location loc : locations){

                ContentValues values = new ContentValues();
                values.put(RunTrackerContract.LocationEntry.LATITUDE_COLUMN, loc.getLatitude());
                values.put(RunTrackerContract.LocationEntry.LONGTITUDE_COLUMN, loc.getLongitude());
                values.put(RunTrackerContract.LocationEntry.RUN_ID_COLUMN, runId);
                db.insert(RunTrackerContract.LocationEntry.TABLE_NAME, null, values);
            }




            return null;
        }


        protected void onPostExecute(){
            //Potentially update the UI here
            //Close the connection to the database


        }
    }
}
