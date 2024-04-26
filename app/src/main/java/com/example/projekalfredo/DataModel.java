package com.example.projekalfredo;

public class DataModel {
    private int id;
    private String name;
    private String harga;
    private String deskripsi;

    public DataModel() {}

    public DataModel(String name, String harga, String deskripsi) {
        this.name = name;
        this.harga = harga;
        this.deskripsi = deskripsi;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getHarga() {
        return harga;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

}
