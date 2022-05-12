package com.example.myapplication.DB;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Entities.NhanVien;
import com.example.myapplication.Entities.PhongKho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NhanVienDB extends AppCompatActivity {
    public String urlGetData = "http://192.168.1.7:8080/androidwebservice/NhanvienDB/getdata.php";
    public String urlInsert = "http://192.168.1.7:8080/androidwebservice/NhanvienDB/insert.php";
    public String urlCapNhat = "http://192.168.1.7:8080/androidwebservice/NhanvienDB/update.php";
    public String urlDelete = "http://192.168.1.7:8080/androidwebservice/NhanvienDBdelete.php";
    public NhanVienDB(){

    }
    public void GetData(ArrayList<NhanVien> nhanVienArrayList, Context context, final VolleyCallBack callBack){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                nhanVienArrayList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        nhanVienArrayList.add(new NhanVien(jsonObject.getString("MaNV"),
                                jsonObject.getString("TenNV"), jsonObject.getString("NgaySinh"),jsonObject.getString("MaPK"),
                                jsonObject.getString("Email")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callBack.onError(error.toString());
                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);
    }
    public void ThemNhanVien(NhanVien nhanVien, Context context,final VolleyCallBack callBack){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("manv",nhanVien.getMaNv());
                params.put("mapk",nhanVien.getMaPk());
                params.put("tennv",nhanVien.getHoTen());
                params.put("ngaysinh",nhanVien.getNgaySinh());
                params.put("email",nhanVien.getEmail());

                return params;
            }
        };
        mRequestQueue.add(stringRequest);

    }
    public void CapNhatNhanVien(NhanVien nhanVien, Context context,final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCapNhat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("manv",nhanVien.getMaNv());
                params.put("mapk",nhanVien.getMaPk());
                params.put("tennv",nhanVien.getHoTen());
                params.put("ngaysinh",nhanVien.getNgaySinh());
                params.put("email",nhanVien.getEmail());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void XoaNhanVien(NhanVien nhanVien, Context context,final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("manv",nhanVien.getMaNv());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public interface VolleyCallBack {
        void onSuccess();
        void onError(String error);
        void onSuccess(String response);
    }
}