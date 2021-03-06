package com.example.root.chrono;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.root.chrono.MainActivity;
import com.example.root.chrono.DatabaseHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseHelper myDb;
    double location1x;
    double location1y;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void viewAll()
    {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0)
        {
            return;
        }
        location1x = Double.parseDouble(res.getString(5));
        location1y = Double.parseDouble(res.getString(6));
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext())
        {
            //buffer.append("Id : " + res.getString(0));
            location1x = Double.parseDouble(res.getString(5));
            location1y = Double.parseDouble(res.getString(6));

        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng main = new LatLng(location1x, location1y);
        mMap.addMarker(new MarkerOptions().position(main).title("Testing"));
        float zoomLevel = 13.0f; // goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(main, zoomLevel));
         mMap.moveCamera(CameraUpdateFactory.newLatLng(main));

    }
}


