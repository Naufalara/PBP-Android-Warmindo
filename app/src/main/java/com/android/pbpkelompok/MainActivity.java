package com.android.pbpkelompok;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnKeluar;
    private TextView tvNama,tvRole,tvStatus;

    private DataBaseHelperLogin db;

    public static final String SHARED_PREF_NAME = "myPref";

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnKeluar = findViewById(R.id.btnKeluar);
        tvNama = findViewById(R.id.tvNama);
        tvStatus = findViewById(R.id.tvStatus);
        tvRole = findViewById(R.id.tvRole);

        db = new DataBaseHelperLogin(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String usernamefrompref = sharedPreferences.getString("username","default");
        Pengguna pengguna = db.getUserByUsername(usernamefrompref);

        if(pengguna != null){
            tvNama.setText("Nama: " + pengguna.getNamaPengguna());
            tvRole.setText("Id Role: " + pengguna.getIdRole());
            tvStatus.setText("Status: "+ pengguna.getStatus());
        }

        Boolean checksession = db.checkSession("ada");
        if (checksession == false){
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
            finish();
        }

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updateSeesion = db.upgradeSession("kosong", 1);
                if (updateSeesion == true){
                    Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("masuk", false);
                    editor.apply();

                    Intent keluar = new Intent(getApplicationContext(), Login.class);
                    startActivity(keluar);
                    finish();
                }
            }
        });

    }
}