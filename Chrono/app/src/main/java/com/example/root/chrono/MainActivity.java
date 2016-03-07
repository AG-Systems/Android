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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.app.FragmentManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public void writeMessage(Double input)
    {
        String inputString = Double.toString(input);
        String filename = "hello_file_auriga.txt";
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            fileOutputStream.write(Integer.parseInt(inputString));
            fileOutputStream.write(Integer.parseInt("/n"));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readMessage()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        double differencex;
        double differencey;
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            ArrayList<Double> elementsx = new ArrayList<>();
            ArrayList<Double> elementsy = new ArrayList<>();
            ArrayList<Double> workx = new ArrayList<>();
            ArrayList<Double> worky = new ArrayList<>();
            int counter = 0;
            // if ^ 0 then it will crash.
            int reset = 0;
            int maxreset = 2000000000;
            int intial = 0;
            boolean tasker = false;
            double positivethresh = 0.000000001;
            double negativethresh = -0.00000001;
            @Override
            public void onLocationChanged(Location location) {
                if (tasker == false)
                {
                    //textView.append("\n" + location.getLatitude() + " " + location.getLongitude());
                }
                if(intial > 2) {
                    counter++;
                }
                intial++;
                elementsx.add(location.getLatitude());
                elementsy.add(location.getLongitude());
                double subtractx = location.getLatitude() -  elementsx.get(counter);
                double subtracty = location.getLongitude() -  elementsy.get(counter);
                //writeMessage(subtractx);
                if (location.getLatitude() -  elementsx.get(counter) < positivethresh && location.getLongitude() -  elementsy.get(counter) < positivethresh && subtractx > negativethresh && subtracty > negativethresh)
                {

                    reset++;
                    if(reset > 1)
                    {
                        //textView.append("Working");
                        String convertx = Double.toString(subtractx);
                        String converty = Double.toString(subtracty);
                        textView.append(convertx);
                        textView.append("        ");
                        textView.append(converty);


                    }

                    tasker = true;
                }
                if (location.getLatitude() -  elementsx.get(counter) > positivethresh || location.getLongitude() -  elementsy.get(counter) > positivethresh)
                {
                    reset = 0;
                    textView.append("Driving");
                    tasker = true;
                }





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
