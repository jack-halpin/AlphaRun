package com.alpharun.jack.alpharun;

import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jack on 10/02/2017.
 */

public class DBManager {

    //The SQLite database object
    private SQLiteDatabase db;

    public DBManager(){
        //initialise the object when the constructor is called
        db = SQLiteDatabase.openOrCreateDatabase("alpharunDB", null);
        createDatabase();
    }

    public void createDatabase(){
        db.execSQL("CREATE TABLE IF NOT EXISTS" +
                "oordinates(id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, Latitude DOUBLE, Longtitude, DOUBlE);");
    }
}
