package com.ngagroupinc.movers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import adapters.ReviewAdapter;
import adapters.Views;
import database.DatabaseHandler;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

/**
 * Created by Userone on 12/5/2016.
 */

public class SingleItemActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoadMore;
    String message;

    LinearLayout footerLinearLayout;
    CallbackManager callbackManager;
    RelativeLayout rll_signin_fb;
    Utilities utilities;
    ImageView likes_imageviews;
    boolean like_status = false;
    RelativeLayout rll_singleitem_popup;
    LinearLayout singleitem_popup_layout;
    Button btn_single_item_login, btn_single_item_signup;
    RelativeLayout rll_home_pages;
    LinearLayout getThisDealsLayout;
    LinearLayout getThisDealLayout1;
    Bundle bundle;

    TextView descriptionTextView, storNameTextView, productNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, enddateTextView, likesTExtView, viewTextView;
    int jsonArrayListsize;
    Singleton singleton;
    String selectedItemData[];
    ServerUtilites serverUtilites;
    GridLayoutManager linearLayoutManager1;
    ViewRelatedItemsAdapter recyclerviewAdapter;
    private RecyclerView similarProductsRecycleView, reviewRecycleView;
    JSONArray similarProductsJArray;
    String dealsReloadStringUrl;
    LinearLayoutManager reviewLinearLayoutManager;
    int arrayCount, count = 1;
    TextView tootolbarTextView;

    ProgressBar progressBar_latestdeasl, progressBar_latestdeas2;
    ImageView imageView, backarrow;
    RelativeLayout shareImageView;
    ReviewAdapter reviewAdapter;
    ArrayList<String> similarProductsArrayList = new ArrayList<String>();
    ArrayList<String> reviewArrayList = new ArrayList<String>();
    RelativeLayout shareRelativeLayout, likesRelativeLayout, viewsRelativeLayout;
    EditText reviewEditText;
    Button postReviewButton;
    DatabaseHandler db;
    NestedScrollView nestedScrollView;
    Calendar cal;
    boolean edittextTouchOnce = true;
    int year_x, month_x, day_x;
    SessionManager session;
    String similarDealPosition = "";
    Views contact1;

    Button getThisDealButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /* facebook intialization */
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.single_item_layout);

        callbackManager = CallbackManager.Factory.create();
        bundle = getIntent().getExtras();
        progressBar_latestdeas2 = (ProgressBar) findViewById(R.id.progressbar_similarproducts2);
        /*toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar); */// Attaching the layout to the toolbar object
        backarrow = (ImageView) findViewById(R.id.backarrow);
        tootolbarTextView = (TextView) findViewById(R.id.tab_text);
        reviewEditText = (EditText) findViewById(R.id.edittext_review);
        getThisDealButton = (Button) findViewById(R.id.getthisdeal_button);
        postReviewButton = (Button) findViewById(R.id.post_review);
        shareImageView = (RelativeLayout) findViewById(R.id.share);
        session = new SessionManager(getApplicationContext());
        singleton = Singleton.getInstance();
        db = new DatabaseHandler(this);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);
        rll_singleitem_popup = (RelativeLayout) findViewById(R.id.rll_popup);
        footerLinearLayout = (LinearLayout) findViewById(R.id.popup);
        likes_imageviews = (ImageView) findViewById(R.id.likes_imageview);
        btn_single_item_login = (Button) findViewById(R.id.btn_login);
        btn_single_item_signup = (Button) findViewById(R.id.btn_signup);
        rll_signin_fb = (RelativeLayout) findViewById(R.id.rll_signin_fb);
        rll_home_pages = (RelativeLayout) findViewById(R.id.rll_home_pages);
        singleitem_popup_layout = (LinearLayout) findViewById(R.id.popup_layout);
        shareRelativeLayout = (RelativeLayout) findViewById(R.id.share);
        getThisDealsLayout = (LinearLayout) findViewById(R.id.get_this_deals_layout);
        getThisDealLayout1 = (LinearLayout) findViewById(R.id.get_this_deals_layout1);
        likesRelativeLayout = (RelativeLayout) findViewById(R.id.heart);
        viewsRelativeLayout = (RelativeLayout) findViewById(R.id.hearttwo);
        utilities = new Utilities(this);
        Utilities.getInstance(this);
        serverUtilites = new ServerUtilites(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        descriptionTextView = (TextView) findViewById(R.id.description_single_page);
        storNameTextView = (TextView) findViewById(R.id.store_name_singlepage);
        productNameTextView = (TextView) findViewById(R.id.product_name_singlepage);
        productNameTextView.setText("");
        discountPriceTextView = (TextView) findViewById(R.id.percent_discount);
        retailPriceTextView = (TextView) findViewById(R.id.retail_price);
        percentDiscountTextView = (TextView) findViewById(R.id.discount_price);
        enddateTextView = (TextView) findViewById(R.id.end_date_singlepage);
        similarProductsRecycleView = (RecyclerView) findViewById(R.id.singleactivity_recycle1);
        reviewRecycleView = (RecyclerView) findViewById(R.id.review_recycleView);
        likesTExtView = (TextView) findViewById(R.id.likes_singlepage);
        viewTextView = (TextView) findViewById(R.id.views_singlepage);
        btnLoadMore = (Button) findViewById(R.id.loadmore_similarproducts);
        progressBar_latestdeasl = (ProgressBar) findViewById(R.id.progressbar_similarproducts1);
        similarProductsRecycleView.setHasFixedSize(true);
        if (singleton.getScreenSize() > 2) {
            linearLayoutManager1 = new GridLayoutManager(this, 3);
        } else {
            linearLayoutManager1 = new GridLayoutManager(this, 2);
        }

        likes_imageviews.setOnClickListener(this);
        footerLinearLayout.setOnClickListener(this);
        rll_singleitem_popup.setOnClickListener(this);
        btn_single_item_login.setOnClickListener(this);
        btn_single_item_signup.setOnClickListener(this);
        rll_signin_fb.setOnClickListener(this);
        similarProductsRecycleView.setLayoutManager(linearLayoutManager1);
        //http://www.dealsweb.com/deals/ws/DealCouponDetails/117/1/true

        selectedItemData = singleton.getSingleActivityData().split("-~-");

        try {

            if (bundle.getString("Deals").equals("Expired Deals")) {
                shareRelativeLayout.setVisibility(View.GONE);
                likesRelativeLayout.setVisibility(View.GONE);
                viewsRelativeLayout.setVisibility(View.GONE);
                getThisDealsLayout.setVisibility(View.GONE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nestedScrollView
                        .getLayoutParams();


                layoutParams.setMargins(0, 0, 0, 0);
                nestedScrollView.setLayoutParams(layoutParams);
            }

        } catch (Exception e) {
        }


        //review adapter
        reviewRecycleView.setHasFixedSize(true);
        reviewLinearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        reviewEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (edittextTouchOnce) {
                    edittextTouchOnce = false;

                    if (!session.isLoggedIn()) {

                        callSingleItemPopup();
                        edittextTouchOnce = true;
                        reviewEditText.setFocusable(false);

                    } else {
                        reviewEditText.setFocusable(true);

                    }
                }

                return false;
            }
        });

        postReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reviewEditText.getText().toString().isEmpty()) {
                    Map<String, String> Header = new HashMap<String, String>();
                    Header.put("ApiKey ", serverUtilites.apiHeader);
                    dealsReloadStringUrl = serverUtilites.serverUrl + "" + serverUtilites.postReview;
                  /*  dealsReloadStringUrl = serverUtilites.serverUrl + "" + serverUtilites.postReview + "" + singleton.getLoginUserId() + "/" + selectedItemData[11] + "/" + selectedItemData[12] + "/" + reviewEditText.getText().toString();*/

                    try {
                        //pass 4 pages through body
                        getPostReviewsResponse(Header, dealsReloadStringUrl, singleton.getLoginUserId(), selectedItemData[11], selectedItemData[12], reviewEditText.getText().toString());
                    } catch (AuthFailureError authFailureError) {
                        authFailureError.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your review", Toast.LENGTH_SHORT).show();
                }

            }
        });
        getThisDealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> categoryHeader = new HashMap<String, String>();
                categoryHeader.put("ApiKey ", serverUtilites.apiHeader);

                String getDealsapi = serverUtilites.serverUrl + "" + serverUtilites.getThisDeal + selectedItemData[12] + "/" + selectedItemData[11];
                getDealsResponse(categoryHeader, getDealsapi);

                     /*   Intent intent= new Intent(SingleItemActivity.this,GetDealActivity.class);
                        intent.putExtra("DealType",selectedItemData[11]);
                        intent.putExtra("DealId",selectedItemData[12]);
                        startActivity(intent);*/
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);


        String imgUrl = serverUtilites.imageLink + "" + selectedItemData[0];

        Picasso.with(SingleItemActivity.this).load(imgUrl).into(imageView);


        storNameTextView.setText(selectedItemData[5]);
        if (selectedItemData[1].equals("null")) {
            productNameTextView.setText("");

        } else
            productNameTextView.setText(selectedItemData[1]);
        discountPriceTextView.setText(selectedItemData[4] + "% Off");
        String splitDate[] = selectedItemData[8].split("T");
        String next = " <font color='black'>Expires:</font> <font color='grey'>" + splitDate[0] + "</font>";
        enddateTextView.setText(Html.fromHtml(next), TextView.BufferType.SPANNABLE);
        likesTExtView.setText(selectedItemData[7]);

        tootolbarTextView.setText(selectedItemData[5]);

        String text = "$ " + selectedItemData[3];
        String priceData = " <font color=#959595>" + text + "</font> ";

        //strike the text
        SpannableString stringData = new SpannableString(Html.fromHtml(priceData));
        stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
        retailPriceTextView.setText(stringData);
        percentDiscountTextView.setText("$ " + selectedItemData[2]);


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();


            }
        });
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareIt();


            }
        });
        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                btnLoadMore.setVisibility(View.GONE);
                count++;
                progressBar_latestdeasl.setVisibility(View.VISIBLE);
                // Starting a new async task
                Map<String, String> Header = new HashMap<String, String>();
                Header.put("ApiKey ", serverUtilites.apiHeader);
                String similarProductsUrl = selectedItemData[9] + "/" + selectedItemData[10] + "/" + selectedItemData[11];
                dealsReloadStringUrl = serverUtilites.serverUrl + "" + serverUtilites.similarProducts + "" + similarProductsUrl + "/" + count + "/24";


                getProductsResponse(Header, dealsReloadStringUrl);

            }
        });
        if (singleton.isOnline()) {
            db.open();
           /* Log.d("view count0", "--->" + db.retriveViewDetails1(selectedItemData[12]));*/
            int viewCount = db.retriveViewDetails1(selectedItemData[12]);
            Map<String, String> Header = new HashMap<String, String>();
            Header.put("ApiKey ", serverUtilites.apiHeader);
            String reviewsString;
            if (viewCount != 1) {
                reviewsString = serverUtilites.serverUrl + "" + serverUtilites.dealCouponDetails + "" + selectedItemData[12] + "/" + selectedItemData[11] + "/true";
                contact1 = new Views(selectedItemData[12], 1);
                db.insertEmpDetails(contact1);
            } else {
                reviewsString = serverUtilites.serverUrl + "" + serverUtilites.dealCouponDetails + "" + selectedItemData[12] + "/" + selectedItemData[11] + "/false";
            }
            getDescription(Header, reviewsString);
            db.close();

            Map<String, String> apiHeader = new HashMap<String, String>();
            apiHeader.put("ApiKey ", serverUtilites.apiHeader);
            String reviewsString1 = serverUtilites.serverUrl + "" + serverUtilites.reviews + "" + selectedItemData[12] + "/" + selectedItemData[11];
            getReviews(apiHeader, reviewsString1);
            //latestDeals Api
            progressBar_latestdeas2.setVisibility(View.VISIBLE);

            String similarProductsUrl = selectedItemData[9] + "/" + selectedItemData[10] + "/" + selectedItemData[11] + "/1/24";
            String latestString = serverUtilites.serverUrl + "" + serverUtilites.similarProducts + "" + similarProductsUrl;
            getSimilarProducts(apiHeader, latestString);

        }

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
                            String BuyUrl = null, sectionName = null, ImageUrl = null;


                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                if (jArray.getJSONObject(i).has("BuyUrl")) {

                                    BuyUrl = jsonobject.getString("BuyUrl");
                                }

                            }

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(BuyUrl));
                            startActivity(i);
                        } catch (Exception e) {

                            Log.d("get this deals screen catch", "--->" + e);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                Log.d("get this deals error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);

    }

    private void shareIt() {


        Uri bmpUri = getLocalBitmapUri(imageView);
        String shareLink = "http://www.dealsweb.com" + selectedItemData[13];
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, selectedItemData[5] + "\n\n" + shareLink);
            shareIntent.setType("image/*");

            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            Log.d("share exp", "---->sharing failed, handle error");
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;


        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 0, out);

            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rll_popup:
                utilities.hidePopup(footerLinearLayout, rll_singleitem_popup);

                break;
            case R.id.popup:
                utilities.enableDisableViewGroup(rll_home_pages, false);
                break;
            case R.id.btn_login:
                utilities.hidePopup(footerLinearLayout, rll_singleitem_popup);
                startActivity(new Intent(SingleItemActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                utilities.hidePopup(footerLinearLayout, rll_singleitem_popup);
                startActivity(new Intent(SingleItemActivity.this, JoinDealswebActivity.class));


                break;
            case R.id.rll_signin_fb:
                SigninwithFb();

                break;
            case R.id.likes_imageview:

                if (!session.isLoggedIn())
                    callSingleItemPopup();
                else if (!like_status) {
                    like_status = true;
                    likes_imageviews.setImageResource(R.drawable.hearticon_red);
                } else {
                    like_status = false;
                    likes_imageviews.setImageResource(R.drawable.hearticon);

                }
                break;
        }

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

                Log.d("call fromloginmanager", "Loginmanager");
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
                                    rll_singleitem_popup.setVisibility(View.GONE);
                                    footerLinearLayout.setVisibility(View.GONE);
                                    Log.d("y not display", "?");
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

    public void callSingleItemPopup() {
        utilities.showPopup(rll_singleitem_popup, singleitem_popup_layout, footerLinearLayout, null);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // tv_username.setText(Utilities.first_name);


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
                                    lastVisibleItem = linearLayoutManager1.findLastVisibleItemPosition();
                                   /* Log.d("addOnScrollListener", "--->" + totalItemCount + "..." + (lastVisibleItem + 1) + "..." + similarProductsArrayList.size() + "..." + arrayCount);*/

                                   /* if (similarProductsArrayList.size() % 2 == 1) {

                                        if ((lastVisibleItem + 1) == (similarProductsArrayList.size() - 1)) {

                                            if (arrayCount == 19 || arrayCount == 20) {

                                                btnLoadMore.setVisibility(View.VISIBLE);
                                            } else {

                                                btnLoadMore.setVisibility(View.GONE);
                                            }

                                        } else {
                                            btnLoadMore.setVisibility(View.GONE);
                                        }
                                    } else {
                                        if ((lastVisibleItem + 1) == similarProductsArrayList.size()) {


                                            if (arrayCount == 19 || arrayCount == 20) {
                                                btnLoadMore.setVisibility(View.VISIBLE);
                                            } else {
                                                btnLoadMore.setVisibility(View.GONE);
                                            }


                                        } else {
                                            btnLoadMore.setVisibility(View.GONE);
                                        }
                                    }*/
                                } catch (Exception e) {
                                    Log.d("loadmore adapter", "--->" + e);
                                }


                            }
                        });
            }
        }


        @Override
        public ViewRelatedItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);

            return new MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final ViewRelatedItemsAdapter.MyViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            latestDealsData = similarProductsArrayList.get(position).split("-~-");
            Log.d("imagelink", "--->" + arrayCount + "..." + similarProductsArrayList.size() + "..." + position);



           /* ((StudentViewHolder) holder).strikePrice.setText(stringData);*/
            if (singleton.getScreenSize() > 2)
            {
                if (similarProductsArrayList.size() % 3 != 0) {

                    if ((similarProductsArrayList.size() - 3) == position) {

                        if (arrayCount == 24 || arrayCount == 23) {

                            btnLoadMore.setVisibility(View.VISIBLE);
                        } else {

                            btnLoadMore.setVisibility(View.GONE);
                        }

                    } else {
                        btnLoadMore.setVisibility(View.GONE);
                    }
                } else {
                    if ((similarProductsArrayList.size() - 1) == position) {


                        if (arrayCount == 23 || arrayCount == 24) {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        } else {
                            btnLoadMore.setVisibility(View.GONE);
                        }


                    } else {
                        btnLoadMore.setVisibility(View.GONE);
                    }
                }
            } else {
                if (similarProductsArrayList.size() % 2 == 1) {

                    if ((similarProductsArrayList.size() - 2) == position) {

                        if (arrayCount == 24 || arrayCount == 23) {

                            btnLoadMore.setVisibility(View.VISIBLE);
                        } else {

                            btnLoadMore.setVisibility(View.GONE);
                        }

                    } else {
                        btnLoadMore.setVisibility(View.GONE);
                    }
                } else {
                    if ((similarProductsArrayList.size() - 1) == position) {


                        if (arrayCount == 23 || arrayCount == 24) {
                            btnLoadMore.setVisibility(View.VISIBLE);
                        } else {
                            btnLoadMore.setVisibility(View.GONE);
                        }


                    } else {
                        btnLoadMore.setVisibility(View.GONE);
                    }
                }
            }

            holder.storeNameTextView.setText(latestDealsData[1]);
            holder.discountPriceTextView.setText("$ " + latestDealsData[2]);

            String text = "$ " + latestDealsData[3];
            String priceData = " <font color=#959595>" + text + "</font> ";

            //strike the text
            SpannableString stringData = new SpannableString(Html.fromHtml(priceData));

            stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
            holder.retailPriceTextView.setText(stringData);
            holder.percentDiscountTextView.setText(latestDealsData[4] + "% Off");
            if (latestDealsData[5].equals("null")) {
                holder.productNameTextView.setText("");

            } else
                holder.productNameTextView.setText(latestDealsData[5]);
            holder.likesCountTextView.setText(latestDealsData[7]);
            if (latestDealsData[7].equals("0")) {
                holder.heartImageView.setImageResource(R.drawable.hearticon);
            } else {
                holder.heartImageView.setImageResource(R.drawable.hearticon_red);
            }
            String imgUrl = serverUtilites.imageLink + "" + latestDealsData[0];

            Picasso.with(context)
                    .load(imgUrl)
                    .resize(200, 200)
                    .noFade()
                    .into(holder.img);

            holder.heartRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (session.isLoggedIn()) {
                        String similarData[] = similarProductsArrayList.get(position).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String hottestdealData = similarProductsArrayList.get(position);
                        int data = serverUtilites.ordinalIndexOf(hottestdealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(hottestdealData, "-~-", 8);

                        if (similarData[7].equals("0")) {

                            similarProductsArrayList.set(position, hottestdealData.substring(0, data) + "-~-1-~-" + hottestdealData.substring(remainData + 3, hottestdealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + similarData[12] + "/1/1/" + singleton.getLoginUserId();


                        } else {
                            similarProductsArrayList.set(position, hottestdealData.substring(0, data) + "-~-0-~-" + hottestdealData.substring(remainData + 3, hottestdealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + similarData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();

                    } else {
                        if ((context instanceof SingleItemActivity)) {


                            ((SingleItemActivity) context).callSingleItemPopup();
                        }


                    }


                }
            });


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(similarProductsArrayList.get(position));
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {

            if (singleton.getScreenSize() > 2) {


                if (similarProductsArrayList.size() % 3 != 0) {

                    if (arrayCount == 23 | arrayCount == 24) {
                        return (similarProductsArrayList.size() - 2);
                    } else {
                        return similarProductsArrayList.size();
                    }

                } else {
                    return similarProductsArrayList.size();
                }
            } else {
                if (similarProductsArrayList.size() % 2 == 1) {

                    if (arrayCount == 23 | arrayCount == 24) {
                        return (similarProductsArrayList.size() - 1);
                    } else {
                        return similarProductsArrayList.size();
                    }

                } else {
                    return similarProductsArrayList.size();
                }
            }


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView img, heartImageView;
            public TextView storeNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, productNameTextView, likesCountTextView;
            RelativeLayout heartRelativeLayout;

            public MyViewHolder(View v) {
                super(v);
                storeNameTextView = (TextView) v.findViewById(R.id.storename);
                likesCountTextView = (TextView) v.findViewById(R.id.likes_count_row_list);
                heartRelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_list_row);
                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                retailPriceTextView = (TextView) v.findViewById(R.id.retail_price);
                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                percentDiscountTextView = (TextView) v.findViewById(R.id.percent_discount);
                productNameTextView = (TextView) v.findViewById(R.id.product_name);
                heartImageView = (ImageView) v.findViewById(R.id.hearImg);


                img = (ImageView) v.findViewById(R.id.img);

            }
        }
    }

    public void getSimilarProducts(final Map<String, String> mHeaders,
                                   String url) {


        similarProductsArrayList.clear();
        Log.d("similarProducts response", "--->" + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("similarProducts response", "--->" + response);
                        try {


                            similarProductsJArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;

                            String jsonTotalData;
                            for (int i = 0; i < similarProductsJArray.length(); i++) {

                                JSONObject jsonobject = similarProductsJArray.getJSONObject(i);
                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverUtilites.myList.size(); k++) {

                                    if (similarProductsJArray.getJSONObject(i).has(serverUtilites.myList.get(k))) {
                                        if (k == 0) {
                                            if (similarProductsJArray.getJSONObject(i).has(serverUtilites.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverUtilites.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverUtilites.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverUtilites.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverUtilites.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverUtilites.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverUtilites.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {
                                            if (selectedItemData[12].equals(jsonobject.getString(serverUtilites.myList.get(k))) && k == 12) {
                                                similarDealPosition = i + "";
                                            }

                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverUtilites.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }

                                }

                                if (!similarDealPosition.equals(i + "")) {

                                    similarProductsArrayList.add(jsonTotalData);
                                }

                            }


                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }
                        progressBar_latestdeas2.setVisibility(View.GONE);

                        arrayCount = similarProductsArrayList.size();


                        // Getting adapter
                        recyclerviewAdapter = new ViewRelatedItemsAdapter(SingleItemActivity.this, similarProductsRecycleView);
                        similarProductsRecycleView.setAdapter(recyclerviewAdapter);
                        similarProductsRecycleView.setNestedScrollingEnabled(false);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar_latestdeas2.setVisibility(View.GONE);
                message = serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                Log.d("error", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);

    }

    public void getReviews(final Map<String, String> mHeaders, String url) {


        Log.d("reviews request url", "--->" + url);
        reviewArrayList.clear();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" reviews response", "--->" + response);
                        try {

                            String ReviewText = null, DateCreated = null, Email = null, ImagePath = null, DisplayReviewName = null;
                            String ApproveImage = null, DisplayName = null;
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("ReviewText")) {

                                    ReviewText = jsonobject.getString("ReviewText");
                                }
                                if (jArray.getJSONObject(i).has("DateCreated")) {
                                    DateCreated = jsonobject.getString("DateCreated");
                                }
                                if (jArray.getJSONObject(i).has("DisplayName")) {
                                    DisplayName = jsonobject.getString("DisplayName");
                                }
                                if (jArray.getJSONObject(i).has("Email")) {

                                    Email = jsonobject.getString("Email");
                                }
                                if (jArray.getJSONObject(i).has("ImagePath")) {
                                    ImagePath = jsonobject.getString("ImagePath");
                                }
                                if (jArray.getJSONObject(i).has("ApproveImage")) {

                                    ApproveImage = jsonobject.getString("ApproveImage");
                                }
                                String imgeUrl = "http://www.dealsweb.com" + ImagePath;
                                if (DisplayName.equals("")) {
                                    DisplayReviewName = Email;
                                } else {
                                    DisplayReviewName = DisplayName;
                                }
                                reviewArrayList.add(ReviewText + "-~-" + DateCreated + "-~-" + DisplayReviewName + "-~-" + imgeUrl + "-~-" + ApproveImage);
                            }

                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }
                        reviewRecycleView.setLayoutManager(reviewLinearLayoutManager);
                        reviewAdapter = new ReviewAdapter(SingleItemActivity.this, reviewArrayList);
                        reviewRecycleView.setAdapter(reviewAdapter);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                message = serverUtilites.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);


    }

    public void getDescription(final Map<String, String> mHeaders, String url) {


        Log.d("description request url", "--->" + url);


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" description response", "--->" + response);
                        try {

                            String Description = null, ViewsCount = null, ShowExpiredDate = null, StoreName = null;

                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonobject = jArray.getJSONObject(i);


                                //check the condition key exists in jsonResponse
                                if (jArray.getJSONObject(i).has("Description")) {

                                    Description = jsonobject.getString("Description");
                                }
                                if (jArray.getJSONObject(i).has("ViewsCnt")) {
                                    if (!jsonobject.getString("ViewsCnt").equals("null")) {
                                        ViewsCount = jsonobject.getString("ViewsCnt");
                                    } else {
                                        ViewsCount = "0";
                                    }
                                }
                                if (jArray.getJSONObject(i).has("ShowExpiredDate")) {

                                    ShowExpiredDate = jsonobject.getString("ShowExpiredDate");
                                }
                                if (jArray.getJSONObject(i).has("StoreName")) {

                                    StoreName = jsonobject.getString("StoreName");
                                    Log.d("store_name(single)", StoreName);
                                }
                                try {
                                    if (bundle.getString("Deals").equals("UserLikes")) {
                                        productNameTextView.setText(StoreName);
                                    }
                                } catch (Exception e) {

                                }

                                viewTextView.setText(ViewsCount + "");

                                if (ShowExpiredDate.equals("false")) {


                                    getThisDealLayout1.setVisibility(View.GONE);

                                    getThisDealButton.setVisibility(View.VISIBLE);

                                } else {
                                    getThisDealLayout1.setVisibility(View.VISIBLE);
                                    getThisDealButton.setVisibility(View.GONE);
                                }
                            }


                            String descriptionText = Html.fromHtml(Description, null, new UlTagHandler()).toString();
                            /*descriptionText = descriptionText.replace("\n", "\n .");*/
                            descriptionTextView.setText(descriptionText);
                        } catch (Exception e) {

                            Log.d("Splash screen catch", "--->" + e);
                        }
                        reviewRecycleView.setLayoutManager(reviewLinearLayoutManager);
                        reviewAdapter = new ReviewAdapter(SingleItemActivity.this, reviewArrayList);
                        reviewRecycleView.setAdapter(reviewAdapter);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                message = serverUtilites.getVolleyError(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (singleton.isReloadPage()) {
            finish();

            startActivity(getIntent());
            singleton.setRefreshPage(true);
            singleton.setReloadPage(false);

        }
    }

    public void getProductsResponse(final Map<String, String> mHeaders,
                                    String url) {



      /*  String url = ServerUtilites.serverUrl + "" + string;*/

        Log.d("request url", "--->" + url);

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("hottestresponse", "--->" + response);
                        try {
                            similarDealPosition = "";

                            similarProductsJArray = new JSONArray(response);
                            String ImageName = null, ViewsCount = null, LikesCount = null;

                            String jsonTotalData;
                            for (int j = 0; j < similarProductsJArray.length(); j++) {

                                JSONObject jsonobject = similarProductsJArray.getJSONObject(j);

                                jsonTotalData = "";

                                //check the condition key exists in jsonResponse
                                for (int k = 0; k < serverUtilites.myList.size(); k++) {

                                    if (similarProductsJArray.getJSONObject(j).has(serverUtilites.myList.get(k))) {
                                        if (k == 0) {
                                            if (similarProductsJArray.getJSONObject(j).has(serverUtilites.myList.get(k))) {
                                                ImageName = jsonobject.getString(serverUtilites.myList.get(k));
                                            }
                                            String imageLink = jsonobject.getString(serverUtilites.myList.get(9)) + "/" + ImageName;
                                            jsonTotalData = jsonTotalData + imageLink + "-~-";

                                        } else if (k == 6) {
                                            if (!jsonobject.getString(serverUtilites.myList.get(k)).equals("null")) {
                                                ViewsCount = jsonobject.getString(serverUtilites.myList.get(k));
                                            } else {
                                                ViewsCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + ViewsCount + "-~-";
                                        } else if (k == 7) {
                                            if (!jsonobject.getString(serverUtilites.myList.get(k)).equals("null")) {
                                                LikesCount = jsonobject.getString(serverUtilites.myList.get(k));
                                            } else {
                                                LikesCount = "0";
                                            }
                                            jsonTotalData = jsonTotalData + LikesCount + "-~-";
                                        } else {

                                            if (selectedItemData[12].equals(jsonobject.getString(serverUtilites.myList.get(k))) && k == 12) {
                                                similarDealPosition = j + "";
                                            }

                                            jsonTotalData = jsonTotalData + jsonobject.getString(serverUtilites.myList.get(k)) + "-~-";
                                        }

                                    } else {
                                        jsonTotalData = jsonTotalData + "-~-";
                                    }
                                }

                                if (!similarDealPosition.equals(j + "")) {

                                    similarProductsArrayList.add(jsonTotalData);

                                }

                            }


                        } catch (Exception e) {


                            Log.d("Display ItemActivity catch", "--->" + e);
                        }

                        progressBar_latestdeasl.setVisibility(View.GONE);

                        recyclerviewAdapter = new ViewRelatedItemsAdapter(SingleItemActivity.this, similarProductsRecycleView);

                        similarProductsRecycleView.setAdapter(recyclerviewAdapter);

                      /*  int count1 = (count - 1) * 16 + 6;
                        similarProductsRecycleView.scrollToPosition(count1);*/

                        arrayCount = similarProductsJArray.length();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("DisplayItemActivity error", "--->" + message);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);

    }

    public void getPostReviewsResponse(final Map<String, String> mHeaders,
                                       String url, String loginId, String DealType, String DealId, String reviewText) throws AuthFailureError, JSONException {


        Log.d("post request url", "--->" + url);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("LoginId", loginId);
        jsonBody.put("DealType", DealType);
        jsonBody.put("DealId", DealId);
        jsonBody.put("ReviewText", reviewText);
        final String mRequestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("post response", "--->" + response);
                        try {
                            if (response.equals("\"Y\"")) {
                                String approvedImageStatus;
                                if (singleton.getLoginImage() != null) {
                                    approvedImageStatus = "1";
                                } else {
                                    approvedImageStatus = "0";
                                }

                                reviewArrayList.add(reviewEditText.getText().toString() + "-~-" + day_x + "-" + (month_x + 1) + "-" + year_x + "-~-" + singleton.getLoginUserEmailId() + "-~-" + singleton.getLoginImage() + "-~-" + approvedImageStatus);
                                reviewAdapter = new ReviewAdapter(SingleItemActivity.this, reviewArrayList);
                                reviewRecycleView.setAdapter(reviewAdapter);
                                reviewEditText.setText("");
                            }


                        } catch (Exception e) {


                            Log.d("Display ItemActivity catch", "--->" + e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                message = serverUtilites.getVolleyError(error);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("review post error", "--->" + message);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                return mRequestBody.getBytes();
            }

            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(SingleItemActivity.this).add(postRequest);

    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    public class UlTagHandler implements Html.TagHandler {


        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equals("ul") && !opening) output.append("");
            if (tag.equals("li") && opening) output.append("\n\t•\t");
        }
    }

    @Override
    public void onBackPressed() {

        if (singleton.isRefreshPage()) {
            singleton.setReloadPage(true);
        }
        super.onBackPressed();
    }
}
