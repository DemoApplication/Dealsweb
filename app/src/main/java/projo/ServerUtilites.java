package projo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by user1 on 9/20/2016.
 */
public class ServerUtilites {

    public static String serverUrl ="http://www.dealsweb.com/deals/ws/";
    public static String apiHeader ="3AF8YG58-7DAC-4E2F-B4E2-E1E9A87SZ2FF";
    public static String categories_url = "http://www.dealsweb.com/deals/ws/categories";
    public static String subcategories_url = "http://www.dealsweb.com/deals/ws/subcategories";
    public String imageLink = "http://www.dealsweb.com/images/products/";
    public String hottestDeals = "Items/HDL/1/24";
    public String latestDeals = "Items/LDL/1/24";
    public String hottestDeals1 = "Items/HDL/";
    public String Register = "Register";
        public String checkLogin = "LoginCheck/";
    public String Profile = "Profile/";
    public String subScriptionsAlerts = "UserSubScrbAlerts/";
    public String likesUpadte = "Likes/";
    public String ForgotPassword = "ForgotPassword";
    public String editProfile = "ProfileInfo";
    public String changePassword="ChangePwd";
    public String contactUs="ContactUs";
    public String RemoveLikes = "RemoveLike/";
    public String latestDeals1 = "Items/LDL/";
    public String expiredDeals = "Items/EDL/";
    public String postReview = "PostReview/";
    public String reviews = "GetDealReview/";
    //descriptionData
    public String dealCouponDetails = "DealCouponDetails/";
    public String similarProducts = "SubCategoryDealsCoupons/";
    public String discountDeals = "DiscountPercentDeals/";

    public String searchIteam = "SearchItem/";
    public String subcategoryDeals = "SubCategoryDealsCoupons/";
    public String categoryDeals = "CategoryDeals/";
    public String privacy_policy = "http://www.dealsweb.com/privacy-policy.html";
    public String likesSession = "http://www.dealsweb.com/deals/ws/Likes/";
    String message;

    String dealsKeys[]={"ImageName","StoreName","DiscountedPrice","RetailPrice","PercentDiscount","ProductName","ViewsCount","LikesCount","EndDate","CategoryId","SubCategoryId","DealType","DealId","ShareUrl"};
    public List<String> myList = new ArrayList<String>(Arrays.asList(dealsKeys));

    Context context;


    public String category="categories";
    public String getThisDeal="GetThisDeal/";
    public String subCategory="subcategories";
    public ServerUtilites(Context context)
    {
        this.context=context;

    }



/*//CategoryApi
    public void categoriesRequest(final Map<String ,String> mHeader,final Context context , final String URL, final ServerCallback1 callback)
    {

      String categoryUrl=URL+category;

        //http://www.dealsweb.com/deals/ws/categories
        StringRequest postRequest = new StringRequest(Request.Method.GET, categoryUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("category response","--->"+response);
                        callback.onSuccess(response); // call call back function here

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("Volley error json object ", "Error: " + error.getMessage());
                callback.onSuccessFault(error);
            }
        }){
            public Map<String, String> getHeaders()
            {


                return mHeader;
            }
        };

        // Adding request to request queue
        Volley.newRequestQueue( context).add(postRequest);


    }*/




    public static int ordinalIndexOf(String str, String substr, int n)
    {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
        {
            pos = str.indexOf(substr, pos + 1);

        }

        return pos;
    }
    //alert Dialog
    public void ShowAlert(String alertMessage)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setMessage(alertMessage);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        alertDialog.show();
    }

    public String getVolleyError(VolleyError volleyError) {

        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        }
        else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        }

        else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        }
        else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }
}

