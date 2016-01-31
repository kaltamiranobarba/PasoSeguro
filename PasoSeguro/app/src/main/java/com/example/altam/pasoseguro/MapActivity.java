package com.example.altam.pasoseguro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DrawerLayout mDrawerLayout;
    private TextView drawerText;
    private Bundle extras;
    private String user;
    private FloatingActionButton fab;
    private GoogleApiClient client;
    private List<ParseObject> allObjects;
    final double latitude=0, longitude=0;

    int yearF, dayF, monthF;
    CheckBox checkAbusoVerbal, checkSilbidos, checkContacto, checkMiradas, checkInsinuacion, checkExposicion, checkGestos;
    ArrayList<String> types = new ArrayList<String>();
    double lat, lng;
    Button b;
    private ArrayList<ParseGeoPoint> locations = new ArrayList<ParseGeoPoint>();
    final public ArrayList<CircleOptions> circles = new ArrayList<>();
    boolean mov = false;
    boolean vibrate=false;
    Marker myMarker ;
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
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
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
                /*
                Intent i = new Intent(MapActivity.this, CaseMapActivity.class);
                startActivity(i);
                */
                showPopUp3();
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
                        Intent intent5 = new Intent(MapActivity.this, MyProfileActivity.class);
                        startActivity(intent5);
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

        PasoSeguro.alarmActivated = false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem alarmItem = menu.getItem(0);
        if(PasoSeguro.alarmActivated == true) {

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
                if(PasoSeguro.alarmActivated == true) {
                    Drawable alarmOff = ContextCompat.getDrawable(this, R.drawable.ic_alarm_off);
                    item.setIcon(alarmOff);
                    PasoSeguro.alarmActivated = false;
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
                    PasoSeguro.alarmActivated = true;
                    showPopUpInfoAlert();
                    //SE ACTIVO
                    circles.clear();
                    createZones();
                    if(PasoSeguro.vibrate==true) {
                        /*
                        runnable.init();
                        thread = new Thread(runnable);
                        thread.start();
                        */
                    }

                    Toast.makeText(getApplicationContext(),"ALARMA ACTIVADA", Toast.LENGTH_LONG).show();
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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        final ArrayList<CircleOptions> cos = this.circles;
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
                double longitude = location.getLongitude();
                int count =0;
                LatLng gye = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gye, 18));
                Toast.makeText(getApplicationContext(),  "LOCATION CHANGED", Toast.LENGTH_LONG).show();
            if(PasoSeguro.alarmActivated==true){
                for(CircleOptions co: circles){
                    ParseGeoPoint pgp = new ParseGeoPoint(latitude, longitude);
                    ParseGeoPoint pgpC = new ParseGeoPoint(co.getCenter().latitude, co.getCenter().longitude);
                    if(pgpC.distanceInKilometersTo(pgp)<0.03){
                        Toast.makeText(getApplicationContext(),  "ESTAS DENTRO DE ZONA DE PELIGRO", Toast.LENGTH_LONG).show();
                        PasoSeguro.vibrate = true;
                    }
                    else{
                        PasoSeguro.vibrate = false;
                    }
                }
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

             }
         };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);

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


    private void showPopUp3() {

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        //helpBuilder.setTitle("titulo");
        helpBuilder.setMessage("Hemos tomado tu ubicación actual, retrocede y toca el mapa para tomar una ubicación diferente");

        LayoutInflater inflater = getLayoutInflater();
        View checkboxLayout = inflater.inflate(R.layout.popuplayout, null);
        b = (Button)checkboxLayout.findViewById(R.id.btn_popUpSave);
        checkContacto = (CheckBox)  checkboxLayout.findViewById(R.id.checkContacto);
        checkAbusoVerbal = (CheckBox) checkboxLayout.findViewById(R.id.checkAbusoVerbal);
        checkSilbidos = (CheckBox) checkboxLayout.findViewById(R.id.checkSilbidos);
        checkMiradas = (CheckBox) checkboxLayout.findViewById(R.id.checkMiradas);
        checkExposicion = (CheckBox) checkboxLayout.findViewById(R.id.checkExposicion);
        checkInsinuacion = (CheckBox) checkboxLayout.findViewById(R.id.checkInsinuacion);
        checkGestos = (CheckBox) checkboxLayout.findViewById(R.id.checkGestos);
        helpBuilder.setView(checkboxLayout);

        final AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser puser = ParseUser.getCurrentUser();
                user = puser.getString("username");
                getTypes();
                if (types.size() == 0) {
                    Toast.makeText(MapActivity.this, "Debes seleccionar al menos un tipo de acoso", Toast.LENGTH_LONG).show();
                } else {
                    addCase(user, lat, lng, types);
                    helpDialog.cancel();
                }


            }
        });
    }

    public void getTypes(){
        types.clear();
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
        po.put("year", year);
        po.put("month",month);
        po.put("day",day);
        po.put("week", week);

        final ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            po.saveInBackground();
            Toast.makeText(MapActivity.this, "Reporte registrado", Toast.LENGTH_LONG).show();
        } else {
            PasoSeguro.pendingCases.add(po);
            Toast.makeText(MapActivity.this, "Reporte en cola", Toast.LENGTH_LONG).show();

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
    public void onMapClick(LatLng latLng) {
        lat = latLng.latitude;
        lng = latLng.longitude;
        if(myMarker!=null){
            myMarker.remove();
        }

        myMarker= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.boardg)));
        mov = true;
    }


    public void getCases() {
        double lat, lng;
        allObjects = new ArrayList<ParseObject>();
        ParseGeoPoint location;
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
        getCases();
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

                   // mMap.addCircle(co);
                    circles.add(co);
                    lTaken.addAll(lTakenTmp);
                }
                lTakenTmp.clear();
                count = 0;
            }
        }
    }

    private void showPopUpInfoAlert() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Conoce:");
        helpBuilder.setMessage("Te avisaremos cuando estes cerca de una zona peligrosa");
        final AlertDialog helpDialog = helpBuilder.create();


        helpBuilder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                helpDialog.cancel();
            }
        });

        // Remember, create doesn't show the dialog

        helpDialog.show();

    }

}
