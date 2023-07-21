package com.example.keepshoes;

public class DataModel {
    private int id;
    private byte[] fotoSepatu;
    private String namaPemilik;
    private String nomorTelepon;
    private String merkSepatu;
    private String warnaSepatu;
    private String ukuranSepatu;

    public  DataModel() {}

    public DataModel(int id, byte[] fotoSepatu,
                     String namaPemilik, String nomorTelepon,
                     String merkSepatu, String warnaSepatu,
                     String ukuranSepatu
    ) {
        this.id = id;
        this.fotoSepatu = fotoSepatu;
        this.namaPemilik = namaPemilik;
        this.nomorTelepon = nomorTelepon;
        this.merkSepatu = merkSepatu;
        this.warnaSepatu = warnaSepatu;
        this.ukuranSepatu = ukuranSepatu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFotoSepatu() {
        return fotoSepatu;
    }

    public void setFotoSepatu(byte[] fotoSepatu) {
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
}
