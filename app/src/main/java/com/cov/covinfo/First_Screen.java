package com.cov.covinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class First_Screen extends AppCompatActivity implements View.OnClickListener {
Button centersbt;
    TextView totalcases,totaldeaths,totalrecovered,yourcountry,world;
    ProgressBar progressBar;
    String country;
    GridLayout detailsgl;
    Button trackothers,dataviz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__screen);
        totalcases = (TextView) findViewById(R.id.totalcases);
        totaldeaths = (TextView) findViewById(R.id.totaldeaths);
        totalrecovered = (TextView) findViewById(R.id.totalrecovered);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        detailsgl = (GridLayout) findViewById(R.id.detailsgl);
        yourcountry = (TextView) findViewById(R.id.Your_Country);
        world = (TextView) findViewById(R.id.World);
        trackothers = (Button) findViewById(R.id.trackothers);
        dataviz = (Button) findViewById(R.id.dataviz);
        TelephonyManager telephoneManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = telephoneManager.getNetworkCountryIso();
        Locale locale = new Locale("", countryCode);
        country = locale.getDisplayCountry();
        yourcountry.setText(country);
        yourcountry.setOnClickListener(this);
        world.setOnClickListener(this);
        trackothers.setOnClickListener(this);
        dataviz.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            yourcountry.setBackground(getDrawable(R.drawable.oval_shape_oc));
        }
        yourcountry();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Your_Country:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    yourcountry.setBackground(getDrawable(R.drawable.oval_shape_oc));
                    yourcountry.setTextColor(WHITE);
                    world.setBackground(getDrawable(R.drawable.oval_shape));
                    world.setTextColor(BLACK);
                }
                progressBar.setVisibility(View.VISIBLE);
                yourcountry();
                break;
            case R.id.World:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    yourcountry.setBackground(getDrawable(R.drawable.oval_shape));
                    yourcountry.setTextColor(BLACK);
                    world.setBackground(getDrawable(R.drawable.oval_shape_oc));
                    world.setTextColor(WHITE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                world();
                break;
            case R.id.trackothers:
                Intent listofc = new Intent(this,List_Countries.class);
                startActivity(listofc);
                break;
            case R.id.dataviz:
                Intent datavizu = new Intent(this,datavizu.class);
                startActivity(datavizu);
        }
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
                   // Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    ArrayList<String> countries = new ArrayList<String>();
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        countries.add(data.getString("country"));
                    }
                    index = countries.indexOf(country);
                    // Toast.makeText(getContext(),""+index,Toast.LENGTH_SHORT);
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    totalcases.setText(jsonObject.getString("cases"));
                    totaldeaths.setText(jsonObject.getString("deaths"));
                    totalrecovered.setText(jsonObject.getString("recovered"));
                    detailsgl.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
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
    private void world() {
        String url = "https://disease.sh/v2/all";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    totalcases.setText(jsonObject.getString("cases"));
                    totaldeaths.setText(jsonObject.getString("deaths"));
                    totalrecovered.setText(jsonObject.getString("recovered"));
                    detailsgl.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
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
}