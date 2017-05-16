package com.ngagroupinc.movers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.SampleAdapter;
import adapters.SubCategoriesAdapter;
import adapters.UserDetailsAdapter;
import database.DatabaseHandler;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    Utilities Utility;

    NavigationView navigationView;
    private TabLayout tabLayout;

    LinearLayout expiredDealsTextView;
    public RelativeLayout navigationIconIamgeView;
    DrawerLayout drawer;
    ImageView searchImageView, search_cross;
    ProgressBar progressBarLatestdeasl;
    JSONArray latestDealsJArray;
    LinearLayout couponsTextView, textViewAbout;

    public static ImageView navigationProfileImageView;
    DatabaseHandler db;
    TextView viewAllButton;
    ArrayList<String> selections;
    public static List<String> hottestDealsArrayList = new ArrayList<String>();
    private View navHeader;
    ArrayList<String> subCategoryDataItems;
    RelativeLayout categoriesFragment, activitySubCategory;
    ProgressBar progressBar1;


    private RecyclerView listView, categoriesRecycleview, latestDealsRecycleView, hotestDealsRecycleView;
    LinearLayoutManager mLayoutManager, cateroriesLinearLayoutManager;
    Utilities utilities;


    RelativeLayout rll_subcategory_parent;

    ImageView iv_back;
    String title_subcategory;
    Singleton singleton;
    public static ImageView profile;
    String message;

    int count = 1, arrayCount;
    LinearLayout homeLinearLayout;

    GridLayoutManager linearLayoutManager1, hotestGridLayOutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private SubCategoriesAdapter subCategoriesAdapter;
    private SampleAdapter subCategorory_CategoryAdapter;
    private NaviagtionAdapter adapter;
    private List<String> latestDealsArrayList;
    ServerUtilites serverutilities;
    String dealsReloadStringUrl;
    Button btnLoadMore;

    CallbackManager callbackManager;


    private ListView subCategoriesList;
    /*sign*/
    TextView tv_username;
    /*popup menu*/
    RelativeLayout relativelayoutHomePage;
    String selectedCategoryName, selectedCategoryId, selectedSubcategoryName, selectedSubcategoryId;

    RecyclerView.LayoutManager userRelativeViewLayoutManager;
    Button btn_login, btn_signup;
    public RelativeLayout relativelayoutHomeRoot;
    TextView titleSubCategory;
    public static TextView displayNameTextView, displayNameTextView1;
    public static RelativeLayout relativeLayoutPopup;
    LinearLayout popupLayout;
    RecyclerView popupMenu;
    UserDetailsAdapter userDetailsDisplayAdapter;
    ArrayList<String> loginUserIcons;
    private ViewRelatedItemsAdapter recyclerviewAdapter;
    public static LinearLayout linearLayoutViewFooter;
    ServerUtilites serverUtilites;
    Button h80DealsButon, h70DealsButon, h60DealsButon;
    RelativeLayout relativelayoutSignInfb;
    private static String[] loginUserDetailsStringArray = null;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

        session = new SessionManager(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        db = new DatabaseHandler(this);
        selections = new ArrayList<String>();


        searchImageView = (ImageView) findViewById(R.id.search_home);

        navigationIconIamgeView = (RelativeLayout) findViewById(R.id.toggle);
        btnLoadMore = (Button) findViewById(R.id.loadmore);
        h80DealsButon = (Button) findViewById(R.id.h80_persent_deals);
        h70DealsButon = (Button) findViewById(R.id.h70_persent_deals);
        h60DealsButon = (Button) findViewById(R.id.h60_persent_deals);
       /* progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);*/
        Utility = new Utilities(this);
        expiredDealsTextView = (LinearLayout) findViewById(R.id.expired_deals);
        serverutilities = new ServerUtilites(this);
        latestDealsArrayList = new ArrayList<String>();
        couponsTextView = (LinearLayout) findViewById(R.id.coupons);
        textViewAbout = (LinearLayout) findViewById(R.id.tv_about);
        progressBarLatestdeasl = (ProgressBar) findViewById(R.id.progressBar_latestdeasl);
        viewAllButton = (TextView) findViewById(R.id.viewall);
        navigationProfileImageView = (ImageView) findViewById(R.id.login_user_image);
        relativelayoutHomeRoot = (RelativeLayout) findViewById(R.id.rll_home_root);

        relativelayoutHomePage = (RelativeLayout) findViewById(R.id.rll_home_page);
        profile = (ImageView) findViewById(R.id.profile);
        popupLayout = (LinearLayout) findViewById(R.id.popup_layout);
        popupMenu = (RecyclerView) findViewById(R.id.popup_menu);
        tv_username = (TextView) findViewById(R.id.tv_user_name);
        tv_username.setVisibility(View.GONE);
        tv_username.setText("Sign in");
        loginUserIcons = new ArrayList<String>();
        loginUserDetailsStringArray = getResources().getStringArray(R.array.user_details);
        displayNameTextView = (TextView) findViewById(R.id.tv_browse_by);
        displayNameTextView1 = (TextView) findViewById(R.id.tv_browse_by1);
        prepareUserIcons();
        userDetailsDisplayAdapter = new UserDetailsAdapter(getApplicationContext(), loginUserIcons, new OnLogoutListener() {
            @Override
            public void onLogout() {
                logout();

            }


        });
        userRelativeViewLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        popupMenu.setLayoutManager(userRelativeViewLayoutManager);
        popupMenu.setAdapter(userDetailsDisplayAdapter);
        singleton = Singleton.getInstance();
        utilities = new Utilities(this);
        Utilities.getInstance(this);
        serverUtilites = new ServerUtilites(this);
         /*if user want to login */
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        relativelayoutSignInfb = (RelativeLayout) findViewById(R.id.rll_signin_fb);
        linearLayoutViewFooter = (LinearLayout) findViewById(R.id.popup);
        relativeLayoutPopup = (RelativeLayout) findViewById(R.id.rll_popup);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        relativeLayoutPopup.setOnClickListener(this);
        linearLayoutViewFooter.setOnClickListener(this);
        relativelayoutSignInfb.setOnClickListener(this);

        textViewAbout.setOnClickListener(this);

        profile.setOnClickListener(this);
        //CHANGE navigation icon color
        utilities.checkForUserLogin(getApplicationContext());

        if (singleton.isOnline()) {


        } else {

            utilities.ShowAlert("No Internet Connection");
        }
        listView = (RecyclerView) findViewById(R.id.drawerList);


        categoriesFragment = (RelativeLayout) findViewById(R.id.categories_fragment);
        activitySubCategory = (RelativeLayout) findViewById(R.id.activity_sub_category);
        subCategoriesList = (ListView) findViewById(R.id.sub_category_list);

        adapter = new NaviagtionAdapter(MainActivity.this, singleton.categoriesData);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(adapter);


        expiredDealsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                singleton.setDealsType("ExpiredDeals");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);
            }
        });
        couponsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                singleton.setDealsType("Coupons");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        singleton = Singleton.getInstance();

        utilities = new Utilities(this);


        //api call for hotestDeals

        Map<String, String> apiHeader = new HashMap<String, String>();
        apiHeader.put("ApiKey ", serverutilities.apiHeader);

        String hottestString = serverutilities.serverUrl + "" + serverutilities.hottestDeals;
        getHottestDeals(apiHeader, hottestString);

        //latestDeals Api


        String latestString = serverutilities.serverUrl + "" + serverutilities.latestDeals;
        getLatestDeals(apiHeader, latestString);


        navigationIconIamgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);


        latestDealsRecycleView = (RecyclerView) findViewById(R.id.latestdeals_recycleview);
        hotestDealsRecycleView = (RecyclerView) findViewById(R.id.deals_recycleView);
        hotestDealsRecycleView.setNestedScrollingEnabled(false);
        categoriesRecycleview = (RecyclerView) findViewById(R.id.categort_recyclerView);
        categoriesRecycleview.setHasFixedSize(true);
        cateroriesLinearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        subCategorory_CategoryAdapter = new SampleAdapter(MainActivity.this, "category");
        categoriesRecycleview.setLayoutManager(cateroriesLinearLayoutManager);
        categoriesRecycleview.setAdapter(subCategorory_CategoryAdapter);


        //HotestDeals recycleView
        hotestDealsRecycleView.setHasFixedSize(true);
        if (singleton.getScreenSize() > 2) {
            hotestGridLayOutManager = new GridLayoutManager(this, 3);
        } else {
            hotestGridLayOutManager = new GridLayoutManager(this, 2);
        }


        hotestDealsRecycleView.setLayoutManager(hotestGridLayOutManager);


        latestDealsRecycleView.setHasFixedSize(true);

        if (singleton.getScreenSize() > 2) {
            linearLayoutManager1 = new GridLayoutManager(MainActivity.this, 3);
        } else {
            linearLayoutManager1 = new GridLayoutManager(MainActivity.this, 2);
        }


        // use a linear layout manager
        latestDealsRecycleView.setLayoutManager(linearLayoutManager1);


        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btnLoadMore.setVisibility(View.GONE);
                count++;
                progressBarLatestdeasl.setVisibility(View.VISIBLE);
                // Starting a new async task
                Map<String, String> Header = new HashMap<String, String>();
                Header.put("ApiKey ", serverutilities.apiHeader);

                dealsReloadStringUrl = serverutilities.serverUrl + "" + serverutilities.latestDeals1 + "/" + count + "/24";

                getDealsResponse(Header, dealsReloadStringUrl);


            }
        });
        h80DealsButon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Log.d("80% deals", "--->" + selections);
                singleton.setDealsType("80%Deals");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);

            }
        });
        h70DealsButon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                singleton.setDealsType("70%Deals");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);

            }
        });
        h60DealsButon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                singleton.setDealsType("60%Deals");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);

            }
        });

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.setDealsType("Hottest");
                Intent intent = new Intent(MainActivity.this, DisplayItemsActivity.class);
                startActivity(intent);

            }
        });
        homeLinearLayout = (LinearLayout) findViewById(R.id.home);


        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    private void prepareUserIcons() {

        for (int i = 0; i < loginUserDetailsStringArray.length; i++) {
            loginUserIcons.add(loginUserDetailsStringArray[i]);
        }


    }

    public interface OnLogoutListener {
        void onLogout();
    }


    public interface SomeFBListener<T> {
        public void getFBResult(T String);

        public void getFBErrorResult(T String);


    }


    public void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (session.isLoggedIn()) {
                            if (AccessToken.getCurrentAccessToken() != null) {

                                LoginManager.getInstance().logOut();

                                Utilities.email = null;

                                tv_username.setText("Sign in");
                                relativeLayoutPopup.setVisibility(View.GONE);
                                linearLayoutViewFooter.setVisibility(View.GONE);

                                profile.setImageResource(R.drawable.profileone);
                                session.logoutUser();
                                utilities.checkForUserLogin(getApplicationContext());

                            } else {

                                String email = singleton.getLoginUserEmailId();
                                Log.d("before_logout_user", "" + email);

                                relativeLayoutPopup.setVisibility(View.GONE);
                                linearLayoutViewFooter.setVisibility(View.GONE);
                                profile.setImageResource(R.drawable.profileone);
                                session.logoutUser();
                                utilities.checkForUserLogin(getApplicationContext());

                            }
                        }
                        Toast.makeText(MainActivity.this, "logged out succesfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        tv_username.setText(singleton.getLoginUserDisplayName());
        // checkForUserLogin();


    }

    private void facebook() {

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me"));
        final LoginBehavior WEB_VIEW_ONLY;

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                if (AccessToken.getCurrentAccessToken() != null) {
                    Utilities.getInstance().RequestData(getApplicationContext(), new SomeFBListener<String>() {
                        @Override
                        public void getFBResult(String response) {
                            Log.d("after_calling_fb", response);
                            Utility.cancelProgressDialog();
                            String result = response;
                            if (result != null) {
                                profile.setImageResource(R.drawable.profile_icon_login);
                                relativeLayoutPopup.setVisibility(View.GONE);
                                linearLayoutViewFooter.setVisibility(View.GONE);
                                utilities.checkForUserLogin(getApplicationContext());

                                // displayNameTextView.setText("Hi");
                                // displayNameTextView1.setText(singleton.getLoginUserDisplayName());
                                //Picasso.with(MainActivity.this).load(singleton.getLoginImage()).into(navigationProfileImageView);
                                Log.d("y not display", "?");
                                Toast.makeText(MainActivity.this, "you have your account with dealsweb", Toast.LENGTH_SHORT).show();


                            }


                        }

                        @Override
                        public void getFBErrorResult(String String) {
                            utilities.ShowAlert(String);

                        }
                    });


                    tv_username.setText(utilities.first_name);
                }
            }

            @Override
            public void onCancel() {
                utilities.DisplayToast("Facebook got canceled");
            }

            @Override
            public void onError(FacebookException error) {
                utilities.DisplayToast("Facebook login error");
            }
        });

    }

    private void callHidePopup() {

        Utility.hidePopup(linearLayoutViewFooter, relativeLayoutPopup);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home:


                break;
            case R.id.btn_login:
                utilities.hidePopup(linearLayoutViewFooter, relativeLayoutPopup);

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                break;
            case R.id.btn_signup:
                utilities.hidePopup(linearLayoutViewFooter, relativeLayoutPopup);
                startActivity(new Intent(MainActivity.this, JoinDealswebActivity.class));
                break;
            case R.id.rll_popup:
                callHidePopup();


                break;
            case R.id.popup:
                Utility.enableDisableViewGroup(linearLayoutViewFooter, false);

                break;
            case R.id.rll_signin_fb:

                signinwithFb();
                break;
            case R.id.profile:
                callShowPopup();

                break;

            case R.id.tv_about:
                startActivity(new Intent(MainActivity.this, AboutTabLayoutActivity.class));
                Toast.makeText(getApplicationContext(), "About dealsweb will be displayed", Toast.LENGTH_SHORT).show();
                break;

        }


    }

    public void callShowPopup() {
        Utility.showPopup(relativeLayoutPopup, popupLayout, linearLayoutViewFooter, popupMenu);


    }

    private void signinwithFb() {
        if (singleton.isOnline()) {

            Utility.showProgressDialog();

            facebook();
        } else {
            Utility.ShowAlert("No Internet Connection");

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private void getSubCategoriesView() {

        iv_back = (ImageView) findViewById(R.id.iv_back_categories);
        titleSubCategory = (TextView) findViewById(R.id.title_sub_category);
        rll_subcategory_parent = (RelativeLayout) findViewById(R.id.activity_sub_category);


    }


    private void displayView(int position) {
        subCategoryDataItems = new ArrayList<String>();
        Animation slide_in_right = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_right);
        Animation slide_in_left = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_left);
        categoriesFragment.startAnimation(slide_in_left);
        activitySubCategory.startAnimation(slide_in_right);
        activitySubCategory.setVisibility(View.VISIBLE);

        categoriesFragment.setVisibility(View.GONE);


        getSubCategoriesView();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slide_in_right = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.left_to_right_one);
                Animation slide_in_left = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.left_to_right_two);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selections.clear();

                        for (int i = 0; i < subCategoryDataItems.size(); i++) {
                            selections.add("0");
                        }
                        subCategoriesAdapter = new SubCategoriesAdapter(getApplicationContext(), subCategoryDataItems, selections);
                        subCategoriesList.setAdapter(subCategoriesAdapter);
                    }
                }, 500);


                activitySubCategory.startAnimation(slide_in_right);
                categoriesFragment.startAnimation(slide_in_left);
                activitySubCategory.setVisibility(View.GONE);

                categoriesFragment.setVisibility(View.VISIBLE);


            }
        });
        String[] categoriesData = singleton.categoriesData.get(position).split("-~-");

        subCategoryDataItems.clear();
        title_subcategory = categoriesData[1];
        titleSubCategory.setText(title_subcategory);


        Log.d("selected_categorynameof", categoriesData[1]);

        for (int j = 0; j < singleton.subCategoriesData.size(); j++) {
            String[] subcategoriesData = singleton.subCategoriesData.get(j).split("-~-");

            if (categoriesData[0].equals(subcategoriesData[0])) {
                subCategoryDataItems.add(subcategoriesData[1]);
                Log.d("subcat(selectedcategory", subcategoriesData[1]);


            }


        }
        subCategoriesAdapter = new SubCategoriesAdapter(getApplicationContext(), subCategoryDataItems, selections);
        subCategoriesList.setAdapter(subCategoriesAdapter);
        subCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MainActivity.this, SubCategoryActivity.class);
                selectedSubcategoryName = subCategoryDataItems.get(position);
                utilities.DisplayToast(selectedSubcategoryName + " " + "subcategory items will be displayed");
                selections.clear();
                for (int i = 0; i < subCategoryDataItems.size(); i++) {
                    if (position == i) {

                        selections.add("1");
                        Log.d("adding1", "1");

                    } else {
                        Log.d("adding0", "0");

                        selections.add("0");
                    }

                }
                int subCategoryPosition = 0;


                subCategoriesAdapter.notifyDataSetChanged();
