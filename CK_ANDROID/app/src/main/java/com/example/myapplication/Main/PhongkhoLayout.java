package com.example.myapplication.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DB.PhongKhoDB;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.Notification;
import com.example.myapplication.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class PhongkhoLayout extends AppCompatActivity {
    // Main Layout
    TableLayout phongkho_table_list;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button exitBtn;

    // Navigation
    Button navPK;
    Button navNV;
    Button navVT;
    Button navPN;
    Button navCTPN;

    List<PhongKho> phongkholist;

    // Dialog Layout
    Dialog phongkhodialog;

    Button backBtn;
    Button yesBtn;
    Button noBtn;

    EditText inputMaPK;
    EditText inputTenPK;
    EditText inputDiaChi;
    EditText inputSDT;

    EditText PK_searchView;

    TextView showMPKError;
    TextView showTPKError;
    TextView showDCPKError;
    TextView showSDTPKError;
    TextView showResult;
    TextView showConfirm;
    TextView showLabel;

    // Database Controller
    PhongKhoDB phongkhoDB;

    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaPK;
    TextView focusTenPK;
    TextView focusDiaChi;
    TextView focusSDT;


    // Other
    float scale;
    String urlGetData = "http://192.168.1.9:8080/androidwebservice/getdata.php";
    String urlInsert = "http://192.168.1.9:8080/androidwebservice/insert.php";
    String urlCapNhat = "http://192.168.1.9:8080/androidwebservice/update.php";
    String urlDelete = "http://192.168.1.9:8080/androidwebservice/delete.php";
    ArrayList<PhongKho> phongKhoArrayList;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phongkho_layout);
        scale = this.getResources().getDisplayMetrics().density;
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();

        PK_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) { filter(s.toString());
                editBtn.setVisibility(View.INVISIBLE);
                delBtn.setVisibility(View.INVISIBLE);}
        });
        hideSystemUI();
    }

    private void filter(String toString) {
        TableRow tr = (TableRow) phongkho_table_list.getChildAt(0);
        int dem =1;
        phongkho_table_list.removeAllViews();
        phongkho_table_list.addView(tr);
        for (int k = 0; k < phongKhoArrayList.size(); k++) {
            PhongKho phongKho = phongKhoArrayList.get(k);
            if (phongKho.getMapk().toLowerCase().trim().contains(toString.trim().toLowerCase()) || phongKho.getTenpk().toLowerCase().contains(toString.toLowerCase())) {
                tr = createRow1(PhongkhoLayout.this, phongKho);
                tr.setId((int) dem++);
                phongkho_table_list.addView(tr);
                setEventTableRows(tr, phongkho_table_list);
            }

        }
    }

    // --------------- MAIN HELPER -----------------------------------------------------------------
    public void setCursorWindowImageSize( int B ){
        // Khai b??o m???t field m???i cho kh??? n??ng l??u h??nh ????? ph??n gi???i l???n
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, B); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setControl() {
        phongkho_table_list = findViewById(R.id.PK_table_list);
        insertBtn = findViewById(R.id.PK_insertBtn);
        editBtn = findViewById(R.id.PK_editBtn);
        delBtn = findViewById(R.id.PK_delBtn);
        exitBtn = findViewById(R.id.PK_exitBtn);

        PK_searchView = findViewById(R.id.PK_searchEdit);

        navPK = findViewById(R.id.PK_navbar_phongkho);
        navNV = findViewById(R.id.PK_navbar_nv);
        navVT = findViewById(R.id.PK_navbar_VT);
        navPN = findViewById(R.id.PK_navbar_pn);
        navCTPN = findViewById(R.id.PK_navbar_capphat);
    }

    public void setEvent() {
        editBtn.setVisibility(View.INVISIBLE); // turn on when click items
        delBtn.setVisibility(View.INVISIBLE);  // this too
        setEventTable(phongkho_table_list);
    }

    public void setNavigation(){
        navNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhongkhoLayout.this, NhanvienLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );

            }
        });
        navVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhongkhoLayout.this, VattuLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );

            }
        });
        navPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhongkhoLayout.this, PhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );

            }
        });
        // navCP
        navCTPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhongkhoLayout.this, ChiTietPhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );
            }

        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setEventTable(TableLayout list) {
        // Log.d("count", list.getChildCount()+""); // s??? table rows + 1
        // Kh??ng c???n thay ?????i v?? ????y ch??? m???i set Event
        // Do c?? th??m 1 th???ng example ????? l??m g???c, n??n s??? row th?? lu??n lu??n ph???i + 1
        // C?? example th?? khi th??m row th?? n?? s??? theo khu??n
//        for (int i = 0; i < list.getChildCount(); i++) {
//            setEventTableRows((TableRow) list.getChildAt(i), list);
//        }
        // Khi t???o, d??ng n l??m tag ????? th??m row
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // H b???m 1 c??i l?? hi???n ra c??i pop up
                createDialog(R.layout.popup_phongkho);
                // Control
                setControlDialog();
                // Event
                setEventDialog(v);
            }
        });
        // Khi edit
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {
                    createDialog(R.layout.popup_phongkho);
                    // Control
                    setControlDialog();
                    showLabel.setText("S???a ph??ng kho");
                    showConfirm.setText("B???n c?? mu???n s???a h??ng n??y kh??ng?");
                    // Event
                    setEventDialog(v);
                    inputMaPK.setText(focusMaPK.getText());
                    inputMaPK.setEnabled(false);
                    inputTenPK.setText(focusTenPK.getText());
                    inputDiaChi.setText(focusDiaChi.getText());
                    inputSDT.setText(focusSDT.getText());
                }
            }
        });
        // Khi delete, c?? 3 TH : n???m ??? cu???i ho???c n???m ??? ?????u ho???c ch??nh gi???a
        // N???m ??? cu???i th?? ch??? c???n x??a cu???i
        // C??n l???i th?? sau khi x??a xong th?? ph???i c???p nh???t l???i tag cho to??n b??? col
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {
                    createDialog(R.layout.popup_phongkho);
                    // Control
                    setControlDialog();
                    showLabel.setText("X??a ph??ng kho");
                    showConfirm.setText("B???n c?? mu???n x??a h??ng n??y kh??ng?");
                    // Event
                    setEventDialog(v);
                    String mapk = focusMaPK.getText().toString();
                    String tenpk = focusTenPK.getText().toString();
                    String diachi = focusDiaChi.getText().toString();
                    String sdt = focusSDT.getText().toString();
                    inputMaPK.setText(focusMaPK.getText());
                    inputTenPK.setText(focusTenPK.getText());
                    inputDiaChi.setText(focusDiaChi.getText());
                    inputSDT.setText(focusSDT.getText());
                    inputMaPK.setEnabled(false);
                    inputTenPK.setEnabled(false);
                    inputDiaChi.setEnabled(false);
                    inputSDT.setEnabled(false);

                }
            }
        });

    }

    // To set all rows to normal state, set focusRowid = -1
    public void setNormalBGTableRows(TableLayout list) {
        // 0: l?? th???ng example ???? INVISIBLE
        // N??n b???t ?????u t??? 1 -> 9
        for (int i = 1; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt((int) i);
            if (indexofRow != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    public void setEventTableRows(TableRow tr, TableLayout list) {
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.VISIBLE);
                delBtn.setVisibility(View.VISIBLE);
                // v means TableRow
                v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                indexofRow = (int) v.getId();
                focusRow = (TableRow) list.getChildAt(indexofRow);
                focusMaPK = (TextView) focusRow.getChildAt(0);
                focusTenPK = (TextView) focusRow.getChildAt(1);
                focusDiaChi = (TextView) focusRow.getChildAt(2);
                focusSDT = (TextView) focusRow.getChildAt(3);
                setNormalBGTableRows(list);
             }
        });
    }

    // Load from the Database to the Table Layout
    public void loadDatabase() {
        setCursorWindowImageSize(100 * 1024* 1024);
        notification = new Notification();
        phongkhoDB = new PhongKhoDB();
        phongKhoArrayList = new ArrayList<>();
        phongkho_table_list.removeAllViews();
        phongKhoArrayList.clear();
        phongkhoDB.GetData(phongKhoArrayList, PhongkhoLayout.this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                TableRow tr = null;
                for (int i = 0; i < phongKhoArrayList.size(); i++) {
                    tr = createRow1(PhongkhoLayout.this, phongKhoArrayList.get(i));
                    tr.setId((int) i);
                    phongkho_table_list.addView(tr);
                    setEventTableRows(tr, phongkho_table_list);
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


    // --------------- DIALOG HELPER -----------------------------------------------------------------
    public void createDialog(int layout) {
        phongkhodialog = new Dialog(PhongkhoLayout.this);
        phongkhodialog.setContentView(layout);
        phongkhodialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phongkhodialog.show();
    }

    public void setControlDialog() {
        backBtn = phongkhodialog.findViewById(R.id.PK_backBtn);
        yesBtn = phongkhodialog.findViewById(R.id.PK_yesInsertBtn);
        noBtn = phongkhodialog.findViewById(R.id.PK_noInsertBtn);

        inputMaPK = phongkhodialog.findViewById(R.id.PK_inputMaPK);
        inputTenPK = phongkhodialog.findViewById(R.id.PK_inputTenPK);
        inputDiaChi = phongkhodialog.findViewById(R.id.PK_inputDiaChi);
        inputSDT = phongkhodialog.findViewById(R.id.PK_inputSDT);

        showMPKError = phongkhodialog.findViewById(R.id.PK_showMPKError);
        showTPKError = phongkhodialog.findViewById(R.id.PK_showTPKError);
        showDCPKError = phongkhodialog.findViewById(R.id.PK_showDCPKError);
        showSDTPKError = phongkhodialog.findViewById(R.id.PK_showSDTPKError);
        showResult = phongkhodialog.findViewById(R.id.PK_showResult);
        showConfirm = phongkhodialog.findViewById(R.id.PK_showConfirm);
        showLabel = phongkhodialog.findViewById(R.id.PK_showLabel);
    }

    public void setEventDialog(View view) {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phongkhodialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phongkhodialog.dismiss();
            }
        });
        // D???a v??o c??c n??t m?? th???ng yesBtn s??? c?? event kh??c
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;
                switch (view.getId()) {
                    case R.id.PK_insertBtn: {
                        if (!isSafeDialog( false )) {
                            ErrorDialog();
                            break;
                        }
                        PhongKho pk = new PhongKho(inputMaPK.getText().toString().trim() + "", inputTenPK.getText().toString().trim() + ""
                                , inputDiaChi.getText().toString().trim() + "", inputSDT.getText().toString().trim() + "");
                        phongkhoDB.ThemPhongKho(pk, PhongkhoLayout.this, new PhongKhoDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(PhongkhoLayout.this, "Loi!", Toast.LENGTH_SHORT).show();
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(PhongkhoLayout.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    phongkho_table_list.removeAllViews();
                                    loadDatabase();
                                    notification.SendNotification(PhongkhoLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pk.getTenpk());
                                }else {
                                    Toast.makeText(PhongkhoLayout.this,"That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });

                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        focusRow = null;
                        focusMaPK = null;
                        focusTenPK = null;
                        focusDiaChi = null;
                        focusSDT = null;
                    }
                    break;
                    case R.id.PK_editBtn: {
                        if (!isSafeDialog( true )) break;
                        TableRow tr = (TableRow) phongkho_table_list.getChildAt(indexofRow);
                        TextView id = (TextView) tr.getChildAt(0);
                        TextView name = (TextView) tr.getChildAt(1);
                        PhongKho pk = new PhongKho(id.getText().toString().trim(),
                                inputTenPK.getText().toString().trim(), inputDiaChi.getText().toString().trim(),
                                inputSDT.getText().toString().trim());
                        phongkhoDB.CapNhatPhongKho(pk, PhongkhoLayout.this, new PhongKhoDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if (response.trim().equalsIgnoreCase("success")){
                                    SuccesssDialog();
                                    loadDatabase();
                                    notification.SendNotification(PhongkhoLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pk.getTenpk());
                                }
                                else{
                                    ErrorDialog();
                                }
                            }
                        });
                    }
                    break;
                    case R.id.PK_delBtn: {
                        TableRow tr = (TableRow) phongkho_table_list.getChildAt(indexofRow);
                        TextView id = (TextView) tr.getChildAt(0);
                        PhongKho pk = new PhongKho(id.getText().toString().trim(),
                                inputTenPK.getText().toString().trim(), inputDiaChi.getText().toString().trim(),
                                inputSDT.getText().toString().trim());
                        phongkhoDB.XoaPhongKho(pk, PhongkhoLayout.this, new PhongKhoDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(PhongkhoLayout.this, "X???y ra l???i !", Toast.LENGTH_SHORT).show();
                                Log.d("AAA", "L???i! \n" + error.toString());
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if (response.trim().equalsIgnoreCase("success")){
                                    Toast.makeText(PhongkhoLayout.this, "Xoa Th??nh C??ng!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    loadDatabase();
                                    notification.SendNotification(PhongkhoLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pk.getTenpk());
                                }
                                else{
                                    Toast.makeText(PhongkhoLayout.this, "L???i Xoa!", Toast.LENGTH_SHORT).show();
                                    Log.d("AAA", "L???i! \n" + response);
                                    ErrorDialog();
                                }
                            }
                        });
                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        focusRow = null;
                        focusMaPK = null;
                        focusTenPK = null;
                    }
                    break;
                    default:
                        break;
                }
            }
        });


    }

    public boolean isSafeDialog( boolean allowSameID ) {
        String id, mapk, tenpk, diachi, sdt;
        // M?? PK kh??ng ???????c tr??ng v???i M?? PK kh??c v?? ko ????? tr???ng
        mapk = inputMaPK.getText().toString().trim();
        boolean noError = true;
        if (mapk.equals("")) {
            showMPKError.setText("M?? PK kh??ng ???????c tr???ng ");
            showMPKError.setVisibility(View.VISIBLE);
            noError = false;
        }else{
            showMPKError.setVisibility(View.INVISIBLE);
            noError = true;
        }

        // T??n PK kh??ng ???????c ????? tr???ng v?? kh??ng tr??ng
        tenpk = inputTenPK.getText().toString().trim();
        if (tenpk.equals("")) {
            showTPKError.setText("T??n PK kh??ng ???????c tr???ng ");
            showTPKError.setVisibility(View.VISIBLE);
            noError = false;
        }else{
            showTPKError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        diachi = inputDiaChi.getText().toString().trim();
        if (diachi.equals("")) {
            showDCPKError.setText("?????a ch??? kh??ng ???????c tr???ng ");
            showDCPKError.setVisibility(View.VISIBLE);
            noError = false;
        }else{
            showDCPKError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }
        sdt = inputSDT.getText().toString().trim();
        if (sdt.equals("")) {
            showSDTPKError.setText("S??? ??i???n tho???i kh??ng ???????c tr???ng ");
            showSDTPKError.setVisibility(View.VISIBLE);
            noError = false;
        }else{
            showSDTPKError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        if( noError ) {
            for (int i = 1; i < phongkho_table_list.getChildCount(); i++) {
                TableRow tr = (TableRow) phongkho_table_list.getChildAt(i);
                TextView mapk_data = (TextView) tr.getChildAt(0);
                TextView tenpk_data = (TextView) tr.getChildAt(1);

                if (!allowSameID)
                    if (mapk.equalsIgnoreCase(mapk_data.getText().toString())) {
                        showMPKError.setText("M?? PK kh??ng ???????c tr??ng ");
                        showMPKError.setVisibility(View.VISIBLE);
                        return noError = false;
                    }
                if (tenpk.equalsIgnoreCase(tenpk_data.getText().toString())
                        && !tenpk_data.getText().toString().equalsIgnoreCase(
                        focusTenPK.getText().toString().trim() )
                ) {
                    showTPKError.setText("T??n PK kh??ng ???????c tr??ng");
                    showTPKError.setVisibility(View.VISIBLE);
                    return noError = false;
                }
            }
            showMPKError.setVisibility(View.INVISIBLE);
            showTPKError.setVisibility(View.INVISIBLE);
        }
        return noError;
    }

    // --------------- CUSTOM HELPER --------------------------------------------------------------------
    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
    }
    public void SuccesssDialog(){
        showResult.setText(showLabel.getText() + " th??nh c??ng !");
        showResult.setTextColor(getResources().getColor(R.color.yes_color));
        showResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMaPK.setText("");
                inputTenPK.setText("");
                showResult.setVisibility(View.INVISIBLE);
                phongkhodialog.dismiss();
            }
        }, 1000);
    }
    public void ErrorDialog(){
        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
        showResult.setText(showLabel.getText() + " th???t b???i !");
        showResult.setVisibility(View.VISIBLE);
    }
    // This Custom Columns' Max Width : 80 / 300
    public TableRow createRow(Context context, PhongKho pk) {
        TableRow tr = new TableRow(context);
        // Id


        //   Ma PK
        TextView maPK = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maPK ?????t t???i max width th?? n?? s??? t??ng height cho b??n tenPK lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        maPK.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maPK.setMaxWidth(DPtoPix(80));
        maPK.setText(pk.getMapk());

        //   Ten PK
        TextView tenPK = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? tenPK ?????t t???i max width th?? n?? s??? t??ng height cho b??n maPK lu??n
        tenPK.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenPK.setText(pk.getTenpk());
        tenPK.setMaxWidth(DPtoPix(300));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(maPK);
        tr.addView(tenPK);

        return tr;
    }
    public TableRow createRow1(Context context, PhongKho pk) {
        TableRow tr = new TableRow(context);

        TextView maPK = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        maPK.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maPK.setWidth(DPtoPix(75));
        maPK.setText(pk.getMapk());

        //   Ten PK
        TextView tenPK = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        tenPK.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenPK.setText(pk.getTenpk());
        tenPK.setWidth(DPtoPix(165));

        TextView diachi = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        diachi.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        diachi.setWidth(DPtoPix(120));
        diachi.setText(pk.getDiachi());

        TextView sdt = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        sdt.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        sdt.setWidth(DPtoPix(120));
        sdt.setText(pk.getSdt());

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(maPK);
        tr.addView(tenPK);
        tr.addView(diachi);
        tr.addView(sdt);

        return tr;
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemUIBars());
            }
        });
    }
    private int hideSystemUIBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    //API

}
