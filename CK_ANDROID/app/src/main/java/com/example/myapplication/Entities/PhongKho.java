package com.example.myapplication.Entities;

public class PhongKho {
    //    private long id;
    private String mapk;
    private String tenpk;

    public PhongKho(String mapb, String tenpb) {
        this.mapk = mapb;
        this.tenpk = tenpb;
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
}
