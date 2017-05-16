package adapters;

import android.app.Activity;
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
import android.widget.TextView;

import com.ngagroupinc.movers.MainActivity;
import com.ngagroupinc.movers.R;
import com.ngagroupinc.movers.SingleItemActivity;
import com.ngagroupinc.movers.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import projo.ServerUtilites;
import projo.Singleton;

/**
 * Created by userone on 1/9/2017.
 */

public class SubCategoryCirclerAdapter extends RecyclerView.Adapter<SubCategoryCirclerAdapter.ViewHolder> {


    Context context;
    View itemView;
    String itemType;
    Singleton singleton;

    String hottestDealsData[];

    ServerUtilites serverUtilities;

    public SubCategoryCirclerAdapter(Context context, String itemType) {

        this.context = context;
        this.itemType = itemType;
        singleton = Singleton.getInstance();
        serverUtilities = new ServerUtilites(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (itemType.equals("category")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_cat_recyclerview, parent, false);
        } else if (itemType.equals("hotestDeals")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (itemType.equals("category")) {
            String categoryData[] = singleton.categoriesData.get(position).split("-~-");
            holder.categoriesTextView.setText(categoryData[1]);


            String imgUrl = "http://www.dealsweb.com" + categoryData[2].replace("\\", "//");


            Picasso.with(context).load(imgUrl).into(holder.img);
        } else if (itemType.equals("hotestDeals")) {
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


            String imgUrl = serverUtilities.imageLink + "" + hottestDealsData[0];
   /*         Log.d("imagelink", "--->" + imgUrl);*/
            Picasso.with(context).load(imgUrl).into(holder.img);


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
                    ((Activity)context).finish();

                } else if (itemType.equals("hotestDeals")) {
                    singleton.setSingleActivityData(singleton.hottestDeals.get(position));


                    Intent intent = new Intent(context, SingleItemActivity.class);

                    context.startActivity(intent);
                    ((Activity)context).finish();
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

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView categoriesTextView;


        public TextView storeNameTextView, discountPriceTextView, retailPriceTextView, percentDiscountTextView, productNameTextView, positionTextView;


        public ImageView img,hearImageview;

        public ViewHolder(View v) {
            super(v);
            if (itemType.equals("category")) {
                categoriesTextView = (TextView) v.findViewById(R.id.categories_text1one);
                img = (CircleImageView) v.findViewById(R.id.categories_circleImageoneonew);


            } else if (itemType.equals("hotestDeals")) {
                storeNameTextView = (TextView) v.findViewById(R.id.storename);

                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                retailPriceTextView = (TextView) v.findViewById(R.id.retail_price);
                discountPriceTextView = (TextView) v.findViewById(R.id.discount_price);
                percentDiscountTextView = (TextView) v.findViewById(R.id.percent_discount);
                productNameTextView = (TextView) v.findViewById(R.id.product_name);
                hearImageview= (ImageView) v.findViewById(R.id.hearImg);

                img = (ImageView) v.findViewById(R.id.img);

            }


        }
    }
}
