package com.ngagroupinc.movers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projo.ServerUtilites;
import projo.Singleton;

/**
 * Created by Userone on 1/6/2017.
 */

public class SubScriptionsActivity extends AppCompatActivity {
    RecyclerView subscriptionsRecycleView;
    GridLayoutManager subScriptionsGridViewManager;
    SubScriptionsAdapter adapter;
    Singleton singleton;
    ImageView backArrowImageView;
    ServerUtilites serverUtilities;

    ArrayList<String> subScriptionsAlertArrayList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcriptions_layout);
        singleton = Singleton.getInstance();
        serverUtilities = new ServerUtilites(this);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow1);
        subscriptionsRecycleView = (RecyclerView) findViewById(R.id.subscriptions);
        subscriptionsRecycleView.setHasFixedSize(true);
        subScriptionsGridViewManager = new GridLayoutManager(this, 3);
        loadData();



        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        if(singleton.isOnline())
        {

            Map<String, String> apiHeader = new HashMap<String, String>();
            apiHeader.put("ApiKey ", serverUtilities.apiHeader);
            String reviewsString = serverUtilities.serverUrl + "" + serverUtilities.subScriptionsAlerts+""+singleton.getLoginUserId() ;
            getSubScriptionsData(apiHeader, reviewsString);
        }
        else{

        }
    }

    // load initial data
    private void loadData() {


       /* subScriptionsArrayList.clear();

        for (int i = 0; i < singleton.categoriesData.size(); i++) {


            subScriptionsArrayList.add(singleton.categoriesData.get(i));


        }*/
    }
    public void getSubScriptionsData(final Map<String, String> mHeaders, String url) {
        Log.d("subscriptions request url", "--->" + url);
        subScriptionsAlertArrayList.clear();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" subscriptions response", "--->" + response);
                        try {

                            String CategoryId = null, CategoryName = null, ImageUrl = null, Status = null;

                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("CategoryId")) {

                                    CategoryId = jsonobject.getString("CategoryId");
                                }
                                if (jArray.getJSONObject(i).has("CategoryName")) {
                                    CategoryName = jsonobject.getString("CategoryName");
                                }

                                if (jArray.getJSONObject(i).has("ImageUrl")) {

                                    ImageUrl = jsonobject.getString("ImageUrl");
                                }

                                if (jArray.getJSONObject(i).has("Status")) {
                                    if (!jsonobject.getString("Status").equals("null")) {
                                        Status = jsonobject.getString("Status");
                                    } else {
                                        Status = "0";
                                    }

                                }
                                subScriptionsAlertArrayList.add(CategoryId+"-~-"+CategoryName+"-~-"+ImageUrl+"-~-"+Status);
                            }
                            subscriptionsRecycleView.setLayoutManager(subScriptionsGridViewManager);
                            adapter = new SubScriptionsAdapter(SubScriptionsActivity.this);
                            subscriptionsRecycleView.setAdapter(adapter);
                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SubScriptionsActivity.this).add(postRequest);


    }
    class SubScriptionsAdapter extends RecyclerView.Adapter<SubScriptionsAdapter.MyViewHolder> {

        Context context;
        View itemView;

        ArrayList<String> selections= new ArrayList<String>();
        public SubScriptionsAdapter(Context context) {
            this.context = context;


        }


        @Override
        public SubScriptionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subscriptions_adapter_layout, parent, false);

            return new SubScriptionsAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(SubScriptionsAdapter.MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            String categoryData[] = subScriptionsAlertArrayList.get(position).split("-~-");
            holder.categoriesTextView.setText(categoryData[1]);
            if(categoryData[3].equals("0"))
            {
                holder.tickImageView.setVisibility(View.GONE);

            }
            else{
                holder.tickImageView.setVisibility(View.VISIBLE);
                holder.img.setAlpha(110);

            }

            String imgUrl = "http://www.dealsweb.com" + categoryData[2].replace("\\", "//");

         /*   holder.img.setAlpha(127);*/
            Picasso.with(context).load(imgUrl).into(holder.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String categoryData[] = subScriptionsAlertArrayList.get(position).split("-~-");
                    String SubScriptionsString;

                    Map<String, String> apiHeader = new HashMap<String, String>();
                    apiHeader.put("ApiKey ", serverUtilities.apiHeader);
                    int data=serverUtilities.ordinalIndexOf(subScriptionsAlertArrayList.get(position),"-~-",3);
                    if(categoryData[3].equals("0"))
                    {


                        subScriptionsAlertArrayList.set(position,subScriptionsAlertArrayList.get(position).substring(0,data)+"-~-1");
                        SubScriptionsString = serverUtilities.serverUrl + "" + serverUtilities.subScriptionsAlerts+""+singleton.getLoginUserId()+"/"+categoryData[0]+"/1" ;
                    }
                   else{
                        subScriptionsAlertArrayList.set(position,subScriptionsAlertArrayList.get(position).substring(0,data)+"-~-0");
                        SubScriptionsString = serverUtilities.serverUrl + "" + serverUtilities.subScriptionsAlerts+""+singleton.getLoginUserId()+"/"+categoryData[0]+"/0" ;
                    }
                    String arrayString=subScriptionsAlertArrayList.get(position).substring(0,data)+"-~-0";
                    getSubScriptionsUpdateData(apiHeader, SubScriptionsString,position,arrayString);
                    notifyDataSetChanged();


                  /*  String categoryData[] = subScriptionsAlertArrayList.get(position).split("-~-");
                   */
                }
            });
        }

        @Override
        public int getItemCount() {
            return subScriptionsAlertArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView categoriesTextView;
            public ImageView img, tickImageView;

            public MyViewHolder(View v) {
                super(v);
                categoriesTextView = (TextView) v.findViewById(R.id.categories_text1);
                img = (ImageView) v.findViewById(R.id.categories_circleImage);
                tickImageView = (ImageView) v.findViewById(R.id.tick);

            }
        }
    }
    public void getSubScriptionsUpdateData(final Map<String, String> mHeaders, String url, final int position, final String arrayData) {
        Log.d("subscriptions update  request url", "--->" + url);


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" subscriptions update response", "--->" + response);

                            if(!response.equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                                subScriptionsAlertArrayList.set(position,arrayData);
                                adapter = new SubScriptionsAdapter(SubScriptionsActivity.this);
                                subscriptionsRecycleView.setAdapter(adapter);

                            }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SubScriptionsActivity.this).add(postRequest);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
