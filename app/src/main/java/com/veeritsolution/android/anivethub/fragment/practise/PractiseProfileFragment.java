package com.veeritsolution.android.anivethub.fragment.practise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.vet.VetSessionRateFragment;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

/**
 * Created by veerk on 4/1/2017.
 */

public class PractiseProfileFragment extends Fragment implements OnClickEvent, OnBackPressedEvent {

    // xml components
    private TextView tvName, tvUserName, tvHeader;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private TextView tvBasicInfo, tvPayPalInfo, tvSessionRateInfo;

    // object and variable declaration
    private HomeActivity homeActivity;
    private Bundle bundle;
    private TextView tvManageVet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_vet_practise_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_my_profile));

        tvName = (TextView) getView().findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(PractiseLoginModel.getPractiseCredentials().getVetName());

        tvUserName = (TextView) getView().findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(PractiseLoginModel.getPractiseCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) getView().findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) getView().findViewById(R.id.img_vet_bannerPic);

        Utils.setBannerImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        imgSelectBannerPhoto = (ImageView) getView().findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) getView().findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        tvBasicInfo = (TextView) getView().findViewById(R.id.tv_basicInfo);
        tvBasicInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPayPalInfo = (TextView) getView().findViewById(R.id.tv_payPalInfo);
        tvPayPalInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionRateInfo = (TextView) getView().findViewById(R.id.tv_sessionRate);
        tvSessionRateInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvManageVet = (TextView) getView().findViewById(R.id.tv_addVet);
        tvManageVet.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_addBasicInfo:

                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_EDIT);
                homeActivity.pushFragment(new PractiseBasicInfo(), true, true, bundle);
                break;

            case R.id.lin_addPayPalInfo:

                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_EDIT);
                homeActivity.pushFragment(new PractiseAccountInfo(), true, true, bundle);
                break;

            case R.id.lin_addSessionRate:

                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
                homeActivity.pushFragment(new VetSessionRateFragment(), true, true, bundle);
                break;

            case R.id.lin_addVet:

                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
                homeActivity.pushFragment(new ManagePracticeVetFragment(), true, true, bundle);
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
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
        if (id == R.id.action_home) {
            homeActivity.popBackFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }
}
