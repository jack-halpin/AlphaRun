package com.alpharun.jack.alpharun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the database ready
        DBManager db = new DBManager();
    }

    public void startRunCallback(View view){
        Intent intent = new Intent(this, RunActivity.class);
        startActivity(intent);
    }
}
