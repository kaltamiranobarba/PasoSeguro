package com.example.altam.pasoseguro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LoginActivity extends Activity implements View.OnClickListener {
    Button b_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        b_enter = (Button) findViewById(R.id.B_enter);
        b_enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
