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
          History icon by Josy_Dom_Alexis at Pixabay

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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
        getLast7Days();
        sensorRegister();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if(sharedPreferences.contains(key)) {
            return sharedPreferences.getInt(key, 0);
        }else return -1;
    }

    /**
     * deletes all sharedpreferences
     */
    public void deletePreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

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

    /**
     * getLast7Days saves the last 7 days keys and values to arrays, deletes old preferences,
     * and saves last 7 days to shared preferences, so sharedpreferences don't get weighted down.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getLast7Days(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();// today
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);//tomorrow
        String tomorrowStr = sdf.format(tomorrow.getTime());

        String stepArr[]= new String[7];
        Integer intArr[] = new Integer[7];

        // get starting date
        cal.add(Calendar.DATE, -7);

        // loop adding one day in each iteration
        //assigns dates as preference keys to array
        //gets preferences (steps) based on keys
        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DATE, 1);
            stepArr[i] = (sdf.format(cal.getTime()));//key for shared preferences
            intArr[i] = getPreferences(stepArr[i]);
        }
        //delete all shared preferences and rewrite them
        deletePreferences();

        for(int i = 0; i < 7; i++){
            writePreferenceSet(stepArr[i], intArr[i]);
        }

        Log.d(TAG, "getLast7Days: " + getPreferences(stepArr[5]));
    }



}