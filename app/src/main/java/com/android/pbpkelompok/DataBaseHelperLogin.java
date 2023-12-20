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
        String createTablePengguna = "CREATE TABLE Pengguna (" +
                "idpengguna INTEGER PRIMARY KEY," +
                "username TEXT," +
                "password TEXT," +
                "namapengguna TEXT," +
                "idrole TEXT," +
                "status TEXT," +
                "foto BLOB DEFAULT NULL)";
        db.execSQL(createTablePengguna);
        db.execSQL("INSERT INTO Pengguna(idpengguna, username, password, namapengguna, idrole, status, foto) VALUES " +
                "(1, 'user1', 'pass1', 'User Satu', 'role1', 'active', null), " +
                "(2, 'user2', 'pass2', 'User Dua', 'role2', 'active', null), " +
                "(3, 'user3', 'pass3', 'User Tiga', 'role1', 'inactive', null)");


        // Tabel AktivitasPengguna
        String createTableAktivitasPengguna = "CREATE TABLE AktivitasPengguna (" +
                "idaktivitas INTEGER PRIMARY KEY," +
                "tanggal TEXT," +
                "waktu TEXT," +
                "idpengguna INTEGER," +
                "aktivitas TEXT," +
                "FOREIGN KEY(idpengguna) REFERENCES Pengguna(idpengguna))";
        db.execSQL(createTableAktivitasPengguna);
        db.execSQL("INSERT INTO AktivitasPengguna(idaktivitas, tanggal, waktu, idpengguna, aktivitas) VALUES " +
                "(1, '2023-01-01', '09:00', 1, 'Login'), " +
                "(2, '2023-01-02', '10:30', 2, 'Logout'), " +
                "(3, '2023-01-03', '11:45', 1, 'Update Profil')");


        // Tabel Role
        String createTableRole = "CREATE TABLE Role (" +
                "idrole INTEGER PRIMARY KEY," +
                "role TEXT," +
                "status TEXT)";
        db.execSQL(createTableRole);
        db.execSQL("INSERT INTO Role(idrole, role, status) VALUES " +
                "(1, 'Admin', 'active'), " +
                "(2, 'Karyawan', 'active'), " +
                "(3, 'Pengunjung', 'inactive')");


        // Tabel Warung
        String createTableWarung = "CREATE TABLE Warung (" +
                "idwarung INTEGER PRIMARY KEY," +
                "namawarung TEXT," +
                "logo BLOB," +
                "gambar BLOB)";
        db.execSQL(createTableWarung);
        db.execSQL("INSERT INTO Warung(idwarung, namawarung, logo, gambar) VALUES " +
                "(1, 'Warung A', null, null), " +
                "(2, 'Warung B', null, null), " +
                "(3, 'Warung C', null, null)");


        // Tabel Meja
        String createTableMeja = "CREATE TABLE Meja (" +
                "idmeja INTEGER PRIMARY KEY," +
                "idwarung INTEGER," +
                "kodemeja TEXT," +
                "FOREIGN KEY(idwarung) REFERENCES Warung(idwarung))";
        db.execSQL(createTableMeja);
        db.execSQL("INSERT INTO Meja(idmeja, idwarung, kodemeja) VALUES " +
                "(1, 1, 'A001'), " +
                "(2, 1, 'A002'), " +
                "(3, 2, 'B001')");


        // Tabel Menu
        String createTableMenu = "CREATE TABLE Menu (" +
                "idmenu INTEGER PRIMARY KEY," +
                "namamenu TEXT," +
                "kategori TEXT," +
                "harga REAL," +
                "gambar BLOB DEFAULT NULL)";
        db.execSQL(createTableMenu);
        db.execSQL("INSERT INTO Menu(idmenu, namamenu, kategori, harga, gambar) VALUES " +
                "(1, 'Menu A', 'Makanan', 20.0, null), " +
                "(2, 'Menu B', 'Minuman', 10.0, null), " +
                "(3, 'Menu C', 'Makanan', 25.0, null)");


        // Tabel Transaksi
        String createTableTransaksi = "CREATE TABLE Transaksi (" +
                "idtransaksi INTEGER PRIMARY KEY," +
                "tanggal TEXT," +
                "waktu TEXT," +
                "shift INTEGER," +
                "idpengguna INTEGER," +
                "idpelanggan INTEGER," +
                "status TEXT," +
                "kodemeja TEXT," +
                "namapelanggan TEXT," +
                "total REAL," +
                "metodepembayaran TEXT," +
                "totaldiskon REAL," +
                "idpromosi INTEGER," +
                "FOREIGN KEY(idpengguna) REFERENCES Pengguna(idpengguna)," +
                "FOREIGN KEY(idpelanggan) REFERENCES Pelanggan(idpelanggan)," +
                "FOREIGN KEY(idpromosi) REFERENCES Promosi(idpromosi))";
        db.execSQL(createTableTransaksi);
        db.execSQL("INSERT INTO Transaksi(idtransaksi, tanggal, waktu, shift, idpengguna, idpelanggan, status, kodemeja, " +
                "namapelanggan, total, metodepembayaran, totaldiskon, idpromosi) VALUES " +
                "(1, '2023-01-01', '12:00', 1, 1, 1, 'completed', 'A001', 'Pelanggan A', 50.0, 'cash', 5.0, 1), " +
                "(2, '2023-01-02', '14:30', 2, 2, 2, 'completed', 'A002', 'Pelanggan B', 30.0, 'card', 0.0, null), " +
                "(3, '2023-01-03', '11:45', 1, 1, 3, 'pending', 'B001', 'Pelanggan C', 70.0, 'cash', 10.0, 2)");


        // Tabel DetailTransaksi
        String createTableDetailTransaksi = "CREATE TABLE DetailTransaksi (" +
                "idtransaksi INTEGER," +
                "idmenu INTEGER," +
                "namamenu TEXT," +
                "harga REAL," +
                "jumlah INTEGER," +
                "subtotal REAL," +
                "status TEXT," +
                "PRIMARY KEY(idtransaksi, idmenu)," +
                "FOREIGN KEY(idtransaksi) REFERENCES Transaksi(idtransaksi)," +
                "FOREIGN KEY(idmenu) REFERENCES Menu(idmenu))";
        db.execSQL(createTableDetailTransaksi);
        db.execSQL("INSERT INTO DetailTransaksi(idtransaksi, idmenu, namamenu, harga, jumlah, subtotal, status) VALUES " +
                "(1, 2, 'Menu B', 10.0, 3, 30.0, 'completed'), " +
                "(2, 1, 'Menu A', 20.0, 1, 20.0, 'completed'), " +
                "(3, 3, 'Menu C', 25.0, 2, 50.0, 'pending')");


        // Tabel Pelanggan
        String createTablePelanggan = "CREATE TABLE Pelanggan (" +
                "idpelanggan INTEGER PRIMARY KEY," +
                "namapelanggan TEXT," +
                "tanggaldaftar TEXT," +
                "waktudaftar TEXT," +
                "poin INTEGER," +
                "status TEXT)";
        db.execSQL(createTablePelanggan);
        db.execSQL("INSERT INTO Pelanggan(idpelanggan, namapelanggan, tanggaldaftar, waktudaftar, poin, status) VALUES " +
                "(1, 'Pelanggan A', '2023-01-01', '09:00', 100, 'active'), " +
                "(2, 'Pelanggan B', '2023-01-02', '10:30', 50, 'active'), " +
                "(3, 'Pelanggan C', '2023-01-03', '11:45', 75, 'inactive')");


        // Tabel PoinTransaksi
        String createTablePoinTransaksi = "CREATE TABLE PoinTransaksi (" +
                "idpointransaksi INTEGER PRIMARY KEY," +
                "tanggal TEXT," +
                "waktu TEXT," +
                "idpelanggan INTEGER," +
                "jumlahpoin INTEGER," +
                "status TEXT," +
                "poinawal INTEGER," +
                "poinakhir INTEGER," +
                "sumber TEXT," +
                "FOREIGN KEY(idpelanggan) REFERENCES Pelanggan(idpelanggan))";
        db.execSQL(createTablePoinTransaksi);
        db.execSQL("INSERT INTO PoinTransaksi(idpointransaksi, tanggal, waktu, idpelanggan, jumlahpoin, status, poinawal, poinakhir, sumber) VALUES " +
                "(1, '2023-01-01', '12:00', 1, 20, 'earned', 100, 120, 'Pembelian'), " +
                "(2, '2023-01-02', '14:30', 2, 10, 'earned', 50, 60, 'Promosi'), " +
                "(3, '2023-01-03', '11:45', 1, 30, 'spent', 120, 90, 'Pembelian')");


        // Tabel Promosi
        String createTablePromosi = "CREATE TABLE Promosi (" +
                "idpromosi INTEGER PRIMARY KEY," +
                "namapromosi TEXT," +
                "deskripsi TEXT," +
                "jumlahpoin INTEGER," +
                "gambar BLOB)";
        db.execSQL(createTablePromosi);
        db.execSQL("INSERT INTO Promosi(idpromosi, namapromosi, deskripsi, jumlahpoin, gambar) VALUES " +
                "(1, 'Promosi Diskon', 'Diskon 10% untuk pelanggan setia', 50, null), " +
                "(2, 'Promosi Gratis', 'Beli 2 Gratis 1 untuk semua menu', 100, null), " +
                "(3, 'Promosi Spesial', 'Harga khusus untuk pelanggan premium', 75, null)");


        db.execSQL("INSERT INTO session(id,login) VALUES (1,'kosong')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS session");
        db.execSQL("DROP TABLE IF EXISTS Pengguna");
        db.execSQL("DROP TABLE IF EXISTS AktivitasPengguna");
        db.execSQL("DROP TABLE IF EXISTS Role");
        db.execSQL("DROP TABLE IF EXISTS Warung");
        db.execSQL("DROP TABLE IF EXISTS Meja");
        db.execSQL("DROP TABLE IF EXISTS Menu");
        db.execSQL("DROP TABLE IF EXISTS Transaksi");
        db.execSQL("DROP TABLE IF EXISTS DetailTransaksi");
        db.execSQL("DROP TABLE IF EXISTS Pelanggan");
        db.execSQL("DROP TABLE IF EXISTS PoinTransaksi");
        db.execSQL("DROP TABLE IF EXISTS Promosi");
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
    public boolean simpanUser(String username, String password, String namaPengguna, String idRole, String status, byte[] foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("namapengguna", namaPengguna);
        values.put("idrole", idRole);
        values.put("status", status);
        values.put("foto", foto); // Simpan foto sebagai byte array

        long insert = db.insert("Pengguna", null, values);
        return insert != -1; // Mengembalikan true jika penyimpanan berhasil, false jika gagal
    }

    //check login
    public boolean checkLogin(String username,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pengguna WHERE username=? AND password=?",new String[]{username,password});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query untuk mencari username di tabel Pengguna
            String query = "SELECT * FROM Pengguna WHERE username=?";
            cursor = db.rawQuery(query, new String[]{username});

            // Jika cursor memiliki data, berarti username sudah ada
            return cursor.getCount() > 0;
        } finally {
            // Pastikan cursor ditutup untuk menghindari memory leaks
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Pengguna getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Pengguna pengguna = null;

        // Kolom yang ingin Anda ambil dari tabel Pengguna
        String[] columns = {"idpengguna", "username", "password", "namapengguna", "idrole", "status", "foto"};

        // Clause WHERE untuk mengambil data berdasarkan username
        String selection = "username=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query("Pengguna", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Ambil data dari cursor dan inisialisasi objek Pengguna
            int idPengguna = cursor.getInt(cursor.getColumnIndex("idpengguna"));
            String namaPengguna = cursor.getString(cursor.getColumnIndex("namapengguna"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String idRole = cursor.getString(cursor.getColumnIndex("idrole"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            byte[] foto = cursor.getBlob(cursor.getColumnIndex("foto"));

            // Inisialisasi objek Pengguna dengan data dari database
            pengguna = new Pengguna(idPengguna, username, password, namaPengguna, idRole, status, foto);

            cursor.close();
        }

        return pengguna;
    }
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query untuk mencari username di tabel Pengguna
            String query = "SELECT * FROM Pengguna WHERE username=?";
            cursor = db.rawQuery(query, new String[]{username});

            // Jika cursor memiliki data, berarti username sudah ada
            return cursor.getCount() > 0;
        } finally {
            // Pastikan cursor ditutup untuk menghindari memory leaks
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

