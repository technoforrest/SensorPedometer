package com.example.technoforrest.sensorpedometer;

/**
 * This program detects user steps based on native step counter, records the steps,
 * and displays them for the user.
 * CPSC 312-01, Fall 2017
 * Final Project
 *Sources: developer.android.com/reference/android/hardware/Sensor
 *         Tihomir RAdeff. Develop simple Step Counter in Android Studio. https://youtu.be/CNGMWnmldaU
 *
 * @author Danielle Forrest and Elouisa Serrano
 * @version v1.0 11/29/17
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

import static android.provider.Contacts.SettingsColumns.KEY;

public class MainActivity extends AppCompatActivity {
    private SensorManager manager;
    private TextView count;
    static final String TAG = "MainActivity";
    private float steps;
    private Boolean zeroSteps;
    private String dayName;
    private float stepsDontCount;
    final String PREFERENCE_FILENAME1 = "DayOfWeek";
    final String PREFERENCE_FILENAME2 = "savedSteps";
    final String KEY_TEXT1 = "savedDaysKey";
    final String KEY_TEXT2 = "savedStepsKey";
    private SharedPreferences sharedPreferencesDay;
    private SharedPreferences sharedPreferencesSteps;
    private String stringPrefs1;
    private String stringPrefs2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        count = findViewById(R.id.stepsText);
        zeroSteps = true;
        dayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        Log.d(TAG, "onCreate: " + dayName);
        sharedPreferencesDay = this.getSharedPreferences(PREFERENCE_FILENAME1, Context.MODE_PRIVATE);
        stringPrefs1 = sharedPreferencesDay.getString(KEY_TEXT1, "");
        sharedPreferencesSteps = this.getSharedPreferences(PREFERENCE_FILENAME2, Context.MODE_PRIVATE);
        stringPrefs2 = sharedPreferencesSteps.getString(KEY_TEXT2, "");
        TextView textView1 = findViewById(R.id.today);
        TextView textView2 = findViewById(R.id.savedSteps);

        textView1.setText(stringPrefs1);
        textView2.setText(stringPrefs2);


        sensorRegister();
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferencesDay = this.getSharedPreferences(PREFERENCE_FILENAME1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferencesDay.edit();
        editor1.putString(KEY_TEXT1, dayName );
        Log.d(TAG, "onStop: DAY OF WEEK storing");
        editor1.commit(); // don't forget this!
        SharedPreferences sharedPreferencesSteps = this.getSharedPreferences(PREFERENCE_FILENAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferencesSteps.edit();
        editor2.putString(KEY_TEXT2, count.getText().toString() );
        Log.d(TAG, "onStop: DAY OF WEEK storing");
        editor2.commit();
    }

    /**
     * sensor is activated and steps from sensor assigned to the textview
     */
    public void sensorRegister(){
        manager.registerListener(new SensorEventListener() {

          @Override
          public void onSensorChanged(SensorEvent event) {
              //determines if the activity is just launched
              if(zeroSteps){
                  stepsDontCount = event.values[0];//records steps since device reboot
                  zeroSteps = false;//activity will no loner be considered new
              }
            steps = event.values[0] - stepsDontCount;//subtracts the values stored in the
                                                // phone so only the steps taken since the app show
            count.setText(steps + "");
          }
          @Override
          public void onAccuracyChanged(Sensor sensor, int accuracy) {

          }
        }, manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);
  }



}