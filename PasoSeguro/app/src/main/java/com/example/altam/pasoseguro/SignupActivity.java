package com.example.altam.pasoseguro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity implements View.OnClickListener {
    Button btn_signup;
    EditText et_signupUser, et_signupPass, et_signupEmail, et_signupAge, et_signupPass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);





        et_signupUser = (EditText) findViewById(R.id.et_signupUser);
        et_signupPass = (EditText) findViewById(R.id.et_signupPass);
        et_signupEmail = (EditText) findViewById(R.id.et_signupEmail);
        et_signupAge = (EditText) findViewById(R.id.et_signupAge);
        et_signupPass2 = (EditText) findViewById(R.id.et_signupPass2);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        final String user, pass, email, age, pass2;

        user = et_signupUser.getText().toString();
        pass = et_signupPass.getText().toString();
        email = et_signupEmail.getText().toString();
        age = et_signupAge.getText().toString();
        pass2 = et_signupPass2.getText().toString();
        ParseUser pUser = new ParseUser();
        pUser.setUsername(user);
        pUser.setPassword(pass);
        pUser.setEmail(email);
        pUser.put("age", age);

        if(user.matches("") || pass.matches("") || email.matches("") || pass2.matches("") || age.matches("") ){
            Toast.makeText(getApplicationContext(), "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }else if(!pass.equals(pass2)){
            Toast.makeText(getApplicationContext(),"Las contraseñas no son iguales"  , Toast.LENGTH_LONG).show();
        }else{
            pUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Usuario registrado!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, MapActivity.class);
                        startActivity(intent);
                    } else {

                        if(e.getMessage().equals("invalid email address")){
                            Toast.makeText(getApplicationContext(),"Email no valido", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
