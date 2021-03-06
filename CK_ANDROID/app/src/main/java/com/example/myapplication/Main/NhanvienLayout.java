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

import com.example.myapplication.DB.NhanVienDB;
import com.example.myapplication.DB.PhongKhoDB;
import com.example.myapplication.Entities.NhanVien;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.JavaMailAPI;
import com.example.myapplication.Notification;
import com.example.myapplication.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class NhanvienLayout extends AppCompatActivity {
    // Main Layout
    TableLayout nhanvien_table_list;

    Spinner PK_spinner;
    String PK_spinner_maPK;

    Button insertBtn;
    Button editBtn;
    Button delBtn;
    Button mailBtn;
    Button exitBtn;

    // Navigation
    Button navPK;
    Button navNV;
    Button navVT;
    Button navPN;
    Button navCTPN;

    // Dialog Layout
    Dialog nhanviendialog;
    Dialog maildialog;

    Button backBtn;
    Button yesBtn;
    Button noBtn;

    Button backMailBtn;
    Button yesMailBtn;
    Button noMailBtn;

    EditText NV_searchView;

    Spinner PK_spinner_mini;
    Spinner Mail_spinner_mini;

    EditText inputMaNV;
    EditText inputTenNV;
    EditText inputMailNV;

    EditText inputsubMail, inputmessMail;

    DatePicker datepickerNSNV;

    String PK_spinner_mini_maPK;
    String NV_spinner_mini_mail;
    String strDate;

    TextView showMPKError;
    TextView showMNVError;
    TextView showTNVError;
    TextView showResult;
    TextView showConfirm;
    TextView showLabel;

    TextView showEmailError, showSubjectError, showMessageError;

    // Database Controller
    NhanVienDB nhanvienDB;
    PhongKhoDB phongkhoDB;

    ArrayList<String> email = new ArrayList<>();

    ArrayList<PhongKho> phongkholist;
    ArrayList<NhanVien> nhanvienlist;

    // Focus
    int indexofRow = -1;
    TableRow focusRow;
    TextView focusMaNV;
    TextView focusTenNV;
    TextView focusMailNV;
    TextView focusNSNV;
    String focusPKNV;

    // Other
    float scale;
    Notification notification = new Notification();

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nhanvien_layout);
        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
        NV_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String toString) {
        TableRow tr = (TableRow) nhanvien_table_list.getChildAt(0);
        int dem =0;
                nhanvien_table_list.removeAllViews();
                nhanvien_table_list.addView(tr);
                for (int k = 0; k < nhanvienlist.size(); k++) {
                    NhanVien nv = nhanvienlist.get(k);
                    if (nv.getMaNv().toLowerCase().trim().contains(toString.toLowerCase().trim()) || nv.getHoTen().toLowerCase().contains(toString.toLowerCase())) {

                        tr = createRow(NhanvienLayout.this, nv);

                        tr.setId((int) dem++);
                        nhanvien_table_list.addView(tr);
                        setEventTableRows(tr, nhanvien_table_list);
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
        nhanvien_table_list = findViewById(R.id.NV_table_list);
        NV_searchView = findViewById(R.id.NV_searchEdit);
        PK_spinner = findViewById(R.id.NV_PKSpinner);
        insertBtn = findViewById(R.id.NV_insertBtn);
        editBtn = findViewById(R.id.NV_editBtn);
        delBtn = findViewById(R.id.NV_delBtn);
        mailBtn = findViewById(R.id.NV_mailBtn);
        exitBtn = findViewById(R.id.NV_exitBtn);

        navPK = findViewById(R.id.NV_navbar_phongkho);
        navNV = findViewById(R.id.NV_navbar_nhanvien);
        navVT = findViewById(R.id.NV_navbar_VT);
        navPN = findViewById(R.id.NV_navbar_PN);
        navCTPN = findViewById(R.id.NV_navbar_capphat);
    }

    public void loadDatabase() {
        Log.d("data", "Load Database --------");
        nhanvienDB = new NhanVienDB();
        phongkhoDB = new PhongKhoDB();
        nhanvienlist = new ArrayList<>();
        phongkholist = new ArrayList<>();
        setCursorWindowImageSize(100 * 1024 * 1024);
        nhanvienDB.GetData(nhanvienlist, this, new NhanVienDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                TableRow tr = null;
                for (int i = 0; i < nhanvienlist.size(); i++) {
                    Log.d("data", nhanvienlist.get(i).toString());
                    tr = createRow(NhanvienLayout.this, nhanvienlist.get(i));
                    tr.setId((int) i + 1);
                    nhanvien_table_list.addView(tr);
                    setEventTableRows(tr, nhanvien_table_list);
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });

        phongkhoDB.GetData(phongkholist, this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                ArrayList<String> PK_name = new ArrayList<>();
                PK_name.add("T???t c??? ph??ng kho");
                for (PhongKho pk : phongkholist) {
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
        setEventTable(nhanvien_table_list);
        setEventSpinner();
    }

    public void setNavigation() {
        // navNV onclick none
        // navPB
        navPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NhanvienLayout.this, PhongkhoLayout.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(intent);

            }
        });
        // navVPP
        navVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NhanvienLayout.this, VattuLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(intent);

            }

        });
        // navPN
        navPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(NhanvienLayout.this, PhieuNhapLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity(intent);

            }

        });
        // navCP
        navCTPN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhanvienLayout.this, ChiTietPhieuNhapLayout.class);
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

    public void setEventSearchView() {
//        NV_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//
//                int dem = 1;
//                // Select l???i to??n b??? table
//                TableRow tr = (TableRow) nhanvien_table_list.getChildAt(0);
//                nhanvien_table_list.removeAllViews();
//                nhanvien_table_list.addView(tr);
//                for (int k = 0; k < nhanvienlist.size(); k++) {
//                    NhanVien nv = nhanvienlist.get(k);
//                    if (nv.getMaNv().trim().equals(s.trim())) {
//                        tr = createRow(NhanvienLayout.this, nv);
//                        tr.setId((int) dem++);
//                        nhanvien_table_list.addView(tr);
//                        setEventTableRows(tr, nhanvien_table_list);
//                    }
//                }
//
//
//                return false;
//            }
//        });

    }

    public void setEventSpinner() {
        PK_spinner_maPK = "All";
        PK_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (nhanvien_table_list.getChildCount() < nhanvienlist.size() + 1) {
                        PK_spinner_maPK = "All";
                        // N???u c?? sort tr?????c ???? l??m cho s??? nh??n vi??n nh??? h??n s??? nh??n vi??n t???ng th?? m???i sort l???i theo all
                        TableRow tr = (TableRow) nhanvien_table_list.getChildAt(0);
                        nhanvien_table_list.removeAllViews();
                        nhanvien_table_list.addView(tr);
                        // Tag s??? b???t ?????u ??? 1 v?? ph???i c???ng th??m th???ng example ???? c?? s???n
                        for (int i = 0; i < nhanvienlist.size(); i++) {
                            NhanVien nv = nhanvienlist.get(i);
                            tr = createRow(NhanvienLayout.this, nv);
                            tr.setId((int) i + 1);
                            nhanvien_table_list.addView(tr);
                            setEventTableRows(tr, nhanvien_table_list);
                        }
                    }
                } else {
                    int dem = 1;
                    String mapk = phongkholist.get(position - 1).getMapk();
                    PK_spinner_maPK = mapk;
                    // Select l???i to??n b??? table
                    TableRow tr = (TableRow) nhanvien_table_list.getChildAt(0);
                    nhanvien_table_list.removeAllViews();
                    nhanvien_table_list.addView(tr);
                    for (int i = 0; i < nhanvienlist.size(); i++) {
                        NhanVien nv = nhanvienlist.get(i);
                        if (nv.getMaPk().trim().equals(mapk.trim())) {
                            tr = createRow(NhanvienLayout.this, nv);
                            tr.setId((int) dem++);
                            nhanvien_table_list.addView(tr);
                            setEventTableRows(tr, nhanvien_table_list);
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
        for (int i = 0; i < list.getChildCount(); i++) {
            setEventTableRows((TableRow) list.getChildAt(i), list);
        }
        mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMailDialog(R.layout.popup_sendemail);
                setControlMailDialog();
                setEventMailDialog();
            }
        });
        // Khi t???o, d??ng n l??m tag ????? th??m row
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // H b???m 1 c??i l?? hi???n ra c??i pop up
                createDialog(R.layout.popup_nhanvien);
                // Control
                setControlDialog();
                // Event
                strDate = formatDate(InttoStringDate(30, 8, 1999), true);
                setEventDialog(v);
            }
        });
        // Khi edit
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexofRow != -1) {
                    createDialog(R.layout.popup_nhanvien);
                    setControlDialog();
                    showLabel.setText("S???a th??ng tin nh??n vi??n");
                    showConfirm.setText("B???n c?? mu???n s???a h??ng n??y kh??ng?");
                    setEventDialog(v);
                    int index = 0;
                    for (int i = 0; i < phongkholist.size(); i++) {
                        // N???u th???ng ???????c focus c?? m?? PB tr??ng v???i PB trong list th?? break
                        if (phongkholist.get(i).getMapk().trim().equals(focusPKNV.trim())) {
                            index = i;
                            break;
                        }
                    }
                    int[] date = StringtoIntDate(focusNSNV.getText().toString().trim());
                    PK_spinner_mini.setSelection(index);
                    inputMaNV.setText(focusMaNV.getText());
                    inputTenNV.setText(focusTenNV.getText());
                    for(int i = 0; i < nhanvienlist.size(); i++) {
                        if(nhanvienlist.get(i).getMaNv().equals(focusMaNV.getText())) {
                            inputMailNV.setText(nhanvienlist.get(i).getEmail());
                        }
                    }
                    datepickerNSNV.updateDate(date[2], date[1] - 1, date[0]);
                    inputMaNV.setEnabled(false);
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
                    // Test
                    //  Toast.makeText( PhongbanLayout.this, indexofRow+"", Toast.LENGTH_LONG).show();
                    createDialog(R.layout.popup_nhanvien);
                    // Control
                    setControlDialog();
                    showLabel.setText("X??a th??ng tin nh??n vi??n");
                    showConfirm.setText("B???n c?? mu???n x??a h??ng n??y kh??ng?");
                    // Event
                    setEventDialog(v);
                    int index = 0;
                    for (int i = 0; i < phongkholist.size(); i++) {
                        // N???u th???ng ???????c focus c?? m?? PB tr??ng v???i PB trong list th?? break
                        if (phongkholist.get(i).getMapk().trim().equals(focusPKNV.trim())) {
                            index = i;
                            break;
                        }
                    }
                    int[] date = StringtoIntDate(focusNSNV.getText().toString().trim());

                    PK_spinner_mini.setSelection(index);
                    inputMaNV.setText(focusMaNV.getText());
                    inputTenNV.setText(focusTenNV.getText());
                    for(int i = 0; i < nhanvienlist.size(); i++) {
                        if(nhanvienlist.get(i).getMaNv().equals(focusMaNV.getText())) {
                            inputMailNV.setText(nhanvienlist.get(i).getEmail());
                        }
                    }
                    datepickerNSNV.updateDate(date[2], date[1] - 1, date[0]);
                    PK_spinner_mini.setEnabled(false);
                    inputMaNV.setEnabled(false);
                    inputTenNV.setEnabled(false);
                    datepickerNSNV.setEnabled(false);
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
                focusMaNV = (TextView) focusRow.getChildAt(0);
                focusTenNV = (TextView) focusRow.getChildAt(1);
                focusNSNV = (TextView) focusRow.getChildAt(2);
                focusPKNV = focusRow.getTag().toString();
                setNormalBGTableRows(list);
            }
        });
    }

    // --------------- DIALOG HELPER -----------------------------------------------------------------
    public void createDialog(int layout) {
        nhanviendialog = new Dialog(NhanvienLayout.this);
        nhanviendialog.setContentView(layout);
        nhanviendialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        nhanviendialog.show();
    }
    public void createMailDialog(int layout){
        maildialog = new Dialog(this);
        maildialog.setContentView(layout);
        maildialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        maildialog.show();
    }


    public void setEventSpinnerMini() {
        PK_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PK_spinner_mini_maPK = phongkholist.get(position).getMapk();
//                Toast.makeText( NhanvienLayout.this, PB_spinner_mini_maPB+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PK_spinner_mini_maPK = phongkholist.get(0).getMapk();
            }
        });
    }
    public void setEventMailSpinnerMini() {
        Mail_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NV_spinner_mini_mail = email.get(position).trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                NV_spinner_mini_mail = email.get(0);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventDatePicker() {
        strDate = formatDate(InttoStringDate(30, 8, 1999), true);
        datepickerNSNV.init(1999, 07, 30, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                strDate = formatDate(InttoStringDate(dayOfMonth, monthOfYear + 1, year), true);
//                 Toast.makeText( NhanvienLayout.this, strDate+"", Toast.LENGTH_LONG).show();
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setControlDialog() {
        backBtn = nhanviendialog.findViewById(R.id.NV_backBtn);
        yesBtn = nhanviendialog.findViewById(R.id.NV_yesInsertBtn);
        noBtn = nhanviendialog.findViewById(R.id.NV_noInsertBtn);

        PK_spinner_mini = nhanviendialog.findViewById(R.id.NV_PKSpinner_mini);
        ArrayList<String> PK_name = new ArrayList<>();
        for (PhongKho pk : phongkholist) {
            PK_name.add(pk.getTenpk());
        }
        PK_spinner_mini.setAdapter(loadSpinnerAdapter(PK_name));
        setEventSpinnerMini();

        inputMaNV = nhanviendialog.findViewById(R.id.NV_inputMaNV);
        inputTenNV = nhanviendialog.findViewById(R.id.NV_inputTenNV);
        inputMailNV = nhanviendialog.findViewById(R.id.NV_inputMailNV);

        datepickerNSNV = nhanviendialog.findViewById(R.id.NV_inputNS);

        setEventDatePicker();

        showMPKError = nhanviendialog.findViewById(R.id.NV_showMPKError);
        showMNVError = nhanviendialog.findViewById(R.id.NV_showMNVError);
        showTNVError = nhanviendialog.findViewById(R.id.NV_showTNVError);

        showResult = nhanviendialog.findViewById(R.id.NV_showResult);
        showConfirm = nhanviendialog.findViewById(R.id.NV_showConfirm);
        showLabel = nhanviendialog.findViewById(R.id.NV_showLabel);
    }
    public void setControlMailDialog(){
        backMailBtn = maildialog.findViewById(R.id.NV_backMailBtn);
        yesMailBtn = maildialog.findViewById(R.id.NV_yesMailInsertBtn);
        noMailBtn = maildialog.findViewById(R.id.NV_noMailInsertBtn);

        Mail_spinner_mini = maildialog.findViewById(R.id.NV_EmailSpinner_mini);
        email.clear();
        for(NhanVien nv : nhanvienlist){
            String mail = nv.getEmail().trim();
            boolean check = !mail.equalsIgnoreCase("null") && !mail.isEmpty();
            if(check){
                email.add(mail);
            }
        }
        Mail_spinner_mini.setAdapter(loadSpinnerAdapter(email));
        setEventMailSpinnerMini();
        inputsubMail = maildialog.findViewById(R.id.NV_inputSubject);
        inputmessMail = maildialog.findViewById(R.id.NV_inputMessage);

        showEmailError = maildialog.findViewById(R.id.NV_showEmailError);
        showSubjectError = maildialog.findViewById(R.id.NV_showSubjectError);
        showMessageError = maildialog.findViewById(R.id.NV_showMessageError);

        showResult = maildialog.findViewById(R.id.NV_showMailResult);
        showConfirm = maildialog.findViewById(R.id.NV_showMailConfirm);
        showLabel = maildialog.findViewById(R.id.NV_showMailLabel);
    }
    public void setEventMailDialog(){
        backMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maildialog.dismiss();
            }
        });
        noMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maildialog.dismiss();
            }
        });
        yesMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSafeMailDialog()){
                    SendMail();
                    SuccesssMailDialog();
                }else {
                    ErrorMailDialog();
                }
            }
        });
    }
    public void SendMail(){
        String email = NV_spinner_mini_mail.trim();
        String subject = inputsubMail.getText().toString().trim();
        String message = inputmessMail.getText().toString();
        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email, subject, message);

        javaMailAPI.execute();
    }
    public void setEventDialog(View view) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanviendialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanviendialog.dismiss();
            }
        });
        // D???a v??o c??c n??t m?? th???ng yesBtn s??? c?? event kh??c
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showMPBError.setVisibility(View.VISIBLE);
                //  showTPBError.setVisibility(View.VISIBLE);
                //  showResult.setVisibility(View.VISIBLE);
                boolean success = false;
                switch (view.getId()) {
                    case R.id.NV_insertBtn: {
                        if (!isSafeDialog(false)) break;
//                        Log.d("process","1True");
                        NhanVien nv = new NhanVien(
                                inputMaNV.getText().toString().trim() + ""
                                , inputTenNV.getText().toString().trim() + ""
                                , strDate
                                , PK_spinner_mini_maPK.trim(), inputMailNV.getText().toString().trim() + "");
                        nhanvienDB.ThemNhanVien(nv, NhanvienLayout.this, new NhanVienDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(NhanvienLayout.this, "Loi!", Toast.LENGTH_SHORT).show();
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(NhanvienLayout.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    TableRow tr = createRow(NhanvienLayout.this, nv);
                                    int n = nhanvien_table_list.getChildCount();
                                    tr.setId(n);
                                    if (!PK_spinner_mini_maPK.trim().equals(PK_spinner_maPK.trim())) {
                                        // N???u th???ng b??n trong l?? ph??ng kho nh??ng b??n ngo??i l?? t???t c??? ph??ng kho th??
                                        if (PK_spinner_maPK.trim().equals("All")) {
                                            // c??? insert nh?? bth
                                            nhanvien_table_list.addView(tr);
                                            setEventTableRows((TableRow) nhanvien_table_list.getChildAt(n), nhanvien_table_list);
                                        }
                                        // N???u th???ng b??n trong l?? ph??ng ban nh??ng b??n ngo??i l?? ph??ng ban kh??c th?? kh???i th??m table
                                        // N???u tr??ng
                                    } else {
                                        nhanvien_table_list.addView(tr);
                                        setEventTableRows((TableRow) nhanvien_table_list.getChildAt(n), nhanvien_table_list);
                                    }
                                    nhanvienlist.add(nv);
                                    editBtn.setVisibility(View.INVISIBLE);
                                    delBtn.setVisibility(View.INVISIBLE);
                                    focusRow = null;
                                    focusMaNV = null;
                                    focusTenNV = null;
                                    focusNSNV = null;
                                    focusPKNV = "";
                                    notification.SendNotification(NhanvienLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + nv.getHoTen());
                                }else {
                                    Toast.makeText(NhanvienLayout.this,"That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });
                    }
                    break;
                    case R.id.NV_editBtn: {
                        if (!isSafeDialog(true)) break;
//                        Log.d("process","1True");
                        TableRow tr = (TableRow) nhanvien_table_list.getChildAt(indexofRow);
                        TextView id = (TextView) tr.getChildAt(0);
                        TextView name = (TextView) tr.getChildAt(1);
                        TextView date = (TextView) tr.getChildAt(2);
                        NhanVien nv = new NhanVien(
                                id.getText().toString().trim()
                                , inputTenNV.getText().toString().trim()
                                , strDate
                                , PK_spinner_mini_maPK,inputMailNV.getText().toString().trim() + "");
                        nhanvienDB.CapNhatNhanVien(nv, NhanvienLayout.this, new NhanVienDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {

                            }

                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(NhanvienLayout.this, "Cap nhat thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    int index = 0;
                                    for (int i = 0; i < nhanvienlist.size(); i++) {
                                        if (nhanvienlist.get(i).getMaNv().equals(id.getText().toString().trim())) {
                                            index = i;
                                            break;
                                        }
                                    }
                                    Log.d("process", index + "");
                                    nhanvienlist.set(index, nv);
                                    boolean edit = false, changePB = false;
                                    if (!PK_spinner_mini_maPK.trim().equals(PK_spinner_maPK.trim())) {
                                        // Khi kh??ng c???n bi???t thay ?????i PB nh?? th??? n??o nh??ng b??n ngo??i l?? All th?? c??? edit th??i
                                        if (PK_spinner_maPK.trim().equals("All"))
                                            edit = true;
                                            // V???y tr?????ng h???p ??ang l?? Ph??ng gi??m ?????c mu???n thay Ph??ng k??? thu???t th??
                                        else {
                                            changePB = true;
                                        }
                                    } else {
                                        // Khi gi??? nguy??n ph??ng ban
                                        edit = true;
                                    }

                                    if (edit) {
                                        name.setText(nv.getHoTen() + "");
                                        date.setText(formatDate(strDate, false));
                                        tr.setTag(nv.getMaPk());
                                    }
                                    if (changePB) {
                                        if (indexofRow == nhanvien_table_list.getChildCount() - 1) {
                                            nhanvien_table_list.removeViewAt(indexofRow);
                                        } else {
                                            nhanvien_table_list.removeViewAt(indexofRow);
                                            for (int i = 0; i < nhanvien_table_list.getChildCount(); i++) {
                                                nhanvien_table_list.getChildAt(i).setId((int) i);
                                            }
                                        }
                                    }
                                    notification.SendNotification(NhanvienLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + nv.getHoTen());
                                }else {
                                    Toast.makeText(NhanvienLayout.this,"That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });
                    }
                    break;
                    case R.id.NV_delBtn: {
                        NhanVien nv = new NhanVien(
                                focusMaNV.getText().toString().trim(),
                                focusTenNV.getText().toString().trim(),
                                formatDate(focusNSNV.getText().toString().trim(), true),
                                focusPKNV.trim());
                        nhanvienDB.XoaNhanVien(nv, NhanvienLayout.this, new NhanVienDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {

                            }

                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(NhanvienLayout.this, "Xoa thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                if (indexofRow == nhanvien_table_list.getChildCount() - 1) {
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                } else {
                                    nhanvien_table_list.removeViewAt(indexofRow);
                                    for (int i = 0; i < nhanvien_table_list.getChildCount(); i++) {
                                        nhanvien_table_list.getChildAt(i).setId((int) i);
                                    }
                                }
                                int index = 0;
                                for (int i = 0; i < nhanvienlist.size(); i++) {
                                    if (nhanvienlist.get(i).getMaNv().equals(focusMaNV.getText().toString().trim())) {
                                        index = i;
                                        break;
                                    }
                                }
                                nhanvienlist.remove(index);
                                editBtn.setVisibility(View.INVISIBLE);
                                delBtn.setVisibility(View.INVISIBLE);
                                focusRow = null;
                                focusMaNV = null;
                                focusTenNV = null;
                                focusNSNV = null;
                                focusPKNV = "";
                                    notification.SendNotification(NhanvienLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + nv.getHoTen());
                                }else {
                                    Toast.makeText(NhanvienLayout.this,"That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });
                    }
                    break;
                    default:
                        break;
                }
            }
        });


    }

    public boolean isSafeDialog(boolean allowSameID) {
        String manv, tennv;
        // M?? NV kh??ng ???????c tr??ng v???i M?? NV kh??c v?? ko ????? tr???ng
        manv = inputMaNV.getText().toString().trim();
        boolean noError = true;
        if(PK_spinner_mini_maPK == null){
            showMPKError.setText("M?? PK kh??ng ???????c tr???ng ");
            showMPKError.setVisibility(View.VISIBLE);
            noError = false;
        }else{
            showMPKError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }
        if (manv.equals("")) {
            showMNVError.setText("M?? NV kh??ng ???????c tr???ng ");
            showMNVError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showMNVError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        // T??n NV kh??ng ???????c ????? tr???ng v?? kh??ng tr??ng
        tennv = inputTenNV.getText().toString().trim();
        if (tennv.equals("")) {
            showTNVError.setText("T??n NV kh??ng ???????c tr???ng ");
            showTNVError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showTNVError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        if (noError) {
            for (int i = 1; i < nhanvien_table_list.getChildCount(); i++) {
                TableRow tr = (TableRow) nhanvien_table_list.getChildAt(i);
                TextView mapb_data = (TextView) tr.getChildAt(0);
                TextView tenpb_data = (TextView) tr.getChildAt(1);

                if (!allowSameID)
                    if (manv.equalsIgnoreCase(mapb_data.getText().toString())) {
                        showMNVError.setText("M?? NV kh??ng ???????c tr??ng ");
                        showMNVError.setVisibility(View.VISIBLE);
                        return noError = false;
                    }
            }
            showMNVError.setVisibility(View.INVISIBLE);
            showTNVError.setVisibility(View.INVISIBLE);
        }
        return noError;
    }
    public boolean isSafeMailDialog(){
        String sub, mess;
        sub = inputsubMail.getText().toString().trim();
        mess = inputmessMail.getText().toString();
        boolean noError = true;
        if (NV_spinner_mini_mail == null) {
            showEmailError.setText("Email kh??ng ???????c tr???ng ");
            showEmailError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showEmailError.setVisibility(View.INVISIBLE);
            noError = true;
        }
        if (sub.equals("")) {
            showSubjectError.setText("Ti??u ????? kh??ng ???????c tr???ng ");
            showSubjectError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showSubjectError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }
        if (mess.equals("")) {
            showMessageError.setText("N???i dung kh??ng ???????c tr???ng ");
            showMessageError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showMessageError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }
        return noError;
    }
    public void SuccesssDialog(){
        showResult.setText(showLabel.getText() + " th??nh c??ng !");
        showResult.setTextColor(getResources().getColor(R.color.yes_color));
        showResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMaNV.setText("");
                inputTenNV.setText("");
                inputMailNV.setText("");
                showResult.setVisibility(View.INVISIBLE);
                nhanviendialog.dismiss();
            }
        }, 1000);
    }
    public void ErrorDialog(){
        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
        showResult.setText(showLabel.getText() + " th???t b???i !");
        showResult.setVisibility(View.VISIBLE);
    }
    public void SuccesssMailDialog(){
        showResult.setText(showLabel.getText() + " th??nh c??ng !");
        showResult.setTextColor(getResources().getColor(R.color.yes_color));
        showResult.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showResult.setVisibility(View.INVISIBLE);
                maildialog.dismiss();
            }
        }, 1000);
    }
    public void ErrorMailDialog(){
        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
        showResult.setText(showLabel.getText() + " th???t b???i !");
        showResult.setVisibility(View.VISIBLE);
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
        String[] arr = str.split("/");
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
    public TableRow createRow(Context context, NhanVien nv) {
        TableRow tr = new TableRow(context);

        //   M?? NV
        TextView maNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        maNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maNV.setMaxWidth(DPtoPix(65));
        maNV.setPadding(0, 0, 0, 0);
        maNV.setText(nv.getMaNv());

        //   T??n NV
        TextView tenNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        tenNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenNV.setMaxWidth(DPtoPix(220));
        tenNV.setText(nv.getHoTen());

        //  Ng??y sinh
        TextView ngaysinhNV = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);

        ngaysinhNV.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        ngaysinhNV.setPadding(0, 0, 0, 0);
        ngaysinhNV.setMaxWidth(DPtoPix(100));
        ngaysinhNV.setText(formatDate(nv.getNgaySinh(), false));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        //  M?? PB
        tr.setTag(nv.getMaPk() + "");

        tr.addView(maNV);
        tr.addView(tenNV);
        tr.addView(ngaysinhNV);

        return tr;
    }
}
