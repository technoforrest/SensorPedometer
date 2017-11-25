package com.example.technoforrest.sensorpedometer;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private SensorManager manager;
    private TextView count;
    private Boolean activityRunning;
    static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        count = findViewById(R.id.stepsText);
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
            Log.d(TAG, "onCreate: NO PERMISSIONS IF STATEMENT");
        } else {
            Log.d(TAG, "onCreate: HAS PERMISSIONS ELSE STATEMENT");
            accessGoogleFit();
        }
    }
    /**
     * determines if the app is running
     */
    public void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor countSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            manager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Count sensor error!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * determines if the app is no longer running
     */
    @Override
    public void onPause(){
        super.onPause();
        activityRunning = false;
    }

    /**
     * updates the TextView when steps are increased
     * @param event is the sensor that is receiving the steps
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(activityRunning){
            count.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void accessGoogleFit() {
        //Does GoogleFit use the calendar to store steps???
        Log.d(TAG, "accessGoogleFit: ");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();


        /*DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();*/


    }
}