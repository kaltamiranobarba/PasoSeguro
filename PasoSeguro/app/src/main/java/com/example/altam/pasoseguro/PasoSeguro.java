package com.example.altam.pasoseguro;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by altam on 13/12/2015.
 */
public class PasoSeguro extends Application {

    public static ArrayList<ParseObject> pendingCases = new ArrayList<ParseObject>();

    @Override
    public void onCreate(){
        super .onCreate();
        Parse.initialize(this, "gtUrodDDuFstbcyGN9gkyBGTDliHmtk5ADsMzGjH", "3rKd6641H0J5ILak2yPUiIBmOjsQxtc3PlYODIWG");

    }
}
