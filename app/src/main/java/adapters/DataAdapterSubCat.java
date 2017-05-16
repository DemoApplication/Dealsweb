package adapters;

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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ngagroupinc.movers.DisplayItemsActivity;
import com.ngagroupinc.movers.R;
import com.ngagroupinc.movers.SingleItemActivity;
import com.ngagroupinc.movers.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import Interface.OnLoadMoreListener;
import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;
import projo.Utilities;

/**
 * Created by Userone on 1/12/2017.
 */

public class DataAdapterSubCat extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static List<String> SubCategoriesDealsList;
    ServerUtilites serverUtilites;
    View v;
    SessionManager session;
    String hottestDealsData[], hottestDealsData1[], hottestDealsData2[];
    ProgressBar progressBar;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    public String loginUserId;
    static Singleton singleton;
    private OnLoadMoreListener onLoadMoreListener;
    static Context context;
    String className;
    Utilities utilities;
    boolean toastStatus;
    int thirdPosition;


    public DataAdapterSubCat(List<String> SubCategoriesDealsList, RecyclerView recyclerView, final Context context, String className) {
        this.SubCategoriesDealsList = SubCategoriesDealsList;
        this.context = context;
        this.className = className;
        toastStatus = true;
        session = new SessionManager(context);
        utilities = new Utilities(context);
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
        return SubCategoriesDealsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
           /* if(singleton.getScreenSize()>2)
            {
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_row_subcat_largescreen, parent, false);

            }
          else{*/
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_row_subcat, parent, false);
         /*   }*/

            vh = new StudentViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder1(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (holder instanceof StudentViewHolder) {
            final int firstPosition;
            final int secondPosition;

            if (singleton.getScreenSize() > 2) {
                ((StudentViewHolder) holder).linearLayoputMain.setWeightSum(3);
                ((StudentViewHolder) holder).thirdCardViewLargeScreen.setVisibility(View.VISIBLE);
                firstPosition = (position * 3)+1;
                secondPosition = (position * 3) + 2;
                thirdPosition = (position * 3);


            } else {
                firstPosition = (position * 2);
                secondPosition = (position * 2) + 1;
                ((StudentViewHolder) holder).linearLayoputMain.setWeightSum(2);


            }
            if (singleton.getScreenSize() > 2) {
                if (SubCategoriesDealsList.size() % 3 == 2) {
                    if (SubCategoryActivity.jsonArrayCount != 24 && position == (SubCategoriesDealsList.size() / 3)) {

                        utilities.DisplayToast("No more deals to load");

                    }
                }
                else if(SubCategoriesDealsList.size() % 3 == 1) {
                    if (SubCategoryActivity.jsonArrayCount != 24 && position == (SubCategoriesDealsList.size() / 3)-1) {

                        utilities.DisplayToast("No more deals to load");

                    }
                }
                else {
                    if (SubCategoryActivity.jsonArrayCount != 24 && position == (SubCategoriesDealsList.size() / 3) - 2) {

                        utilities.DisplayToast("No more deals to load");

                    }
                }
            }
            else {
                if (SubCategoriesDealsList.size() % 2 == 1) {
                    if (SubCategoryActivity.jsonArrayCount != 24 && position == (SubCategoriesDealsList.size() / 2)) {

                        utilities.DisplayToast("No more deals to load");

                    }
                } else {
                    if (SubCategoryActivity.jsonArrayCount != 24 && position == (SubCategoriesDealsList.size() / 2) - 1) {

                        utilities.DisplayToast("No more deals to load");

                    }
                }
            }
            if (position == 0) {

                if (className.equals("SubCategoryActivity")) {
                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.VISIBLE);


                    ((StudentViewHolder) holder).riskTextView.setText(SubCategoryActivity.subCategoryName);
                } else {
                    ((StudentViewHolder) holder).riskTextView.setVisibility(View.GONE);
                }


            } else {
                ((StudentViewHolder) holder).riskTextView.setVisibility(View.GONE);
            }


          /*  Log.d("imagelink", "--->" + secondPosition + "..." + SubCategoriesDealsList.size() + "..." + position);*/
            try {
                hottestDealsData = SubCategoriesDealsList.get(firstPosition).split("-~-");
                ((StudentViewHolder) holder).storeName1TextView.setText(hottestDealsData[1]);
                ((StudentViewHolder) holder).discountPrice1TextView.setText("$ " + hottestDealsData[2]);

                String text = "$ " + hottestDealsData[3];
                String priceData = " <font color=#959595>" + text + "</font> ";

                //strike the text
                SpannableString stringData = new SpannableString(Html.fromHtml(priceData));

                stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
                ((StudentViewHolder) holder).retailPrice1TextView.setText(stringData);
                ((StudentViewHolder) holder).percentDiscount1TextView.setText(hottestDealsData[4] + "% Off");
                ((StudentViewHolder) holder).productName1TextView.setText(hottestDealsData[5]);
                ((StudentViewHolder) holder).likesCount1TextView.setText(hottestDealsData[7]);
                if (hottestDealsData[7].equals("0")) {
                    ((StudentViewHolder) holder).hearImg1Image1View.setImageResource(R.drawable.hearticon);
                } else {
                    ((StudentViewHolder) holder).hearImg1Image1View.setImageResource(R.drawable.hearticon_red);

                }

                String imgUrl = serverUtilites.imageLink + "" + hottestDealsData[0];

                Picasso.with(context).load(imgUrl)
                        .noFade().into(((StudentViewHolder) holder).img1);
            } catch (Exception e) {
                ((StudentViewHolder) holder).firstLinearLayout.setVisibility(View.GONE);

            }
//1
           /* ((StudentViewHolder) holder).strikePrice.setText(stringData);*/


            ((StudentViewHolder) holder).img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(SubCategoriesDealsList.get(firstPosition));
                    context.startActivity(intent);

                }
            });
            ((StudentViewHolder) holder).img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(SubCategoriesDealsList.get(secondPosition));
                    context.startActivity(intent);

                }
            });
            ((StudentViewHolder) holder).img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(SubCategoriesDealsList.get(thirdPosition));
                    context.startActivity(intent);

                }
            });

            try {

                hottestDealsData1 = SubCategoriesDealsList.get(secondPosition).split("-~-");
//2

                ((StudentViewHolder) holder).storeName2TextView.setText(hottestDealsData1[1]);
                ((StudentViewHolder) holder).discountPrice2TextView.setText("$ " + hottestDealsData1[2]);

                String text2 = "$ " + hottestDealsData1[3];
                String priceData2 = " <font color=#959595>" + text2 + "</font> ";

                //strike the text
                SpannableString stringData2 = new SpannableString(Html.fromHtml(priceData2));

                stringData2.setSpan(new StrikethroughSpan(), 0, stringData2.length(), 0);
                ((StudentViewHolder) holder).retailPrice2TextView.setText(stringData2);
                ((StudentViewHolder) holder).percentDiscount2TextView.setText(hottestDealsData1[4] + "% Off");
                ((StudentViewHolder) holder).productName2TextView.setText(hottestDealsData1[5]);
                ((StudentViewHolder) holder).likesCount2TextView.setText(hottestDealsData1[7]);
                if (hottestDealsData1[7].equals("0")) {
                    ((StudentViewHolder) holder).hearImg1Image2View.setImageResource(R.drawable.hearticon);
                } else {
                    ((StudentViewHolder) holder).hearImg1Image2View.setImageResource(R.drawable.hearticon_red);

                }
                String imgUrl1 = serverUtilites.imageLink + "" + hottestDealsData1[0];

                Picasso.with(context).load(imgUrl1)
                        .noFade().into(((StudentViewHolder) holder).img2);

            } catch (Exception e) {

                ((StudentViewHolder) holder).secondLinearLayout.setVisibility(View.GONE);
            }
            try {

                hottestDealsData2 = SubCategoriesDealsList.get(thirdPosition).split("-~-");
//2

                ((StudentViewHolder) holder).storeName3TextView.setText(hottestDealsData1[1]);
                ((StudentViewHolder) holder).discountPrice3TextView.setText("$ " + hottestDealsData2[2]);

                String text2 = "$ " + hottestDealsData2[3];
                String priceData2 = " <font color=#959595>" + text2 + "</font> ";

                //strike the text
                SpannableString stringData2 = new SpannableString(Html.fromHtml(priceData2));

                stringData2.setSpan(new StrikethroughSpan(), 0, stringData2.length(), 0);
                ((StudentViewHolder) holder).retailPrice3TextView.setText(stringData2);
                ((StudentViewHolder) holder).percentDiscount3TextView.setText(hottestDealsData2[4] + "% Off");
                ((StudentViewHolder) holder).productName3TextView.setText(hottestDealsData2[5]);
                ((StudentViewHolder) holder).likesCount3TextView.setText(hottestDealsData2[7]);
                if (hottestDealsData2[7].equals("0")) {
                    ((StudentViewHolder) holder).hearImg1Image3View.setImageResource(R.drawable.hearticon);
                } else {
                    ((StudentViewHolder) holder).hearImg1Image3View.setImageResource(R.drawable.hearticon_red);

                }
                String imgUrl1 = serverUtilites.imageLink + "" + hottestDealsData2[0];

                Picasso.with(context).load(imgUrl1)
                        .noFade().into(((StudentViewHolder) holder).img3);

            } catch (Exception e) {

                ((StudentViewHolder) holder).thirdLinearLayout.setVisibility(View.GONE);
            }
            ((StudentViewHolder) holder).heart1RelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loginUserId = singleton.getLoginUserId();
                    if (session.isLoggedIn()) {
                        String categoryData[] = SubCategoriesDealsList.get(firstPosition).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String subcatDealData = SubCategoriesDealsList.get(firstPosition);
                        int data = ServerUtilites.ordinalIndexOf(subcatDealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(subcatDealData, "-~-", 8);
                        if (categoryData[7].equals("0")) {


                            SubCategoriesDealsList.set(firstPosition, subcatDealData.substring(0, data) + "-~-1-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();
                        } else {
                            SubCategoriesDealsList.set(firstPosition, subcatDealData.substring(0, data) + "-~-0-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();
                    } else {
                        if (context instanceof SubCategoryActivity) {
                            ((SubCategoryActivity) context).callshowSubcatPopup();
                            SubCategoryActivity.categories_recyclerView.setVisibility(View.GONE);

                        }
                    }
                }
            });

            ((StudentViewHolder) holder).heart2RelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loginUserId = singleton.getLoginUserId();
                    if (session.isLoggedIn()) {
                        String categoryData[] = SubCategoriesDealsList.get(secondPosition).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String subcatDealData = SubCategoriesDealsList.get(secondPosition);
                        int data = ServerUtilites.ordinalIndexOf(subcatDealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(subcatDealData, "-~-", 8);
                        if (categoryData[7].equals("0")) {
                            SubCategoriesDealsList.set((secondPosition), subcatDealData.substring(0, data) + "-~-1-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();
                        } else {
                            SubCategoriesDealsList.set((secondPosition), subcatDealData.substring(0, data) + "-~-0-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();
                    } else {
                        if (context instanceof SubCategoryActivity) {
                            ((SubCategoryActivity) context).callshowSubcatPopup();
                            SubCategoryActivity.categories_recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            });

            ((StudentViewHolder) holder).heart3RelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loginUserId = singleton.getLoginUserId();
                    if (session.isLoggedIn()) {
                        String categoryData[] = SubCategoriesDealsList.get(thirdPosition).split("-~-");

                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String subcatDealData = SubCategoriesDealsList.get(thirdPosition);
                        int data = ServerUtilites.ordinalIndexOf(subcatDealData, "-~-", 7);
                        int remainData = serverUtilites.ordinalIndexOf(subcatDealData, "-~-", 8);
                        if (categoryData[7].equals("0")) {


                            SubCategoriesDealsList.set(thirdPosition, subcatDealData.substring(0, data) + "-~-1-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();
                        } else {
                            SubCategoriesDealsList.set(thirdPosition, subcatDealData.substring(0, data) + "-~-0-~-" + subcatDealData.substring(remainData + 3, subcatDealData.length()));
                            SubScriptionsString = serverUtilites.serverUrl + "" + serverUtilites.likesUpadte + "" + categoryData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();
                    } else {
                        if (context instanceof SubCategoryActivity) {
                            ((SubCategoryActivity) context).callshowSubcatPopup();
                            SubCategoryActivity.categories_recyclerView.setVisibility(View.GONE);

                        }
                    }
                }
            });
        } else {
            Log.d("progressbar", "--->" + position);

            ((ProgressViewHolder1) holder).progressBar.setVisibility(View.GONE);
          /*  SubCategoryActivity.progressbarEndless.setVisibility(View.VISIBLE);*/

        }

    }

    public void setLoaded() {

        loading = false;
    }

    @Override
    public int getItemCount() {
        if (singleton.getScreenSize() > 2) {
            if (SubCategoriesDealsList.size() % 3 == 1) {
                return (SubCategoriesDealsList.size() / 3) + 1;
            }
            else if(SubCategoriesDealsList.size() % 3 == 2) {
                return (SubCategoriesDealsList.size() / 3) + 2;
            }
            else {
                return SubCategoriesDealsList.size() / 3;
            }
        } else {
            if (SubCategoriesDealsList.size() % 2 == 1) {
                return (SubCategoriesDealsList.size() / 2) + 1;
            } else {
                return SubCategoriesDealsList.size() / 2;
            }
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView storeName1TextView, discountPrice1TextView, retailPrice1TextView, percentDiscount1TextView, productName1TextView;
        public TextView storeName2TextView, discountPrice2TextView, retailPrice2TextView, percentDiscount2TextView, productName2TextView;
        public TextView storeName3TextView, discountPrice3TextView, retailPrice3TextView, percentDiscount3TextView, productName3TextView;
        public TextView riskTextView, likesCount1TextView, likesCount2TextView, likesCount3TextView;
        CardView thirdCardViewLargeScreen;
        public ImageView img1, img2, hearImg1Image1View, hearImg1Image2View, img3, hearImg1Image3View;
        LinearLayout secondLinearLayout, firstLinearLayout, thirdLinearLayout, linearLayoputMain;
        RelativeLayout heart1RelativeLayout, heart2RelativeLayout, heart3RelativeLayout;

        public StudentViewHolder(View v) {
            super(v);
            storeName1TextView = (TextView) v.findViewById(R.id.storename1);
            storeName2TextView = (TextView) v.findViewById(R.id.storename2);
            storeName3TextView = (TextView) v.findViewById(R.id.storename0);
            discountPrice1TextView = (TextView) v.findViewById(R.id.discount_price1);
            discountPrice2TextView = (TextView) v.findViewById(R.id.discount_price2);
            discountPrice3TextView = (TextView) v.findViewById(R.id.discount_price0);
            retailPrice1TextView = (TextView) v.findViewById(R.id.retail_price1);
            retailPrice2TextView = (TextView) v.findViewById(R.id.retail_price2);
            retailPrice3TextView = (TextView) v.findViewById(R.id.retail_price0);
            percentDiscount1TextView = (TextView) v.findViewById(R.id.percent_discount1);
            percentDiscount2TextView = (TextView) v.findViewById(R.id.percent_discount2);
            percentDiscount3TextView = (TextView) v.findViewById(R.id.percent_discount0);
            productName1TextView = (TextView) v.findViewById(R.id.product_name1);
            productName2TextView = (TextView) v.findViewById(R.id.product_name2);
            productName3TextView = (TextView) v.findViewById(R.id.product_name0);
            hearImg1Image1View = (ImageView) v.findViewById(R.id.hearImg1);
            hearImg1Image2View = (ImageView) v.findViewById(R.id.hearImg2);
            hearImg1Image3View = (ImageView) v.findViewById(R.id.hearImg0);
            riskTextView = (TextView) v.findViewById(R.id.risktext1);
            img1 = (ImageView) v.findViewById(R.id.img1);
            img2 = (ImageView) v.findViewById(R.id.img2);
            img3 = (ImageView) v.findViewById(R.id.img0);
            secondLinearLayout = (LinearLayout) v.findViewById(R.id.linearlayout_subcat2);
            firstLinearLayout = (LinearLayout) v.findViewById(R.id.linearlayout_subcat1);
            thirdLinearLayout = (LinearLayout) v.findViewById(R.id.linearlayout_subcat0);
            linearLayoputMain = (LinearLayout) v.findViewById(R.id.linearlayout_main);
            likesCount1TextView = (TextView) v.findViewById(R.id.likes_count_sub_cat1);
            likesCount2TextView = (TextView) v.findViewById(R.id.likes_count_sub_cat2);
            likesCount3TextView = (TextView) v.findViewById(R.id.likes_count_sub_cat0);
            heart1RelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_subcat1);
            heart2RelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_subcat2);
            heart3RelativeLayout = (RelativeLayout) v.findViewById(R.id.heart_subcat0);
            thirdCardViewLargeScreen = (CardView) v.findViewById(R.id.card_view0);
        }
    }

    public static class ProgressViewHolder1 extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder1(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar123);
        }
    }

}
