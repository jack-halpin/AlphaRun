package com.alpharun.jack.alpharun.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

/**
 * Created by Jack on 13/03/2017.
 */

public class RunDbHelper extends SQLiteOpenHelper {
    //The database version variable is used to monitor updates to the schema
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Runtracker.db";

    public static final String SQL_CREATE_RUNENTRY_TABLE = "CREATE TABLE " + RunTrackerContract.RunEntry.TABLE_NAME + " (" +
            RunTrackerContract.RunEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RunTrackerContract.RunEntry.TIME_COLUMN + " INTEGER," +
            RunTrackerContract.RunEntry.DISTANCE_COLUMN + " INTEGER)" + "; ";

    public static final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + RunTrackerContract.LocationEntry.TABLE_NAME + " (" +
            RunTrackerContract.LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RunTrackerContract.LocationEntry.LONGTITUDE_COLUMN + " REAL," +
            RunTrackerContract.LocationEntry.LATITUDE_COLUMN + " REAL," +
            RunTrackerContract.LocationEntry.RUN_ID_COLUMN + " INTEGER," +
            "FOREIGN KEY(" + RunTrackerContract.LocationEntry.RUN_ID_COLUMN + ") REFERENCES " +
            RunTrackerContract.RunEntry.TABLE_NAME + "(" + RunTrackerContract.RunEntry._ID + "));";

    private static final String SQL_DELETE_RUNENTRY = "DROP TABLE IF EXISTS " + RunTrackerContract.RunEntry.TABLE_NAME + "; ";
    private static final String SQL_DELETE_LOCATIONENTRY = "DROP TABLE IF EXISTS " + RunTrackerContract.LocationEntry.TABLE_NAME;





    public RunDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //This method is called when the databse is first created. Need to put the query for the creation
    //of tables or entry of seed data (if any exists)
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_RUNENTRY);
        db.execSQL(SQL_DELETE_LOCATIONENTRY);
        db.execSQL(SQL_CREATE_RUNENTRY_TABLE);
        db.execSQL(SQL_CREATE_LOCATION_TABLE);

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.e( "deleteding: ", "db");
        db.execSQL(SQL_DELETE_RUNENTRY);
        db.execSQL(SQL_DELETE_LOCATIONENTRY);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
