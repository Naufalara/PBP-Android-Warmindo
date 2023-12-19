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

        // Tabel AktivitasPengguna
        String createTableAktivitasPengguna = "CREATE TABLE AktivitasPengguna (" +
                "idaktivitas INTEGER PRIMARY KEY," +
                "tanggal TEXT," +
                "waktu TEXT," +
                "idpengguna INTEGER," +
                "aktivitas TEXT," +
                "FOREIGN KEY(idpengguna) REFERENCES Pengguna(idpengguna))";
        db.execSQL(createTableAktivitasPengguna);

        // Tabel Role
        String createTableRole = "CREATE TABLE Role (" +
                "idrole INTEGER PRIMARY KEY," +
                "role TEXT," +
                "status TEXT)";
        db.execSQL(createTableRole);

        // Tabel Warung
        String createTableWarung = "CREATE TABLE Warung (" +
                "idwarung INTEGER PRIMARY KEY," +
                "namawarung TEXT," +
                "logo BLOB," +
                "gambar BLOB)";
        db.execSQL(createTableWarung);

        // Tabel Meja
        String createTableMeja = "CREATE TABLE Meja (" +
                "idmeja INTEGER PRIMARY KEY," +
                "idwarung INTEGER," +
                "kodemeja TEXT," +
                "FOREIGN KEY(idwarung) REFERENCES Warung(idwarung))";
        db.execSQL(createTableMeja);

        // Tabel Menu
        String createTableMenu = "CREATE TABLE Menu (" +
                "idmenu INTEGER PRIMARY KEY," +
                "namamenu TEXT," +
                "kategori TEXT," +
                "harga REAL," +
                "gambar BLOB)";
        db.execSQL(createTableMenu);

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

        // Tabel Pelanggan
        String createTablePelanggan = "CREATE TABLE Pelanggan (" +
                "idpelanggan INTEGER PRIMARY KEY," +
                "namapelanggan TEXT," +
                "tanggaldaftar TEXT," +
                "waktudaftar TEXT," +
                "poin INTEGER," +
                "status TEXT)";
        db.execSQL(createTablePelanggan);

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

        // Tabel Promosi
        String createTablePromosi = "CREATE TABLE Promosi (" +
                "idpromosi INTEGER PRIMARY KEY," +
                "namapromosi TEXT," +
                "deskripsi TEXT," +
                "jumlahpoin INTEGER," +
                "gambar BLOB)";
        db.execSQL(createTablePromosi);

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
    // Method untuk mengambil data pengguna dari tabel Pengguna
    public Pengguna getDataPengguna() {
        SQLiteDatabase db = this.getReadableDatabase();
        Pengguna pengguna = null;

        Cursor cursor = db.rawQuery("SELECT * FROM Pengguna", null);

        if (cursor.moveToFirst()) {
            int idPengguna = cursor.getInt(cursor.getColumnIndex("idpengguna"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String namaPengguna = cursor.getString(cursor.getColumnIndex("namapengguna"));
            String idRole = cursor.getString(cursor.getColumnIndex("idrole"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            byte[] foto = cursor.getBlob(cursor.getColumnIndex("foto"));

            pengguna = new Pengguna(idPengguna, username, password, namaPengguna, idRole, status, foto);
        }

        cursor.close();
        return pengguna;
    }

}

