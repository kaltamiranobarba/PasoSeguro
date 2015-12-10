package com.example.altam.pasoseguro;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignupActivity extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner spAge;
    ArrayAdapter<String> ages;
    private String[] arraySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spAge = (Spinner) findViewById(R.id.sp_signupAge);
        createAges();
        ages = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        spAge.setAdapter(ages);

    }

    private void createAges(){
        this.arraySpinner = new String[81];
        int i;
        for(i=10; i <= 90 ; i++){
            this.arraySpinner[i-10] = Integer.toString(i);

       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
