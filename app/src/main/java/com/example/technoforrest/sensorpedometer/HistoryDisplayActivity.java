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
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

public class HistoryDisplayActivity extends AppCompatActivity {
    final static String TAG = "HistoryDisplayActvity: ";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getGraph();


    }

    /**
     * creates a graph and displays points based on the date and number of steps taken
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getGraph(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String dateArr[] = new String[7];
        Date arrDate[] = new Date[7];
        // get starting date
        cal.add(Calendar.DATE, -7);

        // loop adding one day in each iteration
        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DATE, 1);
            dateArr[i] = sdf.format(cal.getTime());//key for shared preferences
            arrDate[i] = cal.getTime();

            Log.d(TAG, "getLast7Dates: " + sdf.format(cal.getTime()) + " " + arrDate[i]);
        }
        Log.d(TAG, "getGraph: " + dateArr[6]);


        GraphView graph = findViewById(R.id.graph);
        //how to get steps for the last 7 days by date?
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(arrDate[0], getPreferences(dateArr[0])),//label date and steps by date
                new DataPoint(arrDate[1], getPreferences(dateArr[1])),
                new DataPoint(arrDate[2], getPreferences(dateArr[2])),
                new DataPoint(arrDate[3], getPreferences(dateArr[3])),
                new DataPoint(arrDate[4], getPreferences(dateArr[4])),
                new DataPoint(arrDate[5], getPreferences(dateArr[5])),
                new DataPoint(arrDate[6], getPreferences(dateArr[6]))
        });

        graph.addSeries(series);

        Log.d(TAG, "getGraph: " + series);
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);


// set manual x bounds to have nice steps
        Log.d(TAG, "getGraph: " );
        graph.getViewport().setMinX(arrDate[0].getTime());
        graph.getViewport().setMaxX(arrDate[6].getTime());
        graph.getViewport().setMinY(0);
        //graph.getViewport().setMaxY(20000);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);


// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    /**
     * gets the shared preferences from the previous activity based on date
     * @param key the date that needs to be accessed
     * @return the number of steps taken on the specific day accessed
     */
    public int getPreferences(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPreferences.getInt(key, 0);
    }







}