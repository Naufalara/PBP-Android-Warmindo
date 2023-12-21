package com.android.pbpkelompok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TransaksiDetail extends AppCompatActivity {
    private ListView listRekapPesanan;
    private String statustransaksi, metodebayar;
    private TextView textStatus;
    private ArrayList<String> listpesanan;
    private Button btnUbahStatus, btnSelesaikanPesanan, btnKembali_Transaksi, btnLogout;
    private Spinner spinnerMetodePembayaran, spinnerUbahStatus;
    private ArrayAdapter<String> adapter;
    private DataBaseHelperLogin dbHelper;
    public static final String SHARED_PREF_NAME = "myPref";
    private SharedPreferences sharedPreferences;
    private String newStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_detail);
        dbHelper = new DataBaseHelperLogin(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        listRekapPesanan = findViewById(R.id.listRekapPesanan);
        textStatus = findViewById(R.id.textStatus);
        btnUbahStatus = findViewById(R.id.btnUbahStatus);
        btnSelesaikanPesanan = findViewById(R.id.btnSelesaikanPesanan);
        spinnerMetodePembayaran = findViewById(R.id.spinnerMetodePembayaran);
        spinnerUbahStatus = findViewById(R.id.spinnerUbahStatus);
        btnLogout = findViewById(R.id.btnLogout);
        btnKembali_Transaksi = findViewById(R.id.btnKembali_Transaksi);

        listpesanan = new ArrayList<>();
        statustransaksi = new String();
        metodebayar = new String();

        String idtransaksi = getIntent().getStringExtra("idtransaksi");

        // Mendapatkan data dari DetailTransaksi
        Cursor cursorTransaksiDetail = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM DetailTransaksi WHERE idtransaksi=?", new String[]{idtransaksi});
        if (cursorTransaksiDetail != null) {
            while (cursorTransaksiDetail.moveToNext()) {
                String namamenu = cursorTransaksiDetail.getString(cursorTransaksiDetail.getColumnIndex("namamenu"));
                String jumlah = cursorTransaksiDetail.getString(cursorTransaksiDetail.getColumnIndex("jumlah"));

                // Menambahkan data ke listpesanan
                listpesanan.add("Menu \t\t\t\t\t\t: " + namamenu);
                listpesanan.add("Jumlah \t\t\t\t\t: " + jumlah);
            }
        }

        // Mendapatkan data dari Transaksi
        Cursor cursorTransaksi = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM Transaksi WHERE idtransaksi=?", new String[]{idtransaksi});
        if (cursorTransaksi != null && cursorTransaksi.moveToFirst()) {
            String idTransaksi = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("idtransaksi"));
            double total = cursorTransaksi.getDouble(cursorTransaksi.getColumnIndex("total"));
            String tanggaltransaksi = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("tanggal"));
            String waktu = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("waktu"));
            String status = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("status"));
            String metodepembayaran = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("metodepembayaran"));

            // Menambahkan data Transaksi ke listpesanan
            listpesanan.add("ID Transaksi : " + idTransaksi);
            listpesanan.add("Total \t\t\t\t\t\t\t: " + total);
            listpesanan.add("Status \t\t\t\t\t\t : " + status);
            listpesanan.add("Tanggal Transaksi : " + tanggaltransaksi + " " + waktu);
            listpesanan.add("Metode Pembayaran : " + metodepembayaran);
        }

        // Set adapter untuk listRekapPesanan
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listpesanan);
        listRekapPesanan.setAdapter(adapter);

        // Inisialisasi adapter untuk spinner ubah status
        String[] statusOptions = {"baru", "diproses", "disajikan", "selesai"};
        ArrayAdapter<String> spinnerUbahStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        spinnerUbahStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUbahStatus.setAdapter(spinnerUbahStatusAdapter);

        // Inisialisasi adapter untuk spinner metode pembayaran
        String[] metodePembayaran = {"cash", "kartu debit", "kartu kredit", "qris"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodePembayaran);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetodePembayaran.setAdapter(spinnerAdapter);

        btnUbahStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = spinnerUbahStatus.getSelectedItem().toString();

                // Update status transaksi
                dbHelper.updateStatusTransaksi(idtransaksi, status);

                // Tampilkan pesan bahwa metode pembayaran dan status telah diubah
                Toast.makeText(TransaksiDetail.this, "Status pesanan diubah menjadi " + status, Toast.LENGTH_SHORT).show();

                // Lakukan refresh halaman
                recreate();
            }
        });



        btnSelesaikanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan status dari listpesanan
                String statusTransaksi = "";

                for (String pesanan : listpesanan) {
                    if (pesanan.startsWith("Status")) {
                        statusTransaksi = pesanan.substring(pesanan.indexOf(":") + 1).trim();
                        break;
                    }
                }
                // Lakukan sesuatu dengan nilai statusTransaksi yang telah diperoleh
                if (statusTransaksi.equals("selesai")) {
                    // Lakukan tindakan jika status transaksi adalah "selesai"
                    Toast.makeText(TransaksiDetail.this, "Pesanan " + statusTransaksi + " Terima Kasih", Toast.LENGTH_SHORT).show();
                    Intent kembali = new Intent(getApplicationContext(), TransaksiActivity.class);
                    startActivity(kembali);
                    finish();
                } else {
                    Toast.makeText(TransaksiDetail.this, "Pesanan " + statusTransaksi, Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updateSession = dbHelper.upgradeSession("kosong", 1);
                if (updateSession) {
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

        btnKembali_Transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kembali = new Intent(getApplicationContext(), TransaksiActivity.class);
                startActivity(kembali);
                finish();
            }
        });
    }

}
