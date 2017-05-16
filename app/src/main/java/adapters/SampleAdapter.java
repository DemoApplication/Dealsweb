package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ngagroupinc.movers.MainActivity;
import com.ngagroupinc.movers.R;
import com.ngagroupinc.movers.SingleItemActivity;
import com.ngagroupinc.movers.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import projo.ServerUtilites;
import projo.SessionManager;
import projo.Singleton;

/**
 * Created by Userone on 12/13/2016.
 */


public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

    Context context;
    View itemView;
    String itemType;
    Singleton singleton;

    String hottestDealsData[];

    ServerUtilites serverUtilities;

    SessionManager session;

    public String loginUserId;
    public String LoginUserDisplayName;
    public String LoginUserEmailId;
    boolean likes_status = false;
    String liked_dealId;
    String likes_response_from_server;
    ArrayList<String> selections;
    String selectedLikedItemData[];
    int selected_position;
    String reftype, like_type;

    public SampleAdapter(Context context, String itemType) {
        Log.d("subcatAdapter", "--->" + MainActivity.hottestDealsArrayList.size());
        this.context = context;
        this.itemType = itemType;
        session = new SessionManager(context);
        singleton = Singleton.getInstance();
        serverUtilities = new ServerUtilites(context);
        selections = new ArrayList<String>(MainActivity.hottestDealsArrayList.size());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (itemType.equals("category")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_recycleview_list_items, parent, false);
        } else if (context instanceof MainActivity) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
        }


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        try {
            if (selections.get(position).equals("1")) {
                holder.heartImageview.setImageResource(R.drawable.hearticon_red);
            } else {

                holder.heartImageview.setImageResource(R.drawable.hearticon);

            }


        } catch (Exception e) {

            Log.d("catch", "--->" + e);
        }
        if (itemType.equals("category")) {
            String categoryData[] = singleton.categoriesData.get(position).split("-~-");
            holder.categoriesTextView.setText(categoryData[1]);


            String imgUrl = "http://www.dealsweb.com" + categoryData[2].replace("\\", "//");


            Picasso.with(context).load(imgUrl).into(holder.img);
        } else if (context instanceof MainActivity) {
            hottestDealsData = MainActivity.hottestDealsArrayList.get(position).split("-~-");

           /* ((StudentViewHolder) holder).strikePrice.setText(stringData);*/
            holder.storeNameTextView.setText(hottestDealsData[1]);
            holder.discountPriceTextView.setText("$ " + hottestDealsData[2]);

            String text = "$ " + hottestDealsData[3];
            String priceData = " <font color=#959595>" + text + "</font> ";

            //strike the text
            SpannableString stringData = new SpannableString(Html.fromHtml(priceData));

            stringData.setSpan(new StrikethroughSpan(), 0, stringData.length(), 0);
            holder.retailPriceTextView.setText(stringData);
            holder.percentDiscountTextView.setText(hottestDealsData[4] + "% Off");
            holder.productNameTextView.setText(hottestDealsData[5]);
            holder.likesCountTextView.setText(hottestDealsData[7]);
            if (hottestDealsData[7].equals("0"))
            {
               holder.heartImageview.setImageResource(R.drawable.hearticon);
            }
            else {
                holder.heartImageview.setImageResource(R.drawable.hearticon_red);
            }

            String imgUrl = serverUtilities.imageLink + "" + hottestDealsData[0];
       /*     Log.d("imagelink", "--->" + imgUrl);*/
            Picasso.with(context)
                    .load(imgUrl)
                    .resize(200, 200)

                    .noFade()
                    .into(holder.img);


         /*   holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SingleItemActivity.class);
                    singleton.setSingleActivityData(singleton.hottestDeals.get(position));
                    context.startActivity(intent);
                }
            });*/
            holder.heartRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loginUserId = singleton.getLoginUserId();

                    if (session.isLoggedIn()) {
                        String categoryData[] = MainActivity.hottestDealsArrayList.get(position).split("-~-");
                        Log.d("hello1", "--->" + categoryData[12]);
                        String SubScriptionsString;
                          /*  Map<String, String> apiHeader = new HashMap<String, String>();
                            apiHeader.put("ApiKey ", serverUtilites.apiHeader);*/
                        String hottestdealData=MainActivity.hottestDealsArrayList.get(position);
                        int data=serverUtilities.ordinalIndexOf(hottestdealData,"-~-",7);
                        int remainData=serverUtilities.ordinalIndexOf(hottestdealData,"-~-",8);
                        if (categoryData[7].equals("0")) {


                            MainActivity.hottestDealsArrayList.set(position, hottestdealData.substring(0,data)+ "-~-1-~-" + hottestdealData.substring(remainData+3,hottestdealData.length()));
                            SubScriptionsString = serverUtilities.serverUrl + "" + serverUtilities.likesUpadte + "" + categoryData[12] + "/1/1/" + singleton.getLoginUserId();
                        }
                        else {
                            MainActivity.hottestDealsArrayList.set(position,hottestdealData.substring(0,data) + "-~-0-~-" + hottestdealData.substring(remainData+3,hottestdealData.length()));
                            SubScriptionsString = serverUtilities.serverUrl + "" + serverUtilities.likesUpadte + "" + categoryData[12] + "/1/0/" + singleton.getLoginUserId();
                        }
                         /*   String arrayString = categoryData[0] + "-~-" + categoryData[1] + "-~-" + categoryData[2] + "-~-0";
                            getLikesUpdateData(apiHeader, SubScriptionsString, position);*/
                        notifyDataSetChanged();

                    } else {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).callShowPopup();
                        }
                    }


                }
            });
        }


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("position", "--->" + position);
                if (itemType.equals("category"))
                {
                    Intent intent = new Intent(context, SubCategoryActivity.class);

                    String categoryData1[] = singleton.categoriesData.get(position).split("-~-");
                    singleton.setSelectedCategoryId(categoryData1[0]);
                    intent.putExtra("CategoryName", categoryData1[1]);
                    intent.putExtra("ComingFrom", "MainActivity");
                    context.startActivity(intent);

                }
                else if (itemType.equals("hotestDeals"))
                {
                    singleton.setSingleActivityData(singleton.hottestDeals.get(position));



                        Intent intent = new Intent(context, SingleItemActivity.class);

                        context.startActivity(intent);

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        if (itemType.equals("category")) {
            return singleton.categoriesData.size();
        } else {
            return MainActivity.hottestDealsArrayList.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView categoriesTextView;


        public TextView storeNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, productNameTextView, positionTextView,likesCountTextView;


        public ImageView img,heartImageview;
RelativeLayout heartRelativeLayout;
        public MyViewHolder(View v) {
            super(v);
            if (itemType.equals("category")) {
                categoriesTextView = (TextView) v.findViewById(R.id.categories_text1);
                img = (ImageView) v.findViewById(R.id.categories_circleImage);


            } else if (itemType.equals("hotestDeals")) {
                storeNameTextView = (TextView) v.findViewById(R.id.storename);

                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                retailPriceTextView = (TextView) v.findViewById(R.id.retail_price);
                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                percentDiscountTextView = (TextView) v.findViewById(R.id.percent_discount);
                productNameTextView = (TextView) v.findViewById(R.id.product_name);
                heartImageview= (ImageView) v.findViewById(R.id.hearImg);
                likesCountTextView=(TextView) v.findViewById(R.id.likes_count_row_list);
                heartRelativeLayout=(RelativeLayout) v.findViewById(R.id.heart_list_row);
                img = (ImageView) v.findViewById(R.id.img);

            }


        }
    }
}