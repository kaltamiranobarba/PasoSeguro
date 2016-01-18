package com.example.altam.pasoseguro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CustomFilterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_fromDate;
    Bundle extras;
    DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String title, description;
    int yearF, dayF, monthF;
    Button filter;
    CheckBox checkAbusoVerbal, checkSilbidos, checkContacto, checkMiradas, checkInsinuacion, checkExposicion, checkGestos;
    ArrayList<String> types = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        checkContacto = (CheckBox) findViewById(R.id.checkContacto);
        checkAbusoVerbal = (CheckBox) findViewById(R.id.checkAbusoVerbal);
        checkSilbidos = (CheckBox) findViewById(R.id.checkSilbidos);
        checkMiradas = (CheckBox) findViewById(R.id.checkMiradas);
        checkExposicion = (CheckBox) findViewById(R.id.checkExposicion);
        checkInsinuacion = (CheckBox) findViewById(R.id.checkInsinuacion);
        checkGestos = (CheckBox) findViewById(R.id.checkGestos);

        et_fromDate = (EditText) findViewById(R.id.et_fromDate);
        extras = getIntent().getExtras();
        filter = (Button)findViewById(R.id.btn_siguiente);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomFilterActivity.this, MapActivity.class);
                intent.putExtra("yearF",yearF);
                intent.putExtra("monthF",monthF);
                intent.putExtra("dayF",dayF);
                getTypes();
                for ( String s: types) {
                    intent.putExtra(s,s);
                }
                startActivity(intent);
            }
        });
        setDateTimeField();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                //Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDateTimeField() {
        et_fromDate.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                yearF = year;
                monthF = monthOfYear+1;
                dayF = dayOfMonth;
                Toast.makeText(CustomFilterActivity.this, yearF+" "+monthF+" "+dayF, Toast.LENGTH_LONG).show();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_fromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public void onClick(View v) {
        if(v == et_fromDate) {
            fromDatePickerDialog.show();
        }
    }

    /*public void getCaseInfo(){
        ParseObject po;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereEqualTo("title", title);
        try {
            po = query.getFirst();
            description = po.getString("description");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    public void getTypes(){
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

}
