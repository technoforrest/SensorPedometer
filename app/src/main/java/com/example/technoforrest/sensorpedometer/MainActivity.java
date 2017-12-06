package com.example.technoforrest.sensorpedometer;

/*
  This program detects user steps based on native step counter, records the steps,
  and displays them for the user.
  CPSC 312-01, Fall 2017
  Final Project
 Sources: developer.android.com/reference/android/hardware/Sensor
          Tihomir RAdeff. Develop simple Step Counter in Android Studio. https://youtu.be/CNGMWnmldaU
          https://stackoverflow.com/questions/42661678/
               android-how-to-get-the-sensor-step-counter-data-only-one-day
          http://www.android-graphview.org/

  @author Danielle Forrest
 * @version v1.0 12/8/17
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
    protected TextView stepsTxt;
    private SensorManager manager;
    private int newSteps;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stepsTxt = findViewById(R.id.stepsText);

        Log.d(TAG, "onCreate: " );
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorRegister();
        Button historyBtn = findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryDisplayActivity.class);

                startActivity(intent);
            }
        });
    }

    /**
     * registers the step counter and calls to shared preferences to store the steps
     */
    public void sensorRegister(){

        manager.registerListener(new SensorEventListener() {

                                     @RequiresApi(api = Build.VERSION_CODES.N)
                                     @Override
                                     public void onSensorChanged(SensorEvent event) {
                                         int todayStep = getPreferences(dateTime());// steps saved
                                                                            //from previous session
                                         newSteps = todayStep + 1;
                                         Log.d(TAG, "onSensorChanged: newSteps=" + newSteps);
                                         writePreferenceSet(dateTime(), newSteps);
                                         Log.d(TAG, "onSensorChanged: write to preference = " + todayStep + newSteps);
                                         stepsTxt.setText(getPreferences(dateTime()) + "");//update
                                                                // Textview to current day's steps
                                     }
                                     @Override
                                     public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                     }
                                 }, manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * writes steps to shared preferences file
     * @param key the current date
     * @param value the number of steps counted for the date
     */
    public void writePreferenceSet(String key, int value){
        Log.d(TAG, "writePreferenceSet: ");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    /**
     * gets the steps for today to shared preferences
     * @param key is the current date
     * @return returns the steps for the current date
     */
    public int getPreferences(String key){
        Log.d(TAG, "getPreferences: ");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * Computes the current date
     * @return the current date
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String dateTime(){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(TAG, "dateTime: " + date.format(Calendar.getInstance().getTime()));
        return date.format(Calendar.getInstance().getTime());
    }




}