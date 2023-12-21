package com.android.pbpkelompok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TransaksiActivity extends AppCompatActivity {

    private Button btnTambahTransaksi, btnKembali_Dashboard, btnLogout;
    private ListView listTransaksi;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> transaksiList;
    private DataBaseHelperLogin dbHelper;
    public static final String SHARED_PREF_NAME = "myPref";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_main);

        dbHelper = new DataBaseHelperLogin(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        int shift = getIntent().getIntExtra("shift", 0);
        String tanggal = getIntent().getStringExtra("tanggal");

        btnTambahTransaksi = findViewById(R.id.btnTambahTransaksi);
        btnLogout = findViewById(R.id.btnLogout);
        btnKembali_Dashboard = findViewById(R.id.btnKembali_Dashboard);
        listTransaksi = findViewById(R.id.listTransaksi);

        transaksiList = new ArrayList<>();

        Cursor cursorTransaksi = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM Transaksi WHERE tanggal=? AND shift=" + shift, new String[]{tanggal});

        if (cursorTransaksi.moveToFirst()) {
            do {
                String idTransaksi = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("idtransaksi"));
                String waktu = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("waktu"));
                double total = cursorTransaksi.getDouble(cursorTransaksi.getColumnIndex("total"));

                Cursor cursorDetail = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM DetailTransaksi WHERE idtransaksi=?", new String[]{String.valueOf(idTransaksi)});
                StringBuilder detailTransaksi = new StringBuilder();
                if (cursorDetail.moveToFirst()) {
                    do {
                        String namaMenu = cursorDetail.getString(cursorDetail.getColumnIndex("namamenu"));
                        int jumlah = cursorDetail.getInt(cursorDetail.getColumnIndex("jumlah"));

                        detailTransaksi.append(namaMenu).append(" (").append(jumlah).append("), ");
                    } while (cursorDetail.moveToNext());
                }
                cursorDetail.close();

                String transaksi = "ID: " + idTransaksi + ", Waktu: " + waktu + ", Total: " + total + "\nDetail: " + detailTransaksi.toString();
                transaksiList.add(transaksi);
            } while (cursorTransaksi.moveToNext());
        }
        cursorTransaksi.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transaksiList);
        listTransaksi.setAdapter(adapter);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updateSeesion = dbHelper.upgradeSession("kosong", 1);
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

        btnKembali_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Kembali = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Kembali);
                finish();
            }
        });
    }
}

