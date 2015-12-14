package com.example.altam.pasoseguro;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btn_login, btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            break;

            case R.id.btn_signup:
                Intent intent2 = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent2);
            break;

        }
    }
}
