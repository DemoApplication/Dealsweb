package com.ngagroupinc.movers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import adapters.DataAdapter;
import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;


/**
 * Created by user1 on 12-Jan-17.
 */

public class UserLikesSectionActivity extends AppCompatActivity {
    RecyclerView rv_user_likes_section;
    GridLayoutManager gridLayoutManager;
    DataAdapter mAdapter;
    Singleton singleton;
    public static ProgressBar progressBar_UserLikesSection;
    ServerUtilites serverUtilites;
    String loginUserId;
    ImageView backarrow3;
    TextView toobarTitle;
    boolean stop = true;
    Utilities utilities;
    TextView emptyLikesTextView;
    int count = 1, responseDataSize;
    public static String liked_products = "Liked Products";

    private static String LikDislkType = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_likes_section_layout);
        singleton = Singleton.getInstance();
        rv_user_likes_section = (RecyclerView) findViewById(R.id.rv_user_liked_deals);
        if(singleton.getScreenSize()>2)
        {
            gridLayoutManager = new GridLayoutManager(UserLikesSectionActivity.this,3);
        }
        else{
            gridLayoutManager = new GridLayoutManager(UserLikesSectionActivity.this, 2);
        }

        progressBar_UserLikesSection = (ProgressBar) findViewById(R.id.progressbar_user_likes);
        rv_user_likes_section.setLayoutManager(gridLayoutManager);
        utilities = new Utilities(this);
        backarrow3 = (ImageView) findViewById(R.id.backarrow);
        emptyLikesTextView = (TextView) findViewById(R.id.tv_likes);

        serverUtilites = new ServerUtilites(this);
        toobarTitle = (TextView) findViewById(R.id.tab_text);
        toobarTitle.setText("Likes");
        getLikesData(LikDislkType);
        Log.d("likes_data_size", " " + singleton.user_likes_data.size());
        backarrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void getLikesData(String LikDislkType) {
        progressBar_UserLikesSection.setVisibility(View.VISIBLE);
        Log.d("calling likespage", "gettings likes_data ");

        //  if (singleton.isOnline()) {
        Singleton singleton = Singleton.getInstance();
        Log.d("calling likespage", "gettings likes_data ");

        //api call for hotestDeals
        Map<String, String> apiHeader = new HashMap<String, String>();
        apiHeader.put("ApiKey ", serverUtilites.apiHeader);
        String loginUserId = singleton.getLoginUserId();
        if (loginUserId != null) {
            getLikesDataFromServer(loginUserId, LikDislkType, emptyLikesTextView, progressBar_UserLikesSection, rv_user_likes_section);
        } else {
            Toast.makeText(getApplicationContext(), "your id is null,please login again", Toast.LENGTH_SHORT).show();
        }


    }
    public void ShowLikesRemoveAlert(String alertMessage)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(UserLikesSectionActivity.this).create();

        alertDialog.setMessage(alertMessage);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* String likdislikid="2";

                user_likes.getLikesData(likdislikid);*/
            }
        });
        alertDialog.show();
    }
    public void showDialog() {
        final Dialog     dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(300, 700);


        dialog.setContentView(R.layout.custom_dialog);
        Button okButton = (Button) dialog.findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getLikesDataFromServer(String loginuserid, String LikeDislikeType, final TextView emptyLikesTextView, final ProgressBar progressBar_UserLikesSection,final RecyclerView rv_user_likes_section) {
        String url = serverUtilites.likesSession + loginuserid + "/" + LikeDislikeType;
        Log.d("request url", "--->" + url);

        stop = true;
        count = 1;
        responseDataSize = 0;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("likes_response", response);
                        try {
                            singleton.user_likes_data.clear();
                            JSONArray likes_section_JArray = new JSONArray(response);
                            String StoreName = null, DiscountedPrice = null, RetailPrice = null, PercentDiscount = null, ProductName = null, ImageName = null, CategoryId = null, Description = null, ViewsCount = null, LikesCount = null;
                            String EndDate = null;
                            String SubCategoryId = null, DealType = null, DealId = null,LikeDislikeId=null;

                            if (likes_section_JArray.length() == 0) {
                                emptyLikesTextView.setVisibility(View.VISIBLE);
                            }

                            for (int j = 0; j < likes_section_JArray.length(); j++) {

                                JSONObject jsonobject = likes_section_JArray.getJSONObject(j);

                                //check the condition key exists in jsonResponse
                                if (likes_section_JArray.getJSONObject(j).has("LikeDislikeId")) {

                                    LikeDislikeId = jsonobject.getString("LikeDislikeId");
                                    Log.d("user_LikeDislikeId", LikeDislikeId);
                                }
                                if (likes_section_JArray.getJSONObject(j).has("StoreName")) {

                                    StoreName = jsonobject.getString("StoreName");
                                    Log.d("user_StoreName", StoreName);
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DiscountedPrice")) {
                                    DiscountedPrice = jsonobject.getString("DiscountedPrice");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("RetailPrice")) {

                                    RetailPrice = jsonobject.getString("RetailPrice");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("PercentDiscount")) {
                                    PercentDiscount = jsonobject.getString("PercentDiscount");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ProductName")) {

                                    ProductName = jsonobject.getString("ProductName");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ImageName")) {
                                    ImageName = jsonobject.getString("ImageName");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("CategoryId")) {
                                    CategoryId = jsonobject.getString("CategoryId");
                                }

                                if (likes_section_JArray.getJSONObject(j).has("Description")) {

                                    Description = jsonobject.getString("Description");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("ViewsCount")) {
                                    if (!jsonobject.getString("ViewsCount").equals("null")) {
                                        ViewsCount = jsonobject.getString("ViewsCount");
                                    } else {
                                        ViewsCount = "0";
                                    }

                                }
                                if (likes_section_JArray.getJSONObject(j).has("LikesCount")) {

                                    if (!jsonobject.getString("LikesCount").equals("null")) {
                                        LikesCount = jsonobject.getString("LikesCount");
                                    } else {
                                        LikesCount = "0";
                                    }

                                }
                                if (likes_section_JArray.getJSONObject(j).has("EndDate")) {

                                    EndDate = jsonobject.getString("EndDate");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("SubCategoryId")) {

                                    SubCategoryId = jsonobject.getString("SubCategoryId");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DealType")) {

                                    DealType = jsonobject.getString("DealType");
                                }
                                if (likes_section_JArray.getJSONObject(j).has("DealId")) {

                                    DealId = jsonobject.getString("DealId");
                                }
                                String imageLink = CategoryId + "/" + ImageName;
                                Log.d("likes(product_name)", ProductName);

                                String totalApiData = imageLink + "-~-" + StoreName + "-~-" + DiscountedPrice + "-~-" + RetailPrice +
                                        "-~-" + PercentDiscount + "-~-" + ProductName + "-~-" + ViewsCount + "-~-"
                                        + LikesCount + "-~-" + EndDate + "-~-" + CategoryId + "-~-" + SubCategoryId + "-~-" + DealType + "-~-" + DealId + "-~-" +LikeDislikeId;

                                singleton.user_likes_data.add(totalApiData);
                                Log.d("likes_data_size(anm)", " " + singleton.user_likes_data.size());

                                Log.d("likes_section_data", "--->" + totalApiData);

                            }
                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }
                        progressBar_UserLikesSection.setVisibility(View.GONE);

                        Log.d("likes_data_size(a)", " " + singleton.user_likes_data.size());
                        mAdapter = new DataAdapter(singleton.user_likes_data, rv_user_likes_section, UserLikesSectionActivity.this, "UserLikesSectionActivity");
                        rv_user_likes_section.setAdapter(mAdapter);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar_UserLikesSection.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> params1 = new HashMap<String, String>();


                params1.put("ApiKey ", "3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF");


                return params1;

            }
        };

        Volley.newRequestQueue(UserLikesSectionActivity.this).add(postRequest);


    }
}