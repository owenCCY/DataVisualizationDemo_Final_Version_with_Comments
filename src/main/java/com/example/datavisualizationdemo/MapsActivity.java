package com.example.datavisualizationdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//show sensor location on this activity, using google map
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int index; //index number of the sensor
    double lat;//latitude
    double lon;//longitude
    String str;//store the string we get from extra data
    Intent in; //intent

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        in = getIntent();
        str = in.getStringExtra("com.example.datavisualizationdemo.PIC_INDEX");//get the extra message from previous activity
        //parsing the message
        String[] strArr = str.split(" ");
        System.out.println(strArr[1]);
        index = Integer.valueOf(strArr[0]);
        lon = Double.valueOf(strArr[1]);
        lat = Double.valueOf(strArr[2]);
        //building the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //input the latitude and longitude and show the map
        LatLng usc = new LatLng(lon, lat);
        mMap.addMarker(new MarkerOptions().position(usc).title("Sensor"+Integer.toString(index)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(usc,
                15f);
        mMap.moveCamera(update);
    }
}