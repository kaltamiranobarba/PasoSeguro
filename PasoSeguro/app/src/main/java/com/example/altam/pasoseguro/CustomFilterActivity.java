package com.example.altam.pasoseguro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomFilterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_fromDate, et_toDate;
    Bundle extras;
    DatePickerDialog fromDatePickerDialog;
    DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_fromDate = (EditText) findViewById(R.id.et_fromDate);
        et_toDate = (EditText) findViewById(R.id.et_toDate);
        extras = getIntent().getExtras();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

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
        et_toDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_fromDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_toDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if(v == et_fromDate) {
            fromDatePickerDialog.show();
        } else if(v == et_toDate) {
            toDatePickerDialog.show();
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
}
