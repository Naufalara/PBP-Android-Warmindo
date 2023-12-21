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
    private String statustransaksi,metodebayar;
    private TextView textStatus;
    private ArrayList<String> listpesanan;
    private Button btnUbahStatus, btnSelesaikanPesanan,btnKembali_Transaksi,btnLogout;
    private Spinner spinnerMetodePembayaran;
    private ArrayAdapter<String> adapter;
    private DataBaseHelperLogin dbHelper;
    public static final String SHARED_PREF_NAME = "myPref";
    private SharedPreferences sharedPreferences;

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
        btnLogout = findViewById(R.id.btnLogout);
        btnKembali_Transaksi = findViewById(R.id.btnKembali_Transaksi);

        listpesanan = new ArrayList<>();
        statustransaksi = new String();
        metodebayar = new String();

        // Mendapatkan ID transaksi dari intent
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("TRANSAKSI_ID")) {
            String selectedTransactionId = intent.getStringExtra("TRANSAKSI_ID");
            Cursor cursorTransaksiDetail = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM DetailTransaksi WHERE idtransaksi=?", new String[]{String.valueOf(selectedTransactionId)});
            if (cursorTransaksiDetail != null) {
                while (cursorTransaksiDetail.moveToNext()) {
                    String namamenu = cursorTransaksiDetail.getString(cursorTransaksiDetail.getColumnIndex("namamenu"));
                    String status = cursorTransaksiDetail.getString(cursorTransaksiDetail.getColumnIndex("status"));

                    String menu = "menu: " + namamenu;
                    listpesanan.add(menu);
                    statustransaksi = status;
                    textStatus.setText("Status: " + statustransaksi);
                }
            }
            Cursor cursorTransaksi = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM Transaksi WHERE idtransaksi=?", new String[]{String.valueOf(selectedTransactionId)});
            if (cursorTransaksi != null) {
                while (cursorTransaksi.moveToNext()) {
                    String status = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("status"));
                    String metodepembayaran = cursorTransaksi.getString(cursorTransaksi.getColumnIndex("metodepembayaran"));
                    statustransaksi = status;
                    metodebayar = metodepembayaran;
                }
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listpesanan);
            listRekapPesanan.setAdapter(adapter);

            btnUbahStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ganti status dari aktif menjadi batal, dan sebaliknya
                    if (statustransaksi.equals("aktif")) {
                        statustransaksi = "batal";
                    } else if (statustransaksi.equals("batal")) {
                        statustransaksi = "aktif";
                    }

                    // Update status yang terlihat pada TextView
                    textStatus.setText("Status: " + statustransaksi);

                    // Simpan perubahan status ke dalam database sesuai id transaksi
                    // Misalkan, perubahan status disimpan dalam kolom 'status' di tabel DetailTransaksi
                    dbHelper.updateTransactionStatus(selectedTransactionId, statustransaksi);

                    // Tampilkan pesan bahwa status telah diubah
                    Toast.makeText(TransaksiDetail.this, "Status diubah menjadi " + statustransaksi, Toast.LENGTH_SHORT).show();
                }
            });


            // Contoh data untuk pilihan metode pembayaran
            String[] metodePembayaran = {"cash","kartu debit","kartu kredit", "qris"};

            // Inisialisasi adapter untuk spinner metode pembayaran
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodePembayaran);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMetodePembayaran.setAdapter(spinnerAdapter);

            btnSelesaikanPesanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mendapatkan metode pembayaran yang dipilih dari spinner
                    String metodePembayaranTerpilih = spinnerMetodePembayaran.getSelectedItem().toString();

                    // Update metode pembayaran sesuai dengan yang dipilih pada transaksi yang terkait
                    dbHelper.updateTransactionPaymentMethod(selectedTransactionId, metodePembayaranTerpilih);

                    // Tampilkan pesan bahwa metode pembayaran telah diubah
                    Toast.makeText(TransaksiDetail.this, "Pesanan selesai dengan metode pembayaran " + metodePembayaranTerpilih, Toast.LENGTH_SHORT).show();
                    Intent Kembali = new Intent(getApplicationContext(), TransaksiActivity.class);
                    startActivity(Kembali);
                    finish();
                }
            });
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean updateSeesion = dbHelper.upgradeSession("kosong", 1);
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
        btnKembali_Transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Kembali = new Intent(getApplicationContext(), TransaksiActivity.class);
                startActivity(Kembali);
                finish();
            }
        });

    }
}
