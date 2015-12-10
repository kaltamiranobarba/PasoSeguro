package com.example.altam.pasoseguro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by altam on 8/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    Context ctx;
    public DBHelper(Context c ){
        super(c, "db_prueba", null, 1);
        ctx  = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(idUser INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT NOT NULL, pass TEXT NOT NULL, email TEXT NOT NULL, age TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
