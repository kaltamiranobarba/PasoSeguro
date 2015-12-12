package com.example.altam.pasoseguro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class SignupActivity extends Activity implements View.OnClickListener {
    Button btn_signup;
    EditText et_signupUser, et_signupPass, et_signupEmail, et_signupAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Parse.initialize(this, "gtUrodDDuFstbcyGN9gkyBGTDliHmtk5ADsMzGjH","3rKd6641H0J5ILak2yPUiIBmOjsQxtc3PlYODIWG" );






        et_signupUser = (EditText) findViewById(R.id.et_signupUser);
        et_signupPass = (EditText) findViewById(R.id.et_signupPass);
        et_signupEmail = (EditText) findViewById(R.id.et_signupEmail);
        et_signupAge = (EditText) findViewById(R.id.et_signupAge);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        String user, pass, email, age;

        user = et_signupUser.getText().toString();
        pass = et_signupPass.getText().toString();
        email = et_signupEmail.getText().toString();
        age = et_signupAge.getText().toString();
        ParseObject po = new ParseObject("User");
        po.put("user",user);
        po.put("pass", pass);
        po.put("email", email);
        po.put("age", age);
        po.saveInBackground();

        Toast.makeText(getApplicationContext(), "Usuario registrado!",
            Toast.LENGTH_LONG).show();

    }


}
