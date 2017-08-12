package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;

/**
 * Created by admin on 12/13/2016.
 */

public class AdpGridView extends BaseAdapter {

    private int homeImages[];
    private String homeCategory[];
    private Context mContext;


    public AdpGridView(int[] homeImages, String[] homeCategory, Context mContext) {
        this.homeImages = homeImages;
        this.homeCategory = homeCategory;
        this.mContext = mContext;
    }

   /* public AdpGridView(Context mContext) {

        this.mContext = mContext;
    }*/

    @Override
    public int getCount() {
        return homeImages.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gridview, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.linearLayout.setTag(R.integer.int_key, position);
        viewHolder.imgHomeIcons.setImageDrawable(mContext.getResources().getDrawable(homeImages[position]));
        viewHolder.tvHomeCategory.setText(homeCategory[position]);

        return view;
    }

    private class ViewHolder {

        ImageView imgHomeIcons;
        TextView tvHomeCategory;
        LinearLayout linearLayout;

        ViewHolder(View view) {

            imgHomeIcons = (ImageView) view.findViewById(R.id.img_homeIcon);
            tvHomeCategory = (TextView) view.findViewById(R.id.tv_homeCategory);
            tvHomeCategory.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            linearLayout = (LinearLayout) view.findViewById(R.id.lin_homeGridView);
        }
    }
}
