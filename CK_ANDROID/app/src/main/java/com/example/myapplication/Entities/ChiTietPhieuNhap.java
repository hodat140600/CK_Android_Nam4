package com.example.myapplication.Entities;

public class ChiTietPhieuNhap {

    private String soPhieu;
    private String maVT;
    private long soLuong;

    public ChiTietPhieuNhap(String soPhieu, String maVT, long sl){
        this.soPhieu = soPhieu;
        this.maVT = maVT;
        this.soLuong = sl;
    }

    @Override
    public String toString() {
        return "ChiTietCapPhat{" +
                "soPhieu='" + soPhieu + '\'' +
                ", maVT='" + maVT + '\'' +
                ", sl=" + soLuong +
                '}';
    }

    public String getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(String soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public long getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(long soLuong) {
        this.soLuong = soLuong;
    }
}
