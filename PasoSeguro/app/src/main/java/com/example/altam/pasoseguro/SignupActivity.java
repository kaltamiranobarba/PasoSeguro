package com.example.altam.pasoseguro;

import android.os.Bundle;
import android.app.Activity;

import com.parse.Parse;
import com.parse.ParseObject;

public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

       ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

    }




}
