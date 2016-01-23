package com.example.altam.pasoseguro;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CaseMapActivity extends  AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    ScrollView mainScrollView;
    ImageView transparentImageView;
    private GoogleMap mMap;
    EditText txtTitle, txtDescription;
    String title, description, user;
    Button btnGuardar;
    double lat, lng;
    CheckBox checkAbusoVerbal, checkSilbidos, checkContacto, checkMiradas, checkInsinuacion, checkExposicion, checkGestos;
    ArrayList<String> types = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainScrollView = (ScrollView) findViewById(R.id.sv_container);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);


        checkContacto = (CheckBox) findViewById(R.id.checkContacto);
        checkAbusoVerbal = (CheckBox) findViewById(R.id.checkAbusoVerbal);
        checkSilbidos = (CheckBox) findViewById(R.id.checkSilbidos);
        checkMiradas = (CheckBox) findViewById(R.id.checkMiradas);
        checkExposicion = (CheckBox) findViewById(R.id.checkExposicion);
        checkInsinuacion = (CheckBox) findViewById(R.id.checkInsinuacion);
        checkGestos = (CheckBox) findViewById(R.id.checkGestos);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser puser = ParseUser.getCurrentUser();
                user = puser.getString("username");
                getTypes();
                addCase(user,lat,lng,types);

                Intent i = new Intent(CaseMapActivity.this, MapActivity.class );
                startActivity(i);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    public void getTypes(){
        if(checkContacto.isChecked())
            types.add(checkContacto.getText().toString());
        if(checkMiradas.isChecked())
            types.add(checkMiradas.getText().toString());
        if(checkAbusoVerbal.isChecked())
            types.add(checkAbusoVerbal.getText().toString());
        if(checkSilbidos.isChecked())
            types.add(checkSilbidos.getText().toString());
        if(checkExposicion.isChecked())
            types.add(checkExposicion.getText().toString());
        if(checkInsinuacion.isChecked())
            types.add(checkInsinuacion.getText().toString());
        if(checkGestos.isChecked())
            types.add(checkGestos.getText().toString());
    }



    public void addCase(String user, double lat, double lng, ArrayList<String> types){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        ParseObject po = new ParseObject("Cases");
        po.put("user",user);
        po.put("location", new ParseGeoPoint(lat,lng));
        po.put("types", types);
        po.put("year",year);
        po.put("month",month);
        po.put("day",day);
        po.put("week", week);

        final ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            po.saveInBackground();
            Toast.makeText(CaseMapActivity.this, "Reporte registrado", Toast.LENGTH_LONG).show();
        } else {
            PasoSeguro.pendingCases.add(po);
            Toast.makeText(CaseMapActivity.this, "Reporte en cola", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MapActivity.class);
            PasoSeguro.NOTIFICATION_ID = (int) System.currentTimeMillis();
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

            Notification n  = new Notification.Builder(this)
                    .setContentTitle("Reporte espera")
                    .setContentText("Se enviará automaticamente cuando te conectes a internet")
                    .setSmallIcon(R.drawable.board)
                    //.setContentIntent(pIntent)
                    .setAutoCancel(true).build()
                 ;

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);


        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.setOnMapClickListener(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
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
                // Get longitude of the current location
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
    }


    @Override
    public void onMapClick(LatLng latLng) {
        lat = latLng.latitude;
        lng = latLng.longitude;
        mMap.clear();
        Marker tmp = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.board)));
    }
}
