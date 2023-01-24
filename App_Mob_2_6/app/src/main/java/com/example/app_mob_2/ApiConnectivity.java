package com.example.app_mob_2;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiConnectivity {

    // This application operates under the following decisions:
    // When the application is opened, if there's a mismatch
    // between the Remote and Local databases, the Remote database
    // will take precedence.

    String ip = "10.69.10.15";
    String rootAdress = "http://"+ip+":8000/";
    String getAdress = "api/get/alarms/";
    String postAdress = "api/post/alarms/";
    String editAdress = "api/edit/alarms/";

    private DbHelper dbLocal;
    private Context context;
    private RequestQueue queue;


    public ApiConnectivity(DbHelper dbLocal, Context context) {
        this.context = context;
        this.dbLocal = dbLocal;
        this.queue = Volley.newRequestQueue(context);
    }

    public void deleteToRemote(String id){
        String url = rootAdress + editAdress + id + "/";
        Log.d("API DEBUG", "deleteToRemote: " + url);
        Response.Listener<String> responseListener = new APIResponseListener();
        Response.ErrorListener responseErrorListener = new APIResponseErrorListener();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, responseListener,
                responseErrorListener);
        queue.add(stringRequest);

    }

    public void putToRemote(String id, String name, String mode, String cr_t, String time, String m_med, String quant){
        String url = rootAdress + editAdress + id + "/";
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams()
            {


                Map<String, String>  params = new HashMap<String, String>();
                DateHandler dh = new DateHandler(time, cr_t);
                Log.d("API DEBUG", "getParams: " + name + " | "
                        + mode + " | " + dh.getDate() + " | " + dh.getTime() + " | " + m_med + " | " + quant);

                params.put("name", name);
                params.put("mode", mode);
                params.put("cr_t", "");
                params.put("time", dh.getDateTime());
                params.put("m_med", m_med);
                params.put("quant", quant);

                return params;

            }
        };
        queue.add(postRequest);
    }

    public void postToRemote(String name, String mode, String cr_t, String time, String m_med, String quant){
        String url = rootAdress + postAdress;
        Log.d("API DEBUGGER", "postToRemote: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getParams()
            {


                Map<String, String>  params = new HashMap<String, String>();
                DateHandler dh = new DateHandler(time, cr_t);
                Log.d("API DEBUG", "getParams: " + name + " | "
                        + mode + " | " + dh.getDate() + " | " + dh.getTime() + " | " + m_med + " | " + quant);

                params.put("name", name);
                params.put("mode", "0");
                params.put("cr_t", "");
                params.put("time", dh.getDateTime());
                params.put("m_med", m_med);
                params.put("quant", quant);

                return params;

            }
        };
        queue.add(postRequest);
    }

    public void copyFromRemote(){
        // Copy data from the remote database into the local one
        fetch();
    }

    private void copyFromJsonArray(JSONArray data){
        // Copy data from the json response into the local database
        try {
            this.dbLocal.RepopulateDatabase(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void copyFromJsonObject(JSONObject data){
        // Copy data from the json response into the local database

        try {
            this.dbLocal.RepopulateDatabaseWithOneItem(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fetch(){
        String url = this.rootAdress + this.getAdress;
        Response.Listener<String> responseListener = new APIResponseListener();
        Response.ErrorListener responseErrorListener = new APIResponseErrorListener();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                responseErrorListener);
        queue.add(stringRequest);
    }

    private class APIResponseListener implements Response.Listener<String> {


        @Override
        public void onResponse(String response) {

            try {

                if(response.startsWith("[")){
                    JSONArray jsonArray = new JSONArray(response);
                    copyFromJsonArray(jsonArray);
                } else {
                    JSONObject jsonObject = new JSONObject(response);
                    copyFromJsonObject(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class APIResponseErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }

}

