package com.example.technoforrest.sensorpedometer;

import android.content.Intent;
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


//label graph
//remove shared preferences older than 7 days

public class HistoryDisplayActivity extends AppCompatActivity {
    final static String TAG = "HistoryDisplayActvity: ";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getGraph();
        Intent intent = getIntent();

    }

    /**
     * creates a graph and displays points based on the date and number of steps taken
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getGraph(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String dateArr[] = new String[7];
        // get starting date
        cal.add(Calendar.DAY_OF_YEAR, -7);

        // loop adding one day in each iteration
        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            dateArr[i] = sdf.format(cal.getTime());
            Log.d(TAG, "getLast7Dates: " + sdf.format(cal.getTime()));
        }
        Log.d(TAG, "getGraph: " + dateArr[6]);

        Date d1 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d2 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d3 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d4 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d5 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d6 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date d7 = cal.getTime();


        GraphView graph = (GraphView) findViewById(R.id.graph);
        //how to get steps for the last 7 days by date?
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, getPreferences(dateArr[0])),//label date and steps by date
                new DataPoint(1, getPreferences(dateArr[1])),
                new DataPoint(2, getPreferences(dateArr[2])),
                new DataPoint(3, getPreferences(dateArr[3])),
                new DataPoint(4, getPreferences(dateArr[4])),
                new DataPoint(5, getPreferences(dateArr[5])),
                new DataPoint(6, getPreferences(dateArr[6]))
        });
        graph.addSeries(series);
        // set date label formatter
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space

// set manual x bounds to have nice steps
        //graph.getViewport().setMinX(d7.getTime());
        //graph.getViewport().setMaxX(d1.getTime());
        //graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        //graph.getGridLabelRenderer().setHumanRounding(false);
    }

    /**
     * gets the shared preferences from the previous activity based on date
     * @param key the date that needs to be accessed
     * @return the number of steps taken on the specific day accessed
     */
    public int getPreferences(String key){
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * @return returns the current date
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String dateTime(){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(TAG, "dateTime: " + date.format(Calendar.getInstance().getTime()));
        return date.format(Calendar.getInstance().getTime());
    }



}