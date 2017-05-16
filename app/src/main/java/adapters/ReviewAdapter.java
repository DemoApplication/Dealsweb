package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ngagroupinc.movers.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import projo.SessionManager;
import projo.Singleton;

/**
 * Created by Userone on 12/14/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    Context context;
    Singleton singleton;


    private ArrayList<String> dataList;
    View itemView;
    SessionManager session;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView nameTextView, dateTextView, descriptionTextView;

        protected ImageView postedPersonImage;


        public MyViewHolder(View convertView) {
            super(convertView);

            this.nameTextView = (TextView) convertView.findViewById(R.id.name_text);
            this.dateTextView = (TextView) convertView.findViewById(R.id.date_text);
            this.descriptionTextView = (TextView) convertView.findViewById(R.id.posted_text);
            this.postedPersonImage = (ImageView) convertView.findViewById(R.id.posted_image);




          /*  convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   *//* Intent intent = new Intent(context, SubCategoryActivity.class);

                    String categoryData1[] = singleton.categoriesData.get(Integer.parseInt(countTextView.getText().toString())).split("-~-");
                    singleton.setSelectedSubCategory(categoryData1[0]);
                    context.startActivity(intent);*//*

                    Toast.makeText(v.getContext(), countTextView.getText()+"..."+categoriesTextView.getText(), Toast.LENGTH_SHORT).show();

                }
            });*/

        }
    }


    public ReviewAdapter(Context context, ArrayList<String> dataList) {

        this.dataList = dataList;
        this.context = context;
        session = new SessionManager(context);
        Log.d("review adapter", "--->" + dataList.size());
        singleton = Singleton.getInstance();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcategory_list_items, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        String reviewData[] = dataList.get(position).split("-~-");
        Log.d("adapter", "--->" + dataList.get(position));
        holder.nameTextView.setText(reviewData[2]);
//convert date to string date
        String trDate = "20120106";
        Date tradeDate = null;
        try {
            tradeDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reviewData[1].split("T")[0]);
          /*  String krwtrDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(tradeDate);*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String stringDate = tradeDate.toString();
        holder.dateTextView.setText(stringDate.replace(" 00:00:00 EST ", " "));

        holder.descriptionTextView.setText(reviewData[0]);


        if (session.isLoggedIn())
        {
            Log.d(" status1","--->"+singleton.getLoginImage()+"-~-"+(singleton.getLoginImage()!=null));
            if(reviewData[4].equals("0"))
            {
                holder.postedPersonImage.setImageResource(R.drawable.profilebig);


            }
            else{
                Picasso.with(context).load(reviewData[3]).into(holder.postedPersonImage);
            }


        }
        else{


                holder.postedPersonImage.setImageResource(R.drawable.profilebig);


        }




    }


    @Override
    public int getItemCount() {


        return dataList.size();

    }


}

