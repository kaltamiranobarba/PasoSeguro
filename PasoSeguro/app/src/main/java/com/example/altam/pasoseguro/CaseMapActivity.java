package com.example.altam.pasoseguro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

public class CaseMapActivity extends  AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    ScrollView mainScrollView;
    ImageView transparentImageView;
    private GoogleMap mMap;
    EditText txtTitle, txtDescription;
    String title, description, user;
    Button btnGuardar;
    double lat, lng;
    CheckBox checkPiropos, checkSilbidos, checkContacto, checkMiradas;
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
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtDescription = (EditText) findViewById(R.id.txtDescripcion);

        checkContacto = (CheckBox) findViewById(R.id.checkContacto);
        checkPiropos = (CheckBox) findViewById(R.id.checkPiropos);
        checkSilbidos = (CheckBox) findViewById(R.id.checkSilbidos);
        checkMiradas = (CheckBox) findViewById(R.id.checkMiradas);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txtTitle.getText().toString();
                description = txtDescription.getText().toString();
                ParseUser puser = ParseUser.getCurrentUser();
                user = puser.getString("username");
                getTypes();
                addCase(title, description,user,lat,lng,types);

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
        if(checkPiropos.isChecked())
            types.add(checkPiropos.getText().toString());
        if(checkSilbidos.isChecked())
            types.add(checkSilbidos.getText().toString());
    }


    public void addCase(String title, String description, String user, double lat, double lng, ArrayList<String> types){
        ParseObject po = new ParseObject("Cases");
        po.put("title", title);
        po.put("description",description);
        po.put("user",user);
        po.put("location", new ParseGeoPoint(lat,lng));
        po.put("types", types);
        po.saveInBackground();
        Toast.makeText(CaseMapActivity.this, "Historia registrada", Toast.LENGTH_LONG).show();
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
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        LatLng gye = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 14));
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
