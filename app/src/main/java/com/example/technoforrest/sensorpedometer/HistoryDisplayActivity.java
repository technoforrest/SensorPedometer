package com.example.technoforrest.sensorpedometer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
//get shared preferences from previous activity
//get dates from last 7 days
//assign shared preferences values to graph
//label graph
//fix navigation between activities

public class HistoryDisplayActivity extends AppCompatActivity {
    final static String TAG = "HistoryDisplayActvity: ";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getGraph();
        Intent intent = getIntent();

    }

    /**
     * creates a graph and displays points based on the date and number of steps taken
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getGraph(){
        int day1 = 0;
        int day2 = 0;
        int day3 = 0;
        int day4 = 0;
        int day5 = 0;
        int day6 = 0;
        int day7 = getPreferences(dateTime());
        Log.d(TAG, "getGraph: " + day7);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        //how to get steps for the last 7 days by date?
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, day1),//label date and steps by date
                new DataPoint(1, day2),
                new DataPoint(2, day3),
                new DataPoint(3, day4),
                new DataPoint(4, day5),
                new DataPoint(5, day6),
                new DataPoint(6, day7)
        });
        graph.addSeries(series);
    }

    /**
     * gets the shared preferences from the previous activity based on date
     * @param key the date that needs to be accessed
     * @return the number of steps taken on the specific day accessed
     */
    public int getPreferences(String key){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * @return returns the current date
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String dateTime(){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return date.format(Calendar.getInstance().getTime());
    }

    /**
     * this function will calculate the date for the past 7 days
     * @return returns the dates for the past 7 days UNFINISHED
     */
    public String otherDates(){
        String otherDatesStr = "nothing";
        return otherDatesStr;

    }

}
