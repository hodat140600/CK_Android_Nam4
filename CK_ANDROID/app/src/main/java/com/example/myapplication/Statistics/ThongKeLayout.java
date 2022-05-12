package com.example.myapplication.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.DB.ChiTietPhieuNhapDB;
import com.example.myapplication.DB.PhieuNhapDB;
import com.example.myapplication.DB.PhongKhoDB;
import com.example.myapplication.Entities.PhieuNhap;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ThongKeLayout extends AppCompatActivity {
    BarChart barChart;
    PieChart pieChart;

    ChiTietPhieuNhapDB chiTietPhieuNhapDB;

    // PhieuNhap
    PhieuNhapDB phieuNhapDB;
    ArrayList<PhieuNhap> phieuNhapList;
    ArrayList<String> phieunhapStringList;


    // PhongKho
    PhongKhoDB phongKhoDB;
    ArrayList<PhongKho> phongKhoList;
    ArrayList<String> tenPhongKhoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_ke_layout);
        setControl();
        loadDatabase();
        setChart();
    }

    private void setControl() {
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
    }

    private void loadDatabase() {
        phieuNhapDB = new PhieuNhapDB();
        phongKhoDB = new PhongKhoDB();
        chiTietPhieuNhapDB = new ChiTietPhieuNhapDB();
        phongKhoList = new ArrayList<>();
        phongKhoDB.GetData(phongKhoList, this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                tenPhongKhoList = new ArrayList<>();
                for( PhongKho phongKho : phongKhoList){
                    tenPhongKhoList.add(phongKho.getTenpk().trim());
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });

    }

    public void setChart(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        List<String> str = new ArrayList<>();
        chiTietPhieuNhapDB.GetDataChart(str, this, new ChiTietPhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.d("AAA", "test! \n" + str.toString());
                for (int i = 0; i < phongKhoList.size(); i++){
                    String mapk = phongKhoList.get(i).getMapk().trim();
                    String tenpk = phongKhoList.get(i).getTenpk().trim();
                    Log.d("mapk", i +" " + mapk.toString());
                    Log.d("strpk", i +" " + str.get(i).trim());
                    for (int k = 0; k < str.size(); k++) {
                        if (mapk.trim().equalsIgnoreCase(str.get(k).trim())) {
                            String str1 = str.get(k + 1) == null ? "0" : str.get(k + 1);
                            Log.d("str", k + " " + str1.toString());
                            float value = Float.valueOf(str1);

                            BarEntry barEntry = new BarEntry(k, value, tenpk);

                            PieEntry pieEntry = new PieEntry(value, tenpk);

                            barEntries.add(barEntry);

                            pieEntries.add(pieEntry);
                            k++;
                        }
                    }
                }


                BarDataSet barDataSet = new BarDataSet(barEntries, "Phòng Kho");
                barDataSet.setValueTextSize(12f);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.setData(new BarData(barDataSet));
                barChart.animateY(5000);
                barChart.getDescription().setText("Tổng Số lượng Vật tư mỗi Phòng Kho");
                barChart.getDescription().setTextSize(12f);
                barChart.getDescription().setTextColor(Color.BLUE);
//        barChart.getDescription().setEnabled(false);

                PieDataSet pieDataSet = new PieDataSet(pieEntries, ("Phòng Kho"));
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextSize(12f);
                pieChart.setData(new PieData(pieDataSet));
                pieChart.animateXY(5000,5000);
                pieChart.getDescription().setEnabled(false);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });

    }
}