package com.example.altam.pasoseguro;

import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import static java.util.Calendar.*;

public class MyCasesActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener,  GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private List<ParseObject> allObjects;
    private String user;
    int y, m, d;
    LocationManager locationManager;
    Location mLastLocation;
    Boolean delete = false;
    FloatingActionButton fab;
    int total=0;
    TextView totalCases;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cases_final);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle((Html.fromHtml("<small>Toca un reporte para eliminarlo</small>")));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ParseUser puser = ParseUser.getCurrentUser();
        user = puser.getString("username");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        totalCases = (TextView) findViewById(R.id.totalCases);
        totalCases.setText(String.valueOf(total));
      fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete){
                    delete = false;
                    setTitle((Html.fromHtml("<small>Tus reportes["+total+"]</small>")));
                }
                else{
                    delete = true;
                    setTitle((Html.fromHtml("<small>Toca un reporte para eliminarlo["+total+"]</small>")));
                }

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
        getCases();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }


    public void getCases() {
        double lat, lng;
        allObjects = new ArrayList<ParseObject>();
        ParseUser puser = ParseUser.getCurrentUser();
        this.user = puser.getString("username");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereExists("location");
        query.whereEqualTo("user", this.user);
        query.setLimit(500);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                total = list.size();
                setTitle((Html.fromHtml("<small>Tus reportes["+total+"]</small>")));
                if (e == null) {
                    for (ParseObject o : list) {
                        double lat = o.getParseGeoPoint("location").getLatitude();
                        double lng = o.getParseGeoPoint("location").getLongitude();
                        String des = o.getString("description");
                        String userCase = o.getString("user");
                        String p = userCase + " vivió:";
                        String id = o.getObjectId();
                        JSONArray types = o.getJSONArray("types");
                        String title = " ";
                        try {
                            for (int i = 0; i < types.length(); i++) {
                                title = title.concat(" " + types.getString(i));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(p)
                                .snippet(title)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mboard)));

                    }
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MyCases Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.altam.pasoseguro/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MyCases Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.altam.pasoseguro/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng gye = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 18));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if(myLocation!=null){
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();
            LatLng gye = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 18));
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("VALOR BOOLEAN "+delete);
        if(delete==false){
            return false;
        }
        else {
            ParseObject po;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
            ParseGeoPoint pgp = new ParseGeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
            query.whereEqualTo("location", pgp);
            try {
                po = query.getFirst();
                po.delete();
                marker.remove();
                total--;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(delete){
                setTitle((Html.fromHtml("<small>Toca un reporte para eliminarlo["+total+"]</small>")));

            }
            else{
                setTitle((Html.fromHtml("<small>Tus reportes["+total+"]</small>")));
            }
        return true;
    }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                client);
        if (mLastLocation != null) {

            LatLng gye = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 17));
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        LocationServices.FusedLocationApi.requestLocationUpdates(client,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
