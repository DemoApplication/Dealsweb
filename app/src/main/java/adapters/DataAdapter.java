package adapters;

/**
 * Created by Userone on 11/11/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngagroupinc.movers.DisplayItemsActivity;
import com.ngagroupinc.movers.MainActivity;
import com.ngagroupinc.movers.R;
import com.ngagroupinc.movers.SearchActivity;
import com.ngagroupinc.movers.SingleItemActivity;
import com.ngagroupinc.movers.SubCategoryActivity;
import com.ngagroupinc.movers.UserLikesSectionActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import Interface.LikesRemoveListener;
import Interface.OnLoadMoreListener;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

public class DataAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static List<String> hottestDealsList;
    ServerUtilites serverUtilites;
    View v;
    String hottestDealsData[];
    ProgressBar progressBar;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 3;
    SessionManager session;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    static Singleton singleton;
    ArrayList<String> selections;
    public String loginUserId;
    private OnLoadMoreListener onLoadMoreListener;
    private LikesRemoveListener likesRemoveListener;
    static Context context;
    String className;
    boolean likes_status = false;
    Utilities utilities;
    RecyclerView recycleview;
    private String likesRemoveResponse;

    public DataAdapter(List<String> hottestDealsList, RecyclerView recyclerView, final Context context, String className) {
        this.hottestDealsList = hottestDealsList;
        this.context = context;
        this.className = className;
        this.recycleview = recyclerView;
        utilities = new Utilities(context);
        selections = new ArrayList<String>(hottestDealsList.size());
        session = new SessionManager(context);
        serverUtilites = new ServerUtilites(context);
        singleton = Singleton.getInstance();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();



                           /* Log.d("addOnScrollListener", "--->" + totalItemCount + "..." + lastVisibleItem + "..." + !loading + "..." + (totalItemCount <= (lastVisibleItem + visibleThreshold)) + "..." + (onLoadMoreListener != null));*/
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {


                                // End has been reached
                                // Do something

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }


                        }


                    });


        }
    }

    @Override
    public int getItemViewType(int position) {
        return hottestDealsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_row, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        if (holder instanceof StudentViewHolder) {

            hottestDealsData = hottestDealsList.get(position).split("-~-");

           /* ((StudentViewHolder) holder).strikePrice.setText(stringData);*/
            if (className.equals("UserLikesSectionActivity")) {
                ((StudentViewHolder) holder).storeNameTextView.setVisibility(View.GONE);
                ((StudentViewHolder) holder).heartRelativeLayout.setVisibility(View.GONE);
                ((StudentViewHolder) holder).heartUnlikeRelativelayout.setVisibility(View.VISIBLE);
            }
            ((StudentViewHolder) holder).storeNameTextView.setText(hottestDealsData[1]);
            ((StudentViewHolder) holder).discountPriceTextView.setText("$ " + hottestDealsData[2]);

            String text = "$ " + hottestDealsData[3];
            String priceData = " <font color=#959595>" + text + "</font> ";

            //strike the text
            SpannableString stringData = new SpannableString(Html.fromHtml(priceData));

            stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
            ((StudentViewHolder) holder).retailPriceTextView.setText(stringData);
            ((StudentViewHolder) holder).percentDiscountTextView.setText(hottestDealsData[4] + "% Off");
            ((StudentViewHolder) holder).productNameTextView.setText(hottestDealsData[5]);
            if (context instanceof DisplayItemsActivity) {
                if (DisplayItemsActivity.jsonArrayListLength != 24 && position == (hottestDealsList.size()) - 1) {
                    utilities.DisplayToast("No more deals to load");
                }
                if (DisplayItemsActivity.titleText.equals("Expired Deals")) {
                    ((StudentViewHolder) holder).heartRelativeLayout.setVisibility(View.GONE);
                } else {
                    ((StudentViewHolder) holder).heartRelativeLayout.setVisibility(View.VISIBLE);
                }

            } else {
                if (SearchActivity.jsonArrayListLength != 24 && position == (hottestDealsList.size()) - 1) {
                    utilities.DisplayToast("No more deals to load");
                }
            }



          /*  if(SearchActivity.jsonArrayListLength!=20 && position==(hottestDealsList.size())-1)
            {
                utilities.DisplayToast("No more deals to load");
            }*/
            ((StudentViewHolder) holder).likesCountTextView.setText(hottestDealsData[7]);
            ((StudentViewHolder) holder).UnlikesCountTextView.setText(hottestDealsData[7]);


            String imgUrl = serverUtilites.imageLink + "" + hottestDealsData[0];

            Picasso.with(context).load(imgUrl)
                    .noFade().into(((StudentViewHolder) holder).img);
            if (hottestDealsData[7].equals("0")) {
                ((StudentViewHolder) holder).heartImgImageView.setImageResource(R.drawable.hearticon);
            } else {
                ((StudentViewHolder) holder).heartImgImageView.setImageResource(R.drawable.hearticon_red);
            }
            if (position == 0) {


                if (context instanceof DisplayItemsActivity) {


                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.VISIBLE);
                    ((StudentViewHolder) holder).riskTextView.setText(DisplayItemsActivity.titleText);
                } else {
                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.GONE);
                }


            } else if (position == 1) {

                if (context instanceof DisplayItemsActivity) {
                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.VISIBLE);
                    ((StudentViewHolder) holder).riskTextView.setText("");
                }


            } else {
                if(singleton.getScreenSize()>2 && ((position == 2) )){
                    if (context instanceof DisplayItemsActivity) {
                        ((StudentViewHolder) holder).riskTextView.setVisibility(View.VISIBLE);
                        ((StudentViewHolder) holder).riskTextView.setText("");
                    }
                }
                else{
                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.GONE);
                }

            }

            ((StudentViewHolder) holder).img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("get data", "---->" + DisplayItemsActivity.titleText);

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    if (context instanceof DisplayItemsActivity) {
                        intent.putExtra("Deals", DisplayItemsActivity.titleText);
                    }
                    if (className.equals("UserLikesSectionActivity")) {
                        intent.putExtra("Deals", "UserLikes");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    }

                    singleton.setSingleActivityData(hottestDealsList.get(position));

                    context.startActivity(intent);

                }
            });
            ((StudentViewHolder) holder).heartRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (session.isLoggedIn()) {

                        String categoryData[] = hottestDealsList.get(position).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String hottestdealData = hottestDealsList.get(position);
                        int data = serverUtilites.ordinalIndexOf(hottestdealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(hottestdealData, "-~-", 8
                        );

                        if (categoryData[7].equals("0")) {

                            hottestDealsList.set(position, hottestdealData.substring(0, data) + "-~-1-~-" + hottestdealData.substring(remainData + 3, hottestdealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();


                        } else {
                            hottestDealsList.set(position, hottestdealData.substring(0, data) + "-~-0-~-" + hottestdealData.substring(remainData + 3, hottestdealData.length()));
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
                        } else if (context instanceof SearchActivity) {
                            ((SearchActivity) context).callshowSearchPopup();
                        }


                    }


                }

            });
            ((StudentViewHolder) holder).heartImgUnlikeImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLikesData(position,((StudentViewHolder) holder).cardView);

                }
            });

            ((StudentViewHolder) holder).crossUnlikeImgImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    callLikesData(position,((StudentViewHolder) holder).cardView);

                }
            });

        } else {


            ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
            if (context instanceof DisplayItemsActivity) {
                DisplayItemsActivity.progressbarEndless.setVisibility(View.VISIBLE);
            }

            if (className.equals("UserLikesSectionActivity")) {
                UserLikesSectionActivity.progressBar_UserLikesSection.setVisibility(View.VISIBLE);
            }
            if (context instanceof SearchActivity) {
                SearchActivity.progressbarEndless.setVisibility(View.VISIBLE);
            }

        }

    }

    private void callLikesData(final int position, final CardView  cardview ) {
        String likesDealData[] = hottestDealsList.get(position).split("-~-");
        //String loginuserid = singleton.getLoginUserId();
        String LikeDislikeType = "2";
        String LikeDislikeId = likesDealData[13];
        String likesAlertMessage = "would you like to remove this deal item from your likes section";
        utilities.ShowLikesAlert(likesAlertMessage, LikeDislikeId, LikeDislikeType, new LikesRemoveListener<String>() {
            @Override
            public void getLikesRemoveResult(String response) {
                Log.d("afterlikesremovere", " " + response);
                likesRemoveResponse = response;
                cardview.setVisibility(View.GONE);
                hottestDealsList.remove(position);

                notifyDataSetChanged();
            }
        });
    }

    public void setLoaded() {

        loading = false;
    }

    @Override
    public int getItemCount() {

        return hottestDealsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLikesRemoveListener(LikesRemoveListener likesRemoveListener) {
        this.likesRemoveListener = likesRemoveListener;
    }

    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView storeNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, productNameTextView;

        public TextView riskTextView, discountTextView, likesCountTextView, UnlikesCountTextView;
        public ImageView img, heartImgImageView, heartImgUnlikeImageView, crossUnlikeImgImageView;
        RelativeLayout heartRelativeLayout, heartUnlikeRelativelayout;
        CardView cardView;

        public StudentViewHolder(View v) {
            super(v);
            storeNameTextView = (TextView) v.findViewById(R.id.storename);
            discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
            retailPriceTextView = (TextView) v.findViewById(R.id.retail_price);
            discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
            percentDiscountTextView = (TextView) v.findViewById(R.id.percent_discount);
            productNameTextView = (TextView) v.findViewById(R.id.product_name);
            heartImgImageView = (ImageView) v.findViewById(R.id.hearImg);
            riskTextView = (TextView) v.findViewById(R.id.risktext);
            img = (ImageView) v.findViewById(R.id.img);
            likesCountTextView = (TextView) v.findViewById(R.id.likes_count_row_list);
            heartRelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_list_row);
            heartUnlikeRelativelayout = (RelativeLayout) v.findViewById(R.id.heart_list_unlike);
            heartImgUnlikeImageView = (ImageView) v.findViewById(R.id.hearImg_unlike);
            crossUnlikeImgImageView = (ImageView) v.findViewById(R.id.cross_unlike_image);
            UnlikesCountTextView = (TextView) v.findViewById(R.id.likes_count_row_list_unlike);
            cardView = (CardView) v.findViewById(R.id.card_view);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar123);
        }
    }

    public void getLikesUpdateData(final Map<String, String> mHeaders, String url, final int position) {
        Log.d("hotest update  request url", "--->" + url);


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" hotest update response", "--->" + response);

                        if (!response.equals("1")) {
                            Toast.makeText(context, "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();


                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Sorry! Server could not be reached.", Toast.LENGTH_LONG).show();
                Log.d("sub categoryerror", "--->" + error);
            }
        }) {
            public Map<String, String> getHeaders() {


                return mHeaders;
            }
        };

        Volley.newRequestQueue(context).add(postRequest);


    }
}