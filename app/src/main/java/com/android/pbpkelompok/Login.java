package com.android.pbpkelompok;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private DataBaseHelperLogin db;
    //shared pref
    public static final String SHARED_PREF_NAME = "myPref";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register.newInstance().show(getSupportFragmentManager(), Register.TAG);
            }
        });

        db = new DataBaseHelperLogin(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = etUsername.getText().toString();
                String getPassword = etPassword.getText().toString();

                if (getPassword.isEmpty() || getPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username dan password wajib diisi!", Toast.LENGTH_LONG).show();
                }else{
                    Boolean masuk = db.checkLogin(getUsername, getPassword);
                    if (masuk == true){
                        Boolean updateSession = db.upgradeSession("ada", 1);
                        if (updateSession == true){
                            Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean("masuk", true);
                            editor.apply();
                            Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(dashboard);
                            finish();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Username dan password tidak sesuai", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
