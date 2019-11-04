package com.example.systemqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    IntentResult result;
    String ram,hd,processor,monitor,keyboard,mouse,img,os;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,tram,thd,tprocessor,tmonitor,tkeyboard,tmouse,tos;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.ram);
        t2=findViewById(R.id.monitor);
        t3=findViewById(R.id.keyboard);
        t4=findViewById(R.id.processor);
        t5=findViewById(R.id.os);
        t6=findViewById(R.id.HD);
        t7=findViewById(R.id.mouse);
        tram=findViewById(R.id.txt_ram);
        thd=findViewById(R.id.txt_processor);
        tmonitor=findViewById(R.id.txt_monitor);
        tprocessor=findViewById(R.id.txt_processor);
        tkeyboard=findViewById(R.id.txt_key);
        tmouse=findViewById(R.id.txt_mouse);
        tos=findViewById(R.id.txt_os);
       imageView = findViewById(R.id.imageView);

        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data

                //to a toast
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
              getSystemInfo();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    public  void getSystemInfo()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://full-bottomed-cushi.000webhostapp.com/SystemINFO.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//If we are getting success from server


                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_obj = jsonArray.getJSONObject(i);
                                ram=json_obj.getString("ram");
                                hd=json_obj.getString("harddisk");
                                processor=json_obj.getString("processor");
                                monitor=json_obj.getString("monitor");
                                keyboard=json_obj.getString("keyboard");
                                mouse=json_obj.getString("ram");
                                os=json_obj.getString("os");
                                img=json_obj.getString("image");
                                tram.setText(ram);
                                tmonitor.setText(monitor);
                                tkeyboard.setText(keyboard);
                                tprocessor.setText(processor);
                                tos.setText(os);
                                thd.setText(hd);
                                tmouse.setText(mouse);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//You can handle error here if you want
                    }

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//Adding parameters t o request

                params.put("systemid",result.getContents());

//returning parameter
                return params;
            }
        };

//Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

