package com.veeritsolution.android.anivethub.fragment.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.VetAccountInfoModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

/**
 * Created by ${hitesh} on 1/31/2017.
 */
public class VetAccInfoFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // xml components
    private Button btnSave;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private TextView tvHeader, tvName, tvUserName;
    private EditText edtPayPalId;
    private View rootView;

    // object and variable declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private Bundle bundle;
    private VetAccountInfoModel vetAccountInfoModel;
    private int addEditSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);

        bundle = getArguments();
        if (bundle != null) {
            addEditSignUp = bundle.getInt(Constants.ADD_EDIT_SINGUP);
        }
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
                homeActivity.popBackFragment();
            }
        });

        // imgBack = (ImageView) rootView.findViewById(R.id.img_back_header);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvHeader = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        switch (addEditSignUp) {
            case Constants.FROM_ADD:
                tvHeader.setText(getString(R.string.str_add_paypal_info));
                break;
            case Constants.FROM_EDIT:
                tvHeader.setText(getString(R.string.str_edit_paypal_info));
                break;
            case Constants.FROM_SIGN_UP:
                tvHeader.setText(getString(R.string.str_add_paypal_info));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                //  imgBack.setVisibility(View.INVISIBLE);
                break;
        }

        tvName = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        edtPayPalId = (EditText) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.edt_payPalId);
        edtPayPalId.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setupOutSideTouchHideKeyboard(rootView);
        if (addEditSignUp == Constants.FROM_EDIT) {
            getVetAccountInfo();
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetAccountInfo:

                rootView.setVisibility(View.VISIBLE);
                vetAccountInfoModel = (VetAccountInfoModel) mObject;
                edtPayPalId.setText(vetAccountInfoModel.getPaypalAccountId());
                break;

            case VetAccountInsert:

                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetAccount(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));

                switch (addEditSignUp) {

                    case Constants.FROM_ADD:
                        homeActivity.popBackFragment();
                        break;
                    case Constants.FROM_EDIT:
                        homeActivity.popBackFragment();
                        break;
                    case Constants.FROM_SIGN_UP:
                        homeActivity.removeAllFragment();
                        homeActivity.pushFragment(new VetHomeFragment(), true, false, null);
                        break;
                }
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

            case com.veeritsolution.android.anivethub.R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case com.veeritsolution.android.anivethub.R.id.btn_save:
                if (edtPayPalId.getText().toString().isEmpty()) {
                    edtPayPalId.requestFocus();
                    ToastHelper.getInstance().showMessage("Please enter your PayPal Account Id");

                } else if (!edtPayPalId.getText().toString().matches(Patterns.EMAIL_ADDRESS.pattern())) {
                    edtPayPalId.requestFocus();
                    ToastHelper.getInstance().showMessage("Please enter your valid PayPal Account Id");
                } else {
                    vetAccountInsert();
                }
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (addEditSignUp != Constants.FROM_SIGN_UP) {
            inflater.inflate(com.veeritsolution.android.anivethub.R.menu.other_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.veeritsolution.android.anivethub.R.id.action_home) {
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (addEditSignUp != Constants.FROM_SIGN_UP)
            homeActivity.popBackFragment();
        else
            homeActivity.finish();
    }

    private void getVetAccountInfo() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_ACCOUNT_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_ACCOUNT_INFO,
                    true, RequestCode.GetVetAccountInfo, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vetAccountInsert() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_ACCOUNT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("PaypalAccountId", edtPayPalId.getText().toString());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_ACCOUNT_INSERT,
                    true, RequestCode.VetAccountInsert, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
