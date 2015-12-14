package com.example.altam.pasoseguro;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by altam on 13/12/2015.
 */
public class PasoSeguro extends Application {

    @Override
    public void onCreate(){
        super .onCreate();
        Parse.initialize(this, "gtUrodDDuFstbcyGN9gkyBGTDliHmtk5ADsMzGjH", "3rKd6641H0J5ILak2yPUiIBmOjsQxtc3PlYODIWG");

    }
}
