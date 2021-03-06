package com.example.myapplication.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.myapplication.DB.ChiTietPhieuNhapDB;
import com.example.myapplication.DB.PhieuNhapDB;
import com.example.myapplication.DB.PhongKhoDB;
import com.example.myapplication.DB.VatTuDB;
import com.example.myapplication.Entities.ChiTietPhieuNhap;
import com.example.myapplication.Entities.PhieuNhap;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.Entities.Rows;
import com.example.myapplication.Entities.VatTu;
import com.example.myapplication.Notification;
import com.example.myapplication.R;
import com.example.myapplication.Statistics.BaoCaoLayout;
import com.example.myapplication.Statistics.ThongKeLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ChiTietPhieuNhapLayout extends AppCompatActivity {
    public static PhongKho selectedPK = null;
    public static int totalMoney = 0;
    // Main Layout

    Button editBtn;
    Button delBtn;
    Button exitBtn;

    Button backBtn;
    Button yesBtn;
    Button noBtn;

    Spinner PKSpinner;
    Spinner PK_spinner_mini;
    Spinner PN_spinner_mini;
    Spinner VT_spinner_mini;
    Spinner NV_spinner_mini;

    String PK_spinner_mini_maPK;
    String PN_spinner_mini_maPN;
    String NV_spinner_mini_maNV;
    String VT_spinner_mini_maVT;

    EditText CP_searchView;
    EditText inputSLVT;

    DatePicker datePickerNLP;

    LinearLayout cp_tablesall_container;

    LinearLayout cp_tablesindex_container;
    TableLayout cp_tablevt_list;
    TableLayout cp_tablectpn_list;
    TableLayout cp_tablepn_list;

    TextView cp_totalCount;
    TextView cp_totalPrice;
    TextView warningLabel;
    TextView labelVT;
    TextView noteVTLabel;
    TextView noteTotalLabel;

    TextView showPNError;
    TextView showVTError;
    TextView showSLVTError;

    TextView showResult;
    TextView showConfirm;
    TextView showLabel;


    Button previewVTBtn;
    Button cpInsertBtn;
    Button navBC;
    Button navTK;

    // Data
    PhieuNhapDB phieunhapDB;
    ChiTietPhieuNhapDB chitietpnDB;
    VatTuDB vattuDB;
    PhongKhoDB phongkhoDB;

    ArrayList<PhieuNhap> phieuNhapArrayList;
    ArrayList<ChiTietPhieuNhap> chiTietPhieuNhapArrayList;
    ArrayList<VatTu> vatTuArrayList;
    ArrayList<PhongKho> phongKhoArrayList;

    int totalPrice = 0;
    int VTCount = 0;

    // Dialog
    Dialog dialog;

    // Preview Image Layout
    TextView VT_IP_maVT;
    TextView VT_IP_tenVT;
    TextView VT_IP_DVT;
    TextView VT_IP_Gia;
    ImageView VT_IP_Hinh;

    // Focus
    TableRow focusRow;
    TextView focusSP;
    TextView focusDate;
    TextView focusMaVT;
    TextView focusTenVT;
    TextView focusMaPN;
    TextView focusDVT;
    TextView focusSL;
    TextView focusMaK;
    String dataMaPKSpinner;
    String strDate;
    // Other
    float scale;
    int indexofRow;
    Notification notification = new Notification();
    List<String> listAll;
    List<String> listIndex;
    List<String> listPnIndexPK;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chitietphieunhap_layout);
        scale = this.getResources().getDisplayMetrics().density;
        Rows.scale = scale;
        Rows.tvtemplate = com.example.myapplication.R.layout.tvtemplate;

        setControl();
        loadDatabase();
        setEvent();
        setNavigation();
        CP_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(dataMaPKSpinner.equalsIgnoreCase("All"))
                    filterALL(s.toString());
                else if(!dataMaPKSpinner.equalsIgnoreCase("All")) {
                    try {
                        filterIndex(s.toString(), dataMaPKSpinner);
                    } catch (JSONException | AuthFailureError e) {
                        e.printStackTrace();
                    }
                }
                editBtn.setVisibility(View.INVISIBLE);
                delBtn.setVisibility(View.INVISIBLE);
                previewVTBtn.setVisibility(View.INVISIBLE);
                labelVT.setVisibility(View.INVISIBLE);
            }
        });
        hideSystemUI();
    }

    private void filterALL(String toString) {
        Rows rowGenarator = new Rows(this);
        TableLayout pn_table1 = cp_tablectpn_list;
        pn_table1.removeViews(1, pn_table1.getChildCount() - 1);
        int[] sizeOfCell = {80, 40, 100, 100, 100};
        boolean[] isPaddingZero = {true, false, true, true, true};
        // Create List<TableRow> for TableList
        // TABLE CP INDEX 01 ----------------------------------------------------------------------------------------
        rowGenarator.setData(rowGenarator.enhanceRowData(listAll, 5));
        rowGenarator.setSizeOfCell(sizeOfCell);
        rowGenarator.setIsCellPaddingZero(isPaddingZero);
        List<TableRow> rows = rowGenarator.generateArrayofRows();
        for (TableRow row : rows) {
            focusMaPN = (TextView) row.getChildAt(0);
            String sptxt = focusMaPN.getText().toString();
            TextView mavt = (TextView) row.getChildAt(1);
            String mavttxt = mavt.getText().toString();
            TextView tenvt = (TextView) row.getChildAt(2);
            String tenvttxt = tenvt.getText().toString();
            if (sptxt.trim().toLowerCase().contains(toString.trim().toLowerCase())
                    || mavttxt.toLowerCase().contains(toString.toLowerCase())
                    || tenvttxt.toLowerCase().contains(toString.toLowerCase())) {

                pn_table1.addView(row);
                SetEventTableRows(row, cp_tablectpn_list);
            }

        }
    }

    private void filterIndex(String toString, String maPK) throws JSONException, AuthFailureError {
        Rows rowGenarator = new Rows(this);
        TableLayout cp_table1 = cp_tablesindex_container.findViewById(R.id.CP_tableVT);
        cp_table1.removeViews(1, cp_table1.getChildCount() - 1);
        int[] sizeOfCell = {85, 180, 50, 80};
        boolean[] isPaddingZero = {false, true, true, true};
        TableLayout cp_table2 = cp_tablesindex_container.findViewById(R.id.CP_tableSP);
        cp_table2.removeViews(1, cp_table2.getChildCount() - 1);
        int[] sizeOfCell2 = {90, 240};
        boolean[] isPaddingZero2 = {false, false};
        chitietpnDB.GetDataIndex(listIndex, this, new ChiTietPhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                rowGenarator.setData(rowGenarator.enhanceRowData(listIndex, 4));
                rowGenarator.setSizeOfCell(sizeOfCell);
                rowGenarator.setIsCellPaddingZero(isPaddingZero);
                List<TableRow> rows = rowGenarator.generateArrayofRows();
                for (TableRow row : rows) {

                    TextView mavt = (TextView) row.getChildAt(0);
                    String mavttxt = mavt.getText().toString();
                    TextView tenvt = (TextView) row.getChildAt(1);
                    String tenvttxt = tenvt.getText().toString();
                    if (mavttxt.toLowerCase().contains(toString.toLowerCase())
                            || tenvttxt.toLowerCase().contains(toString.toLowerCase())) {

                        cp_table1.addView(row);
                    }
                }
                for (int i = 1; i < cp_table1.getChildCount(); i++) {
                    TableRow row = (TableRow) cp_table1.getChildAt(i);

                    // T??? th???ng VT ???????c b???m gen ra th???ng nh??n vi??n ???? m?????n n??
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int dem = v.getId();
                            // TABLE CP INDEX 02 ----------------------------------------------------------------------------------------
                            // Set text for noteVTLabel -------------------------------------------------------------------------
                            TextView tenVTView = (TextView) row.getChildAt(1);
                            noteVTLabel.setVisibility(View.VISIBLE);
                            noteVTLabel.setText(tenVTView.getText().toString().trim() + " ???????c nh???p kho b???i phi???u nh???p d?????i ????y");
                            // ----------------------------------------------------------------------------------------------------
                            TextView maVTView = (TextView) row.getChildAt(0);
                            for (TableRow row : rows) {
                                row.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            row.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                            chitietpnDB.GetDataPnIndexPK(listPnIndexPK, ChiTietPhieuNhapLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    rowGenarator.setData(rowGenarator.enhanceRowData(
                                            listPnIndexPK, 2));
                                    cp_table2.removeViews(1, cp_table2.getChildCount() - 1);
                                    List<TableRow> rows2 = rowGenarator.generateArrayofRows();
                                    for (TableRow row2 : rows2) {
                                        cp_table2.addView(row2);
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }

                                @Override
                                public void onSuccess(String response) {

                                }
                            }, maPK, maVTView.getText().toString().trim());

                        }
                    });
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        }, maPK);
        // Create List<TableRow> for TableList
        // TABLE CP INDEX 01 ----------------------------------------------------------------------------------------

    }
    // --------------- MAIN HELPER -----------------------------------------------------------------
    public void setControl() {
//        Log.d("process", "setControl");
        exitBtn = findViewById(R.id.CP_backBtn);
        cpInsertBtn = findViewById(R.id.CP_insertBtn);

        PKSpinner = findViewById(R.id.CP_PKSpinner);
        CP_searchView = findViewById(R.id.CP_searchEdit);

        cp_tablesall_container = findViewById(R.id.CP_tablesAll_container);
        cp_tablesindex_container = findViewById(R.id.CP_tablesIndex_container);
        cp_tablevt_list = findViewById(R.id.CP_tableVT);
        cp_tablectpn_list = findViewById(R.id.CP_tableCTPN);
        cp_tablepn_list = findViewById(R.id.CP_tablePN);

        warningLabel = findViewById(R.id.CP_warningLabel);
        labelVT = findViewById(R.id.CP_labelVT);

        previewVTBtn = findViewById(R.id.CP_previewVTBtn);
        editBtn = findViewById(R.id.CP_editBtn);
        delBtn = findViewById(R.id.CP_delBtn);

        navBC = findViewById(R.id.CP_navbar_baocao);
        navTK = findViewById(R.id.CP_navbar_thongke);

    }

    public void loadDatabase() {
        //   Log.d("process", "loadDatabase");
        // 1.  Load Spinner ra tr?????c
        phongkhoDB = new PhongKhoDB();
        phongKhoArrayList = new ArrayList<>();
        listAll = new ArrayList<>();
        listIndex = new ArrayList<>();
        listPnIndexPK = new ArrayList<>();
        phongkhoDB.GetData(phongKhoArrayList, this, new PhongKhoDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                PKSpinner.setAdapter(loadPBSpinner());
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });


        phieunhapDB = new PhieuNhapDB();
        phieuNhapArrayList = new ArrayList<>();
        chitietpnDB = new ChiTietPhieuNhapDB();
        chiTietPhieuNhapArrayList = new ArrayList<>();
        vattuDB = new VatTuDB();
        vatTuArrayList = new ArrayList<>();
        phieunhapDB.GetData(phieuNhapArrayList, this, new PhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                for (int i = 0; i < phieuNhapArrayList.size(); i++) {
                    PhieuNhap phieunhap = phieuNhapArrayList.get(i);
                    TableRow tr = createRow(ChiTietPhieuNhapLayout.this, phieunhap);
                    tr.setId(i + 1);
                    cp_tablepn_list.addView(tr);
                }
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });
        chitietpnDB.GetDataALL(listAll, this, new ChiTietPhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess() {
                table(listAll);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });
        vattuDB.GetData(vatTuArrayList, this, new VatTuDB.VolleyCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

            }
        });

    }

    public void table(List<String> list) {
        Rows rowGenarator = new Rows(this);
        TableLayout pn_table1 = cp_tablectpn_list;
        pn_table1.removeViews(1, pn_table1.getChildCount() - 1);
        int[] sizeOfCell = {80, 40, 100, 100, 100};
        boolean[] isPaddingZero = {true, false, true, true, true};
        // Create List<TableRow> for TableList
        // TABLE CP INDEX 01 ----------------------------------------------------------------------------------------
        rowGenarator.setData(rowGenarator.enhanceRowData(list, 5));
        rowGenarator.setSizeOfCell(sizeOfCell);
        rowGenarator.setIsCellPaddingZero(isPaddingZero);
        List<TableRow> rows = rowGenarator.generateArrayofRows();

        for (TableRow row : rows) {
            pn_table1.addView(row);
            SetEventTableRows(row, pn_table1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEvent() {
        labelVT.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.INVISIBLE); // turn on when click items
        delBtn.setVisibility(View.INVISIBLE);  // this too
        previewVTBtn.setVisibility(View.INVISIBLE);
        // 1. Set Event cho Spinner
        setEventPBSpinner();

        setEventTable(cp_tablectpn_list);
    }

    public void SetEventTableRows(TableRow tr, TableLayout list){
        for (int i = 1; i < list.getChildCount(); i++) {

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editBtn.setVisibility(View.VISIBLE);
                    delBtn.setVisibility(View.VISIBLE);
                    previewVTBtn.setVisibility(View.VISIBLE);
                    for(int i = 0; i < list.getChildCount(); i ++) {
                        TableRow row = (TableRow) list.getChildAt(i);
                        row.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    v.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                    focusSL = (TextView) tr.getChildAt(4);
                    focusMaPN = (TextView) tr.getChildAt(0);
                    focusTenVT = (TextView) tr.getChildAt(2);
                    focusMaVT = (TextView) tr.getChildAt(1);
                    String mavttxt = focusMaVT.getText().toString();
                    setEventTableRowsHelper(cp_tablepn_list);
                    setEventDisplayVT(mavttxt);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEventTable(TableLayout list) {
        // Log.d("count", list.getChildCount()+""); // s??? table rows + 1
        // Kh??ng c???n thay ?????i v?? ????y ch??? m???i set Event
        // Do c?? th??m 1 th???ng example ????? l??m g???c, n??n s??? row th?? lu??n lu??n ph???i + 1
        // C?? example th?? khi th??m row th?? n?? s??? theo khu??n
        for (int i = 0; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt(i);
            SetEventTableRows(row, list);
        }
        // Khi t???o, d??ng n l??m tag ????? th??m row
        cpInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(R.layout.popup_chitietphieunhap);
                // Control
                setControlDialog();
                // Event
                strDate = formatDate(InttoStringDate(30, 8, 1999), true);
                setEventDialog(v);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(R.layout.popup_chitietphieunhap);
                // Control
                setControlDialog();
                setEventDialog(v);
                showLabel.setText("S???a CTPN");
                showConfirm.setText("B???n c?? mu???n s???a h??ng n??y kh??ng?");

                inputSLVT.setText(focusSL.getText());
                int pn = 0, vt = 0;
                //phieuNhapArrayList = phieunhapDB.select();
                for (int i = 0; i < phieuNhapArrayList.size(); i++){
                    String maPNlist = phieuNhapArrayList.get(i).getSoPhieu();
                    String maPN = focusMaPN.getText().toString().trim();
                    if (maPNlist.equalsIgnoreCase(maPN)){
                        pn = i;break;
                    }
                }
                for (int i = 0; i < vatTuArrayList.size(); i++){
                    String mavtl = vatTuArrayList.get(i).getMaVt().trim();
                    String mavt = focusMaVT.getText().toString().trim();
                    if (mavtl.equalsIgnoreCase(mavt)){
                        vt = i;break;
                    }
                }
                PN_spinner_mini.setSelection(pn);
                VT_spinner_mini.setSelection(vt);
                PN_spinner_mini.setEnabled(false);
                VT_spinner_mini.setEnabled(false);
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(R.layout.popup_chitietphieunhap);
                // Control
                setControlDialog();
                setEventDialog(v);
                showLabel.setText("X??a CTPN");
                showConfirm.setText("B???n c?? mu???n x??a h??ng n??y kh??ng?");

                inputSLVT.setText(focusSL.getText());
                int pn = 0, vt = 0;
                //phieuNhapArrayList = phieunhapDB.select();
                for (int i = 0; i < phieuNhapArrayList.size(); i++){
                    String maPNlist = phieuNhapArrayList.get(i).getSoPhieu();
                    String maPN = focusMaPN.getText().toString().trim();
                    if (maPNlist.equalsIgnoreCase(maPN)){
                        pn = i;break;
                    }
                }
                for (int i = 0; i < vatTuArrayList.size(); i++){
                    String mavtl = vatTuArrayList.get(i).getMaVt().trim();
                    String mavt = focusMaVT.getText().toString().trim();
                    if (mavtl.equalsIgnoreCase(mavt)){
                        vt = i;break;
                    }
                }
                PN_spinner_mini.setSelection(pn);
                VT_spinner_mini.setSelection(vt);
                PN_spinner_mini.setEnabled(false);
                VT_spinner_mini.setEnabled(false);
                inputSLVT.setEnabled(false);
            }
        });
    }

    public void setNavigation() {
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if( selectedPK != null )
                    intent = new Intent(ChiTietPhieuNhapLayout.this, BaoCaoLayout.class);
                else
                    intent = new Intent(ChiTietPhieuNhapLayout.this, BaoCaoLayout.class);
                if( totalPrice != 0 ) totalMoney = totalPrice;  // with selectedPB
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity( intent );
            }
        });
        navTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietPhieuNhapLayout.this, ThongKeLayout.class);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                startActivity( intent );
            }
        });
    }

    public void transferLayout(String maPK) throws JSONException, AuthFailureError {
        if (maPK.trim().equalsIgnoreCase("")) return;
        // 1. maPK l?? all th?? chuy???n sang layout maPK
        switch (maPK) {
            case "All": {
                warningLabel.setText("Khi ch???n ph??ng kho c??? th???, c???u tr??c b???ng s??? kh??c");
                // All : show
                cp_tablesall_container.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                // Index : hide
                cp_tablesindex_container.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0)
                );
                selectedPK = null;
                totalMoney = 0;
            }
            ;
            break;
            default: {
                warningLabel.setText("Khi ch???n t???t c??? ph??ng kho, c???u tr??c b???ng s??? kh??c");
                // All : hide
                cp_tablesall_container.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0)
                );
                // Index : show
                cp_tablesindex_container.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                noteVTLabel = cp_tablesindex_container.findViewById(R.id.CP_noteVTLabel);
                noteVTLabel.setVisibility(View.INVISIBLE);
                createCPLayout_fromPK(maPK);
            }
            ;
            break;
        }
    }


    public void createCPLayout_fromPK(String maPK){
        if (maPK.trim().equalsIgnoreCase("All")) return;
        // Init Variables and Control
        Rows rowGenarator = new Rows(this);
        TableLayout cp_table1 = cp_tablesindex_container.findViewById(R.id.CP_tableVT);
        cp_table1.removeViews(1, cp_table1.getChildCount() - 1);
        int[] sizeOfCell = {85, 180, 50, 80};
        boolean[] isPaddingZero = {false, true, true, true};
        TableLayout cp_table2 = cp_tablesindex_container.findViewById(R.id.CP_tableSP);
        cp_table2.removeViews(1, cp_table2.getChildCount() - 1);
        int[] sizeOfCell2 = {90, 240};
        boolean[] isPaddingZero2 = {false, false};
        cp_totalCount = cp_tablesindex_container.findViewById(R.id.CP_totalCount);
        cp_totalPrice = cp_tablesindex_container.findViewById(R.id.CP_totalPrice);

        noteTotalLabel = cp_tablesindex_container.findViewById(R.id.CP_noteTotalLabel);
        for (PhongKho pk : phongKhoArrayList) {
            if (maPK.equalsIgnoreCase(pk.getMapk().trim())) {
                selectedPK = pk;
//                noteTotalLabel.setText("T???ng Chi ph?? trong " + pk.getTenpk() + " ??ang ch???a :");
                break;
            }
        }
        totalPrice = 0;
        // Create List<TableRow> for TableList
        // TABLE CP INDEX 01 ----------------------------------------------------------------------------------------
        chitietpnDB.GetDataIndex(listIndex, this, new ChiTietPhieuNhapDB.VolleyCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(String error) {
                Log.d("AAA", "L???i! \n" + error.toString());
            }

            @Override
            public void onSuccess() {
                rowGenarator.setData(rowGenarator.enhanceRowData(listIndex, 4));
                rowGenarator.setSizeOfCell(sizeOfCell);
                rowGenarator.setIsCellPaddingZero(isPaddingZero);
                List<TableRow> rows = rowGenarator.generateArrayofRows();
                if (rows == null) {
                    cp_totalCount.setText("0");
                    cp_totalPrice.setText("0");
                    return;
                }
                for (TableRow row : rows) {
                    cp_table1.addView(row);
                    TextView totalpriceofVTView = (TextView) row.getChildAt(row.getChildCount() - 1);
                    int totalpriceofVT = 0;
                    for (int i = 0; i < vatTuArrayList.size(); i++){
                        TextView slVTView = (TextView) row.getChildAt(0);
                        String mavt = slVTView.getText().toString().trim();
                        String mavtl = vatTuArrayList.get(i).getMaVt().trim();
                        if (mavtl.equalsIgnoreCase(mavt)) {
                            int slVT = Integer.parseInt(totalpriceofVTView.getText().toString().trim());
                            totalpriceofVT = slVT * Integer.parseInt(vatTuArrayList.get(i).getGiaNhap());
                        }
                    }
                    totalPrice += totalpriceofVT;
                }
                rowGenarator.setSizeOfCell(sizeOfCell2);
                rowGenarator.setIsCellPaddingZero(isPaddingZero2);

                for (int i = 1; i < cp_table1.getChildCount(); i++) {
                    TableRow row = (TableRow) cp_table1.getChildAt(i);

                    // T??? th???ng VT ???????c b???m gen ra th???ng nh??n vi??n ???? m?????n n??
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int dem = v.getId();
                            // TABLE CP INDEX 02 ----------------------------------------------------------------------------------------
                            // Set text for noteVTLabel -------------------------------------------------------------------------
                            TextView tenVTView = (TextView) row.getChildAt(1);
                            noteVTLabel.setVisibility(View.VISIBLE);
                            noteVTLabel.setText(tenVTView.getText().toString().trim() + " ???????c nh???p kho b???i phi???u nh???p d?????i ????y");
                            // ----------------------------------------------------------------------------------------------------
                            TextView maVTView = (TextView) row.getChildAt(0);
                            for (TableRow row : rows) {
                                row.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            row.setBackgroundColor(getResources().getColor(R.color.selectedColor));
                            chitietpnDB.GetDataPnIndexPK(listPnIndexPK, ChiTietPhieuNhapLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    rowGenarator.setData(rowGenarator.enhanceRowData(listPnIndexPK, 2));
                                    cp_table2.removeViews(1, cp_table2.getChildCount() - 1);
                                    List<TableRow> rows2 = new ArrayList<>();
                                    rows2 = rowGenarator.generateArrayofRows();
                                    for (TableRow row2 : rows2) {
                                        cp_table2.addView(row2);
                                    }
                                }

                                @Override
                                public void onError(String error) {

                                }

                                @Override
                                public void onSuccess(String response) {

                                }
                            }, maPK, maVTView.getText().toString().trim());

                        }
                    });
                }
