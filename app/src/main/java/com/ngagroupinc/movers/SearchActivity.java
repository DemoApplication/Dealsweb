package com.ngagroupinc.movers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.OnLoadMoreListener;
import adapters.DataAdapter;
import projo.ServerUtilites;
import projo.Singleton;
import projo.Utilities;


/**
 * Created by Userone on 11/15/2016.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    EditText searchEditText;


    GridLayoutManager gridLayoutManager;
    RecyclerView searchRecycleView;

    ServerUtilites serverUtilities;
    Utilities utilities;
    ImageView backArrowImageView,search_cross;
    /*fb integration*/
    CallbackManager callbackManager;
    public static int jsonArrayListLength;
    DataAdapter mAdapter;
    public ProgressBar progressBar_hotestdeals;
    public static ProgressBar progressbarEndless;
    int count = 1,responseDataSize;
    RelativeLayout rll_signin_fb;
    public RelativeLayout rll_search_popup;
    Button btn_login, btn_signup;
    Singleton singleton;
    public LinearLayout search_popup_layout;
    public LinearLayout llv_footer, llv_search_root;


    boolean stop = true;
    public static String titleText;
    String message;
    String dealsReloadStringUrl;
    private List<String> searchItemsArrayList = new ArrayList<String>();
    RelativeLayout searchRelativeLayout, notSearchRelativeLayout;
    TextView  noRecordsTextView;
    boolean onceOnly=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.toolbar_layout);
        callbackManager = CallbackManager.Factory.create();
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.hotestdeals_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));*/

        notSearchRelativeLayout = (RelativeLayout) findViewById(R.id.not_search);
        searchRelativeLayout = (RelativeLayout) findViewById(R.id.search);
        noRecordsTextView = (TextView) findViewById(R.id.empty_view1);
        utilities=new Utilities(this);
        Utilities.getInstance(this);
        singleton= Singleton.getInstance();
        search_cross = (ImageView) findViewById(R.id.search_cross);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow2);
        progressBar_hotestdeals = (ProgressBar) findViewById(R.id.progressbar_search);
        progressbarEndless = (ProgressBar) findViewById(R.id.progressbar_endless3);
        serverUtilities = new ServerUtilites(this);
        llv_search_root=(LinearLayout)findViewById(R.id.llv_search_root);
        rll_search_popup = (RelativeLayout) findViewById(R.id.rll_popup);
        llv_footer = (LinearLayout) findViewById(R.id.popup);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        search_popup_layout = (LinearLayout) findViewById(R.id.popup_layout);
        rll_signin_fb = (RelativeLayout) findViewById(R.id.rll_signin_fb);
        rll_search_popup.setOnClickListener(this);
        llv_footer.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        rll_signin_fb.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
       /* xTextView = (TextView) findViewById(R.id.x);*/
        searchRecycleView = (RecyclerView) findViewById(R.id.my_recycler_view);
        searchEditText = (EditText) findViewById(R.id.search1);
        search_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                /*singleton.setDealsType("ExpiredDeals");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);*/
            }
        });



        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("data", "--->" + (actionId == EditorInfo.IME_ACTION_DONE));
              /*  if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                else {*/
                if(onceOnly)
                {
                    onceOnly=false;
                    noRecordsTextView.setVisibility(View.GONE);
                 /*   Toast.makeText(getApplicationContext(), "done clicked", Toast.LENGTH_SHORT).show();*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    progressBar_hotestdeals.setVisibility(View.VISIBLE);
                    count = 0;
                    Map<String, String> Header = new HashMap<String, String>();
                    Header.put("ApiKey ", serverUtilities.apiHeader);
                    titleText = searchEditText.getText().toString();
                    String keyword = searchEditText.getText().toString().trim();


                    String dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.searchIteam;
                    try {
                        getSearchResponse(Header, dealsReloadStringUrl, keyword);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

               /* }*/
                return  false;
            }
        });
        //Text changed Listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {





            }
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    search_cross.setVisibility(View.GONE);
                    onceOnly=true;
                    searchItemsArrayList.clear();
                    mAdapter = new DataAdapter(searchItemsArrayList, searchRecycleView, SearchActivity.this, "SearchActivity");

                    // set the adapter object to the Recyclerview
                    searchRecycleView.setAdapter(mAdapter);
                } else{
                    onceOnly=true;
                    search_cross.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void callshowSearchPopup() {
        utilities.showPopup(rll_search_popup,search_popup_layout,llv_footer,null);


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rll_popup:

                utilities.hidePopup(llv_footer,rll_search_popup);


                break;
            case R.id.popup:

                utilities.enableDisableViewGroup(llv_search_root, false);
                break;
            case R.id.btn_login:
                utilities.hidePopup(llv_footer,rll_search_popup);
                startActivity(new Intent(SearchActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                utilities.hidePopup(llv_footer,rll_search_popup);
                startActivity(new Intent(SearchActivity.this, JoinDealswebActivity.class));

                break;
            case R.id.rll_signin_fb:
                signinwithFb();

                break;
        }
    }


    private void signinwithFb() {
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

                Log.d("call fromloginmanager", "Loginmanager");
                if (AccessToken.getCurrentAccessToken() != null) {
                    Utilities.getInstance().RequestData(getApplicationContext(), new MainActivity.SomeFBListener<String>() {
                        @Override
                        public void getFBResult(String response) {

                            utilities.cancelProgressDialog();
                            String result = response;
                            if(result!=null) {


                                //  MainActivity.profile.setImageResource(R.drawable.profile_icon_login);

                                rll_search_popup.setVisibility(View.GONE);
                                llv_footer.setVisibility(View.GONE);
                                //MainActivity.profile.setImageResource(R.drawable.profile_icon_login);
                                utilities.checkForUserLogin(getApplicationContext());
                                Log.d("y not display","?");
                                Toast.makeText(getApplicationContext(), "you have your account with dealsweb", Toast.LENGTH_SHORT).show();



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


    public void getSearchResponse(final Map<String, String> mHeaders, String url, String keyword) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("PageSize", "24");
        jsonBody.put("PageNumber","1");
        jsonBody.put("Keyword", keyword);

        final String mRequestBody = jsonBody.toString();
        searchItemsArrayList.clear();
        stop = true;
        count = 1;
        responseDataSize = 0;
        Log.d("search deals url", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("search  response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String  ImageName = null,ViewsCount = null, LikesCount = null;
                            String jsonTotalData;
                            responseDataSize = jArray.length();

                            if (responseDataSize == 0) {

                                searchRecycleView.setVisibility(View.GONE);

                            } else {

                                searchRecycleView.setVisibility(View.VISIBLE);
                            }
                            jsonArrayListLength=jArray.length();
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

                                    }
                                    else{
                                        jsonTotalData=jsonTotalData+"-~-";
                                    }
                                }
                                searchItemsArrayList.add(jsonTotalData);


                            }
                            if (jArray.length() == 0) {
                                progressBar_hotestdeals.setVisibility(View.GONE);
                                noRecordsTextView.setVisibility(View.VISIBLE);
                                noRecordsTextView.setText("We don't have any deals for " + searchEditText.getText().toString().trim() + " right now.");
                            }

                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }

                        searchRecycleView.setHasFixedSize(true);
                        progressBar_hotestdeals.setVisibility(View.GONE);
                        if(singleton.getScreenSize()>2)
                        {
                            gridLayoutManager = new GridLayoutManager(SearchActivity.this, 3);
                        }
                        else{
                            gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
                        }


                        // use a linear layout manager
                        searchRecycleView.setLayoutManager(gridLayoutManager);
                        mAdapter = new DataAdapter(searchItemsArrayList, searchRecycleView, SearchActivity.this, "SearchActivity");

                        // set the adapter object to the Recyclerview
                        searchRecycleView.setAdapter(mAdapter);

                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {

                                if (stop  && responseDataSize != 0  && jsonArrayListLength==24) {
                                    count++;
                                    //add null , so the adapter will check view_type and show progress bar at bottom
                                    searchItemsArrayList.add(null);


                                    mAdapter.notifyItemInserted(searchItemsArrayList.size() - 1);


                                    //api call
                                    Map<String, String> Header = new HashMap<String, String>();
                                    Header.put("ApiKey ", serverUtilities.apiHeader);
                                    dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.searchIteam ;
                                    try {
                                        getSearchDealsResponse(Header, dealsReloadStringUrl,searchEditText.getText().toString(),count+"");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }


                            }
                        });

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressBar_hotestdeals.setVisibility(View.GONE);
                message =serverUtilities.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("Splash screen error", "--->" + message);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("body", "--->" + mRequestBody);
                return  mRequestBody.getBytes();
            }
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SearchActivity.this).add(postRequest);

    }

    public void getSearchDealsResponse(final Map<String, String> mHeaders,
                                       String url, String keyword, String count) throws JSONException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("PageSize", "24");
        jsonBody.put("PageNumber",count);
        jsonBody.put("Keyword", keyword);

        final String mRequestBody1 = jsonBody.toString();
      /*  String url = ServerUtilites.serverUrl + "" + string;*/

        Log.d("request url123", "--->" + url+"...."+mRequestBody1);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("search response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String ImageName = null,  ViewsCount = null, LikesCount = null;

                            //  endlessscrollview data
                            searchItemsArrayList.remove(searchItemsArrayList.size() - 1);
                            mAdapter.notifyItemRemoved(searchItemsArrayList.size());

                            //add items one by one
                            int start = searchItemsArrayList.size();


                            String jsonTotalData;
                            jsonArrayListLength=jArray.length();
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

                                    }
                                    else{
                                        jsonTotalData=jsonTotalData+"-~-";
                                    }
                                }

                                Log.d("search response","--->"+jsonTotalData);
                                searchItemsArrayList.add(jsonTotalData);


                                mAdapter.notifyItemInserted(searchItemsArrayList.size());

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
            public void onErrorResponse(VolleyError volleyError) {
                message =serverUtilities.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("DisplayItemActivity error", "--->" + message);
            }
        })  {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("body", "--->" + mRequestBody1);
                return  mRequestBody1.getBytes();
            }
            public Map<String, String> getHeaders() {


                return mHeaders;
            }

        };

        Volley.newRequestQueue(SearchActivity.this).add(postRequest);

    }
    ;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
