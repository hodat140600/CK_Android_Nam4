package com.example.myapplication.Statistics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DB.ChiTietPhieuNhapDB;
import com.example.myapplication.DB.PhieuNhapDB;
import com.example.myapplication.DB.PhongKhoDB;
import com.example.myapplication.Entities.PhieuNhap;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.Entities.Rows;
import com.example.myapplication.R;
import com.example.myapplication.XinChoLayout;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class BaoCaoLayout extends AppCompatActivity {
    Button backBtn,
            printBtn;

    Spinner PKSpinner,
            PNSpinner;

    TableLayout table;

    TextView dateView,
            totalMoneyView;

    // ChitietPhieuNhap
    ChiTietPhieuNhapDB chiTietPhieuNhapDB;

    // PhieuNhap
    PhieuNhapDB phieuNhapDB;
    ArrayList<PhieuNhap> phieuNhapArrayList;
    ArrayList<String> phieunhapList;
    PhieuNhap selectedPhieuNhap;

    // PhongKho
    PhongKhoDB phongKhoDB;
    ArrayList<PhongKho> phongKhoList;
    ArrayList<String> tenPhongKhoList;
    PhongKho selectedPhongKho;



    // Data
    float scale;
    Rows rowGenerator = null;
    List<TableRow> rows = null;
    //<!-- 50 / 100 / 100 / 80 / 80 -->
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baocao_layout);
        scale = this.getResources().getDisplayMetrics().density;
        Rows.scale = scale;
        Rows.tvtemplate = R.layout.tvtemplate;
        rowGenerator = new Rows(this);
        setControl();
        loadDatabase();
        setEvent();

    }
    private void setControl() {
        backBtn = findViewById(R.id.BC_All_backBtn);
        printBtn = findViewById(R.id.BC_All_printBtn);

        PKSpinner = findViewById(R.id.BC_All_PKSpinner);
        PNSpinner = findViewById(R.id.BC_All_PNSpinner);

        table    = findViewById(R.id.BC_All_table);

        dateView = findViewById(R.id.BC_All_date);
        totalMoneyView = findViewById(R.id.BC_All_totalMoney);
    }

    private void loadDatabase() {
        phongKhoList = new ArrayList<>();
        phieuNhapDB = new PhieuNhapDB();
        phongKhoDB = new PhongKhoDB();
        chiTietPhieuNhapDB = new ChiTietPhieuNhapDB();
        phongKhoDB.GetData(phongKhoList, this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                tenPhongKhoList = new ArrayList<>();
                for( PhongKho phongKho : phongKhoList){
                    tenPhongKhoList.add(phongKho.getTenpk().trim());
                }
                PKSpinner.setAdapter( loadSpinnerAdapter(tenPhongKhoList) );
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateView(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String str = dtf.format(now);
        String[] date ;
        date = str.split("/");
        String msg = "TPHCM, ng??y "+ date[2] +" th??ng "+ date[1] +" n??m "+ date[0];
        dateView.setText(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if( resultCode == RESULT_OK ) {
                Toast.makeText(BaoCaoLayout.this, "In b??o c??o th??nh c??ng", Toast.LENGTH_LONG).show();
                int result = data.getIntExtra("result",0);
                try {
                    createPDF(selectedPhongKho, selectedPhieuNhap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(BaoCaoLayout.this, "In b??o c??o th???t b???i", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEvent(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaoCaoLayout.this, XinChoLayout.class);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                startActivityForResult( intent, 1 );
            }
        });
        //        PBSpinner
        PKSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhongKho = phongKhoList.get(position);
                phieuNhapArrayList = new ArrayList<>();
                phieuNhapDB.GetDataPK(phieuNhapArrayList, BaoCaoLayout.this, new PhieuNhapDB.VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        PNtoStringArray(phieuNhapArrayList);
                        setNVSpinnerEvent();
                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onSuccess(String response) {

                    }
                }, selectedPhongKho.getMapk().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPhongKho = phongKhoList.get(0);
                phieuNhapArrayList = new ArrayList<>();
                phieuNhapDB.GetDataPK(phieuNhapArrayList, BaoCaoLayout.this, new PhieuNhapDB.VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        PNtoStringArray(phieuNhapArrayList);
                        setNVSpinnerEvent();
                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onSuccess(String response) {

                    }
                }, selectedPhongKho.getMapk().trim());
            }
        });

        setDateView();
        //        NVSpinner

    }
    public void setTable( String pn, String pk){
//        <!-- 40 / 100 / 100 / 80 / 80 -->
        List<String> list = new ArrayList<>();
        table.removeViews(1, table.getChildCount()-1);
        int[] sizeOfCell = {50,100,110,80,100};
        boolean[] isPaddingZero = {false, false, true, false, true};
        rowGenerator.setSizeOfCell(sizeOfCell);
        rowGenerator.setIsCellPaddingZero(isPaddingZero);
        chiTietPhieuNhapDB.GetDataIndexBC(list, BaoCaoLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                rowGenerator.setData( rowGenerator.enhanceRowData( list, 5 ) );
                rows = rowGenerator.generateArrayofRows();
                if( rows == null ) return;
                for( TableRow row : rows ){
                    table.addView(row);
                }
                setTotalMoneyView((ArrayList<TableRow>) rows);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        },pk, pn);


    }

    public void setTotalMoneyView( ArrayList<TableRow> rows){
        if(rows == null || rows.size() == 0) {
            totalMoneyView.setText("0");
            return;
        }
        int tongtien = 0;
        TextView tienView = null;
        for( TableRow row : rows){
            tienView = (TextView) row.getChildAt(4);
            tongtien += Integer.parseInt( tienView.getText().toString().trim() );
        }
        if( tongtien <= 0 ) return;
        totalMoneyView.setText( MoneyFormat(tongtien) );
    }

    public void setNVSpinnerEvent(){
        PNSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhieuNhap = phieuNhapArrayList.get(position);
                String datapk = selectedPhongKho.getMapk().trim();
                String datapn = selectedPhieuNhap.getSoPhieu().trim();
                setTable(datapn, datapk);
                setTotalMoneyView((ArrayList<TableRow>) rows);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPhieuNhap = phieuNhapArrayList.get(0);
                String datapk = selectedPhongKho.getMapk().trim();
                String datapn = selectedPhieuNhap.getSoPhieu().trim();
                setTable(datapn, datapk);
                setTotalMoneyView((ArrayList<TableRow>) rows);
            }
        });
    }

    public void PNtoStringArray(ArrayList<PhieuNhap> list ){
        phieunhapList = new ArrayList<>();
        for( PhieuNhap phieuNhap : list){
            phieunhapList.add( phieuNhap.toSpinnerString() );
        }
        Log.d("PN", "array! \n" + phieunhapList.toString());
        PNSpinner.setAdapter( loadSpinnerAdapter(phieunhapList) );
    }

    public ArrayAdapter<String> loadSpinnerAdapter(ArrayList<String> str) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str);
        return adapter;
    }

    public String MoneyFormat( int money ){
        if( money == 0) return "0 ??";
        int temp_money = money;
        String moneyFormat = "";
        if( money < 1000) return String.valueOf(money) +" ??";
        else {
            int count = 0;
            while (temp_money != 0) {
                moneyFormat += (temp_money % 10) + "";
                if ((count + 1) % 3 == 0 && temp_money > 10) moneyFormat += ".";
                count++;
                temp_money /= 10;
            }
        }
        return new StringBuilder(moneyFormat).reverse().toString() +" ??";
    }

    public Text LocalDateToStrng(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String str = dtf.format(now);
        String[] date ;
        date = str.split("/");
        String msg = "TPHCM, ng??y "+ date[2] +" th??ng "+ date[1] +" n??m "+ date[0];
        Text txt = new Text(msg);
        return txt;
    }

    public void createPDF(PhongKho selectedPhongKho, PhieuNhap selectedPhieuNhap) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, selectedPhieuNhap.getSoPhieu().trim() + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("assets/font/vuArial.ttf", PdfEncodings.IDENTITY_H, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(0,0,0,0);

        Drawable logo = getDrawable(R.drawable.lg);
        Bitmap bitmap = ((BitmapDrawable)logo).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData).setWidth(100).setHorizontalAlignment(HorizontalAlignment.CENTER);

        Paragraph tenpk = new Paragraph(selectedPhongKho.getTenpk()).setBold().setFontSize(25).setTextAlignment(TextAlignment.CENTER).setFont(font);
        Paragraph phieunhap = new Paragraph(selectedPhieuNhap.getSoPhieu()
                +"\nNg??y L???p Phi???u: " + selectedPhieuNhap.getNgayLap()).setTextAlignment(TextAlignment.CENTER).setFontSize(16).setFont(font);

        float[] sizeOfCell = {50,100,110,80,100};
        Table tb = new Table(sizeOfCell);
        tb.setHorizontalAlignment(HorizontalAlignment.CENTER);

        tb.addCell(new Cell().add(new Paragraph("M?? VT")));
        tb.addCell(new Cell().add(new Paragraph("T??n VT")));
        tb.addCell(new Cell().add(new Paragraph("DVT")));
        tb.addCell(new Cell().add(new Paragraph("SL")));
        tb.addCell(new Cell().add(new Paragraph("Tr??? Gi??")));

        for (int i = 1; i < table.getChildCount(); i++){
            TableRow tr = (TableRow) table.getChildAt(i);
            TextView mavt_data = (TextView) tr.getChildAt(0);
            TextView tenvt_data = (TextView) tr.getChildAt(1);
            TextView dvt_data = (TextView) tr.getChildAt(2);
            TextView sl_data = (TextView) tr.getChildAt(3);
            TextView trigia_data = (TextView) tr.getChildAt(4);

            String mavt = mavt_data.getText().toString().trim();
            String tenvt = tenvt_data.getText().toString().trim();
            String dvt = dvt_data.getText().toString().trim();
            String sl = sl_data.getText().toString().trim();
            String trigia = trigia_data.getText().toString().trim();

            tb.addCell(new Cell().add(new Paragraph(mavt).setFont(font)));
            tb.addCell(new Cell().add(new Paragraph(tenvt).setFont(font)));
            tb.addCell(new Cell().add(new Paragraph(dvt).setFont(font)));
            tb.addCell(new Cell().add(new Paragraph(sl).setFont(font)));
            tb.addCell(new Cell().add(new Paragraph(trigia).setFont(font)));
        }

        Paragraph giatriPN = new Paragraph("T???ng gi?? tr??? Phi???u Nh???p : " + totalMoneyView.getText()).setTextAlignment(TextAlignment.CENTER).setFontSize(16).setBold().setFont(font);

        Text txt = LocalDateToStrng();
        Paragraph localDate = new Paragraph(txt).setTextAlignment(TextAlignment.RIGHT).setFontSize(16).setItalic().setFont(font);

        Text qrcodeTxt = new Text(selectedPhongKho.getTenpk()
                + "\n" + selectedPhieuNhap.getSoPhieu()
                + "\n" + selectedPhieuNhap.getNgayLap()).setFont(font);
        BarcodeQRCode qrCode = new BarcodeQRCode(qrcodeTxt.getText());
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
        Image qrCodeImage = new Image(qrCodeObject).setWidth(80).setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(image);
        document.add(tenpk);
        document.add(phieunhap);
        document.add(tb);
        document.add(giatriPN);
        document.add(localDate);
        document.add(qrCodeImage);

        document.close();
        Toast.makeText(this, "PDF created !", Toast.LENGTH_LONG).show();
    }
}
