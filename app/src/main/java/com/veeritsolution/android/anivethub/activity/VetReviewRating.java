package com.veeritsolution.android.anivethub.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpReviewRatingList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.ReviewRatingModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.veeritsolution.android.anivethub.R.id.tv_noReviewRating;

/**
 * Created by hitesh on 8/17/2017.
 */

public class VetReviewRating extends Fragment implements DataObserver {

    // xml components
    private TextView tvUserName, tvName, tvHeader, tvRatings, tvNorReviewRatings, tvReview;
    private ImageView imgVetProfilePhoto, imgSelectBannerPhoto, imgSelectProfilePhoto, imgVetBannerPhoto;
    private Toolbar toolbar;
    private ListView lvRatingAndReview;
    private RatingBar ratingBar;
    // private View footerView;
    private View rootView;
    private CoordinatorLayout snackBarView;

    // object and variable declaration
    private JSONObject params;
    private Intent intent;
    private VetLoginModel vetLoginModel;
    private AdpReviewRatingList adpReview;
    private List<ReviewRatingModel> reviewRatingList;
    private int totalPage;
    private int totalRecords;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_review_and_rating, container, false);
        bundle = getArguments();
        vetLoginModel = (VetLoginModel) bundle.getSerializable(Constants.VET_DATA);
        init();
        return rootView;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void init() {

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_review_ratings));

        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //tvName.setText(VetLoginModel.getLoginVetCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        // tvUserName.setText("@" + VetLoginModel.getLoginVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        tvRatings = (TextView) rootView.findViewById(R.id.tv_ratings);
        ratingBar = (RatingBar) rootView.findViewById(R.id.rating);
        lvRatingAndReview = (ListView) rootView.findViewById(R.id.lv_reviewAndRating);
        // footerView = LayoutInflater.from(this).inflate(R.layout.footer_item, null, false);

        tvNorReviewRatings = (TextView) rootView.findViewById(tv_noReviewRating);
        tvNorReviewRatings.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvNorReviewRatings.setVisibility(View.GONE);

        tvReview = (TextView) rootView.findViewById(R.id.tv_reviews);
        tvReview.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        reviewRatingList = new ArrayList<>();

        lvRatingAndReview.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                if (totalItemsCount <= totalRecords && page < totalPage) {
                    getReviewData(page, false);
                    Utils.showSnackBar(getActivity(), snackBarView);
                    return true;
                } else {
                    Utils.dismissSnackBar();
                    return false;
                }
            }
        });
        if (vetLoginModel != null) {

            tvName.setText(vetLoginModel.getVetName());
            tvUserName.setText(vetLoginModel.getUserName());
            tvRatings.setText(vetLoginModel.getRating() + "/5");
            ratingBar.setRating(vetLoginModel.getRating());

            Utils.setProfileImage(getActivity(), vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePhoto);

            Utils.setBannerImage(getActivity(), vetLoginModel.getBannerPic(), R.drawable.img_vet_banner, imgVetBannerPhoto);
        }
        getReviewData(1, false);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetReviewRatings:
                Utils.dismissSnackBar();
                // rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        lvRatingAndReview.setVisibility(View.GONE);
                        tvNorReviewRatings.setVisibility(View.VISIBLE);
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        List<ReviewRatingModel> reviewList = (List<ReviewRatingModel>) mObject;

                        if (!reviewList.isEmpty()) {

                            reviewRatingList.addAll(reviewList);
                            tvNorReviewRatings.setVisibility(View.GONE);
                            totalPage = reviewList.get(0).getTotalPage();
                            totalRecords = reviewList.get(0).getTotalRowNo();
//                            adpReview = new AdpReviewRatingList(reviewRatingList, getActivity());
//                            lvRatingAndReview.setAdapter(adpReview);
                            adpReview = (AdpReviewRatingList) lvRatingAndReview.getAdapter();
                            if (adpReview != null && adpReview.getCount() > 0) {
                                adpReview.refreshList(reviewRatingList);
                            } else {
                                adpReview = new AdpReviewRatingList(reviewRatingList, getActivity());
                                lvRatingAndReview.setAdapter(adpReview);
                                // adpReview.notifyDataSetChanged();
                            }

                        } else {
                            tvNorReviewRatings.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        Utils.dismissSnackBar();
        ToastHelper.getInstance().showMessage(mError);
    }


    private void getReviewData(int pageNo, boolean requiredDialog) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_REVIEW_RATINGS);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());
            params.put("PageNumber", pageNo);
            params.put("Records", 10);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_REVIEW_RATINGS,
                    requiredDialog, RequestCode.GetVetReviewRatings, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
