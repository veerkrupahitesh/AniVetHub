package com.veeritsolution.android.anivethub.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpEducationList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitesh on 8/17/2017.
 */

public class VetEducation extends Fragment implements DataObserver {

    // xml components
    private Button btnAddNewEduInfo;
    private TextView tvHeader, tvName, tvUserName, tvNoEducationInfo, tvPassingYear, navHeaderName, navHeaderLocation;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private ListView listViewEducation;
    private View headerView, header;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private List<com.veeritsolution.android.anivethub.models.VetEducation> vetEducationList;
    private AdpEducationList adpEducationList;
    private VetDetailActivity homeActivity;
    private Bundle bundle;
    private com.veeritsolution.android.anivethub.models.VetEducation vetEducation;
    private VetLoginModel vetLoginModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (VetDetailActivity) getActivity();
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vet_education, container, false);

        bundle = getArguments();
        vetLoginModel = (VetLoginModel) bundle.getSerializable(Constants.VET_DATA);
        //rootView.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        /*((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });
*/

        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_education));

        //backButton = (ImageView) getView().findViewById(R.id.img_back_header);
        headerView = getView().findViewById(R.id.headerView);
        headerView.setVisibility(View.GONE);

        header = getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        tvName = (TextView) getView().findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) getView().findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) getView().findViewById(R.id.img_vetProfilePic);
//        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
//                R.drawable.img_vet_profile, imgVetProfilePhoto);

        imgVetBannerPhoto = (ImageView) getView().findViewById(R.id.img_vet_bannerPic);

//        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
//                R.drawable.img_vet_banner, imgVetBannerPhoto);

        imgSelectBannerPhoto = (ImageView) getView().findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) getView().findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        listViewEducation = (ListView) getView().findViewById(R.id.listView_education);
        tvNoEducationInfo = (TextView) getView().findViewById(R.id.tv_noEducationInfo);
        tvNoEducationInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnAddNewEduInfo = (Button) rootView.findViewById(R.id.btn_addNewEduInfo);
        btnAddNewEduInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnAddNewEduInfo.setVisibility(View.GONE);
        getVetEducationData();
    }

    private void getVetEducationData() {

        try {
            params = new JSONObject();
            params.put("op", "GetVetEducationInfoAll");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EDUCATION_INFO_ALL,
                    false, RequestCode.GetVetEducationInfoAll, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetVetEducationInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        listViewEducation.setVisibility(View.GONE);
                        tvNoEducationInfo.setVisibility(View.VISIBLE);
                    } else {

                        vetEducationList = (ArrayList<com.veeritsolution.android.anivethub.models.VetEducation>) mObject;

                        if (!vetEducationList.isEmpty()) {
                            tvNoEducationInfo.setVisibility(View.GONE);
                            adpEducationList = new AdpEducationList(getActivity(), vetEducationList, true);
                            listViewEducation.setAdapter(adpEducationList);
                        } else {
                            tvNoEducationInfo.setVisibility(View.VISIBLE);
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
        ToastHelper.getInstance().showMessage(mError);
    }
}
