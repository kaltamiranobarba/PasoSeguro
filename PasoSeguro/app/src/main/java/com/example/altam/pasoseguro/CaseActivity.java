package com.example.altam.pasoseguro;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class CaseActivity extends AppCompatActivity {
    EditText et_caseTitle, et_caseDescription;
    Bundle extras;
    String title, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_caseTitle = (EditText) findViewById(R.id.et_caseTitle);
        et_caseDescription = (EditText) findViewById(R.id.et_caseDescription);
        extras = getIntent().getExtras();
        title = extras.getString("title");
        et_caseTitle.setText(title);

        getCaseInfo();

        et_caseDescription.setText(description);
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

    public void getCaseInfo(){
        ParseObject po;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereEqualTo("title", title);
        try {
            po = query.getFirst();
            description = po.getString("description");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
