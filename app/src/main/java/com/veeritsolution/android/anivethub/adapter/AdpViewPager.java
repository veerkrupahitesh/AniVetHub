package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.utility.Utils;

/**
 * Created by Jaymin on 05-12-2016.
 */

public class AdpViewPager extends PagerAdapter {

    private Context mContext;
    private int[] layouts;
    private String[] header, contents;
    //String[] contents;

    public AdpViewPager(Context mContext, int[] layouts, String[] header, String[] contents) {
        this.mContext = mContext;
        this.layouts = layouts;
        this.header = header;
        this.contents = contents;
    }

    public AdpViewPager(Context mContext, int[] layouts, String[] contents) {
        this.mContext = mContext;
        this.layouts = layouts;
        // this.header = header;
        this.contents = contents;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_viewpager, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_welcome);
        Utils.setProfileImage(mContext, layouts[position], R.color.grey, imageView);
        //imageView.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), layouts[position], null));
        TextView tvHeader = (TextView) view.findViewById(R.id.tv_headerWelcome);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

        if (header != null) {

            tvHeader.setText(header[position]);
        } else {
            tvHeader.setVisibility(View.GONE);
        }

        TextView tvContent = (TextView) view.findViewById(R.id.tv_contentWelcome);
        tvContent.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvContent.setText(contents[position]);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
