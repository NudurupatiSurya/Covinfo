package com.cov.covinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Other_Countries extends AppCompatActivity implements View.OnClickListener {
    TextView totaldeaths_sc,totalcases_sc,totalrecovered_sc,country_det;
    ProgressBar progressBar_sc;
    GridLayout details_sc;
    String country;
    Button dataviz2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other__countries);
        country_det = (TextView) findViewById(R.id.country_name);
        totalcases_sc = (TextView) findViewById(R.id.totalcases_sc);
        totaldeaths_sc = (TextView) findViewById(R.id.totaldeaths_sc);
        totalrecovered_sc = (TextView) findViewById(R.id.totalrecovered_sc);
        progressBar_sc = (ProgressBar) findViewById(R.id.progressBar_sc);
        details_sc = (GridLayout) findViewById(R.id.details_sc);
        dataviz2 = (Button) findViewById(R.id.dataviz2);
        dataviz2.setOnClickListener(this);
        Intent intent = getIntent();
        country = intent.getStringExtra("country");
        country_det.setText("Statistics of "+country);
        yourcountry();
    }
    private void yourcountry() {
        String url = "https://disease.sh/v2/countries";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int index=0;
                    JSONArray jsonArray = new JSONArray(response.toString());
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    ArrayList<String> countries = new ArrayList<String>();
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        countries.add(data.getString("country"));
                    }
                    index = countries.indexOf(country);
                    // Toast.makeText(getContext(),""+index,Toast.LENGTH_SHORT);
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    totalcases_sc.setText(jsonObject.getString("cases"));
                    totaldeaths_sc.setText(jsonObject.getString("deaths"));
                    totalrecovered_sc.setText(jsonObject.getString("recovered"));
                    details_sc.setVisibility(View.VISIBLE);
                    progressBar_sc.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.dataviz2) {
            Intent datavizua2 = new Intent(this,datavizu.class);
            startActivity(datavizua2);
        }
    }
}