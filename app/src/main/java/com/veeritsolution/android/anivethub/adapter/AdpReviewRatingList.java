package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.ReviewRatingModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by ${hitesh} on 2/11/2017.
 */

public class AdpReviewRatingList extends BaseAdapter {

    private List<ReviewRatingModel> reviewRatingList;
    private Context context;
    private boolean showLoader;

    public AdpReviewRatingList(List<ReviewRatingModel> reviewRatingList, Context context/*, boolean showLoader*/) {
        this.reviewRatingList = reviewRatingList;
        this.context = context;
        this.showLoader = showLoader;
    }

    @Override
    public int getCount() {
        return reviewRatingList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewRatingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_review_rating, parent, false);

            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        if (position == 0) {
            myViewHolder.linReviewRating.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            myViewHolder.linReviewRating.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            myViewHolder.linReviewRating.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        ReviewRatingModel reviewRatingModel = reviewRatingList.get(position);

        Utils.setProfileImage(context, reviewRatingModel.getClientProfilePic(),
                R.drawable.img_client_profile, myViewHolder.imgProfilePic);

        myViewHolder.tvClientName.setText(reviewRatingModel.getClientName());
        myViewHolder.tvReview.setText(reviewRatingModel.getReview());
        myViewHolder.ratingBar.setRating(reviewRatingModel.getAvgRating());

        return convertView;
    }

    public void showLoader(boolean showLoader) {
        this.showLoader = showLoader;
        notifyDataSetChanged();
    }

    public void refreshList(List<ReviewRatingModel> reviewRatingList) {
        this.reviewRatingList = reviewRatingList;
        notifyDataSetChanged();
    }

    private class MyViewHolder {

        private ImageView imgProfilePic;
        private TextView tvClientName, tvReview/*, tvLoader*/;
        private RelativeLayout linReviewRating;
        private RatingBar ratingBar;

        public MyViewHolder(View view) {

            imgProfilePic = (ImageView) view.findViewById(R.id.img_clientProfilePic);
            tvClientName = (TextView) view.findViewById(R.id.tv_clientName);
            tvClientName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvReview = (TextView) view.findViewById(R.id.tv_reviews);
            tvReview.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            ratingBar = (RatingBar) view.findViewById(R.id.rating);
            linReviewRating = (RelativeLayout) view.findViewById(R.id.lin_vetReviewList);
            //  tvLoader = (TextView) view.findViewById(R.id.tv_loader);

        }
    }
}
