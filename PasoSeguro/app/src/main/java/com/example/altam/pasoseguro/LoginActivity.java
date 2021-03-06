package com.example.altam.pasoseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.*;

import java.io.Serializable;

public class LoginActivity extends Activity implements View.OnClickListener {
    Button btn_loginEnter;
    EditText et_loginUser, et_loginPass;
    TextView tvRegister;
    String passRetrieved, userRetrieved, emailRetrieved, ageRetrieved;
    Boolean retrieved =false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        btn_loginEnter = (Button) findViewById(R.id.btn_loginEnter);
        et_loginPass = (EditText) findViewById(R.id.et_loginPass);
        et_loginUser = (EditText) findViewById(R.id.et_loginUser);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(this);
        btn_loginEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {



        switch (v.getId()) {
            case R.id.btn_loginEnter:
            {
                final String user, pass;
                user = et_loginUser.getText().toString();
                pass= et_loginPass.getText().toString();
                ParseUser.logInInBackground(user, pass, new LogInCallback() {
                    public void done(ParseUser u, ParseException e) {
                        if (u != null) {
                            Toast.makeText(getApplicationContext(), "Inicio exitoso!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o clave erróneo", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            }
            case R.id.tvRegister:
            {
                Intent signUpScreen = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signUpScreen);
                break;
            }

        }
    }


}
