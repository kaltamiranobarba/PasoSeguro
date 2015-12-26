package com.example.altam.pasoseguro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyCasesActivity extends AppCompatActivity {
    ListView lv_myCases;
    public   ArrayList<String>  al;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cases);
        user = ParseUser.getCurrentUser().getUsername();

        lv_myCases = (ListView) findViewById(R.id.lv_myCases);
        al = new ArrayList<String>();
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,al);
        lv_myCases.setAdapter(aa);
        getMyCases();
        lv_myCases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyCasesActivity.this, CaseActivity.class);
                intent.putExtra("title",al.get(position));
                startActivity(intent);
            }
        });

    }


    private void getMyCases(){
        List<ParseObject> li;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cases");
        query.whereEqualTo("user",user);
        query.orderByDescending("createdAt");

        try {
            li = query.find();
            for(ParseObject po : li){
                String tmp   = po.getString("title");
                al.add(tmp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
