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

public class MainActivity extends AppCompatActivity {
    private SensorManager manager;
    private TextView count;
    static final String TAG = "MainActivity";
    private float steps;
    private Boolean zeroSteps;
    private float stepsDontCount;


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
        sensorRegister();

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