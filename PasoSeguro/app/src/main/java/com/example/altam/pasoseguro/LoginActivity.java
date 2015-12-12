package com.example.altam.pasoseguro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;

public class LoginActivity extends Activity implements View.OnClickListener {
    Button btn_loginEnter;
    EditText et_loginUser, et_loginPass;
    String passRetrieved, userRetrieved, emailRetrieved, ageRetrieved;
    Boolean retrieved =false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Parse.initialize(this, "gtUrodDDuFstbcyGN9gkyBGTDliHmtk5ADsMzGjH", "3rKd6641H0J5ILak2yPUiIBmOjsQxtc3PlYODIWG");
        btn_loginEnter = (Button) findViewById(R.id.btn_loginEnter);
        et_loginPass = (EditText) findViewById(R.id.et_loginPass);
        et_loginUser = (EditText) findViewById(R.id.et_loginUser);
        btn_loginEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user, pass;
        user = et_loginUser.getText().toString();
        pass= et_loginPass.getText().toString();

            if (login(user, pass)) {
                Toast.makeText(getApplicationContext(), "LOGIN EXITOSO!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Usuario o clave erróneo", Toast.LENGTH_LONG).show();
            }
        }





    public boolean login(String user, String pass){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("user",user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("objeto","NO SE RECUPERO");

                } else {
                    userRetrieved = object.getString("User");
                    passRetrieved = object.getString("pass");
                    emailRetrieved = object.getString("email");
                    ageRetrieved = object.getString("age");
                    retrieved = true;
                }
            }

        });
        if(retrieved){
            if(pass.equals(passRetrieved))
                return true;
            else
                return false;
        }
        return false;
    }


}
