package com.example.myapplication.Entities;

public class PhongKho {
    //    private long id;
    private String mapk;
    private String tenpk;
    private String diachi;
    private String sdt;

    public PhongKho(String mapb, String tenpb) {
        this.mapk = mapb;
        this.tenpk = tenpb;
    }
    public PhongKho(String mapb, String tenpb, String diachi, String sdt) {
        this.mapk = mapb;
        this.tenpk = tenpb;
        this.diachi = diachi;
        this.sdt = sdt;
    }

//    public PhongBan(long id, String mapb, String tenpb) {
//        this.id = id;
//        this.mapb = mapb;
//        this.tenpb = tenpb;
//    }

    @Override
    public String toString() {
        return "PhongBan{" +
//                "id=" + id +
                ", mapb='" + mapk + '\'' +
                ", tenpb='" + tenpk + '\'' +
                '}';
    }

    public String toIDandName(){
        return mapk +"-"+ tenpk;

    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getMapk() {
        return mapk;
    }

    public void setMapk(String mapk) {
        this.mapk = mapk;
    }

    public String getTenpk() {
        return tenpk;
    }

    public void setTenpk(String tenpk) {
        this.tenpk = tenpk;
    }
    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
