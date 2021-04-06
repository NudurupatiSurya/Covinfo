package com.cov.covinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.webviewsuite.WebViewSuite;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity{
private final int REQUEST_LOCATION_PERMISSION = 1;
private final int STORAGE_PERMISSION = 1;
WebView webView;
ProgressBar pb2;
TextView imtsm2;
GridLayout webviewgl;
ProgressBar progressbarma;
TextView momenttv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
        }
        setContentView(R.layout.activity_main);
        locPermissioncheck();
        webView = (WebView) findViewById(R.id.webview);
        progressbarma = findViewById(R.id.progressbarma);
        momenttv = findViewById(R.id.momenttv);
        webView.setVisibility(View.GONE);
        webView.setWebChromeClient(new WebChromeClient());
       webView.getSettings().setGeolocationEnabled(true);
       webView.getSettings().setTextZoom(80);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('whatcovinsection')[0].style.display='none'; " +
                        "var head2 = document.getElementsByClassName('page-banner')[0].style.display='none'; " +
                        "var head3 = document.getElementsByClassName('container')[0].style.display='none'; " +
                        "var head4 = document.getElementsByClassName('midCenter')[0].style.display='none'; " +
                        "var head5 = document.getElementsByClassName('howitworkblock bs-section')[0].style.display='none'; " +
                        "var head6 = document.getElementsByClassName('covidcertificat')[0].style.display='none'; " +
                        "var head7 = document.getElementsByClassName('footer-social')[0].style.display='none'; " +
                        "})()");
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.cowin.gov.in/home");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
            public void onProgressChanged(WebView view, int progress) {
                if(progress==100){
                    momenttv.setText("Updating Database");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.setVisibility(View.VISIBLE);
                            progressbarma.setVisibility(View.GONE);
                            momenttv.setVisibility(View.GONE);
                        }
                    },3000);
                }
                else{
                        webView.setVisibility(View.GONE);
                        progressbarma.setVisibility(View.VISIBLE);
                        momenttv.setText("Loading..");
                        momenttv.setVisibility(View.VISIBLE);
                }

            }
        });
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    private void internetconncheck() {

    }


    private void Permissioncheck() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"You have already granted this permission!",Toast.LENGTH_SHORT).show();
        }
        else{
            requeststoragepermission();
        }

    }
    private void locPermissioncheck(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"You have already location granted this permission!",Toast.LENGTH_SHORT).show();
        }
        else{
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to find vaccination centers near you")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void requeststoragepermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to find vaccination centers near you")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == STORAGE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this,"storage Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show();
            }
        }
    }


        /*
        otpveritv = findViewById(R.id.otpveritv);
        mobiet = findViewById(R.id.mobiet);
        otpet = findViewById(R.id.otpet);
        otpb = findViewById(R.id.otpb);
        verifyb = findViewById(R.id.verifyb);
        otpb.setOnClickListener(this);
        verifyb.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.otpb:
                mobileno = String.valueOf(mobiet.getText());
                Toast.makeText(this," "+mobileno,Toast.LENGTH_LONG).show();
                String url= "https://api.sit.co-vin.in/api/v2/auth/generateOTP";
                otpveritv.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                otpveritv.setText("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        otpveritv.setText("That didn't work!");
                    }}){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("message",mobileno);
                        return params;
                    }};
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);
                break;
            case R.id.verifyb:
                break;
        }
    }*/

    @Override
    public void onBackPressed() {
       if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();


        if(netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;

        }
        else return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stats,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.stats || item.getItemId() == R.id.textstats){
            startActivity(new Intent(this,First_Screen.class));
        }
        return true;
    }
}