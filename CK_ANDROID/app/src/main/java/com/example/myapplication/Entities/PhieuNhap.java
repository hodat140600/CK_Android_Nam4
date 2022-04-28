package com.example.myapplication.Entities;

public class PhieuNhap {

    private String soPhieu;
    private String ngayLap;
    private String maK;


    public PhieuNhap(String soPhieu, String ngayCap, String maK) {
        this.soPhieu = soPhieu;
        this.ngayLap = ngayCap;
        this.maK = maK;
    }

    @Override
    public String toString() {
        return "CapPhat{" +
                "soPhieu='" + soPhieu + '\'' +
                ", ngayCap='" + ngayLap + '\'' +
                ", maK='" + maK + '\'' +
                '}';
    }
    public String toSpinnerString() {
        return soPhieu+", "+maK+", "+formatDate(ngayLap, false);
    }

    public String formatDate(String str, boolean toSQL ){
        String[] date ;
        String result = "";
        if( toSQL ){
            date = str.split("/");
            result = date[2] +"-"+ date[1] +"-"+ date[0];
        }else{
            date = str.split("-");
            result = date[2] +"/"+ date[1] +"/"+ date[0];
        }

        return result;
    }
    public String getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(String soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaK() {
        return maK;
    }

    public void setMaK(String maK) {
        this.maK = maK;
    }

}
