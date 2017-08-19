package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hitesh} on 1/31/2017.
 */

public class AdpSearchVetPracticeList extends BaseAdapter {

    private Context context;
    private List<SearchVetModel> vetList;
    private View.OnClickListener onClickListener;

    //  private double latitude, longitude, altitude;

    public AdpSearchVetPracticeList(Context context, List<SearchVetModel> vetList, View.OnClickListener onClickListener) {
        this.context = context;
        this.vetList = vetList;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return vetList.size();
    }

    @Override
    public Object getItem(int position) {
        return vetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshList(List<SearchVetModel> vetList) {
        this.vetList = vetList;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_seach, parent, false);

            myViewHolder = new MyViewHolder(view);

            view.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) view.getTag();
        }

        SearchVetModel searchVetModel = vetList.get(position);

        myViewHolder.linearLayout.setTag(searchVetModel);
        myViewHolder.linearLayout.setOnClickListener(onClickListener);
        if (position == 0) {
            myViewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            myViewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            myViewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        Utils.setProfileImage(context, searchVetModel.getProfilePic(),
                R.drawable.img_vet_profile, myViewHolder.imgProfilePhoto);

        myViewHolder.tvPrize.setText("£ " + searchVetModel.getSessionRate());
        myViewHolder.tvVetName.setText(searchVetModel.getVetName());
        myViewHolder.tvSpecialization.setText(searchVetModel.getCountry());
        //myViewHolder.tvSpecialization.setText(searchVetModel.getVetExpertise());
        //myViewHolder.tvDegree.setText(searchVetModel.getVetEducation());
        myViewHolder.ratingBar.setRating(searchVetModel.getRating());

        myViewHolder.tvOnlineStatus.setVisibility(View.INVISIBLE);

/*        int onlineStatus = searchVetModel.getOnlineStatus();


        if (onlineStatus == Constants.OFFLINE_STATUS) {
            myViewHolder.tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.drw_circle_gradient_red, null));
        } else if (onlineStatus == Constants.ONLINE_STATUS) {
            myViewHolder.tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.drw_circle_gradient_green, null));
        } else {
            myViewHolder.tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.drw_circle_gradient_grey, null));
        }*/

        myViewHolder.tvDistance.setText(String.format("%s km", String.valueOf(searchVetModel.getDistance())));
        return view;
    }

    public void refreshList(ArrayList<SearchVetModel> vetList) {
        this.vetList = vetList;
        notifyDataSetChanged();
    }

    private class MyViewHolder {

        ImageView imgProfilePhoto;
        TextView tvPrize, tvVetName, tvSpecialization, tvDegree, tvOnlineStatus, tvDistance;
        RatingBar ratingBar;
        LinearLayout linearLayout;

        MyViewHolder(View itemView) {

            linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_vetSearch);
            imgProfilePhoto = (ImageView) itemView.findViewById(R.id.img_vetProfile);

            tvPrize = (TextView) itemView.findViewById(R.id.tv_vetPrize);
            tvPrize.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvVetName = (TextView) itemView.findViewById(R.id.tv_vetName);
            tvVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvSpecialization = (TextView) itemView.findViewById(R.id.tv_vetSpecialization);
            tvSpecialization.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvDegree = (TextView) itemView.findViewById(R.id.tv_vetDegree);
            tvDegree.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            tvOnlineStatus = (TextView) itemView.findViewById(R.id.tv_onLineStatus);

            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
            tvDistance.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        }
    }
}
