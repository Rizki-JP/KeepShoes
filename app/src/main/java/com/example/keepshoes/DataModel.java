package com.example.keepshoes;

public class DataModel {
    private int id;
    private String fotoSepatu;
    private String namaPemilik;
    private String nomorTelepon;
    private String merkSepatu;
    private String warnaSepatu;
    private String ukuranSepatu;

    private String biaya;
    private String lamaPengerjaan;
    private String catatan;

    public  DataModel() {}

    public DataModel(int id, String fotoSepatu,
                     String namaPemilik, String nomorTelepon,
                     String merkSepatu, String warnaSepatu,
                     String ukuranSepatu, String biaya, String lamaPengerjaan,
                     String catatan
    ) {
        this.id = id;
        this.fotoSepatu = fotoSepatu;
        this.namaPemilik = namaPemilik;
        this.nomorTelepon = nomorTelepon;
        this.merkSepatu = merkSepatu;
        this.warnaSepatu = warnaSepatu;
        this.ukuranSepatu = ukuranSepatu;
        this.biaya = biaya;
        this.lamaPengerjaan = lamaPengerjaan;
        this.catatan = catatan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFotoSepatu() {
        return fotoSepatu;
    }

    public void setFotoSepatu(String fotoSepatu) {
        this.fotoSepatu = fotoSepatu;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getMerkSepatu() {
        return merkSepatu;
    }

    public void setMerkSepatu(String merkSepatu) {
        this.merkSepatu = merkSepatu;
    }

    public String getWarnaSepatu() {
        return warnaSepatu;
    }

    public void setWarnaSepatu(String warnaSepatu) {
        this.warnaSepatu = warnaSepatu;
    }

    public String getUkuranSepatu() {
        return ukuranSepatu;
    }

    public void setUkuranSepatu(String ukuranSepatu) {
        this.ukuranSepatu = ukuranSepatu;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getLamaPengerjaan() {
        return lamaPengerjaan;
    }

    public void setLamaPengerjaan(String lamaPengerjaan) {
        this.lamaPengerjaan = lamaPengerjaan;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
