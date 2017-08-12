package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.PackageSetting;

import java.util.List;

/**
 * Created by ${hitesh} on 2/2/2017.
 */

public class AdpPackageList extends RecyclerView.Adapter<AdpPackageList.MyViewHolder> {

    private Context context;
    private List<PackageSetting> packageSettingList;
    // private List<Integer> colourList;


    public AdpPackageList(Context context, List<PackageSetting> packageSettingList) {
        this.context = context;
        this.packageSettingList = packageSettingList;
     /*   colourList = new ArrayList<>();
        colourList.add(R.color.blue);
        colourList.add(R.color.colorAccent);
        colourList.add(R.color.green);
        colourList.add(R.color.pink);
        colourList.add(R.color.light_violet);
        colourList.add(R.color.yellow1);*/

    }

    @Override
    public AdpPackageList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_package_settings, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdpPackageList.MyViewHolder holder, int position) {

        PackageSetting packageSetting = packageSettingList.get(position);
        // MyViewHolder myViewHolder = holder;
        // Collections.shuffle(colourList,new Random());
        holder.tvCreditsAmount.setText("£ " + packageSetting.getPackageAmount());
        holder.tvCredits.setText(packageSetting.getSessionCount() + " Credits");
        holder.linearLayout.setBackgroundColor(Color.parseColor(packageSetting.getColorCode()));
        holder.linearLayout.setTag(packageSetting);
    }


    @Override
    public int getItemCount() {
        return packageSettingList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCredits, tvCreditsAmount;
        LinearLayout linearLayout;

        private MyViewHolder(View view) {
            super(view);
            tvCredits = (TextView) view.findViewById(R.id.tv_credits);
            tvCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvCreditsAmount = (TextView) view.findViewById(R.id.tv_credits_amounts);
            tvCreditsAmount.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

            linearLayout = (LinearLayout) view.findViewById(R.id.lin_package_settings);
        }
    }
}
