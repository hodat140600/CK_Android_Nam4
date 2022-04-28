package com.example.myapplication.Statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Databases.ChiTietPhieuNhapDatabase;
import com.example.myapplication.Databases.PhieuNhapDatabase;
import com.example.myapplication.Databases.PhongKhoDatabase;
import com.example.myapplication.Entities.PhieuNhap;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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

    // ChitietPhieuNhap
    ChiTietPhieuNhapDatabase chiTietPhieuNhapDatabase;

    // PhieuNhap
    PhieuNhapDatabase phieuNhapDatabase;
    List<PhieuNhap> phieuNhapList;
    ArrayList<String> phieunhapStringList;


    // PhongKho
    PhongKhoDatabase phongKhoDatabase;
    List<PhongKho> phongKhoList;
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
        phieuNhapDatabase = new PhieuNhapDatabase(this);
        phongKhoDatabase = new PhongKhoDatabase(this);
        chiTietPhieuNhapDatabase = new ChiTietPhieuNhapDatabase( this);
        phongKhoList = phongKhoDatabase.select();
        tenPhongKhoList = new ArrayList<>();
        for( PhongKho phongKho : phongKhoList){
            tenPhongKhoList.add(phongKho.getTenpk().trim());
        }
    }

    public void setChart(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < phongKhoList.size(); i++){
            String mapk = phongKhoList.get(i).getMapk().trim();
            String tenpk = phongKhoList.get(i).getTenpk().trim();
            List<String> str = chiTietPhieuNhapDatabase.selectSumSL_IndexPK(mapk);
            String str1 = str.get(0) == null? "0": str.get(0);
            float value = Float.valueOf(str1);

            BarEntry barEntry = new BarEntry(i, value, tenpk);

            PieEntry pieEntry = new PieEntry(value, tenpk);

            barEntries.add(barEntry);

            pieEntries.add(pieEntry);
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
}