//        // CP_totalCount : T???ng s??? c??c VT ???????c c???p
                cp_totalCount.setText((cp_table1.getChildCount() - 1) + "");
//        // CP_totalPrice : T???ng s??? ti???n VT  = s??? l?????ng lo???i VT m?? PN nh???p * s??? ti???n c???a m???i lo???i VT
                cp_totalPrice.setText(MoneyFormat(totalPrice));
            }
        }, maPK);

    }

    public String MoneyFormat(int money) {
        if (money == 0) return "0 ??";
        int temp_money = money;
        String moneyFormat = "";
        if (money < 1000) return String.valueOf(money) + " ??";
        else {
            int count = 0;
            while (temp_money != 0) {
                moneyFormat += (temp_money % 10) + "";
                if ((count + 1) % 3 == 0 && temp_money > 10) moneyFormat += ".";
                count++;
                temp_money /= 10;
            }
        }
        return new StringBuilder(moneyFormat).reverse().toString() + " ??";
    }

    public ArrayAdapter<String> loadPBSpinner() {
        // 1. T???o list Phong kho // 2. ????? Phong_kho.getTenPK() ra 1 List // 3. setAdapter cho c??i list getTenPK() ????
        ArrayList<String> phongbanNames_list = new ArrayList<>();
        phongbanNames_list.add("T???t c??? ph??ng kho");
        // Ph???c v??? cho vi???c x??? ra Option cho Spinner
        for (PhongKho pb : phongKhoArrayList) {
            phongbanNames_list.add(pb.getTenpk());
//            Log.d("data", pb.getTenpb());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongbanNames_list);
        return adapter;
    }

    public void setEventPBSpinner() {
        PKSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) dataMaPKSpinner = phongKhoArrayList.get(position - 1).getMapk();
                else {
                    // 1.
                    dataMaPKSpinner = "All";

                }
                try {
                    transferLayout(dataMaPKSpinner);
                } catch (JSONException | AuthFailureError e) {
                    e.printStackTrace();
                }
//                Toast.makeText( CapphatVTLayout.this, dataMaPKSpinner+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dataMaPKSpinner = "All";
            }
        });
    }

    // To set all rows to normal state, set focusRowid = -1
    public void setNormalBGTableRows(TableLayout list) {
        // 0: l?? th???ng example ???? INVISIBLE
        // N??n b???t ?????u t??? 1 -> 9
        for (int i = 1; i < list.getChildCount(); i++) {
            TableRow row = (TableRow) list.getChildAt((int) i);
            int dem = row.getId();
            if (indexofRow != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
//             Toast.makeText( PhongbanLayout.this, indexofRow+"", Toast.LENGTH_LONG).show();
//        Toast.makeText(CapphatVTLayout.this, indexofRow + ":" + (int) list.getChildAt(indexofRow).getId() + "", Toast.LENGTH_LONG).show();
    }

    public int findMaPNinTableCTPN(TableLayout list) {
        TableRow tr = null;
        TextView maPN = null;
        if (focusMaPN == null) return -1;
        Log.d("focus", focusMaPN.getText() + "");
        for (int i = 1; i < list.getChildCount(); i++) {
            tr = (TableRow) list.getChildAt(i);
            maPN = (TextView) tr.getChildAt(0);
            if (maPN.getText().toString().trim().equalsIgnoreCase(focusMaPN.getText().toString().trim() + ""))
                return i;
        }
        return -1;
    }

    public VatTu findVTinListVT(String maVT) {
        for (VatTu vt : vatTuArrayList) {
            if (vt.getMaVt().trim().equalsIgnoreCase(maVT))
                return vt;
        }
        return null;
    }

    public PhieuNhap findMaPKinTablePN(String maPN){
        for (PhieuNhap pn : phieuNhapArrayList){
            if (pn.getSoPhieu().trim().equalsIgnoreCase(maPN)){
                return pn;
            }
        }
        return null;
    }

    // H??m n??y gi??p h??m tr??n b???ng c??ch d???n t???i nh???ng d??? li???u c?? th??? c??? th??? h??a d??? li???u c???a h??m tr??n
    public void setEventTableRowsHelper(TableLayout sublist) {
        // Ki???m tra focus MaNv
        if (focusMaPN == null || focusMaPN.getText().toString().trim().equalsIgnoreCase("")
                || sublist.getChildCount() == 0) {
            Toast.makeText(ChiTietPhieuNhapLayout.this, "Sorry can't help with no input data", Toast.LENGTH_LONG);
            return;
        }

        // Rect l?? 1 rect t??ng h??nh
        int index = findMaPNinTableCTPN(sublist);
        TableRow tr = (TableRow) sublist.getChildAt(index);

//        Log.d("focus", index + "");
        Rect rc = new Rect(0, 0, tr.getWidth(), tr.getHeight());
        // Khi g???i t???i th???ng TableRow s??? v??? 1 Rectangle t??ng h??nh ??? th???ng TableRow ??ang ch??? ?????nh
        tr.getDrawingRect(rc);
        tr.requestRectangleOnScreen(rc);
        tr.setBackgroundColor(getResources().getColor(R.color.selectedColor));
        // Reset background white for others
        for (int i = 1; i < cp_tablepn_list.getChildCount(); i++) {
            TableRow row = (TableRow) cp_tablepn_list.getChildAt((int) i);
            if (index != (int) row.getId())
                row.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    public void setDataImageView(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setEventDisplayVT(String maVT) {
        labelVT.setVisibility(View.VISIBLE);
        VatTu vt = findVTinListVT( maVT );
        if(vt == null) return;
        String label = vt.getMaVt() + ":" + vt.getTenVt();
        labelVT.setText(label);
        // 1. C?? VanPhongPham r???i th?? set on click // 2. G???i Dialog ????? xem
        previewVTBtn.setVisibility(View.VISIBLE);
        previewVTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Image from Database is handled to load here
                createDialog(R.layout.popup_vt_previewimage);
                // Custom set Control
                VT_IP_maVT = dialog.findViewById(R.id.VT_IP_maVT);
                VT_IP_tenVT = dialog.findViewById(R.id.VT_IP_tenVT);
                VT_IP_DVT = dialog.findViewById(R.id.VT_IP_DVT);
                VT_IP_Gia = dialog.findViewById(R.id.VT_IP_Gia);
                VT_IP_Hinh = dialog.findViewById(R.id.VT_IP_Hinh);
                // Load Data
                setDataImageView( VT_IP_Hinh, vt.getHinh() );
                VT_IP_maVT.setText( vt.getMaVt().toString().trim());
                VT_IP_tenVT.setText( vt.getTenVt().toString().trim());
                VT_IP_DVT.setText( vt.getDvt().toString().trim());
                VT_IP_Gia.setText( vt.getGiaNhap().toString().trim());
            }
        });
    }

    // DIALOG HELPER ----------------------------------------------------------------------------
    public void createDialog(int layout) {
        dialog = new Dialog(ChiTietPhieuNhapLayout.this);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setEventSpinnerMini() {
        PN_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PN_spinner_mini_maPN = phieuNhapArrayList.get(position).getSoPhieu().trim();
//                Toast.makeText( NhanvienLayout.this, PB_spinner_mini_maPB+"", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PN_spinner_mini_maPN = phieuNhapArrayList.get(0).getSoPhieu();
            }
        });
        VT_spinner_mini.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VT_spinner_mini_maVT = vatTuArrayList.get(position).getMaVt().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                VT_spinner_mini_maVT = vatTuArrayList.get(0).getMaVt();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setControlDialog() {
        // 1 Form c???a CP
        backBtn = dialog.findViewById(R.id.CP_backBtn);
        yesBtn = dialog.findViewById(R.id.CP_yesInsertBtn);
        noBtn = dialog.findViewById(R.id.CP_noInsertBtn);

        showPNError = dialog.findViewById(R.id.CP_showPNError);
        showVTError = dialog.findViewById(R.id.CP_showVTError);
        showSLVTError = dialog.findViewById(R.id.CP_showSLVTError);

        showResult = dialog.findViewById(R.id.CP_showResult);
        showConfirm = dialog.findViewById(R.id.CP_showConfirm);
        showLabel = dialog.findViewById(R.id.CP_showLabel);


        PN_spinner_mini = dialog.findViewById(R.id.CP_PNSpinner_mini);
        VT_spinner_mini = dialog.findViewById(R.id.CP_VTSpinner_mini);

        inputSLVT = dialog.findViewById(R.id.CP_inputSLCP);

        ArrayList<String> PN_name = new ArrayList<>();
        for (PhieuNhap pn : phieuNhapArrayList) {
            PN_name.add(pn.getSoPhieu());
        }
        PN_spinner_mini.setAdapter(loadSpinnerAdapter(PN_name));

        ArrayList<String> VT_name = new ArrayList<>();
        for (VatTu vt : vatTuArrayList) {
            VT_name.add(vt.getTenVt());
        }
        VT_spinner_mini.setAdapter(loadSpinnerAdapter(VT_name));

        setEventSpinnerMini();


    }

    public void setEventDialog(View view) {
        // Them/Xoa/Sua CTPN
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;
                switch (view.getId()){
                    case R.id.CP_insertBtn:{
                        if(!isSafeDialog(false))break;
                        ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(PN_spinner_mini_maPN.trim(), VT_spinner_mini_maVT.trim()
                                , Long.valueOf(inputSLVT.getText().toString().trim()));

                        chitietpnDB.ThemCTPhieuNhap(ctpn, ChiTietPhieuNhapLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ChiTietPhieuNhapLayout.this, "Loi!", Toast.LENGTH_SHORT).show();
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if(response.trim().equals("Success")){
                                    Toast.makeText(ChiTietPhieuNhapLayout.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    notification.SendNotification(ChiTietPhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + ctpn.getSoPhieu());
                                }else {
                                    Toast.makeText(ChiTietPhieuNhapLayout.this,"That bai!", Toast.LENGTH_SHORT).show();
                                    ErrorDialog();
                                }
                            }
                        });

                        String maPK = findMaPKinTablePN(PN_spinner_mini_maPN.trim()).getMaK().trim();
                        String dataPK = dataMaPKSpinner.trim();
                        if(!maPK.equalsIgnoreCase(dataPK)){
                            if (dataPK.equalsIgnoreCase("All")){
                                loadDatabase();
                            }
                        }else{
                            createCPLayout_fromPK(dataPK);
                        }
                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        previewVTBtn.setVisibility(View.INVISIBLE);
                        labelVT.setVisibility(View.INVISIBLE);
                        PN_spinner_mini_maPN = null;
                        VT_spinner_mini_maVT = null;
                    }
                        break;
                    case R.id.CP_editBtn: {
                        if (!isSafeDialog(true)) break;
                        ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(PN_spinner_mini_maPN.trim(),
                                VT_spinner_mini_maVT.trim(), Long.valueOf(inputSLVT.getText().toString().trim()));
                        chitietpnDB.CapNhatCTPhieuNhap(ctpn, ChiTietPhieuNhapLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
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
                                    notification.SendNotification(ChiTietPhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + ctpn.getSoPhieu());
                                }
                                else{
                                    ErrorDialog();
                                }
                            }
                        });
                        String maPK = findMaPKinTablePN(PN_spinner_mini_maPN.trim()).getMaK().trim();
                        String dataPK = dataMaPKSpinner.trim();
                        if (!maPK.equalsIgnoreCase(dataPK)) {
                            if (dataPK.equalsIgnoreCase("All")) {
                                loadDatabase();
                            }
                        } else {
                            createCPLayout_fromPK(dataPK);
                        }
                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        previewVTBtn.setVisibility(View.INVISIBLE);
                        labelVT.setVisibility(View.INVISIBLE);

                        PN_spinner_mini_maPN = null;
                        VT_spinner_mini_maVT = null;
                    }
                        break;
                    case R.id.CP_delBtn: {

                        ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap(PN_spinner_mini_maPN.trim(),
                                VT_spinner_mini_maVT.trim(), Long.valueOf(inputSLVT.getText().toString().trim()));
                        chitietpnDB.XoaCTPhieuNhap(ctpn, ChiTietPhieuNhapLayout.this, new ChiTietPhieuNhapDB.VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(ChiTietPhieuNhapLayout.this, "X???y ra l???i !", Toast.LENGTH_SHORT).show();
                                Log.d("AAA", "L???i! \n" + error.toString());
                                ErrorDialog();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if (response.trim().equalsIgnoreCase("success")){
                                    Toast.makeText(ChiTietPhieuNhapLayout.this, "Xoa Th??nh C??ng!", Toast.LENGTH_SHORT).show();
                                    SuccesssDialog();
                                    notification.SendNotification(ChiTietPhieuNhapLayout.this, showResult.getText().toString(),
                                            showLabel.getText().toString() + " " + ctpn.getSoPhieu());
                                }
                                else{
                                    Toast.makeText(ChiTietPhieuNhapLayout.this, "L???i Xoa!", Toast.LENGTH_SHORT).show();
                                    Log.d("AAA", "L???i! \n" + response);
                                    ErrorDialog();
                                }
                            }
                        });
                        String maPK = findMaPKinTablePN(PN_spinner_mini_maPN.trim()).getMaK().trim();
                        String dataPK = dataMaPKSpinner.trim();
                        if (!maPK.equalsIgnoreCase(dataPK)) {
                            if (dataPK.equalsIgnoreCase("All")) {
                                loadDatabase();
                            }
                        } else {
                            createCPLayout_fromPK(dataPK);
                        }
                        editBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                        previewVTBtn.setVisibility(View.INVISIBLE);
                        labelVT.setVisibility(View.INVISIBLE);
                    }
                    break;

                    default:
                        break;
                }
            }
        });
    }

    public boolean isSafeDialog(boolean allowSameID) {
        String sophieu, vattu, slVattu;
        // S??? phi???u kh??ng ???????c tr??ng v???i S??? phi???u kh??c v?? ko ????? tr???ng
        sophieu = "";
        boolean noError = true;
        if ( PN_spinner_mini_maPN == null) {
            showPNError.setText("S??? Phi???u kh??ng ???????c tr???ng ");
            showPNError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            sophieu = PN_spinner_mini_maPN.trim();
            showPNError.setVisibility(View.INVISIBLE);
            noError = true;
        }

        // T??n VT kh??ng ???????c ????? tr???ng v?? kh??ng tr??ng
        vattu = "";
        if (VT_spinner_mini_maVT == null) {
            showVTError.setText(" V???t T?? kh??ng ???????c tr???ng ");
            showVTError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            vattu = VT_spinner_mini_maVT.trim();
            showVTError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }
        slVattu = inputSLVT.getText().toString().trim();
        if (slVattu.length() > 1)
            if( slVattu.charAt(0) == '0')
            {if( slVattu.length() > 1)
            {slVattu = slVattu.substring(1,slVattu.length()-1);}}
        if (slVattu.equals("")) {
            showSLVTError.setText(" V???t T?? kh??ng ???????c tr???ng ");
            showSLVTError.setVisibility(View.VISIBLE);
            noError = false;
        } else {
            showSLVTError.setVisibility(View.INVISIBLE);
            if(noError)noError = true;
        }

        if (noError) {
            for (int i = 1; i < cp_tablectpn_list.getChildCount(); i++) {
                TableRow tr = (TableRow) cp_tablectpn_list.getChildAt(i);
                TextView mapn_data = (TextView) tr.getChildAt(0);
                TextView amvt_data = (TextView) tr.getChildAt(1);

                if (!allowSameID && dataMaPKSpinner.trim().equalsIgnoreCase("All"))
                    if (sophieu.equalsIgnoreCase(mapn_data.getText().toString())) {
                        if(vattu.equalsIgnoreCase(amvt_data.getText().toString())) {
                            showVTError.setText("M?? VT kh??ng ???????c tr??ng ");
                            showVTError.setVisibility(View.VISIBLE);
                            return noError = false;
                        }
                    }
            }
            showPNError.setVisibility(View.INVISIBLE);
            showVTError.setVisibility(View.INVISIBLE);
            showSLVTError.setVisibility(View.INVISIBLE);
        }
        return noError;
    }

    // LAYOUT 01 -----------------------------------------------
    // V??n ph??ng ph???m khi init th?? select theo th???ng CP, sau ???? focus v??o th???ng ?????u ti??n c???a VT
    public void setEventTableVT() {

    }

    // khi 1 h??ng v??n ph??ng ph???m ???????c focus th?? m???i c?? nh??n vi??n
    // --------------- CUSTOM HELPER --------------------------------------------------------------------
    public int DPtoPix(int dps) {
        return (int) (dps * scale + 0.5f);
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

    // Table 3
    // <!-- 80 / 150 / 60 / 60 / 60 -->
    public TableRow createRow(Context context, VatTu vattu) {
        TableRow tr = new TableRow(context);

        //  So phieu
        TextView maVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVT ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        maVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maVT.setWidth(DPtoPix(85));
        maVT.setText(vattu.getMaVt());

        //   Ngay cap
        TextView tenVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maVT lu??n
        tenVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenVT.setText(vattu.getTenVt());
        tenVT.setWidth(DPtoPix(150));

        //  VT
        TextView dvtVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVt ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        dvtVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        dvtVT.setWidth(DPtoPix(80));
        dvtVT.setText(vattu.getDvt());


        //   SL
        TextView trigia = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maVt lu??n
        trigia.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        trigia.setText(vattu.getGiaNhap() + "");
        trigia.setWidth(DPtoPix(80));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(maVT);
        tr.addView(tenVT);
        tr.addView(dvtVT);
        tr.addView(trigia);

        return tr;
    }

    public TableRow createRow(Context context, VatTu vattu, ChiTietPhieuNhap ctpn) {
        TableRow tr = new TableRow(context);
        //  So phieu
        TextView maPN = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVT ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        maPN.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maPN.setWidth(DPtoPix(80));
        maPN.setText(ctpn.getSoPhieu().trim());
        //  So phieu
        TextView maVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVT ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        maVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maVT.setWidth(DPtoPix(40));
        maVT.setText(ctpn.getMaVT().trim());
        //   Ngay cap
        TextView tenVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maVT lu??n
        tenVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        tenVT.setText(vattu.getTenVt());
        tenVT.setWidth(DPtoPix(100));

        //  VT
        TextView dvtVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVt ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        dvtVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        dvtVT.setWidth(DPtoPix(100));
        dvtVT.setText(vattu.getDvt());


        //   SL
        TextView soluong = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maVt lu??n
        soluong.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        soluong.setText(ctpn.getSoLuong() + "");
        soluong.setMaxWidth(DPtoPix(100));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(maPN);
        tr.addView(maVT);
        tr.addView(tenVT);
        tr.addView(dvtVT);
        tr.addView(soluong);

        return tr;
    }

    public TableRow createRow(Context context, ChiTietPhieuNhap ctpn) {
        TableRow tr = new TableRow(context);

        TextView soPhieu = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n tenNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        soPhieu.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        soPhieu.setWidth(DPtoPix(80));
        soPhieu.setText(ctpn.getSoPhieu());

        TextView maVT = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maVT ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        maVT.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maVT.setWidth(DPtoPix(85));
        maVT.setText(ctpn.getMaVT());


        //   SL
        TextView soluong = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maVt lu??n
        soluong.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        soluong.setText(ctpn.getSoLuong() + "");
        soluong.setWidth(DPtoPix(80));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(soPhieu);
        tr.addView(maVT);
        tr.addView(soluong);
        return tr;
    }

    // Table 4
    // <!-- 80 / 300 -->
    public TableRow createRow(Context context, PhieuNhap pn) {
        TableRow tr = new TableRow(context);
        TextView soPhieu = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? maNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n tenNV lu??n
        // L??u ??!! : khi ?????t LayoutParams th?? ph???i theo th???ng c??? n???i v?? ph???i c?? weight
        soPhieu.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        soPhieu.setWidth(DPtoPix(80));
        soPhieu.setText(pn.getSoPhieu());

        //   Ten PB
        TextView ngayLapPhieu = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        // C???n c??i n??y ????? khi m?? tenNV ?????t t???i max width th?? n?? s??? t??ng height cho b??n maNV lu??n
        ngayLapPhieu.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        ngayLapPhieu.setText(formatDate(pn.getNgayLap(), false));
        ngayLapPhieu.setWidth(DPtoPix(190));

        TextView maKho = (TextView) getLayoutInflater().inflate(R.layout.tvtemplate, null);
        maKho.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT, 10.0f));
        maKho.setText(pn.getMaK());
        maKho.setWidth(DPtoPix(85));

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        // Add 2 th??? v??o row
        tr.addView(soPhieu);
        tr.addView(ngayLapPhieu);
        tr.addView(maKho);

        return tr;
    }

    // --------------- CUSTOM HELPER -----------------------------------------------------------------
    public ArrayAdapter<String> loadSpinnerAdapter(ArrayList<String> entity) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, entity);
        return adapter;
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
//                            input.setText("");
//                            inputTenVT.setText("");
//                            inputDVT.setText("");
//                            inputGia.setText("");
                showResult.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }, 1000);
    }
    public void ErrorDialog(){
        showResult.setTextColor(getResources().getColor(R.color.thoatbtn_bgcolor));
        showResult.setText(showLabel.getText() + " th???t b???i !");
        showResult.setVisibility(View.VISIBLE);
    }
}
