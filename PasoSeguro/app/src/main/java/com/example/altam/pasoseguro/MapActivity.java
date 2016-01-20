package com.example.altam.pasoseguro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DrawerLayout mDrawerLayout;
    private TextView drawerText;
    private Bundle extras;
    private String user;
    private FloatingActionButton fab;
    private GoogleApiClient client;
    private List<ParseObject> allObjects;
    final double latitude=0, longitude=0;
    boolean alarmActivated;
    int yearF, dayF, monthF;

    private Thread thread = null;
    private RunnableVibrate runnable = new RunnableVibrate(this);

    private BroadcastReceiver netStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connected = false;

            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                connected = true;
                int size = PasoSeguro.pendingCases.size();
                //Toast.makeText(MapActivity.this, "Conexión reestablecida, casos encolados: " + size, Toast.LENGTH_LONG).show();
                for (ParseObject po : PasoSeguro.pendingCases) {
                    po.saveInBackground();
                }
                PasoSeguro.pendingCases.clear();
            } else {
                //Toast.makeText(MapActivity.this, "Sin conexion", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_final);



        ParseUser puser = ParseUser.getCurrentUser();
        this.user = puser.getString("username");

        registerReceiver(netStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION ));

        //mToolbar = (Toolbar) findViewById(R.id.toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //UiSettings.setMyLocationButtonEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, CaseMapActivity.class);
                startActivity(i);
            }
        });

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()){

                    case R.id.navigation_item_my_cases:
                        Intent intent = new Intent(MapActivity.this, MyCasesActivity.class);
                        startActivity(intent);
                    break;
                    case R.id.navigation_item_logout:
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.logOut();
                        Toast.makeText(MapActivity.this, "Sesión finalizada", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(MapActivity.this, LoginActivity.class);
                        startActivity(intent2);
                    break;
                    case R.id.navigation_item_profile:
                        Toast.makeText(MapActivity.this, "MY PROFILE", Toast.LENGTH_LONG).show();
                    break;
                    case R.id.navigation_item_custom_filter:
                        Intent intent3 = new Intent(MapActivity.this, CustomFilterActivity.class);
                        startActivity(intent3);
                    break;
                    case R.id.navigation_item_danger:
                        Intent intent4 = new Intent(MapActivity.this, DangerZoneActivity.class);
                        startActivity(intent4);
                        break;
                }
                return true;
            }
        });

        drawerText = (TextView) findViewById(R.id.drawer_header_textview);
        drawerText.setText(user);


        /*setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        alarmActivated = false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem alarmItem = menu.getItem(0);
        if(alarmActivated == true) {
            Drawable alarm = ContextCompat.getDrawable(this, R.drawable.ic_alarm);
            alarmItem.setIcon(alarm);
        } else {
            Drawable alarmOff = ContextCompat.getDrawable(this, R.drawable.ic_alarm_off);
            alarmItem.setIcon(alarmOff);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_filter:
                return true;
            case R.id.action_alarm:
                if(alarmActivated == true) {
                    Drawable alarmOff = ContextCompat.getDrawable(this, R.drawable.ic_alarm_off);
                    item.setIcon(alarmOff);
                    alarmActivated = false;
                    //SE DESACTIVO LA ALARMA
                    if (thread != null) {
                        runnable.terminate();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Drawable alarm = ContextCompat.getDrawable(this, R.drawable.ic_alarm);
                    item.setIcon(alarm);
                    alarmActivated = true;
                    //SE ACTIVO
                    runnable.init();
                    thread = new Thread(runnable);
                    thread.start();

                }
                return true;
            case R.id.fast_filter_item_anio:
                item.setChecked(true);
                setTitle("Reportes del Año");
                getCases(1);
                Toast.makeText(getApplicationContext(),"Reportes de este año", Toast.LENGTH_LONG).show();
                return true;
            case R.id.fast_filter_item_mes:
                item.setChecked(true);
                setTitle("Reportes del Mes");
                getCases(2);
                Toast.makeText(getApplicationContext(),"Reportes de este mes", Toast.LENGTH_LONG).show();
                return true;
            case R.id.fast_filter_item_semana:
                item.setChecked(true);
                setTitle("Reportes de la Semana");
                getCases(3);
                Toast.makeText(getApplicationContext(),"Reportes de esta semana", Toast.LENGTH_LONG).show();
                return true;
            case R.id.fast_filter_item_custom:
                item.setChecked(true);
                setTitle("Reportes personalizados");
                extras = getIntent().getExtras();
                if(extras!=null){
                    yearF = extras.getInt("yearF");
                    monthF = extras.getInt("montF");
                    dayF = extras.getInt("dayF");
                    getCases(4);
                    Toast.makeText(getApplicationContext(),"Reportes personalizados", Toast.LENGTH_LONG).show();
                    return true;
                }else{
                    Toast.makeText(getApplicationContext(),"No ha definido fecha", Toast.LENGTH_LONG).show();
                }


        }
        return super.onOptionsItemSelected(item);
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

            // Get longitude of the current location
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000,500, mLocationListener);



        getCases(3);

    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
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
                "Map Page", // TODO: Define a title for the content shown.
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




    public void getCases(int mode){
        double lat, lng;
        allObjects = new ArrayList<ParseObject>();

        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH)+1;
        int cWeek = c.get(Calendar.WEEK_OF_YEAR);
        mMap.clear();;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereExists("location");
        switch (mode){
            case 1:
                query.whereEqualTo("year",cYear);
                break;
            case 2:
                query.whereEqualTo("month",cMonth);
                query.whereEqualTo("year",cYear);
                break;
            case 3:
                query.whereEqualTo("year",cYear);
                query.whereEqualTo("week",cWeek);
                break;
            case 4:
                query.whereGreaterThanOrEqualTo("year", yearF);
                query.whereGreaterThanOrEqualTo("month", monthF);
                query.whereGreaterThanOrEqualTo("day",dayF);
                break;
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for(ParseObject o : list){
                        double lat = o.getParseGeoPoint("location").getLatitude();
                        double lng = o.getParseGeoPoint("location").getLongitude();
                        String des = o.getString("description");
                        String userCase = o.getString("user");
                        String p = userCase+" vivió:";

                        String id = o.getObjectId();
                        JSONArray types = o.getJSONArray("types");
                        String title=" ";

                        try {
                            for (int i = 0; i < types.length(); i++) {
                                title = title.concat(" " + types.getString(i));
                            }
                        }catch (JSONException e1) {
                                e1.printStackTrace();
                        }

                        Marker tmp = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(p)
                                .snippet(title)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.board)));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}
