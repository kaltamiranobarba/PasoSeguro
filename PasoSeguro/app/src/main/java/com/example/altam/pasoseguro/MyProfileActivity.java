package com.example.altam.pasoseguro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MyProfileActivity extends AppCompatActivity {
    String user;
    ParseObject po;
    EditText txt_profileUser, txt_profilePass, txt_profileAge, txt_ProfileEmail;
    Button btn_profileUpdate;
    ParseUser puser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        txt_profileAge = (EditText)findViewById(R.id.txt_profileAge);
        txt_profileUser = (EditText)findViewById(R.id.txt_profileUser);
        txt_ProfileEmail = (EditText)findViewById(R.id.txt_profileEmail);
        txt_profilePass = (EditText)findViewById(R.id.txt_profilePass);

        ParseUser puser = ParseUser.getCurrentUser();
        user = puser.getString("username");

        txt_profileUser.setText(puser.getUsername());
        txt_profileAge.setText(puser.getString("age"));
        txt_ProfileEmail.setText(puser.getEmail());

        btn_profileUpdate = (Button) findViewById(R.id.btn_profileUpdate);
        btn_profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass="", age, email;
                ParseUser puser = ParseUser.getCurrentUser();
                user = txt_profileUser.getText().toString();
                pass = txt_profilePass.getText().toString();
                email = txt_ProfileEmail.getText().toString();
                age = txt_profileAge.getText().toString();
                if(pass!=""){
                    puser.setPassword(pass);
                }
                puser.setUsername(user);
                puser.setEmail(email);
                puser.put("age", age);
                try {
                    puser.save();
                    Intent i = new Intent(MyProfileActivity.this, MapActivity.class);
                    startActivity(i);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }




}
