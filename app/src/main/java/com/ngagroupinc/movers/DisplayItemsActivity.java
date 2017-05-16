package com.ngagroupinc.movers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.OnLoadMoreListener;
import adapters.DataAdapter;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

public class DisplayItemsActivity extends AppCompatActivity {


    ActionBar actionBar;
    String message = null;

    private TextView noDealsTextView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    boolean stop = true;
    private GridLayoutManager linearLayoutManager;

    private List<String> hottestDealsArrayList;
    String dealsReloadStringUrl;
    Singleton singleton;
    ServerUtilites serverUtilities;
    LinearLayout llv_subcategories;
    LinearLayout llv_footer;
    TextView titleTextView;
    protected Handler handler;
    ImageView backArrowImageView;
    CallbackManager callbackManager;
    public static int jsonArrayListLength;
    RelativeLayout rll_signin_fb;
    SessionManager session;
    Utilities utilities;
    RelativeLayout rll_display_popup;
    LinearLayout display_popup_layout;
    public ProgressBar progressBar_hotestdeals;
    public static ProgressBar progressbarEndless;
    Map<String, String> Header;
    Button btn_display_login, btn_display_signup;
    public static String titleText;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.displayitems_layout);
        Log.d("displayItemsActivity", "---->");
        noDealsTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.deals_recycleView1);

        rll_display_popup = (RelativeLayout) findViewById(R.id.rll_popup);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow);
        titleTextView = (TextView) findViewById(R.id.tab_text);
        utilities = new Utilities(this);
        llv_footer = (LinearLayout) findViewById(R.id.popup);
        btn_display_login = (Button) findViewById(R.id.btn_login);
        btn_display_signup = (Button) findViewById(R.id.btn_signup);
        rll_signin_fb = (RelativeLayout) findViewById(R.id.rll_signin_fb);
        display_popup_layout = (LinearLayout) findViewById(R.id.popup_layout);
        progressBar_hotestdeals = (ProgressBar) findViewById(R.id.progressbar_hotestdeals);
        progressbarEndless = (ProgressBar) findViewById(R.id.progressbar_endless);
        llv_subcategories = (LinearLayout) findViewById(R.id.llv_subcategories);
        callbackManager = CallbackManager.Factory.create();
        session = new SessionManager(this);
        hottestDealsArrayList = new ArrayList<String>();
        singleton = Singleton.getInstance();
        serverUtilities = new ServerUtilites(this);
        Utilities.getInstance(this);
        handler = new Handler();
        //api header
        Header = new HashMap<String, String>();
        Header.put("ApiKey ", serverUtilities.apiHeader);
     /*   if (toolbar != null) {
            setSupportActionBar(toolbar);*/
        rll_signin_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninwithFb();


            }
        });
        llv_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.enableDisableViewGroup(llv_subcategories, false);
            }
        });
        rll_display_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.hidePopup(llv_footer, rll_display_popup);


            }
        });
        btn_display_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.hidePopup(llv_footer, rll_display_popup);
                startActivity(new Intent(DisplayItemsActivity.this, LoginActivity.class));

            }
        });
        btn_display_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.hidePopup(llv_footer, rll_display_popup);
                startActivity(new Intent(DisplayItemsActivity.this, JoinDealswebActivity.class));


            }
        });

        if (singleton.getDealsType().equals("Hottest")) {

            titleText = "Hottest Deals";
            titleTextView.setText("Hottest Deals");


        } else if (singleton.getDealsType().equals("80%Deals")) {

            titleText = "80% Off";
            titleTextView.setText("80% Off");

        } else if (singleton.getDealsType().equals("70%Deals")) {

            titleText = "70% Off";
            titleTextView.setText("70% Off");

        } else if (singleton.getDealsType().equals("60%Deals")) {
            titleText = "60% Off";
            titleTextView.setText("60% Off");
        } else if (singleton.getDealsType().equals("ExpiredDeals")) {
            titleText = "Expired Deals";
            titleTextView.setText("Expired Deals");
        } else if (singleton.getDealsType().equals("Coupons")) {
            titleText = "Coupons";
            titleTextView.setText("Coupons");
            noDealsTextView.setVisibility(View.VISIBLE);
            noDealsTextView.setText("We don't have any coupons right now.");
            mRecyclerView.setVisibility(View.GONE);
        }

           /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
       /* }*/
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (singleton.getScreenSize() > 2) {
            linearLayoutManager = new GridLayoutManager(DisplayItemsActivity.this, 3);

        } else {
            linearLayoutManager = new GridLayoutManager(DisplayItemsActivity.this, 2);
        }
        loadData();


    }

    private void SigninwithFb() {
        if (singleton.isOnline()) {

            utilities.showProgressDialog();
            facebook();
        } else {
            utilities.ShowAlert("No Internet Connection");

        }
    }

    private void facebook() {

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me"));
        final LoginBehavior WEB_VIEW_ONLY;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                if (AccessToken.getCurrentAccessToken() != null) {
                    Utilities.getInstance().RequestData(getApplicationContext(), new MainActivity.SomeFBListener<String>() {
                        @Override
                        public void getFBResult(String response) {
                            Log.d("after_calling_fb", response);
                            utilities.cancelProgressDialog();
                            String result = response;
                            if (result != null) {
                                if (result.equals("FB error")) {

                                } else if (result.equals("account error")) {

                                } else {
                                    MainActivity.profile.setImageResource(R.drawable.profile_icon_login);

                                    rll_display_popup.setVisibility(View.GONE);
                                    llv_footer.setVisibility(View.GONE);
                                    utilities.checkForUserLogin(getApplicationContext());
                                    Toast.makeText(getApplicationContext(), "you have your account with dealsweb", Toast.LENGTH_SHORT).show();


                                }
                            }


                        }

                        @Override
                        public void getFBErrorResult(String String) {
                            utilities.ShowAlert(String);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "fb got cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "fb got error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    public void CallShowDisplayPopup() {
        utilities.showPopup(rll_display_popup, display_popup_layout, llv_footer, null);

    }


    // load initial data
    private void loadData() {


        hottestDealsArrayList.clear();

        for (int i = 0; i < singleton.hottestDeals.size(); i++) {
          /*  String hottestDealsData[]=singleton.hottestDeals.get(i).split("-~-");*/
            if (singleton.getDealsType().equals("Hottest")) {

                hottestDealsArrayList.add(singleton.hottestDeals.get(i));

            }
        }

        if (singleton.getDealsType().equals("Hottest")) {
            mRecyclerView.setHasFixedSize(true);
            jsonArrayListLength = singleton.hottestDeals.size();



            // use a linear layout manager
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAdapter = new DataAdapter(hottestDealsArrayList, mRecyclerView, DisplayItemsActivity.this, "DisplayItemsActivity");

            // set the adapter object to the Recyclerview
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    Log.d("load more", "--->");
                    if (stop) {
                        count++;
                        //add null , so the adapter will check view_type and show progress bar at bottom
                        hottestDealsArrayList.add(null);

                        mAdapter.notifyItemInserted(hottestDealsArrayList.size() - 1);
                        //api call
                        Map<String, String> Header = new HashMap<String, String>();
                        Header.put("ApiKey ", serverUtilities.apiHeader);

                        if (singleton.getDealsType().equals("Hottest")) {


                            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.hottestDeals1 + "/" + count + "/24";
                        }


                        getDealsResponse(Header, dealsReloadStringUrl);

                    }


                }
            });
        }
        if (singleton.getDealsType().equals("80%Deals")) {
            progressBar_hotestdeals.setVisibility(View.VISIBLE);
            String percentData = "80/1/24";
            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "" + percentData;

            getpersentDealsResponse(Header, dealsReloadStringUrl);


        } else if (singleton.getDealsType().equals("70%Deals")) {
            progressBar_hotestdeals.setVisibility(View.VISIBLE);
            String percentData = "70/1/24";
            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "" + percentData;
            getpersentDealsResponse(Header, dealsReloadStringUrl);
        } else if (singleton.getDealsType().equals("60%Deals")) {
            progressBar_hotestdeals.setVisibility(View.VISIBLE);
            String percentData = "60/1/24";
            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "" + percentData;
            getpersentDealsResponse(Header, dealsReloadStringUrl);
        } else if (singleton.getDealsType().equals("ExpiredDeals")) {
            progressBar_hotestdeals.setVisibility(View.VISIBLE);
            String expiredData = "1/24";

            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.expiredDeals + "" + expiredData;

            getpersentDealsResponse(Header, dealsReloadStringUrl);
        }


    }


    public void getDealsResponse(final Map<String, String> mHeaders,
                                 String url) {


        Log.d("request url", "--->" + url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("hottestresponse", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;
                            //  endlessscrollview data
                            hottestDealsArrayList.remove(hottestDealsArrayList.size() - 1);
                            mAdapter.notifyItemRemoved(hottestDealsArrayList.size());
                            Log.d("studentslist next", "--->" + hottestDealsArrayList.size());
                            //add items one by one


                            jsonArrayListLength = jArray.length();
                            String jsonTotalData;

                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);

                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverUtilities.myList.size(); k++) {

                                    if (jArray.getJSONObject(i).has(serverUtilities.myList.get(k))) {
                                        if (k == 0) {
                                            if (jArray.getJSONObject(i).has(serverUtilities.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverUtilities.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverUtilities.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverUtilities.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverUtilities.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverUtilities.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverUtilities.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverUtilities.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }
                                }


                                hottestDealsArrayList.add(jsonTotalData);


                                mAdapter.notifyItemInserted(hottestDealsArrayList.size());

                            }


                        } catch (Exception e) {
                            mAdapter.setLoaded();
                            stop = false;
                            Log.d("Display ItemActivity catch", "--->" + e);
                        }

                        progressbarEndless.setVisibility(View.GONE);
                        mAdapter.setLoaded();
                        mAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = serverUtilities.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                Log.d("DisplayItemActivity error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(DisplayItemsActivity.this).add(postRequest);

    }

    public void getpersentDealsResponse(final Map<String, String> mHeaders,
                                        String url) {

        hottestDealsArrayList.clear();

        Log.d("persent and expired deals url", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("latestdeals response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;
                            String jsonTotalData = "";
                            jsonArrayListLength = jArray.length();
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);
                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverUtilities.myList.size(); k++) {

                                    if (jArray.getJSONObject(i).has(serverUtilities.myList.get(k))) {
                                        if (k == 0) {
                                            if (jArray.getJSONObject(i).has(serverUtilities.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverUtilities.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverUtilities.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverUtilities.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverUtilities.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverUtilities.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverUtilities.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverUtilities.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }
                                }

                                hottestDealsArrayList.add(jsonTotalData);


                            }


                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }

                        mRecyclerView.setHasFixedSize(true);
                        progressBar_hotestdeals.setVisibility(View.GONE);

                     /*   linearLayoutManager = new GridLayoutManager(DisplayItemsActivity.this, 2);*/

                        // use a linear layout manager
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mAdapter = new DataAdapter(hottestDealsArrayList, mRecyclerView, DisplayItemsActivity.this, "DisplayItemsActivity");

                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                Log.d("load more", "--->");
                                if (stop) {
                                    count++;
                                    //add null , so the adapter will check view_type and show progress bar at bottom
                                    hottestDealsArrayList.add(null);

                                    mAdapter.notifyItemInserted(hottestDealsArrayList.size() - 1);
                                    //api call
                                    Map<String, String> Header = new HashMap<String, String>();
                                    Header.put("ApiKey ", serverUtilities.apiHeader);
                                    if (singleton.getDealsType().equals("80%Deals")) {


                                        dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "80/" + count + "/24";
                                    } else if (singleton.getDealsType().equals("70%Deals")) {

                                        dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "70/" + count + "/24";
                                    } else if (singleton.getDealsType().equals("60%Deals")) {


                                        dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.discountDeals + "60/" + count + "/24";
                                    } else if (singleton.getDealsType().equals("ExpiredDeals")) {


                                        dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.expiredDeals + count + "/24";
                                    }

                                    getDealsResponse(Header, dealsReloadStringUrl);

                                }


                            }
                        });

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar_hotestdeals.setVisibility(View.GONE);

                message = serverUtilities.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("Splash screen error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(DisplayItemsActivity.this).add(postRequest);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}