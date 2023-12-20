package com.android.pbpkelompok;

public class Pengguna {
    private String  idpengguna;
    private String username;
    private String password;
    private String namaPengguna;
    private String idRole;
    private String status;
    private byte[] foto;

    // Konstruktor untuk kelas Pengguna
    public Pengguna(String idpengguna, String username, String password, String namaPengguna, String idRole, String status, byte[] foto) {
        this.idpengguna = idpengguna;
        this.username = username;
        this.password = password;
        this.namaPengguna = namaPengguna;
        this.idRole = idRole;
        this.status = status;
        this.foto = foto;
    }

    // Getter dan setter untuk setiap atribut Pengguna
    public String getIdPengguna() {
        return idpengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idpengguna = idpengguna;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}

