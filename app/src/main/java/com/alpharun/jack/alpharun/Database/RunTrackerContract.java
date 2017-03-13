package com.alpharun.jack.alpharun.Database;

import android.provider.BaseColumns;

/**
 * Created by Jack on 13/03/2017.
 * This class if for storing the constants used as variables for creating the databse
 */

public class RunTrackerContract {

    //Constant for creating the tables


    private RunTrackerContract(){
        //Empty
    };

    public static class LocationEntry implements BaseColumns{
        public static final String TABLE_NAME = "locationentry";
        public static final String LATITUDE_COLUMN = "latitude";
        public static final String LONGTITUDE_COLUMN = "longtitude";
        public static final String RUN_ID_COLUMN = "run_id";
    }
    public static class RunEntry implements BaseColumns{

        public static final String TABLE_NAME = "runentry";
        public static final String TIME_COLUMN = "time";
        public static final String DISTANCE_COLUMN = "distance";
    }

}
