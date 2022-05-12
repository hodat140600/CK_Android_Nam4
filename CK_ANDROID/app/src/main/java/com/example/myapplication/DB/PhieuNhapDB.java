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
import com.example.myapplication.Entities.PhieuNhap;
import com.example.myapplication.Main.PhieuNhapLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class PhieuNhapDB {
    public String urlGetData = "http://192.168.1.7:8080/androidwebservice/PhieunhapDB/getdata.php";
    public String urlGetDataPK = "http://192.168.1.7:8080/androidwebservice/PhieunhapDB/getdataPK.php";
    public String urlInsert = "http://192.168.1.7:8080/androidwebservice/PhieunhapDB/insert.php";
    public String urlCapNhat = "http://192.168.1.7:8080/androidwebservice/PhieunhapDB/update.php";
    public String urlDelete = "http://192.168.1.7:8080/androidwebservice/PhieunhapDB/delete.php";

    public PhieuNhapDB(){

    }
    public void GetDataPK(ArrayList<PhieuNhap> phieuNhapArrayList, Context context, final VolleyCallBack callBack, String mapk){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetDataPK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PN", "response! \n" + response.toString());
                //Here is main response object
                try {
                    JSONArray arr = new JSONArray(response);
                    phieuNhapArrayList.clear();
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject jsonObject = arr.getJSONObject(i);
                            phieuNhapArrayList.add(new PhieuNhap(jsonObject.getString("MaPN"),jsonObject.getString("NgayLap"),
                                    jsonObject.getString("MaPK")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(context, phieuNhapArrayList.toString(), Toast.LENGTH_LONG).show();
                    Log.d("PN", "array! \n" + phieuNhapArrayList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mapk",mapk);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void GetData( ArrayList<PhieuNhap> phieuNhapArrayList, Context context, final VolleyCallBack callBack){

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                phieuNhapArrayList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        phieuNhapArrayList.add(new PhieuNhap(jsonObject.getString("MaPN").trim(),
                                jsonObject.getString("NgayLap").trim(), jsonObject.getString("MaPK").trim()));
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
    public void ThemPhieuNhap( PhieuNhap phieuNhap, Context context, final VolleyCallBack callBack){
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
                params.put("mapn",phieuNhap.getSoPhieu());
                params.put("mapk",phieuNhap.getMaK());
                params.put("ngaylap",phieuNhap.getNgayLap());

                return params;
            }
        };
        mRequestQueue.add(stringRequest);

    }
    public void CapNhatPhieuNhap(PhieuNhap phieuNhap, Context context, final VolleyCallBack callBack){
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
                params.put("mapn",phieuNhap.getSoPhieu());
                params.put("mapk",phieuNhap.getMaK());
                params.put("ngaylap",phieuNhap.getNgayLap());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void XoaPhieuNhap(PhieuNhap phieuNhap, Context context, final VolleyCallBack callBack){
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
                params.put("mapn",phieuNhap.getSoPhieu().trim());
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