/*
                subCategoriesAdapter = new SubCategoriesAdapter(getApplicationContext(),subCategoryDataItems,selections);
                subCategoriesList.setAdapter(subCategoriesAdapter);
*/
                for (int j = 0; j < singleton.subCategoriesData.size(); j++) {
                    String subcategoryData[] = singleton.subCategoriesData.get(j).split("-~-");


                    if (selectedCategoryId.equals(subcategoryData[0])) {
                        if ((subcategoryData[1]).equals(selectedSubcategoryName)) {
                            selectedSubcategoryId = subcategoryData[2] + "";

                            in.putExtra("CategoryName", selectedCategoryName);

                            in.putExtra("SubCategoryategoryId", selectedSubcategoryId);
                            singleton.setSelectedCategoryId(selectedCategoryId);
                            in.putExtra("SelectedItemPosition", subCategoryPosition + "");
                            in.putExtra("selectedSubcategoryName", selectedSubcategoryName);
                            in.putExtra("ComingFrom", "NavigationDrawer");
                            startActivity(in);
                        }
                        subCategoryPosition++;

                    }

                }


            }
        });

    }


    private class NaviagtionAdapter extends RecyclerView.Adapter<NaviagtionAdapter.MyViewHolder> {

        LayoutInflater infilate;
        List<String> data_items;
        ArrayList<String> selections;
        View itemView;
        Context context;
        String categoriesData1[];

        public NaviagtionAdapter(Context context, List<String> dataitems) {

            this.selections = new ArrayList<String>();
            this.data_items = new ArrayList<String>();
            infilate = LayoutInflater.from(context);
            this.context = context;
            this.data_items = dataitems;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nav_drawer_row, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            try {
                if (selections.get(position).equals("1")) {
                    holder.nav_item_title.setTextColor(Color.parseColor("#03a9f4"));
                } else {

                    holder.nav_item_title.setTextColor(Color.parseColor("#8D8D8D"));

                }


            } catch (Exception e) {

               /* Log.d("catch", "--->" + e);*/
            }
            categoriesData1 = data_items.get(position).split("-~-");
            holder.nav_item_title.setText(categoriesData1[1]);
            holder.textPosition.setText(position + "");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] categoriesData = data_items.get(position).split("-~-");


                    selections.clear();
                    for (int i = 0; i < data_items.size(); i++) {
                        if (position == i) {

                            selections.add("1");

                        } else
                            selections.add("0");


                    }
                    selectedCategoryName = categoriesData[1];
                    selectedCategoryId = categoriesData[0];

                    Log.d("selected_category", selectedCategoryName);
                    Log.d("selectedCategoryId", selectedCategoryId);

                    displayView(position);


                    notifyDataSetChanged();


                }
            });
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {

            return data_items.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nav_item_title, textPosition;
            ImageView nav_right_arrow;


            public MyViewHolder(View itemView) {
                super(itemView);

                nav_item_title = (TextView) itemView.findViewById(R.id.nav_item_title2);
                nav_right_arrow = (ImageView) itemView.findViewById(R.id.nav_right_arrow);
                textPosition = (TextView) itemView.findViewById(R.id.txtcount);
            }
        }

    }


    public void getHottestDeals(final Map<String, String> mHeaders,
                                String url) {

       /* Log.d("hotestdeal url", "--->" + url);*/
        singleton.hottestDeals.clear();
        JSONArray jsonArray = null;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      /*  Log.d("hottestdeals response", "--->" + response);*/
                        try {

                            String jsonTotalData;
                            JSONArray jArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;

                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);
                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverutilities.myList.size(); k++) {

                                    if (jArray.getJSONObject(i).has(serverutilities.myList.get(k))) {
                                        if (k == 0) {
                                            if (jArray.getJSONObject(i).has(serverutilities.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverutilities.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverutilities.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverutilities.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }

                                }

                                singleton.hottestDeals.add(jsonTotalData);


                            }
                            displayHottestDealsApiData();

                        } catch (Exception e) {
/*
                            progressBar1.setVisibility(View.GONE);
*/
                           /* Log.d("Splash screen catch", "--->" + e);*/
                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

/*
                progressBar1.setVisibility(View.GONE);
*/
                message = serverutilities.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
              /*  Log.d("hottest error", "--->" + message);*/
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(postRequest);
    }


    public void getDealsResponse(final Map<String, String> mHeaders,
                                 String url) {



      /*  String url = ServerUtilites.serverUrl + "" + string;*/

 /*       Log.d("request url", "--->" + url);*/

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      /*  Log.d("hottestresponse", "--->" + response);*/
                        try {


                            latestDealsJArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;
                            progressBarLatestdeasl.setVisibility(View.GONE);

                            String jsonTotalData;
                            for (int j = 0; j < latestDealsJArray.length(); j++) {

                                JSONObject jsonobject = latestDealsJArray.getJSONObject(j);
                                jsonTotalData = "";
                                for (int k = 0; k < serverutilities.myList.size(); k++) {

                                    if (latestDealsJArray.getJSONObject(j).has(serverutilities.myList.get(k))) {
                                        if (k == 0) {
                                            if (latestDealsJArray.getJSONObject(j).has(serverutilities.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverutilities.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverutilities.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverutilities.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }

                                }


                                latestDealsArrayList.add(jsonTotalData);

                            }


                        } catch (Exception e) {


                     /*       Log.d("Display ItemActivity catch", "--->" + e);*/
                        }


                        // get listview current position - used to maintain scroll position
                        int firstVisiblePosition = linearLayoutManager1.findFirstVisibleItemPosition();
                        recyclerviewAdapter = new ViewRelatedItemsAdapter(MainActivity.this, latestDealsRecycleView);

                        latestDealsRecycleView.setAdapter(recyclerviewAdapter);


                        arrayCount = latestDealsJArray.length();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                message = serverutilities.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
               /* Log.d("DisplayItemActivity error", "--->" + message);*/
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(postRequest);

    }


    public void getLatestDeals(final Map<String, String> mHeaders,
                               String url) {

      /*  Log.d("latestdeals url", "--->" + url);*/
        latestDealsArrayList.clear();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* Log.d("latestdeals response", "--->" + response);*/
                        try {


                            latestDealsJArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;
                            String jsonTotalData;
                            for (int i = 0; i < latestDealsJArray.length(); i++) {

                                JSONObject jsonobject = latestDealsJArray.getJSONObject(i);
                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverutilities.myList.size(); k++) {

                                    if (latestDealsJArray.getJSONObject(i).has(serverutilities.myList.get(k))) {
                                        if (k == 0) {
                                            if (latestDealsJArray.getJSONObject(i).has(serverutilities.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverutilities.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverutilities.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverutilities.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverutilities.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverutilities.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }

                                }
                                latestDealsArrayList.add(jsonTotalData);


                            }


                        } catch (Exception e) {

                          /*  Log.d("Splash screen catch", "--->" + e);*/
                        }

                        arrayCount = latestDealsJArray.length();
                        // Getting adapter
                        recyclerviewAdapter = new ViewRelatedItemsAdapter(MainActivity.this, latestDealsRecycleView);
                        latestDealsRecycleView.setAdapter(recyclerviewAdapter);
                        latestDealsRecycleView.setNestedScrollingEnabled(false);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = serverutilities.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
              /*  Log.d("latestdeals error", "--->" + error);*/
            }
        }) {
            public Map<String, String> getHeaders() {

                return mHeaders;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(postRequest);

    }

    class ViewRelatedItemsAdapter extends RecyclerView.Adapter<ViewRelatedItemsAdapter.MyViewHolder> {

        Context context;
        View itemView;
        String latestDealsData[];
        private int lastVisibleItem, totalItemCount;

        public ViewRelatedItemsAdapter(Context context, RecyclerView recyclerView) {
            this.context = context;

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();


                recyclerView
                        .addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView,
                                                   int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                try {
                                    totalItemCount = linearLayoutManager1.getItemCount();
                                    lastVisibleItem = linearLayoutManager1
                                            .findLastVisibleItemPosition();

                                   /* if ((lastVisibleItem + 1) == latestDealsArrayList.size()) {
                                  *//*  mRecyclerView.getLayoutParams().height = 480;*//*
                                        if (arrayCount != 20) {
                                            btnLoadMore.setVisibility(View.GONE);
                                        } else {
                                            Log.d("button","---->visible");
                                            btnLoadMore.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        btnLoadMore.setVisibility(View.GONE);
                                    }*/
                                } catch (Exception e) {
                                   /* Log.d("Exception", "--->" + e);*/
                                }


                            }
                        });
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            latestDealsData = latestDealsArrayList.get(position).split("-~-");

           /* ((StudentViewHolder) holder).strikePrice.setText(stringData);*/
            holder.storeNameTextView.setText(latestDealsData[1]);
            holder.discountPriceTextView.setText("$ " + latestDealsData[2]);

            String text = "$ " + latestDealsData[3];
            String priceData = " <font color=#959595>" + text + "</font> ";

            //strike the text
            SpannableString stringData = new SpannableString(Html.fromHtml(priceData));

            stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
            holder.retailPriceTextView.setText(stringData);
            holder.percentDiscountTextView.setText(latestDealsData[4] + "% Off");
            holder.productNameTextView.setText(latestDealsData[5]);
            holder.likesCountTextView.setText(latestDealsData[7]);
            String imgUrl = serverutilities.imageLink + "" + latestDealsData[0];

            Picasso.with(context).load(imgUrl).resize(150, 150).into(holder.img);
            if (latestDealsData[7].equals("0")) {
                holder.heartImageview.setImageResource(R.drawable.hearticon);
            } else {
                holder.heartImageview.setImageResource(R.drawable.hearticon_red);
            }


            if ((latestDealsArrayList.size() - 1) == position) {
                if (arrayCount != 24) {
                    btnLoadMore.setVisibility(View.GONE);

                } else {

                    btnLoadMore.setVisibility(View.VISIBLE);
                }


            }
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(latestDealsArrayList.get(position));
                    context.startActivity(intent);

                }
            });
            holder.heartRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (session.isLoggedIn()) {

                        String categoryData[] = latestDealsArrayList.get(position).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String latestDealData = latestDealsArrayList.get(position);
                        int data = serverUtilites.ordinalIndexOf(latestDealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(latestDealData, "-~-", 8);
                        if (categoryData[7].equals("0")) {
                            latestDealsArrayList.set(position, latestDealData.substring(0, data) + "-~-1-~-" + latestDealData.substring(remainData + 3, latestDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();
                        } else {
                            latestDealsArrayList.set(position, latestDealData.substring(0, data) + "-~-0-~-" + latestDealData.substring(remainData + 3, latestDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();

                    } else {

                        if (context instanceof DisplayItemsActivity) {

                            ((DisplayItemsActivity) context).CallShowDisplayPopup();

                        } else if (context instanceof MainActivity) {
                            ((MainActivity) context).callShowPopup();
                        } else if (context instanceof SubCategoryActivity) {
                            ((SubCategoryActivity) context).callshowSubcatPopup();
                        }


                    }


                }

            });

        }

        @Override
        public int getItemCount() {
            return latestDealsArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView img, heartImageview;
            public TextView storeNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, productNameTextView, likesCountTextView;
            RelativeLayout heartRelativeLayout;

            public MyViewHolder(View v) {
                super(v);
                storeNameTextView = (TextView) v.findViewById(R.id.storename);

                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                retailPriceTextView = (TextView) v.findViewById(R.id.retail_price);
                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                percentDiscountTextView = (TextView) v.findViewById(R.id.percent_discount);
                productNameTextView = (TextView) v.findViewById(R.id.product_name);
                heartImageview = (ImageView) v.findViewById(R.id.hearImg);
                likesCountTextView = (TextView) v.findViewById(R.id.likes_count_row_list);
                heartRelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_list_row);
                img = (ImageView) v.findViewById(R.id.img);

            }
        }
    }

    @Override
    protected void onPause() {
        adapter = new NaviagtionAdapter(MainActivity.this, singleton.categoriesData);
        listView.setAdapter(adapter);
        super.onPause();
    }

    private void displayHottestDealsApiData() {


        hottestDealsArrayList.clear();
        if (singleton.getScreenSize() > 2) {
            for (int i = 0; i < 6; i++) {

                hottestDealsArrayList.add(singleton.hottestDeals.get(i));


            }
        } else {
            for (int i = 0; i < 4; i++) {

                hottestDealsArrayList.add(singleton.hottestDeals.get(i));


            }
        }

        subCategorory_CategoryAdapter = new SampleAdapter(MainActivity.this, "hotestDeals");
        hotestDealsRecycleView.setAdapter(subCategorory_CategoryAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (singleton.isReloadPage()) {
            finish();
            startActivity(getIntent());
            singleton.setReloadPage(false);
            singleton.setRefreshPage(false);

        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        boolean deleteStatus;
        try {
            db.open();
            deleteStatus = db.deleteRecord();
            db.close();
         /*   singleton.setReloadPage(true);*/
            Log.d("On destroy", "--->" + deleteStatus);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("Exception", "--->" + e);
        }
    }
}

