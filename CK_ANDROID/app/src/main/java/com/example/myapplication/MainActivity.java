package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.myapplication.Databases.PhieuNhapDatabase;
import com.example.myapplication.Databases.ChiTietPhieuNhapDatabase;
import com.example.myapplication.Databases.NhanVienDatabase;
import com.example.myapplication.Databases.PhongKhoDatabase;
import com.example.myapplication.Databases.VatTuDatabase;
import com.example.myapplication.Main.ChiTietPhieuNhapLayout;
import com.example.myapplication.Main.PhieuNhapLayout;
import com.example.myapplication.Main.PhongkhoLayout;
import com.example.myapplication.Main.VattuLayout;

public class MainActivity extends AppCompatActivity {
    CardView cardView1,cardView2,cardView3,cardView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //resetAll();
        setControl();
        setEvent();
        setAnim();
        hideSystemUI();
    }

    private void resetAll() {
        PhongKhoDatabase pkDB = new PhongKhoDatabase(this);
//        NhanVienDatabase nvDB = new NhanVienDatabase(this);
        VatTuDatabase vtDB = new VatTuDatabase(this);
        PhieuNhapDatabase cpDB = new PhieuNhapDatabase(this);
        ChiTietPhieuNhapDatabase ctcpDB = new ChiTietPhieuNhapDatabase(this);

        pkDB.reset();
//        nvDB.reset();
        cpDB.reset();
        vtDB.reset();
        ctcpDB.reset();


    }
    private void setEvent() {
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhongkhoLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity( intent );
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VattuLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity( intent );
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity( intent );
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChiTietPhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity( intent );
            }
        });
    }

    private void setAnim() {
        Animation animationLeft1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        Animation animationLeft4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cardView1.setVisibility(View.VISIBLE);
                cardView1.startAnimation(animationLeft1);
            }
        }, 350);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cardView2.setVisibility(View.VISIBLE);
                cardView2.startAnimation(animationLeft2);
            }
        }, 450);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cardView3.setVisibility(View.VISIBLE);
                cardView3.startAnimation(animationLeft3);
            }
        }, 550);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cardView4.setVisibility(View.VISIBLE);
                cardView4.startAnimation(animationLeft4);
            }
        }, 650);


//        cardView2.startAnimation(animationLeft);
//        cardView3.startAnimation(animationLeft);
//        cardView4.startAnimation(animationLeft);


    }

    private void setControl() {
        cardView1 = findViewById(R.id.cardView1);
        cardView1.setVisibility(View.INVISIBLE);
        cardView2 = findViewById(R.id.cardView2);
        cardView2.setVisibility(View.INVISIBLE);
        cardView3 = findViewById(R.id.cardView3);
        cardView3.setVisibility(View.INVISIBLE);
        cardView4 = findViewById(R.id.cardView4);
        cardView4.setVisibility(View.INVISIBLE);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}