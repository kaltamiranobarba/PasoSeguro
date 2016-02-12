package com.example.altam.pasoseguro;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {
    CheckBox sound, vibrate;
    Button save, cancel;
    Spinner lv ;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        lv =(Spinner) findViewById(R.id.listView);
        ArrayList<String> myStrings = new ArrayList<String>();

        myStrings.add("Bajo");
        myStrings.add("Medio");
        myStrings.add("Fuerte");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, myStrings);
        lv.setAdapter(adapter);
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PasoSeguro.intensity = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sound = (CheckBox)findViewById(R.id.sound);
        vibrate = (CheckBox)findViewById(R.id.vibrate);
        save = (Button) findViewById(R.id.btn_alarmSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound.isChecked())
                    PasoSeguro.sounds = true;
                else
                    PasoSeguro.sounds = false;
                if(vibrate.isChecked())
                    PasoSeguro.vibrates = true;
                else
                    PasoSeguro.vibrates = false;

                Intent i = new Intent(AlarmActivity.this, MapActivity.class);
                startActivity(i);
            }
        });

        cancel = (Button) findViewById(R.id.btn_alarmCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AlarmActivity.this, MapActivity.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
