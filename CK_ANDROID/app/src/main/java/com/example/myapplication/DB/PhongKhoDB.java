package com.example.myapplication.DB;



import android.content.Context;
import android.util.Log;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Entities.PhongKho;
import com.example.myapplication.Main.PhongkhoLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhongKhoDB extends Connect{
    public String urlGetData = connect + "PhongKhoDB/getdata.php";
    public String urlInsert = connect + "PhongKhoDB/insert.php";
    public String urlCapNhat = connect + "PhongKhoDB/update.php";
    public String urlDelete = connect + "PhongKhoDB/delete.php";
    public PhongKhoDB(){

    }
    public void GetData(ArrayList<PhongKho> phongKhoArrayList, Context context,final VolleyCallBack callBack){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                phongKhoArrayList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        phongKhoArrayList.add(new PhongKho(jsonObject.getString("MaPK"),
                                jsonObject.getString("TenPK"), jsonObject.getString("DiaChi"),jsonObject.getString("SDT")));
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
    public void ThemPhongKho(PhongKho phongKho, Context context,final VolleyCallBack callBack){
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
                params.put("mapk",phongKho.getMapk());
                params.put("tenpk",phongKho.getTenpk());
                params.put("diachi",phongKho.getDiachi());
                params.put("sdt",phongKho.getSdt());

                return params;
            }
        };
        mRequestQueue.add(stringRequest);

    }
    public void CapNhatPhongKho(PhongKho phongKho, Context context,final VolleyCallBack callBack){
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
                params.put("mapk",phongKho.getMapk());
                params.put("tenpk",phongKho.getTenpk());
                params.put("diachi",phongKho.getDiachi());
                params.put("sdt",phongKho.getSdt());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void XoaPhongKho(PhongKho phongKho, Context context,final VolleyCallBack callBack){
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
                params.put("mapk",phongKho.getMapk().trim());
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
