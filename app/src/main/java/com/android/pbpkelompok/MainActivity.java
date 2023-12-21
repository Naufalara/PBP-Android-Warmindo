package com.android.pbpkelompok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnKeluar, btnMasuk;
    private TextView tvNama, tvRole, tvStatus;

    private DataBaseHelperLogin db;

    public static final String SHARED_PREF_NAME = "myPref";

    private SharedPreferences sharedPreferences;

    private int shift; // menyimpan informasi shift (1 atau 2)
    private String tanggal; // menyimpan informasi tanggal

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnKeluar = findViewById(R.id.btnKeluar);
        tvNama = findViewById(R.id.tvNama);
        tvStatus = findViewById(R.id.tvStatus);
        tvRole = findViewById(R.id.tvRole);
        btnMasuk = findViewById(R.id.btnMasuk);

        db = new DataBaseHelperLogin(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String usernamefrompref = sharedPreferences.getString("username", "default");
        Pengguna pengguna = db.getUserByUsername(usernamefrompref);
        if (pengguna != null) {
            tvNama.setText("Nama: " + pengguna.getNamaPengguna());
            String roleName = db.getRoleNameById(pengguna.getIdRole());
            tvRole.setText("Role: " + roleName);
            tvStatus.setText("Status: " + pengguna.getStatus());
        }

        Boolean checksession = db.checkSession("ada");
        if (checksession == false) {
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
            finish();
        }

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShiftAndTanggal();

                // Validasi sebelum membuka TransaksiActivity
                if (shift != -1) {
                    Intent masuk_transaksi = new Intent(getApplicationContext(), TransaksiActivity.class);
                    masuk_transaksi.putExtra("shift", shift);
                    masuk_transaksi.putExtra("tanggal", tanggal);
                    startActivity(masuk_transaksi);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Shift saat ini masih off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updateSeesion = db.upgradeSession("kosong", 1);
                if (updateSeesion == true) {
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

    private void setShiftAndTanggal() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 10 && hourOfDay < 17) {
            shift = 0;
        } else if (hourOfDay >= 17 && hourOfDay < 23) {
            shift = 1;
        } else {
            shift = -1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tanggal = dateFormat.format(calendar.getTime());
    }
}
