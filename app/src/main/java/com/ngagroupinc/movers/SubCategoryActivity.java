package com.ngagroupinc.movers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Map;

import Interface.OnLoadMoreListener;
import adapters.DataAdapterSubCat;
import adapters.SubCategoryCirclerAdapter;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;


/**
 * Created by Userone on 12/12/2016.
 */

public class SubCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llv_subcategories;
    /*fb integration*/
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    RelativeLayout rll_signin_fb;
    Utilities utilities;
    RelativeLayout rll_subcat_popup;
    Button btn_subcat_login, btn_subcat_signup;
    LinearLayout subcat_popup_layout;
    LinearLayout DealsType, llv_footer, llv_subcateg_page;
    MainActivity main;
SessionManager session;
    public static int jsonArrayCount;
    public ProgressBar progressBar_subcategory;
    private DataAdapterSubCat mAdapter;
    SampleAdapter1 subcategoriesAdapter;
    private RecyclerView mSubCategoriesListItemsRecycleView, mSubCategoryRecycleView;
    Singleton singleton;
    public static RecyclerView categories_recyclerView;
    LinearLayoutManager linearlayoutmanager_categories;
    String dealsReloadStringUrl;
    boolean stop = true;
    String message;
    boolean navigationFirstTime = true;
    ServerUtilites serverUtilities;
    private SubCategoryCirclerAdapter subcategorory_category_adapter;
    TextView subCategoriesTitle, noDealsTextView;
    public static ArrayList<String> SubCategoryArrayListHorizontal, SubCategoryDealsArrayList;
    ImageView backArrowImageView;
    String coming_from;
    TextView titleTextView;
    int scroll_count = 0, scroll_direction_count = 0;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    public static ProgressBar progressbarEndless;
    public static final int SCROLLING_DOWN = 2;
    boolean scrolling_down = false, scrolling_up = false, user_scrolling_down = false, user_scrolling_up = false;
    Bundle bundle;
    View view;
    int storeScrollPosition,storeScrollPosition1;
    String selectedSubCategoryId, selected_category_name, selected_subcategory_position, selected_subcategory_name;
    int count = 1, responseDataSize;
    public static String subCategoryName;

    /*   public static ArrayList<String> categoryData= new ArrayList<String>();*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.sub_category_recycleview);
        Log.d("singleton sub cat","--->"+singleton.subCategoriesData.size());
        callbackManager = CallbackManager.Factory.create();
        Utilities.getInstance(this);
        session=new SessionManager(this);
        singleton = Singleton.getInstance();
        bundle = getIntent().getExtras();
        serverUtilities = new ServerUtilites(this);
        backArrowImageView = (ImageView) findViewById(R.id.backarrow);
        titleTextView = (TextView) findViewById(R.id.tab_text);
        progressBar_subcategory = (ProgressBar) findViewById(R.id.progressbar_subcat);
        progressbarEndless = (ProgressBar) findViewById(R.id.progressbar_endless1);
        subCategoriesTitle = (TextView) findViewById(R.id.subcategories_title);
        noDealsTextView = (TextView) findViewById(R.id.nodeals);
        categories_recyclerView = (RecyclerView) findViewById(R.id.categories_recyclerView);
        categories_recyclerView.setHasFixedSize(true);
        view=(View)findViewById(R.id.innerLineuu);
        linearlayoutmanager_categories = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        selected_category_name = bundle.getString("CategoryName");
        titleTextView.setText(selected_category_name);
        coming_from = bundle.getString("ComingFrom");
        if ((coming_from).equals("NavigationDrawer")) {
            selectedSubCategoryId = bundle.getString("SubCategoryategoryId");

            selected_subcategory_name = bundle.getString("selectedSubcategoryName");
            subCategoriesTitle.setText(selected_subcategory_name);
            subCategoryName = selected_subcategory_name;
        } else {
            subCategoriesTitle.setText(selected_category_name);
            subCategoryName = selected_category_name;
        }
        subcategorory_category_adapter = new SubCategoryCirclerAdapter(SubCategoryActivity.this, "category");
        categories_recyclerView.setLayoutManager(linearlayoutmanager_categories);
        categories_recyclerView.setAdapter(subcategorory_category_adapter);



        rll_subcat_popup = (RelativeLayout) findViewById(R.id.rll_popup);
        llv_footer = (LinearLayout) findViewById(R.id.popup);
        btn_subcat_login = (Button) findViewById(R.id.btn_login);
        btn_subcat_signup = (Button) findViewById(R.id.btn_signup);
        subcat_popup_layout = (LinearLayout) findViewById(R.id.popup_layout);
        rll_signin_fb = (RelativeLayout) findViewById(R.id.rll_signin_fb);
        utilities = new Utilities(this);
        llv_subcateg_page = (LinearLayout)findViewById(R.id.llv_subcateg_page);


        main = new MainActivity();
        categories_recyclerView = (RecyclerView) findViewById(R.id.categories_recyclerView);
        categories_recyclerView.setHasFixedSize(true);
        llv_footer.setOnClickListener(this);
        rll_subcat_popup.setOnClickListener(this);
        btn_subcat_login.setOnClickListener(this);
        btn_subcat_signup.setOnClickListener(this);
        rll_signin_fb.setOnClickListener(this);











        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();

            }
        });
        SubCategoryArrayListHorizontal = new ArrayList<String>();
        SubCategoryDealsArrayList = new ArrayList<String>();
        createDummyData();

        loadData();
        mSubCategoriesListItemsRecycleView = (RecyclerView) findViewById(R.id.subcategory_recycleview);






//subcategories horizontal recycleview

        mSubCategoryRecycleView = (RecyclerView) findViewById(R.id.subcategort_names_recycleview);

        mSubCategoryRecycleView.setHasFixedSize(true);


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSubCategoryRecycleView.setLayoutManager(linearLayoutManager);


        subcategoriesAdapter = new SampleAdapter1(this);
        if ((coming_from).equals("NavigationDrawer")) {
            mSubCategoryRecycleView.scrollToPosition(Integer.parseInt(bundle.getString("SelectedItemPosition")));
        }
        mSubCategoryRecycleView.setAdapter(subcategoriesAdapter);

      /*  if (mSubCategoriesListItemsRecycleView.getChildCount() == 0 || mSubCategoriesListItemsRecycleView.getChildAt(0).getTop() == 0) {

        }*/
        mSubCategoriesListItemsRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int SCROLL_DOWN = 0;
                super.onScrolled(recyclerView, dx, dy);
                scroll_direction_count++;
                /* dx = horizontal scrolled
                *  dy = vertical scrolled */
                int first_pos = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                int last_pos = gridLayoutManager.findLastVisibleItemPosition();


                if (dy < 0) {


                    //if(!user_scrolling_up) {
/*
                    Toast.makeText(getApplicationContext(), "Scrolled Upwards", Toast.LENGTH_SHORT).show();
*/
                    user_scrolling_up = true;
                    if(storeScrollPosition!=dy)
                    {
                        if (categories_recyclerView.getVisibility() == View.GONE) {
                            categories_recyclerView.setVisibility(View.VISIBLE);
                            view.setVisibility(View.GONE);
                            Animation show = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                            categories_recyclerView.startAnimation(show);
                            if (view.getVisibility() == View.GONE) {
                                // animation for splash screen fad in and fad out
                                Thread timer = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            synchronized (this) {
                                                wait(500);

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        view.setVisibility(View.VISIBLE);

                                                    }
                                                });

                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    ;
                                };
                                timer.start();

                            }

                        }
                    }

                    //}
                    storeScrollPosition=dy;
                }
                if (dy > 0) {

/*
                    Toast.makeText(getApplicationContext(), "Scrolled Downwards", Toast.LENGTH_SHORT).show();
*/
                    user_scrolling_down = true;
                    //if(!user_scrolling_down) {
                    if(storeScrollPosition1!=dy) {
                        if (categories_recyclerView.getVisibility() == View.VISIBLE) {


                            Animation hide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                            categories_recyclerView.startAnimation(hide);

                            categories_recyclerView.setVisibility(View.GONE);
                        }
                    }
                    storeScrollPosition1=dy;
                    //  }
                }

            }
        });

    }

    private void loadData() {


        progressBar_subcategory.setVisibility(View.VISIBLE);
        if ((coming_from).equals("NavigationDrawer")) {
          /*  progressBar_subcategory.setVisibility(View.VISIBLE);*/

            //api call
            Map<String, String> Header = new HashMap<String, String>();
            Header.put("ApiKey ", serverUtilities.apiHeader);
            String subCategoryData = "/1/24";
            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.subcategoryDeals + "" + singleton.getSelectedCategoryId() + "/" + selectedSubCategoryId + "/1";
            Log.d("deals data nav", "--->" + dealsReloadStringUrl);

            getSubCategoryResponse(Header, dealsReloadStringUrl + "" + subCategoryData);
        } else {
            //api call
            Map<String, String> Header = new HashMap<String, String>();
            Header.put("ApiKey ", serverUtilities.apiHeader);
            String subCategoryData = "/1/24";
            dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.categoryDeals + "" + singleton.getSelectedCategoryId();
            Log.d("deals data", "--->" + dealsReloadStringUrl);

            getSubCategoryResponse(Header, dealsReloadStringUrl + "" + subCategoryData);
        }


    }
    public void callshowSubcatPopup() {
        utilities.showPopup(rll_subcat_popup,subcat_popup_layout,llv_footer,null);


    }
    public void createDummyData() {


        for (int j = 0; j < singleton.subCategoriesData.size(); j++) {


            String subData2[] = singleton.subCategoriesData.get(j).split("-~-");


            if (singleton.getSelectedCategoryId().equals(subData2[0])) {
                SubCategoryArrayListHorizontal.add(singleton.subCategoriesData.get(j));
            }


        }

    }

    public void getSubCategoryResponse(final Map<String, String> mHeaders,
                                       String url) {

        SubCategoryDealsArrayList.clear();

        stop = true;
        count = 1;
        responseDataSize = 0;
        Log.d("subcat deals url", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("subcat response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;
                            String jsonTotalData;
                            responseDataSize = jArray.length();

                            if (responseDataSize == 0) {
                                noDealsTextView.setVisibility(View.VISIBLE);
                                noDealsTextView.setText("We don't have any " + subCategoryName + " deals right now.");
                                mSubCategoriesListItemsRecycleView.setVisibility(View.GONE);

                            } else {
                                noDealsTextView.setVisibility(View.GONE);
                                mSubCategoriesListItemsRecycleView.setVisibility(View.VISIBLE);
                            }
                            jsonArrayCount= jArray.length();
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
                                SubCategoryDealsArrayList.add(jsonTotalData);


                            }


                        } catch (Exception e) {
                            mAdapter.setLoaded();
                            stop = false;
                            Log.d("Splash screen catch", "--->" + e);
                        }

                        mSubCategoriesListItemsRecycleView.setHasFixedSize(true);


                            gridLayoutManager = new GridLayoutManager(SubCategoryActivity.this, 1);


                        mSubCategoriesListItemsRecycleView.setLayoutManager(gridLayoutManager);
                        progressBar_subcategory.setVisibility(View.GONE);
                        categories_recyclerView.setVisibility(View.VISIBLE);
                        mSubCategoriesListItemsRecycleView.setNestedScrollingEnabled(false);

                        // use a linear layout manager
                        mSubCategoriesListItemsRecycleView.setLayoutManager(gridLayoutManager);
                        mAdapter = new DataAdapterSubCat(SubCategoryDealsArrayList, mSubCategoriesListItemsRecycleView, SubCategoryActivity.this, "SubCategoryActivity");

                        // set the adapter object to the Recyclerview
                        mSubCategoriesListItemsRecycleView.setAdapter(mAdapter);

                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {

                                if (stop && responseDataSize != 0 && jsonArrayCount==24)  {
                                    count++;
                                    //add null , so the adapter will check view_type and show progress bar at bottom
                                    SubCategoryDealsArrayList.add(null);

                                    mAdapter.notifyItemInserted(SubCategoryDealsArrayList.size() - 1);
                                    //api call
                                    Map<String, String> Header = new HashMap<String, String>();
                                    Header.put("ApiKey ", serverUtilities.apiHeader);


                                    String dealsData = "/" + count + "/24";


                                    getDealsResponse(Header, dealsReloadStringUrl + "" + dealsData);

                                }


                            }
                        });

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar_subcategory.setVisibility(View.GONE);
                message =serverUtilities.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();



                Log.d("Splash screen error", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SubCategoryActivity.this).add(postRequest);

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
                utilities.hidePopup(llv_footer,rll_subcat_popup);
                Toast.makeText(getApplicationContext(), "subcat_touchable", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup:
               utilities.enableDisableViewGroup(llv_subcateg_page, false);
                break;
            case R.id.btn_login:
                utilities.hidePopup(llv_footer,rll_subcat_popup);
                startActivity(new Intent(SubCategoryActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                utilities.hidePopup(llv_footer,rll_subcat_popup);
                startActivity(new Intent(SubCategoryActivity.this, JoinDealswebActivity.class));

               /* tv_profile.setEnabled(true);
                tv_subscriptions.setEnabled(true);
                tv_likes.setEnabled(true);
                tv_logout.setEnabled(true);*/
                break;
            case R.id.rll_signin_fb:
                SigninwithFb();

                break;
        }
    }



    private void SigninwithFb() {
        if (singleton.isOnline()) {
            Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
            utilities.showProgressDialog();
            facebook();
        } else {
            utilities.ShowAlert("No Internet Connection");
            Toast.makeText(getApplicationContext(), "no internet available", Toast.LENGTH_SHORT).show();
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
                            Log.d("after_calling_fb", response);
                            utilities.cancelProgressDialog();
                            String result = response;
                            if(result!=null) {


                                    rll_subcat_popup.setVisibility(View.GONE);
                                    llv_footer.setVisibility(View.GONE);
                                    Log.d("y not display","?");
                                    MainActivity.profile.setImageResource(R.drawable.profile_icon_login);
                                    utilities.checkForUserLogin(getApplicationContext());
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

    public class SampleAdapter1 extends RecyclerView.Adapter<SampleAdapter1.MyViewHolder> {

        Context context;
        View itemView;
        String itemType;
        Singleton singleton;
        ArrayList<String> selections;


        public SampleAdapter1(Context context) {
            Log.d("subcatAdapter", "--->" + MainActivity.hottestDealsArrayList.size());
            this.context = context;
            selections = new ArrayList<String>();
            singleton = Singleton.getInstance();
            serverUtilities = new ServerUtilites(context);
        }


        @Override
        public SampleAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subcategory_recycleview_list_items, parent, false);


            return new SampleAdapter1.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final SampleAdapter1.MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            String subcategoryData[] = SubCategoryArrayListHorizontal.get(position).split("-~-");


            SpannableString content = new SpannableString(subcategoryData[1]);
            content.setSpan(new UnderlineSpan(), 0, subcategoryData[1].length(), 0);
            holder.subCategoriesTextView.setText(content);

            if ((coming_from).equals("NavigationDrawer") && navigationFirstTime) {

                if (selected_subcategory_name.equals(subcategoryData[1])) {
                    holder.subCategoriesTextView.setTextColor(getResources().getColor(R.color.black));

                } else {
                    holder.subCategoriesTextView.setTextColor(getResources().getColor(R.color.blue));
                }

            } else {
                try {
                    if (selections.get(position).equals("1")) {
                        holder.subCategoriesTextView.setTextColor(getResources().getColor(R.color.black));
                    } else {

                        holder.subCategoriesTextView.setTextColor(getResources().getColor(R.color.blue));

                    }


                } catch (Exception e) {

                    Log.d("catch", "--->" + e);
                }
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("position", "--->" + position);
                    selections.clear();
                    navigationFirstTime = false;
                    for (int i = 0; i < SubCategoryArrayListHorizontal.size(); i++) {
                        if (position == i) {

                            selections.add("1");

                        } else
                            selections.add("0");


                    }
                    noDealsTextView.setVisibility(View.GONE);
                    holder.subCategoriesTextView.setTextColor(getResources().getColor(R.color.black));
                    progressBar_subcategory.setVisibility(View.VISIBLE);
                    subCategoriesTitle.setText(SubCategoryArrayListHorizontal.get(position).split("-~-")[1]);
                    subCategoryName = SubCategoryArrayListHorizontal.get(position).split("-~-")[1];
                    //api call
                    Map<String, String> Header = new HashMap<String, String>();
                    Header.put("ApiKey ", serverUtilities.apiHeader);
                    String subCategoryData = "/1/24";
                    dealsReloadStringUrl = serverUtilities.serverUrl + "" + serverUtilities.subcategoryDeals + "" + singleton.getSelectedCategoryId() + "/" + SubCategoryArrayListHorizontal.get(position).split("-~-")[2] + "/1";
                    Log.d("deals data", "--->" + dealsReloadStringUrl);

                    getSubCategoryResponse(Header, dealsReloadStringUrl + "" + subCategoryData);
                    notifyDataSetChanged();


                }
            });

        }

        @Override
        public int getItemCount() {

            return SubCategoryArrayListHorizontal.size();


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {


            public TextView subCategoriesTextView;


            public MyViewHolder(View v) {
                super(v);

                subCategoriesTextView = (TextView) v.findViewById(R.id.text_subcategory_horizontal);


            }
        }
    }

    public void getDealsResponse(final Map<String, String> mHeaders,
                                 String url) {



      /*  String url = ServerUtilites.serverUrl + "" + string;*/

        Log.d("request url", "--->" + url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("next subcat  response", "--->" + response);
                        try {


                            JSONArray jArray = new JSONArray(response);
                            String  ImageName = null, ViewsCount = null, LikesCount = null;

                            String jsonTotalData;
                            //  endlessscrollview data
                            SubCategoryDealsArrayList.remove(SubCategoryDealsArrayList.size() - 1);
                            mAdapter.notifyItemRemoved(SubCategoryDealsArrayList.size());
                        /*    Log.d("studentslist next", "--->" + SubCategoryDealsArrayList.size());*/

                            jsonArrayCount= jArray.length();
                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject jsonobject = jArray.getJSONObject(i);

                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverUtilities.myList.size(); k++) {

                                    if (jArray.getJSONObject(i).has(serverUtilities.myList.get(k)))
                                    {
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
                                SubCategoryDealsArrayList.add(jsonTotalData);


                                mAdapter.notifyItemInserted(SubCategoryDealsArrayList.size());

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
                Log.d("DisplayItemActivity error", "--->" + volleyError);
            }
        }) {
            public Map<String, String> getHeaders() {

                Log.d("param", "--->" + mHeaders);
                return mHeaders;
            }
        };

        Volley.newRequestQueue(SubCategoryActivity.this).add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
