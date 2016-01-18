package com.example.altam.pasoseguro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DangerZoneActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParseObject> allObjects;
    private ArrayList<ParseGeoPoint> locations = new ArrayList<ParseGeoPoint>();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zone);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DangerZoneActivity.this, CaseMapActivity.class);
                startActivity(i);
            }
        });
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
        mMap.setMyLocationEnabled(true);

        Toast.makeText(getApplicationContext(),  "Buscando tu ubicación...", Toast.LENGTH_LONG).show();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if(myLocation!=null){
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng gye = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 17));
        }

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng gye = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 17));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 500, mLocationListener);


        getCases();
        createZones();
    }


    public void getCases() {
        double lat, lng;
        allObjects = new ArrayList<ParseObject>();
        ParseGeoPoint location;
        /*
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH)+1;
        int cWeek = c.get(Calendar.WEEK_OF_YEAR);
        int m = cMonth  -2;
        if(m <= 0){
            cYear--;
            m = 12  - m;
        }*/
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereExists("location");
        try {
            allObjects = query.find();
            for(ParseObject o : allObjects){
                location = o.getParseGeoPoint("location");
                locations.add(location);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public void createZones() {
        ArrayList<ParseGeoPoint> lTaken = new ArrayList<ParseGeoPoint>();
        ArrayList<ParseGeoPoint> lTakenTmp = new ArrayList<ParseGeoPoint>();
        int count = 0;
        double d;



        for (ParseGeoPoint i : locations) {
            if (!lTaken.contains(i)) {
                for (ParseGeoPoint j : locations) {
                    if(!lTaken.contains(j)) {
                        d = i.distanceInKilometersTo(j);
                        if (d <= 0.03 && d != 0) {
                            count++;
                            lTakenTmp.add(j);
                        }
                    }
                }
                if (count >= 10) {
                    CircleOptions co = new CircleOptions();
                    co.center(new LatLng(i.getLatitude(), i.getLongitude()));
                    co.radius(30);
                    co.strokeColor(Color.RED);
                    co.strokeWidth(1);
                    if(count>=10 && count < 20)
                        co.fillColor(0x40FFFF00);

                    if(count >= 20 && count<30 )
                        co.fillColor(0x40ffa500);
                    if(count>=30)
                        co.fillColor(0x40ff0000);

                    mMap.addCircle(co);
                    lTaken.addAll(lTakenTmp);
                }
                lTakenTmp.clear();
                count = 0;
            }
        }
    }
}
