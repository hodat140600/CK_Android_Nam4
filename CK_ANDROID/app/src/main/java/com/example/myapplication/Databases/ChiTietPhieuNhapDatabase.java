package com.example.myapplication.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.example.myapplication.Entities.ChiTietPhieuNhap;
import com.example.myapplication.Entities.VatTu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDatabase extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "QLPK.db";

    // Database Path
    private static String DATABASE_PATH;
    public Context mContext;
    // Table name: Note.
    private static final String TABLE_NAME = "CHITIETCAPPHAT";

    public static final String COLUMN_SOPHIEU ="SOPHIEU";
    public static final String COLUMN_MAVT = "MAVT";
    public static final String COLUMN_SOLUONG = "SOLUONG";

    public ChiTietPhieuNhapDatabase(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (Build.VERSION.SDK_INT >= 17) {
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }
    private Boolean checkDataBase(){
        File dbFile = new File( DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }
    private void copyDataBase() throws Exception{
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0){
            mOutput.write(mBuffer,0,mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    public void createDataBase(){
        //if Db doesn't exist then copy it from assets.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist){
            this.getReadableDatabase();
            this.close();
            try {
                //copy Db from assets
                copyDataBase();
                Log.e("TAG", "Create DataBase");
            }catch (IOException mIOException){
                throw new Error("ErrorCopyDataBase");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private SQLiteDatabase mDataBase;
    //open DB so can query it
    public boolean openDataBase() throws Exception{
        String mPath = DATABASE_PATH + DATABASE_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        return mDataBase != null;
    }
    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String script = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
//                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_SOPHIEU + " TEXT NOT NULL,"
                + COLUMN_MAVT + " TEXT NOT NULL,"
                + COLUMN_SOLUONG + " INTEGER NOT NULL,"
                +"PRIMARY KEY (" + COLUMN_SOPHIEU +","+ COLUMN_MAVT+"),"
                + "FOREIGN KEY("+ COLUMN_MAVT +") REFERENCES VATTU("+ COLUMN_MAVT +"), "
                + "FOREIGN KEY("+COLUMN_SOPHIEU+") REFERENCES CAPPHAT("+COLUMN_SOPHIEU+") "
                + "ON UPDATE RESTRICT "
                + "ON DELETE RESTRICT );";

        // Execute script.
        db.execSQL(script);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate
        onCreate(db);
    }
    public List<ChiTietPhieuNhap> reset(){
        dropTable();
        insert(new ChiTietPhieuNhap("PHIEU1","VT1",100));
        insert(new ChiTietPhieuNhap("PHIEU2","VT2",100));
        insert(new ChiTietPhieuNhap("PHIEU3","VT1",100));
        insert(new ChiTietPhieuNhap("PHIEU4","VT3",100));
        insert(new ChiTietPhieuNhap("PHIEU5","VT4",100));
        insert(new ChiTietPhieuNhap("PHIEU6","VT2",100));
        insert(new ChiTietPhieuNhap("PHIEU7","VT3",100));
        insert(new ChiTietPhieuNhap("PHIEU8","VT5",100));
        insert(new ChiTietPhieuNhap("PHIEU9","VT1",100));
        return select();
    }

    public List<ChiTietPhieuNhap> select(){
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_SOPHIEU,
                COLUMN_MAVT,
                COLUMN_SOLUONG
        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder = PhongBanDatabase.COLUMN_ID + " DESC";
        String sortOrder = null;

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<ChiTietPhieuNhap> list_chitietcapphat = new ArrayList<>();

        while(cursor.moveToNext()){
            list_chitietcapphat.add(new ChiTietPhieuNhap(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getLong(2)
            ));
        }
        db.close();
        return list_chitietcapphat;
    }

    public long insert(ChiTietPhieuNhap chiTietPhieuNhap){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOPHIEU, chiTietPhieuNhap.getSoPhieu());
        values.put(COLUMN_MAVT, chiTietPhieuNhap.getMaVT());
        values.put(COLUMN_SOLUONG, chiTietPhieuNhap.getSoLuong());

        // Insert the new row, returning the primary key value of the new row
        return db.insert(TABLE_NAME, null, values);
    }

    public long update(ChiTietPhieuNhap chiTietPhieuNhap){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SOPHIEU, chiTietPhieuNhap.getSoPhieu());
        values.put(COLUMN_MAVT, chiTietPhieuNhap.getMaVT());
        values.put(COLUMN_SOLUONG, chiTietPhieuNhap.getSoLuong());

        // db.update ( Tên bảng, tập giá trị mới, điều kiện lọc, tập giá trị cho điều kiện lọc );
        return db.update(
                ChiTietPhieuNhapDatabase.TABLE_NAME
                , values
                , PhieuNhapDatabase.COLUMN_SOPHIEU +"=?"
                ,  new String[] { String.valueOf(chiTietPhieuNhap.getSoPhieu()) }
        );
    }
    public long delete(ChiTietPhieuNhap chiTietPhieuNhap){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete ( Tên bàng, string các điều kiện lọc - dùng ? để xác định, string[] từng phần tử trong string[] sẽ nạp vào ? );
        return db.delete(
                ChiTietPhieuNhapDatabase.TABLE_NAME
                , ChiTietPhieuNhapDatabase.COLUMN_SOPHIEU +"=?" + " AND " +ChiTietPhieuNhapDatabase.COLUMN_MAVT +"=?"
                ,  new String[] { String.valueOf(chiTietPhieuNhap.getSoPhieu()), String.valueOf(chiTietPhieuNhap.getMaVT()) }
        );
    }
    public long deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ChiTietPhieuNhapDatabase.TABLE_NAME,null,null);
    }

    public List<String> getListResult(Cursor cursor){
        List<String> results = new ArrayList<>();
        while(cursor.moveToNext()){
            for(int i = 0; i < cursor.getColumnCount(); i++){
                results.add(cursor.getString(i));
            }
        }
        cursor.close();
        return results;
    }
    public List<String> select_PN_CTPN_VT(String maPN){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT DISTINCT  CHITIETCAPPHAT.SOPHIEU, CHITIETCAPPHAT.MAVT, VATTU.TENVT, VATTU.DVT, CHITIETCAPPHAT.SOLUONG  FROM \n"
                + "CHITIETCAPPHAT, VATTU"
                + " WHERE CHITIETCAPPHAT.SOPHIEU = '" + maPN + "' AND CHITIETCAPPHAT.MAVT = VATTU.MAVT"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }
    public List<String> select_PN_CTPN_VT(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT DISTINCT  CHITIETCAPPHAT.SOPHIEU, CHITIETCAPPHAT.MAVT, VATTU.TENVT, VATTU.DVT, CHITIETCAPPHAT.SOLUONG  FROM \n"
                + "CHITIETCAPPHAT, VATTU"
                + " WHERE CHITIETCAPPHAT.MAVT = VATTU.MAVT"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }
    public List<String> selectVT_IndexPK(String maPK){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT DISTINCT  VATTU.MAVT, VATTU.TENVT, VATTU.DVT, SUM(CHITIETCAPPHAT.SOLUONG) AS TONGSL FROM \n"
                + "CHITIETCAPPHAT, VATTU, CAPPHAT"
                + " WHERE VATTU.MAVT = CHITIETCAPPHAT.MAVT AND \n" +
                "CHITIETCAPPHAT.SOPHIEU = CAPPHAT.SOPHIEU\n" +
                "AND CAPPHAT.MAPK = '" + maPK +"'GROUP BY VATTU.MAVT"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }

    public List<String> selectVT_IndexPKForBC(String maPK, String sophieu){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT DISTINCT  VATTU.MAVT, VATTU.TENVT, VATTU.DVT, SUM(CHITIETCAPPHAT.SOLUONG) AS TONGSL, (VATTU.GIANHAP * SUM(CHITIETCAPPHAT.SOLUONG)) AS TRIGIA FROM \n"
                + "CHITIETCAPPHAT, VATTU, CAPPHAT"
                + " WHERE VATTU.MAVT = CHITIETCAPPHAT.MAVT AND \n" +
                "CHITIETCAPPHAT.SOPHIEU = CAPPHAT.SOPHIEU\n" +
                "AND CAPPHAT.MAPK = '" + maPK
                +"' AND CAPPHAT.SOPHIEU = '"+ sophieu
                +"'GROUP BY VATTU.MAVT"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }

    public List<String> selectSP_IndexPK(String maPK, String maVT){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT CHITIETCAPPHAT.SOPHIEU, CAPPHAT.NGAYLAP\n" +
                "FROM CHITIETCAPPHAT, CAPPHAT\n" +
                "WHERE CAPPHAT.SOPHIEU = CHITIETCAPPHAT.SOPHIEU\n" +
                "AND CHITIETCAPPHAT.MAVT = '" + maVT+"' AND CAPPHAT.MAPK = '" + maPK+"'"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }
    public List<String> selectSumSL_IndexPK(String maPK){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT sum(CHITIETCAPPHAT.SOLUONG) AS TONGSL\n" +
                "FROM CHITIETCAPPHAT, PHONGKHO, CAPPHAT\n" +
                "WHERE CHITIETCAPPHAT.SOPHIEU = CAPPHAT.SOPHIEU\n" +
                "AND CAPPHAT.MAPK = PHONGKHO.MAPK\n" +
                "AND CAPPHAT.MAPK = '"+maPK +"'"
                ;
        Cursor cursor = db.rawQuery(sql, null);
        return getListResult(cursor);
    }
}
