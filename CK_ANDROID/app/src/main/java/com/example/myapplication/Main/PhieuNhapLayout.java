package com.example.myapplication.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.myapplication.Notification;
import com.example.myapplication.R;


import java.lang.reflect.Field;
import java.util.ArrayList;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class PhieuNhapLayout extends AppCompatActivity {
    // Main Layout
    TableLayout phieunhap_table_list;

    Spinner PK_spinner;
    String PK_spinner_maPK;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button exitBtn;

    // Navigation
    Button navPK;
    Button navNV;
    Button navPN;
    Button navVT;
    Button navCTPN;

    // Dialog Layout
    Dialog phieunhapdialog;

    Button backBtn;
    Button yesBtn;
    Button noBtn;

    EditText PN_searchView;

    Spinner PK_spinner_mini;

    EditText inputMaPN;


    DatePicker datepickerNLPN;

    String PK_spinner_mini_maPK;
    String strDate;

    TextView showMPNError;
    TextView showMPKError;

    TextView showResult;
    TextView showConfirm;
    TextView showLabel;

    // Database Controller
    PhieuNhapDB phieunhapDB;
    PhongKhoDB phongkhoDB;
    ChiTietPhieuNhapDB ctphieunhapDB;

    ArrayList<PhongKho> phongKhoArrayList;
    ArrayList<PhieuNhap> phieuNhapArrayList;

    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaPN;
    TextView focusNLPN;

    // Other
    float scale;
    Notification notification = new Notification();

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phieunhap_layout);
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
        PN_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                editBtn.setVisibility(View.INVISIBLE);
                delBtn.setVisibility(View.INVISIBLE);
            }
        });
        hideSystemUI();
    }

    private void filter(String toString) {
        TableRow tr = null;
        int dem =0;
        phieunhap_table_list.removeAllViews();
        for (int k = 0; k < phieuNhapArrayList.size(); k++) {
            PhieuNhap pn = phieuNhapArrayList.get(k);
            if (pn.getSoPhieu().toLowerCase().trim().contains(toString.trim().toLowerCase()) || pn.getMaK().toLowerCase().contains(toString.toLowerCase())) {

                tr = createRow(PhieuNhapLayout.this, pn);

                tr.setId((int) dem++);
                phieunhap_table_list.addView(tr);
                setEventTableRows(tr, phieunhap_table_list);
            }

        }
    }

    // --------------- MAIN HELPER -----------------------------------------------------------------
    public void setCursorWindowImageSize(int B) {
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
        phieunhap_table_list = findViewById(R.id.PN_table_list);
        PN_searchView = findViewById(R.id.PN_searchEdit);
        PK_spinner = findViewById(R.id.PN_PKSpinner);
        insertBtn = findViewById(R.id.PN_insertBtn);
        editBtn = findViewById(R.id.PN_editBtn);
        delBtn = findViewById(R.id.PN_delBtn);
        exitBtn = findViewById(R.id.PN_exitBtn);

        navPK = findViewById(R.id.PN_navbar_phongkho);
        navPN = findViewById(R.id.PN_navbar_phieunhap);
        navVT = findViewById(R.id.PN_navbar_VT);
        navNV = findViewById(R.id.PN_navbar_nhanvien);
        navCTPN = findViewById(R.id.PN_navbar_chitiet);
    }

    public void loadDatabase() {
        Log.d("data", "Load Database --------");
        phieunhapDB = new PhieuNhapDB();
        phongkhoDB = new PhongKhoDB();
        ctphieunhapDB = new ChiTietPhieuNhapDB();

        phieuNhapArrayList = new ArrayList<>();
        phongKhoArrayList = new ArrayList<>();
        phieunhap_table_list.removeAllViews();
        phieunhapDB.GetData(phieuNhapArrayList, this, new PhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                phieunhap_table_list.removeAllViews();
                TableRow tr = null;
                for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                    tr = createRow(PhieuNhapLayout.this, phieuNhapArrayList.get(i));
                    tr.setId((int) i);
                    phieunhap_table_list.addView(tr);
                    setEventTableRows(tr, phieunhap_table_list);
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });
        phongkhoDB.GetData(phongKhoArrayList, this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                ArrayList<String> PK_name = new ArrayList<>();
                PK_name.add("T???t c??? ph??ng kho");
                for (PhongKho pk : phongKhoArrayList) {
                    PK_name.add(pk.getTenpk());
                }
                PK_spinner.setAdapter(loadSpinnerAdapter(PK_name));
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
    public void setEvent() {
        editBtn.setVisibility(View.INVISIBLE); // turn on when click items
        delBtn.setVisibility(View.INVISIBLE);  // this too
        setEventTable(phieunhap_table_list);
        setEventSpinner();
    }

    public void setNavigation() {
        // navNV onclick none
        // navPB
        navPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhieuNhapLayout.this, PhongkhoLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(intent);

            }
        });
        // navVPP
        navVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhieuNhapLayout.this, VattuLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(intent);

            }

        });
        navNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PhieuNhapLayout.this, NhanvienLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(intent);

            }

        });
        // navCP
        navCTPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhieuNhapLayout.this, ChiTietPhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(intent);
            }

        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setEventSpinner() {
        PK_spinner_maPK = "All";
        PK_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (phieunhap_table_list.getChildCount() < phieuNhapArrayList.size() + 1) {
                        PK_spinner_maPK = "All";
                        TableRow tr = null;
                        phieunhap_table_list.removeAllViews();
                        for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                            PhieuNhap pn = phieuNhapArrayList.get(i);
                            tr = createRow(PhieuNhapLayout.this, pn);
                            tr.setId((int) i + 1);
                            phieunhap_table_list.addView(tr);
                            setEventTableRows(tr, phieunhap_table_list);
                        }
                    }
                } else {
                    int dem = 0;
                    String mapk = phongKhoArrayList.get(position - 1).getMapk();
                    PK_spinner_maPK = mapk;
                    TableRow tr = null;
                    phieunhap_table_list.removeAllViews();
                    for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                        PhieuNhap pn = phieuNhapArrayList.get(i);
                        if (pn.getMaK().trim().equals(mapk.trim())) {
                            tr = createRow(PhieuNhapLayout.this, pn);
                            tr.setId((int) dem++);
                            phieunhap_table_list.addView(tr);
                            setEventTableRows(tr, phieunhap_table_list);
                        }
                    }
                }
                editBtn.setVisibility(View.INVISIBLE); // turn on when click items
                delBtn.setVisibility(View.INVISIBLE);  // this too
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PK_spinner_maPK = "All";
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventTable(TableLayout list) {
        // Log.d("count", list.getChildCount()+""); // s??? table rows + 1
        // Kh??ng c???n thay ?????i v?? ????y ch??? m???i set Event
        // Do c?? th??m 1 th???ng example ????? l??m g???c, n??n s??? row th?? lu??n lu??n ph???i + 1
        // C?? example th?? khi th??m row th?? n?? s??? theo khu??n
        for (int i = 0; i < list.getChildCount(); i++) {
            setEventTableRows((TableRow) list.getChildAt(i), list);
        }
        // Khi t???o, d??ng n l??m tag ????? th??m row
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // H b???m 1 c??i l?? hi???n ra c??i pop up
                createDialog(R.layout.popup_phieunhap);
                // Control
                setControlDialog();
                // Event
                strDate = formatDate(InttoStringDate(30, 5, 2022), true);
                setEventDialog(v);

            }
        });
        // Khi edit
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {
                    createDialog(R.layout.popup_phieunhap);
                    // Control
                    setControlDialog();
                    showLabel.setText("S???a th??ng tin phi???u nh???p");
                    showConfirm.setText("B???n c?? mu???n s???a h??ng n??y kh??ng?");
                    // Event
                    setEventDialog(v);

                    int[] date = StringtoIntDate(focusNLPN.getText().toString().trim());

                    int index = 0;
                    for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                        // N???u th???ng ???????c focus c?? m?? PK tr??ng v???i PB trong list th?? break
                        String maPN = phieuNhapArrayList.get(i).getSoPhieu().trim();
                        String maPNF = focusMaPN.getText().toString().trim();
                        if (maPN.equals(maPNF)) {
                            for (int k = 0; k < phongKhoArrayList.size(); k++) {
                                String makPn = phieuNhapArrayList.get(i).getMaK().trim();
                                String makS = phongKhoArrayList.get(k).getMapk().trim();
                                if (makPn.equals(makS)) {
                                    index = k;
                                    break;
                                }
                            }
                        }
                    }
                    inputMaPN.setText(focusMaPN.getText());
                    PK_spinner_mini.setSelection(index);
                    datepickerNLPN.updateDate(date[0], date[1] - 1, date[2]);
                    inputMaPN.setEnabled(false);

                }
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {
                    createDialog(R.layout.popup_phieunhap);
                    // Control
                    setControlDialog();
                    showLabel.setText("X??a th??ng tin Phi???u nh???p");
                    showConfirm.setText("B???n c?? mu???n x??a h??ng n??y kh??ng?");
                    // Event
                    setEventDialog(v);
                    int index = 0;
                    for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                        // N???u th???ng ???????c focus c?? m?? PK tr??ng v???i PB trong list th?? break
                        String maPN = phieuNhapArrayList.get(i).getSoPhieu().trim();
                        String maPNF = focusMaPN.getText().toString().trim();
                        if (maPN.equals(maPNF)) {
                            for (int k = 0; k < phongKhoArrayList.size(); k++) {
                                String makPn = phieuNhapArrayList.get(i).getMaK().trim();
                                String makS = phongKhoArrayList.get(k).getMapk().trim();
                                if (makPn.equals(makS)) {
                                    index = k;
                                    break;
                                }
                            }
                        }
                    }
                    int[] date = StringtoIntDate(focusNLPN.getText().toString().trim());

                    PK_spinner_mini.setSelection(index);
                    inputMaPN.setText(focusMaPN.getText());
                    datepickerNLPN.updateDate(date[0], date[1] - 1, date[2]);
                    PK_spinner_mini.setEnabled(false);
                    inputMaPN.setEnabled(false);
                    datepickerNLPN.setEnabled(false);
                }
            }
        });

    }

    // To set all rows to normal state, set focusRowid = -1
    public void setNormalBGTableRows(TableLayout list) {
        // 0: l?? th???ng example ???? INVISIBLE
        // N??n b???t ?????u t??? 1 -> 9
        for (int i = 0; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt((int) i);
            if (indexofRow != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
//        Toast.makeText(NhanvienLayout.this, indexofRow + ":" + (int) list.getChildAt(indexofRow).getId() + "", Toast.LENGTH_LONG).show();
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
                focusMaPN = (TextView) focusRow.getChildAt(0);
                focusNLPN = (TextView) focusRow.getChildAt(1);
                setNormalBGTableRows(list);
            }
        });
    }

    // --------------- DIALOG HELPER -----------------------------------------------------------------
    public void createDialog(int layout) {
        phieunhapdialog = new Dialog(PhieuNhapLayout.this);
        phieunhapdialog.setContentView(layout);
        phieunhapdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phieunhapdialog.show();
    }

    public void setEventSpinnerMini() {
        PK_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PK_spinner_mini_maPK = phongKhoArrayList.get(position).getMapk();
//                Toast.makeText( NhanvienLayout.this, PB_spinner_mini_maPB+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PK_spinner_mini_maPK = phongKhoArrayList.get(0).getMapk();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventDatePicker() {
        strDate = formatDate(InttoStringDate(30, 5, 2022), true);
        datepickerNLPN.init(2022, 5, 30, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                strDate = formatDate(InttoStringDate(dayOfMonth, monthOfYear + 1, year), true);
//                 Toast.makeText( NhanvienLayout.this, strDate+"", Toast.LENGTH_LONG).show();
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setControlDialog() {
        backBtn = phieunhapdialog.findViewById(R.id.PN_backBtn);
        yesBtn = phieunhapdialog.findViewById(R.id.PN_yesInsertBtn);
        noBtn = phieunhapdialog.findViewById(R.id.PN_noInsertBtn);

        PK_spinner_mini = phieunhapdialog.findViewById(R.id.PN_PKSpinner_mini);
        ArrayList<String> PK_name = new ArrayList<>();
        for (PhongKho pk : phongKhoArrayList) {
            PK_name.add(pk.getTenpk());
        }
        PK_spinner_mini.setAdapter(loadSpinnerAdapter(PK_name));
        setEventSpinnerMini();

        inputMaPN = phieunhapdialog.findViewById(R.id.PN_inputSP);


        datepickerNLPN = phieunhapdialog.findViewById(R.id.PN_inputNLP);

        setEventDatePicker();

        showMPNError = phieunhapdialog.findViewById(R.id.PN_showMPNError);
        showMPKError = phieunhapdialog.findViewById(R.id.PN_showMPKError);

        showResult = phieunhapdialog.findViewById(R.id.PN_showResult);
        showConfirm = phieunhapdialog.findViewById(R.id.PN_showConfirm);
        showLabel = phieunhapdialog.findViewById(R.id.PN_showLabel);
    }

    public void setEventDialog(View view) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phieunhapdialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phieunhapdialog.dismiss();
            }
        });
        // D???a v??o c??c n??t m?? th???ng yesBtn s??? c?? event kh??c
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;
                switch (view.getId()) {
                    case R.id.PN_insertBtn: {
                        if (!isSafeDialog(false)) break;
                        PhieuNhap pn = new PhieuNhap(inputMaPN.getText().toString().trim(), strDate, PK_spinner_mini_maPK.trim());
                        phieunhapDB.ThemPhieuNhap(pn, PhieuNhapLayout.this, new PhieuNhapDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError(String error) {
                                Toast.makeText(PhieuNhapLayout.this, "Loi Ket Noi!", Toast.LENGTH_SHORT).show();
                                ErrorDialog();
                            }
                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(PhieuNhapLayout.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    phieunhap_table_list.removeAllViews();
                                    loadDatabase();
                                    notification.SendNotification(PhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pn.getSoPhieu());
                                }else {
                                    Toast.makeText(PhieuNhapLayout.this, "That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });
                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        focusRow = null;
                        focusMaPN = null;
                        focusNLPN = null;
                        PK_spinner_mini_maPK = null;
                    }
                    break;
                    case R.id.PN_editBtn: {
                        if (!isSafeDialog(true)) break;
                        TableRow tr = (TableRow) phieunhap_table_list.getChildAt(indexofRow);
                        TextView id = (TextView) tr.getChildAt(0);
                        TextView date = (TextView) tr.getChildAt(1);
                        PhieuNhap pn = new PhieuNhap(
                                id.getText().toString().trim()
                                , strDate
                                , PK_spinner_mini_maPK);
                        phieunhapDB.CapNhatPhieuNhap(pn, PhieuNhapLayout.this, new PhieuNhapDB.VolleyCallBack() {
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
                                    notification.SendNotification(PhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pn.getSoPhieu());
                                }
                                else{
                                    ErrorDialog();
                                }
                            }
                        });
                    }
                    break;
                    case R.id.PN_delBtn: {
                        PhieuNhap pn = new PhieuNhap(
                                focusMaPN.getText().toString().trim(),
                                focusNLPN.getText().toString().trim(),
                                PK_spinner_mini_maPK.trim());
                        phieunhapDB.XoaPhieuNhap(pn, PhieuNhapLayout.this, new PhieuNhapDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(PhieuNhapLayout.this, "X???y ra l???i !", Toast.LENGTH_SHORT).show();
                                Log.d("AAA", "L???i! \n" + error.toString());
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if (response.trim().equalsIgnoreCase("success")){
                                    Toast.makeText(PhieuNhapLayout.this, "Xoa Th??nh C??ng!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    loadDatabase();
                                    notification.SendNotification(PhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + pn.getSoPhieu());
                                }
                                else{
                                    Toast.makeText(PhieuNhapLayout.this, "L???i Xoa!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });

                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        focusRow = null;
                        focusMaPN = null;
                        focusNLPN = null;
                        PK_spinner_mini_maPK = null;
                    }
                    break;
                    default:
                        break;
                }
            }
        });


    }

    public boolean isSafeDialog(boolean allowSameID) {
        String mapn, tenpk;
        // M?? PN kh??ng ???????c tr??ng v???i M?? PN kh??c v?? ko ????? tr???ng
        mapn = inputMaPN.getText().toString().trim();
        boolean noError = true;
        if (mapn.equals("")) {
            showMPNError.setText("M?? PN kh??ng ???????c tr???ng ");
            showMPNError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showMPNError.setVisibility(View.INVISIBLE);
            noError = true;
        }


        if (PK_spinner_mini_maPK == null) {
            showMPKError.setText("M?? PK kh??ng ???????c tr???ng ");
            showMPKError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showMPKError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        if (noError) {
            for (int i = 1; i < phieunhap_table_list.getChildCount(); i++) {
                TableRow tr = (TableRow) phieunhap_table_list.getChildAt(i);
                TextView mapn_data = (TextView) tr.getChildAt(0);
                if (!allowSameID)
                    if (mapn.equalsIgnoreCase(mapn_data.getText().toString())) {
                        showMPNError.setText("M?? PN kh??ng ???????c tr??ng ");
                        showMPNError.setVisibility(View.VISIBLE);
                        return noError = false;
                    }
            }
            showMPNError.setVisibility(View.INVISIBLE);
        }
        return noError;
    }

    // --------------- CUSTOM HELPER -----------------------------------------------------------------
    public ArrayAdapter<String> loadSpinnerAdapter(ArrayList<String> phongkho) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongkho);
        return adapter;
    }

    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
    }

    public int[] StringtoIntDate(String str) {
        int[] date = new int[3];
        String[] arr = str.split("-");
        date[0] = Integer.parseInt(arr[0]);
        date[1] = Integer.parseInt(arr[1]);
        date[2] = Integer.parseInt(arr[2]);
        return date; // 30/08/1999 -> [30,08,1999]
    }

    public String InttoStringDate(int[] date) {
        String day = (date[0] < 10) ? '0' + date[0] + "" : date[0] + "";
        String month = (date[1] < 10) ? '0' + date[1] + "" : date[1] + "";
        String year = date[2] + "";
        return day + "/" + month + "/" + year; // [30,08,1999] -> 30/08/1999
    }

    public String InttoStringDate(int date_day, int date_month, int date_year) {
        Log.d("day", date_day + "");
        String day = (date_day < 10) ? "0" + date_day + "" : date_day + "";
        String month = (date_month < 10) ? "0" + date_month + "" : date_month + "";
        String year = date_year + "";
        return day + "/" + month + "/" + year; // [30,08,1999] -> 30/08/1999
    }

    public String formatDate(String str, boolean toSQL) {
        String[] date;
        String result = "";
        if (toSQL) {
            date = str.split("/");
            result = date[2] + "-" + date[1] + "-" + date[0];
        } else {
            date = str.split("-");
            result = date[2] + "/" + date[1] + "/" + date[0];
        }

        return result;
    }

    // This Custom Columns' Max Width : 65 p0 / 220 / <= 100 p0
    public TableRow createRow(Context context, PhieuNhap pn) {
        TableRow tr = new TableRow(context);

        //   M?? NV
        TextView maPN = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        maPN.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maPN.setWidth(DPtoPix(150));
        maPN.setPadding(20, 20, 20, 20);
        maPN.setText(pn.getSoPhieu().trim());


        //  Ng??y l???p phi???u
        TextView ngayLapPN = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        ngayLapPN.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        ngayLapPN.setPadding(20, 20, 20, 20);
        ngayLapPN.setWidth(DPtoPix(260));
        ngayLapPN.setText(pn.getNgayLap().trim());

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        tr.addView(maPN);
        tr.addView(ngayLapPN);

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
    public void SuccesssDialog(){
        showResult.setText(showLabel.getText() + " th??nh c??ng !");
        showResult.setTextColor(getResources().getColor(R.color.yes_color));
        showResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMaPN.setText("");
                showResult.setVisibility(View.INVISIBLE);
                phieunhapdialog.dismiss();
            }
        }, 1000);
    }
    public void ErrorDialog(){
        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
        showResult.setText(showLabel.getText() + " th???t b???i !");
        showResult.setVisibility(View.VISIBLE);
    }
}
