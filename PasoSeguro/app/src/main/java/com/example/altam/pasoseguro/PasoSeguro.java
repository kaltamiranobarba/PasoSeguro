package com.example.altam.pasoseguro;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by altam on 13/12/2015.
 */
public class PasoSeguro extends Application {

    public static ArrayList<ParseObject> pendingCases = new ArrayList<ParseObject>();
    public static int NOTIFICATION_ID;
    public static boolean alarmActivated;
    public static boolean vibrate;
    public static boolean off=false;
    public static boolean started = off;
    public static int yearS, dayS, monthS;
    public static double lat=0, lng=0;
    public static boolean vibrates=true, sounds=true;
    public static int intensity = 1;
    public static ArrayList<String> types = new ArrayList<String>();
    @Override
    public void onCreate(){
        super .onCreate();
        Parse.initialize(this, "gtUrodDDuFstbcyGN9gkyBGTDliHmtk5ADsMzGjH", "3rKd6641H0J5ILak2yPUiIBmOjsQxtc3PlYODIWG");
        Calendar c = Calendar.getInstance();
        this.yearS = c.get(Calendar.YEAR);
        this.monthS = c.get(Calendar.MONTH)+1;
        this.dayS = c.get(Calendar.DAY_OF_MONTH);

    }
}
