package com.alpharun.jack.alpharun.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alpharun.jack.alpharun.Database.RunDbHelper;
import com.alpharun.jack.alpharun.Database.RunTrackerContract;
import com.alpharun.jack.alpharun.R;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        RunDbHelper runDbHelper = new RunDbHelper(this);

        SQLiteDatabase db = runDbHelper.getWritableDatabase();
        runDbHelper.onCreate(db);


        //Quick test for the databse
        ContentValues values = new ContentValues();
        values.put(RunTrackerContract.RunEntry.DISTANCE_COLUMN, 300);
        values.put(RunTrackerContract.RunEntry.TIME_COLUMN, 39);
        long newRowId = db.insert(RunTrackerContract.RunEntry.TABLE_NAME, null, values);

        //Create the projection. This basically is an array of the rows that you actually want to keep from the results
        String[] projection = { RunTrackerContract.RunEntry._ID, RunTrackerContract.RunEntry.TIME_COLUMN, RunTrackerContract.RunEntry.DISTANCE_COLUMN };

        Cursor cursor = db.query(
                RunTrackerContract.RunEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()){
            long itemid = cursor.getLong(cursor.getColumnIndexOrThrow(RunTrackerContract.RunEntry._ID));
            Log.e("itemid: ", Long.toString(itemid));
        }
    }

    public void startRunCallback(View view){
        Intent intent = new Intent(this, RunActivity.class);
        startActivity(intent);
    }
}
