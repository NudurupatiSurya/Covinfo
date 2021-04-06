package com.cov.covinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

public class List_Countries extends AppCompatActivity {
    MyAdapter adapter;
    ProgressBar progressBarlc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__countries);
        progressBarlc = (ProgressBar) findViewById(R.id.progressrv);
        getData();
    }
    private void getData() {
        String url = "https://disease.sh/v2/countries";
        RequestQueue queue = Volley.newRequestQueue(this);

        final ArrayList<String> countries = new ArrayList<String>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    int index=0;
                    JSONArray jsonArray = new JSONArray(response.toString());
                    //Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                    ArrayList<String> conti = new ArrayList<String>();
                    Toast.makeText(getApplicationContext(),""+jsonArray.length(),Toast.LENGTH_SHORT);
                    int count=0;
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        conti.add(data.getString("country"));
                        index = i;
                        count++;
                    }
                    Toast.makeText(getApplicationContext()," "+conti.size(),Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    countries.addAll(conti);
                    callrecycle(countries);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Inside Exception ra",Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"outside Exception ra",Toast.LENGTH_SHORT);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
    int count = 0;
    public void callrecycle(ArrayList<String> countri) {

        ArrayList<String> countri2 = new ArrayList<String>();
        countri2.addAll(countri);

       // Toast.makeText(getApplicationContext(),""+countri.size(),Toast.LENGTH_SHORT);
        adapter = new MyAdapter(getApplicationContext(),countri);
        RecyclerView listofcountries = findViewById(R.id.listofcountries);
        listofcountries.setAdapter(adapter);
        progressBarlc.setVisibility(View.INVISIBLE);
        listofcountries.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_,menu);
        MenuItem searchitem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchitem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}