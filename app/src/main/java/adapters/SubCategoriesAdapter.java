package adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ngagroupinc.movers.R;

import java.util.ArrayList;


/**
 * Created by user1 on 07-Dec-16.
 */

public class SubCategoriesAdapter extends BaseAdapter {
    ArrayList<String> sub_category_data_items;
    ArrayList<String> selections;

    private LayoutInflater inflater;
    private Context context;

    public SubCategoriesAdapter(Context context, ArrayList<String> dataitems, ArrayList<String> selection) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.sub_category_data_items = dataitems;
        this.selections=selection;
    }


    @Override
    public int getCount() {
        return sub_category_data_items.size();
    }

    @Override
    public Object getItem(int position) {
        return sub_category_data_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if (row == null) {
            Integer selected_position = -1;

            row = inflater.inflate(R.layout.sub_category_list_items, parent, false);
            holder = new Holder();

            holder.sub_category_title = (TextView) row.findViewById(R.id.subcategory_item);
            row.setTag(holder);
        } else {

            holder = (Holder) row.getTag();
        }


        holder.sub_category_title.setText(sub_category_data_items.get(position));


        try {
            if (selections.get(position).equals("1")) {
                holder.sub_category_title.setTextColor(Color.parseColor("#03a9f4"));
            } else {

                holder.sub_category_title.setTextColor(Color.parseColor("#8D8D8D"));

            }


        } catch (Exception e) {

            Log.d("catch", "--->" + e);
        }


        return row;
    }
    class Holder {

        TextView sub_category_title;

    }
}





