package com.example.myapplication.DB;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Entities.ChiTietPhieuNhap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class ChiTietPhieuNhapDB {
    public String urlGetData = "http://192.168.1.7:8080/androidwebservice/CTPhieuNhapDB/getdata.php";
    public String urlGetDataAll = "http://192.168.1.7:8080/androidwebservice/CTPhieuNhapDB/getdataALL.php";
    public String urlGetDataIndex = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/getdataIndex1.php";
    public String urlGetDataIndexBC = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/getdataIndex1BC.php";
    public String urlGetDataPnIndexPk = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/getdataPnIndexPK.php";
    public String urlInsert = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/insert.php";
    public String urlCapNhat = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/update.php";
    public String urlDelete = "http://192.168.1.7:8080/androidwebservice/CTPhieunhapDB/delete.php";
    public ChiTietPhieuNhapDB(){

    }
    public void GetData(ArrayList<ChiTietPhieuNhap> chiTietPhieuNhapArrayList, Context context, final VolleyCallBack callBack){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                chiTietPhieuNhapArrayList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        chiTietPhieuNhapArrayList.add(new ChiTietPhieuNhap(jsonObject.getString("MaPN"),
                                jsonObject.getString("MaVT"), Long.parseLong(jsonObject.getString("SL"))));
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
    public void GetDataALL(List<String> list, Context context, final VolleyCallBack callBack){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetDataAll, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                list.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        list.add(jsonObject.getString("MaPN"));
                        list.add(jsonObject.getString("MaVT"));
                        list.add(jsonObject.getString("TenVT"));
                        list.add(jsonObject.getString("DVT"));
                        list.add(jsonObject.getString("SL"));
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
    public void GetDataIndex(List<String> list, Context context, final VolleyCallBack callBack, String mapk)  {

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetDataIndex, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AAA", "response! \n" + response.toString());
                //Here is main response object
                try {
                    JSONArray arr = new JSONArray(response);
                    list.clear();
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject jsonObject = arr.getJSONObject(i);
                            list.add(jsonObject.getString("MaVT"));
                            list.add(jsonObject.getString("TenVT"));
                            list.add(jsonObject.getString("DVT"));
                            list.add(jsonObject.getString("TongSL"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
                params.put("MaPK", mapk);
                return params;
            }
        };


        mRequestQueue.add(stringRequest);
    }
    public void GetDataPnIndexPK(List<String> list, Context context, final VolleyCallBack callBack, String mapk, String mavt){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetDataPnIndexPk, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AAA", "response! \n" + response.toString());
                Log.d("AAA", "Mapk! \n" + mapk.toString());
                Log.d("AAA", "Mavt! \n" + mavt.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    list.clear();
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            list.add(jsonObject.getString("MaPN"));
                            list.add(jsonObject.getString("NgayLap"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

                params.put("MaVT", mavt);
                params.put("MaPK", mapk);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public void ThemCTPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap, Context context,final VolleyCallBack callBack){
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
                params.put("mapn",chiTietPhieuNhap.getSoPhieu());
                params.put("mavt",chiTietPhieuNhap.getMaVT());
                params.put("soluong",chiTietPhieuNhap.getSoLuong() + "");
                return params;
            }
        };
        mRequestQueue.add(stringRequest);

    }
    public void CapNhatCTPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap, Context context,final VolleyCallBack callBack){
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
                params.put("mapn",chiTietPhieuNhap.getSoPhieu());
                params.put("mavt",chiTietPhieuNhap.getMaVT());
                params.put("soluong",chiTietPhieuNhap.getSoLuong() + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void XoaCTPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap, Context context,final VolleyCallBack callBack){
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
                params.put("mapn",chiTietPhieuNhap.getSoPhieu());
                params.put("mavt",chiTietPhieuNhap.getMaVT());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void GetDataIndexBC(List<String> list, Context context, final VolleyCallBack callBack, String mapk, String pn)  {

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetDataIndexBC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AAA", "response! \n" + response.toString());
                //Here is main response object
                try {
                    JSONArray arr = new JSONArray(response);
                    list.clear();
                    for (int i = 0; i < response.length(); i++){
                        try {
                            JSONObject jsonObject = arr.getJSONObject(i);
                            list.add(jsonObject.getString("MaVT"));
                            list.add(jsonObject.getString("TenVT"));
                            list.add(jsonObject.getString("DVT"));
                            list.add(jsonObject.getString("TongSL"));
                            list.add(jsonObject.getString("TriGia"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
                params.put("MaPK", mapk);
                params.put("MaPN", pn);
                return params;
            }
        };


        mRequestQueue.add(stringRequest);
    }
    public interface VolleyCallBack {
        void onSuccess();
        void onError(String error);
        void onSuccess(String response);
    }
}
