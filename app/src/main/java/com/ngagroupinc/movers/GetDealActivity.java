package com.ngagroupinc.movers;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import projo.ServerUtilites;
import projo.Singleton;

/**
 * Created by Userone on 2/1/2017.
 */

public class GetDealActivity extends AppCompatActivity {
    WebView mWebView;
    ProgressBar progressBar;
    Singleton singleton;
    Dialog dialog;
    Bundle bundle;
    String message;
    ServerUtilites serverUtilities;
    ImageView backArrowImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_deals_layout);
        Log.d("getdeals", "--->");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.webview1);
        singleton = Singleton.getInstance();
        dialog = new Dialog(this);
        bundle = getIntent().getExtras();
        backArrowImageView=(ImageView)findViewById(R.id.backarrow);
        serverUtilities = new ServerUtilites(this);


        if (singleton.isOnline())
        {
            Map<String, String> categoryHeader = new HashMap<String, String>();
            categoryHeader.put("ApiKey ", serverUtilities.apiHeader);
             progressBar.setVisibility(View.VISIBLE);
             String categoryapi = serverUtilities.serverUrl + "" + serverUtilities.getThisDeal + bundle.get("DealId") + "/" + bundle.get("DealType");
             getDealsResponse(categoryHeader, categoryapi);

        }
        else
        {
            serverUtilities.ShowAlert("No Internet Connection");
        }

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });

    }





    public void getDealsResponse(final Map<String, String> mHeaders,
                                 String url) {
        Log.d("url", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String BuyUrl = null, sectionName = null,ImageUrl=null;


                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);



                                if (jArray.getJSONObject(i).has("BuyUrl")) {

                                    BuyUrl = jsonobject.getString("BuyUrl");
                                }

                            }

                            mWebView.getSettings().setLoadWithOverviewMode(true);
                            mWebView.getSettings().setUseWideViewPort(true);
                            mWebView.getSettings().setBuiltInZoomControls(true);
                            mWebView.getSettings().setJavaScriptEnabled(true);
                            mWebView.setWebViewClient(new WebViewClient() {
                                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    Toast.makeText(GetDealActivity.this, description, Toast.LENGTH_SHORT).show();

                                }
                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon)
                                {
                                    Log.d("onPageStarted","--->");



                                }


                                @Override
                                public void onPageFinished(WebView view, String url)
                                {

                                    progressBar.setVisibility(View.GONE);

                                }

                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    Log.d("shouldOverrideUrlLoading","--->"+url);
                                    view.loadUrl(url);
                                    return true;
                                }
                            });


                            mWebView.loadUrl(BuyUrl);
                        } catch (Exception e) {

                            Log.d("get this deals screen catch", "--->" + e);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message =serverUtilities.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                Log.d("get this deals error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(GetDealActivity.this).add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
