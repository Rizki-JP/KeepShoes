package com.example.keepshoes;

public class ItemModel {

    private byte[] fotoSepatu;
    private String namaPemilik;
    private String info;

    public ItemModel(byte[] fotoSepatu, String namaPemilik, String info) {
        this.fotoSepatu = fotoSepatu;
        this.namaPemilik = namaPemilik;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
