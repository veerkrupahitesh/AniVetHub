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
import com.veeritsolution.android.anivethub.adapter.AdpVetSpecialisation;
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

public class VetSpecialization extends Fragment implements DataObserver {

    // xml components
    private Button btnNewSpecialization;
    private TextView tvHeader, tvName, tvUserName, tvNoSpecializationInfo;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private ListView listViewExpertise;
    private View headerView, header;

    // object and variable declaration
    private JSONObject params;
    private List<com.veeritsolution.android.anivethub.models.VetSpecialization> vetSpecializationList;
    private AdpVetSpecialisation adpExpertise;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private com.veeritsolution.android.anivethub.models.VetSpecialization vetSpecialization;
    private View rootView;
    private VetLoginModel vetLoginModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //  homeActivity = (HomeActivity) getActivity();
        vetSpecializationList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_expertise, container, false);
        bundle = getArguments();
        vetLoginModel = (VetLoginModel) bundle.getSerializable(Constants.VET_DATA);
        // rootView.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
      /*  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });*/

        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_specialization));

        //backButton = (ImageView) getView().findViewById(R.id.img_back_header);

        headerView = getView().findViewById(R.id.headerView);
        headerView.setVisibility(View.GONE);

        header = getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        /*tvName = (TextView) getView().findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) getView().findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) getView().findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) getView().findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);


        imgSelectBannerPhoto = (ImageView) getView().findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) getView().findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);*/

        listViewExpertise = (ListView) getView().findViewById(R.id.listView_expertise);
        tvNoSpecializationInfo = (TextView) getView().findViewById(R.id.tv_noExpertiseInfo);
        tvNoSpecializationInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnNewSpecialization = (Button) getView().findViewById(R.id.btn_addMoreSpecialization);
        btnNewSpecialization.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnNewSpecialization.setVisibility(View.GONE);
        getVetSpecialisationData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetExpertiseInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                        listViewExpertise.setVisibility(View.GONE);
                    } else {

                        vetSpecializationList = (ArrayList<com.veeritsolution.android.anivethub.models.VetSpecialization>) mObject;

                        if (!vetSpecializationList.isEmpty()) {
                            tvNoSpecializationInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                            adpExpertise = new AdpVetSpecialisation(getActivity(), vetSpecializationList, true);
                            listViewExpertise.setAdapter(adpExpertise);
                        } else {
                            tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                            listViewExpertise.setVisibility(View.GONE);
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

        switch (mRequestCode) {

            case GetVetExpertiseInfoAll:

                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    private void getVetSpecialisationData() {
        try {
            params = new JSONObject();
            params.put("op", "GetVetExpertiseInfoAll");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EXPERTISE_INFO_ALL,
                    false, RequestCode.GetVetExpertiseInfoAll, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


