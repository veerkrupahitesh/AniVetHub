package com.veeritsolution.android.anivethub.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
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

public class ReviewAndRatingActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_review_and_rating, null, false);
        rootView.setVisibility(View.INVISIBLE);

        setContentView(rootView);

        intent = getIntent();

        if (intent != null) {
            vetLoginModel = (VetLoginModel) intent.getSerializableExtra(Constants.VET_DATA);
        }
        init();
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        snackBarView = (CoordinatorLayout) findViewById(R.id.parentView);

        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_review_ratings));

        tvName = (TextView) findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //tvName.setText(VetLoginModel.getLoginVetCredentials().getVetName());

        tvUserName = (TextView) findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        // tvUserName.setText("@" + VetLoginModel.getLoginVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) findViewById(R.id.img_vetProfilePic);
        imgVetBannerPhoto = (ImageView) findViewById(R.id.img_vet_bannerPic);

        imgSelectBannerPhoto = (ImageView) findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        tvRatings = (TextView) findViewById(R.id.tv_ratings);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        lvRatingAndReview = (ListView) findViewById(R.id.lv_reviewAndRating);
        // footerView = LayoutInflater.from(this).inflate(R.layout.footer_item, null, false);

        tvNorReviewRatings = (TextView) findViewById(tv_noReviewRating);
        tvNorReviewRatings.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvNorReviewRatings.setVisibility(View.GONE);

        tvReview = (TextView) findViewById(R.id.tv_reviews);
        tvReview.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        reviewRatingList = new ArrayList<>();

        lvRatingAndReview.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                if (totalItemsCount <= totalRecords && page < totalPage) {
                    getReviewData(page, false);
                    Utils.showSnackBar(ReviewAndRatingActivity.this, snackBarView);
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

            Utils.setProfileImage(this, vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePhoto);

            Utils.setBannerImage(this, vetLoginModel.getBannerPic(), R.drawable.img_vet_banner, imgVetBannerPhoto);
        }

        getReviewData(1, true);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetReviewRatings:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);

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

                            if (adpReview == null) {
                                adpReview = new AdpReviewRatingList(reviewRatingList, this/*, false*/);
                                lvRatingAndReview.setAdapter(adpReview);
                            } else {
                                adpReview.notifyDataSetChanged();
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getReviewData(int pageNo, boolean requiredDialog) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_REVIEW_RATINGS);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());
            params.put("PageNumber", pageNo);
            params.put("Records", 10);

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_VET_REVIEW_RATINGS,
                    requiredDialog, RequestCode.GetVetReviewRatings, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
