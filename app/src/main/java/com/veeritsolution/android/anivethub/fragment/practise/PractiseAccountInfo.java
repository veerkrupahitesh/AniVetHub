package com.veeritsolution.android.anivethub.fragment.practise;

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
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetAccountInfoModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

/**
 * Created by veerk on 3/31/2017.
 */

public class PractiseAccountInfo extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

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

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
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
                break;
        }
        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(PractiseLoginModel.getPractiseCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(PractiseLoginModel.getPractiseCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getBannerPic(),
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
        if (addEditSignUp == Constants.FROM_EDIT) {
            getVetPracAccountInfo();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetPracAccountInfo:

                rootView.setVisibility(View.VISIBLE);
                vetAccountInfoModel = (VetAccountInfoModel) mObject;
                edtPayPalId.setText(vetAccountInfoModel.getPaypalAccountId());
                break;

            case VetPracAccountInsert:
                PractiseLoginModel practiseLoginModel = PractiseLoginModel.getPractiseCredentials();
                practiseLoginModel.setIsVetAccount(1);
                PractiseLoginModel.savePractiseCredentials(RestClient.getGsonInstance().toJson(practiseLoginModel));
                switch (addEditSignUp) {

                    case Constants.FROM_ADD:
                        homeActivity.popBackFragment();
                        break;
                    case Constants.FROM_EDIT:
                        homeActivity.popBackFragment();
                        break;
                    case Constants.FROM_SIGN_UP:
                        homeActivity.removeAllFragment();
                        homeActivity.pushFragment(new PractiseHomeFragment(), true, false, null);
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
    public void onBackPressed() {
        if (addEditSignUp != Constants.FROM_SIGN_UP)
            homeActivity.popBackFragment();
        else
            homeActivity.finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

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
                    ToastHelper.getInstance().showMessage("Please enter your PayPal Account Id");
                } else {
                    vetAccountInsert();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.other_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.veeritsolution.android.anivethub.R.id.action_home) {
            homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getVetPracAccountInfo() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_ACCOUNT_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_ACCOUNT_INFO,
                    true, RequestCode.GetVetPracAccountInfo, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vetAccountInsert() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_ACCOUNT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            params.put("PaypalAccountId", edtPayPalId.getText().toString());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_ACCOUNT_INSERT,
                    true, RequestCode.VetPracAccountInsert, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
