package com.example.altam.pasoseguro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        et_caseTitle = (EditText) findViewById(R.id.et_caseTitle);
        et_caseDescription = (EditText) findViewById(R.id.et_caseDescription);
        extras = getIntent().getExtras();
        title = extras.getString("title");
        et_caseTitle.setText(title);

        getCaseInfo();

        et_caseDescription.setText(description);
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
