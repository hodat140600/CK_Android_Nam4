package com.example.myapplication.Entities;

public class VatTu {
    private String maVt;
    private String tenVt;
    private String dvt;
    private String giaNhap;
    private byte[] hinh;

    public VatTu( String maVt, String tenVt, String dvt, String giaNhap, byte[] hinh) {
        this.maVt = maVt;
        this.tenVt = tenVt;
        this.dvt = dvt;
        this.giaNhap = giaNhap;
        this.hinh = hinh;
    }
    public String getMaVt() {
        return maVt;
    }

    public void setMaVt(String maVt) {
        this.maVt = maVt;
    }

    public String getTenVt() {
        return tenVt;
    }

    public void setTenVt(String tenVt) {
        this.tenVt = tenVt;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public String getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(String giaNhap) {
        this.giaNhap = giaNhap;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }
}