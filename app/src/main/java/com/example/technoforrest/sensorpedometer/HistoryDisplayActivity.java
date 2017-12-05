package com.example.technoforrest.sensorpedometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HistoryDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String dayOfWeek = intent.getStringExtra("Day");
        String stepsStr = intent.getStringExtra("Steps");

        //TextView Friday = findViewById(R.id.friday);
        //if(dayOfWeek == Friday.getText()){
          //  Friday.setText(dayOfWeek + "need steps");
        //}
    }

}
