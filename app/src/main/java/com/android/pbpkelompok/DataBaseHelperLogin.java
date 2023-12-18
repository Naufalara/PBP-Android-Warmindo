package com.android.pbpkelompok;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelperLogin extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB_LOGIN";
    public DataBaseHelperLogin(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE session (id integer PRIMARY KEY, login text)");
        db.execSQL("CREATE TABLE pengguna (id integer PRIMARY KEY AUTOINCREMENT, username text,password text,namapengguna text,idrole text,status text)");
        db.execSQL("INSERT INTO session(id,login) VALUES (1,'kosong')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS session");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);

    }
    //check session
    public boolean checkSession(String value){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM session WHERE login = ?",new String[]{value});
        if (cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    //upgrade session
    public Boolean upgradeSession(String value,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login",value);
        long update = db.update("session",values,"id="+id,null);

        if(update == -1){
            return false;
        }else{
            return true;
        }
    }

    //input user
    public boolean simpanUser(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",password);

        long insert = db.insert("user",null,values);
        if (insert == -1){
            return false;
        }else {
            return true;
        }
    }

    //check login
    public boolean checkLogin(String username,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? AND password=?",new String[]{username,password});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
}
