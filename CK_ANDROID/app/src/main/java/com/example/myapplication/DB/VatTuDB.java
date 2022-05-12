package com.example.myapplication.DB;

import android.content.Context;
import android.util.Base64;
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
import com.example.myapplication.Entities.VatTu;
import com.example.myapplication.Main.VattuLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class VatTuDB extends Connect{
    public String urlGetData = connect + "VattuDB/getdata.php";
    public String urlInsert = connect + "VattuDB/insert.php";
    public String urlCapNhat = connect + "VattuDB/update.php";
    public String urlDelete = connect + "VattuDB/delete.php";


    public VatTuDB(){}
    //API
    public void ThemVatTu(VatTu vatTu, Context context,final VolleyCallBack callBack){
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
                String mvt = vatTu.getMaVt();
                String tvt = vatTu.getTenVt();
                String dvt = vatTu.getDvt();
                String gia = new String(vatTu.getHinh());
                String hinh = vatTu.getGiaNhap();
                final String imageString = Base64.encodeToString(vatTu.getHinh(), Base64.DEFAULT);
                params.put("mavt",vatTu.getMaVt());
                params.put("tenvt",vatTu.getTenVt());
                params.put("dvt",vatTu.getDvt());
                params.put("hinh",imageString);
                params.put("gianhap",vatTu.getGiaNhap());

                return params;
            }
        };
        mRequestQueue.add(stringRequest);

    }
    public void CapNhatVatTu(VatTu vatTu, Context context,final VolleyCallBack callBack){
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
                final String imageString = Base64.encodeToString(vatTu.getHinh(), Base64.DEFAULT);
                params.put("mavt",vatTu.getMaVt());
                params.put("tenvt",vatTu.getTenVt());
                params.put("dvt",vatTu.getDvt());
                params.put("hinh",imageString);
                params.put("gianhap",vatTu.getGiaNhap());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void XoaVatTu(VatTu vatTu, Context context,final VolleyCallBack callBack){
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
                params.put("mavt",vatTu.getMaVt().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void GetData(ArrayList<VatTu> vatTuArrayList, Context context,final VolleyCallBack callBack){

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                vatTuArrayList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        vatTuArrayList.add(new VatTu(jsonObject.getString("MaVT"),
                                jsonObject.getString("TenVT"), jsonObject.getString("Dvt"),
                                jsonObject.getString("GiaNhap"),Base64.decode(jsonObject.getString("Hinh"), Base64.DEFAULT)));
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
                        Toast.makeText(context, "Loi!", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        mRequestQueue.add(jsonArrayRequest);
    }
    public interface VolleyCallBack {
        void onSuccess();
        void onError(String error);
        void onSuccess(String response);
    }
}
