package com.veeritsolution.android.anivethub.fragment.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

/**
 * Created by veerk on 3/27/2017.
 */

public class UpdateVetAccInfoFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // xml components
    private Button btnSave;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto, imgBackHeader;
    private TextView tvHeader, tvName, tvUserName;
    private EditText edtPayPalId;
    private View rootView;

    // object and variable declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private int loginUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_paypal_info, container, false);

        toolbar = (Toolbar) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.finish();
            }
        });

        //imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        // imgBackHeader.setVisibility(View.INVISIBLE);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_paypal_info));

        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);


        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        edtPayPalId = (EditText) rootView.findViewById(R.id.edt_payPalId);
        edtPayPalId.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetAccountInsert:

                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetAccount(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));

                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

           /* case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();

                break;*/

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_save:
                String paypalId = edtPayPalId.getText().toString().trim();
                if (paypalId.isEmpty()) {
                    edtPayPalId.requestFocus();
                    ToastHelper.getInstance().showMessage("Please enter your PayPal Account Id");
                } else if (!paypalId.matches(Patterns.EMAIL_ADDRESS.pattern())) {
                    edtPayPalId.requestFocus();
                    ToastHelper.getInstance().showMessage("Please enter your valid PayPal Account Id");
                } else {
                    vetAccountInsert();
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        homeActivity.finish();
    }


    private void vetAccountInsert() {
        try {
            params = new JSONObject();
            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            }
            params.put("op", ApiList.VET_ACCOUNT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("PaypalAccountId", edtPayPalId.getText().toString());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_ACCOUNT_INSERT,
                    true, RequestCode.VetAccountInsert, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

