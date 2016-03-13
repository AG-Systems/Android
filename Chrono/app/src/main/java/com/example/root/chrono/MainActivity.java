package com.example.root.chrono;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.app.FragmentManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Chronometer;
import android.os.SystemClock;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    private Button button;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView textView2;
    private TextView textView3;
    public Chronometer chronometer_driving;
    public Chronometer chronometer_working;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        double differencex;
        double differencey;
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        chronometer_driving = (Chronometer)findViewById(R.id.chronometer_driving);
        chronometer_working = (Chronometer)findViewById(R.id.chronometer_working);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            ArrayList<Double> elementsx = new ArrayList<>();
            ArrayList<Double> elementsy = new ArrayList<>();
            ArrayList<Double> workx = new ArrayList<>();
            ArrayList<Double> worky = new ArrayList<>();
            ArrayList<Double> worktime = new ArrayList<>();
            ArrayList<Double> drivetime = new ArrayList<>();
            int counter = 0;
            // if ^ 0 then it will crash.
            int counterforwork = 0;
            int counterfordriving = 0;
            int reset = 0;
            int maxreset = 2000000000;
            int intial = 0;
            boolean tasker = false;
            double positivethresh = 0.00090;
            double negativethresh = -0.00090;
            long worktimestart = 0;
            long drivingtimestart = 0;
            @Override
            public void onLocationChanged(Location location) {


                if (tasker == false)
                {
                    //textView.append("\n" + location.getLatitude() + " " + location.getLongitude());
                }
                if(intial > 2)
                {
                    counter++;
                }
                intial++;
                elementsx.add(location.getLatitude());
                elementsy.add(location.getLongitude());
                double subtractx = location.getLatitude() -  elementsx.get(counter);
                double subtracty = location.getLongitude() -  elementsy.get(counter);
                if (location.getLatitude() -  elementsx.get(counter) < positivethresh && location.getLongitude() -  elementsy.get(counter) < positivethresh && subtractx > negativethresh && subtracty > negativethresh) {

                    reset++;
                    counterforwork++;
                    if(reset > 2)
                    {
                        // if(location.getLatitude() == )
                        counterfordriving = 0;
                        chronometer_driving.stop();
                        boolean isInserted =  myDb.insertData(chronometer_driving.getText().toString(), chronometer_driving.getText().toString(), chronometer_driving.getText().toString());
                        if (isInserted = true)
                        {

                        }
                        else
                        {}
                        if(counterforwork == 3)
                        {
                            chronometer_working.setBase(SystemClock.elapsedRealtime());
                            chronometer_working.start();
                        }

                        workx.add(location.getLatitude());
                        workx.add(location.getLongitude());
                        textView.setText("Idle ");

                    }

                    tasker = true;
                }
                if (location.getLatitude() -  elementsx.get(counter) > positivethresh || location.getLongitude() -  elementsy.get(counter) > positivethresh || subtractx < negativethresh || subtracty < negativethresh)
                {
                    reset = 0;
                    counterfordriving++;
                    if(counterfordriving == 1)
                    {
                        chronometer_driving.setBase(SystemClock.elapsedRealtime());
                        chronometer_driving.start();
                        chronometer_working.stop();
                    }

                    textView.setText("Driving ");
                    // textView2.setText(strDate);
                    // textView2.setText(Double.toString(worksec));
                    tasker = true;
                }

                    /*

                    -What I did:

                    - Get the coordinates.[check]

                    - Determine the difference between driving and idling.[check]

                    - Setup the timers with driving and idling [check]

                    [SIMPLE AND NEEDS]
                    - 2 main locations: Home and work. Most time will be spent in these 2 parts for a average post college grad. Make it determine between the 2 using the coordinates. []

                    - Driving will have big gaps in data. Take the average after each day and compare the next days. That will be the driving path to work. Use times too to know. Generally when there is gaps , then you are driving. []

                    -Time either of the 3 tasks (home,driving,work), Use corrdinates to determine between all of them, be accurate with the time too. []

                    [PLANNED]
                    -Make a option of choosing what times your timeline

                    -Maybe make a option to choose if they have work + school + gym or whatever they want. They can edit it too.

                    -Maybe use weekends by using the calender and finding patterns in days.

                    - Add a manual option to help out the program and be more accurate.

                    - In the settings page there is tons of options so you can choose to be more accurate or save more batter etc etc.

                    - Use google maps api to see the places around you to guess what you are doing. Example next to a gym then it asumes that you are at a gym because you do this often. etc etc
                     */



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            configureButton();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 30000, 0, locationListener);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
                        /* public void viewAll()
                        {
                            Cursor res. myBd.getAllData();
                            if (res.getCount() == 0)
                            {
                                return;
                            }

                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext())
                            {
                                buffer.append("Id : " + res.getString(0));
                            }
                        }
                         */