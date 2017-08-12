package com.veeritsolution.android.anivethub.fragment.practise;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.activity.SignInActivity;
import com.veeritsolution.android.anivethub.adapter.AdpGridView;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.RegisterBy;
import com.veeritsolution.android.anivethub.fragment.SettingFragment;
import com.veeritsolution.android.anivethub.fragment.TermsAndConditionsFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetAppFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetHIWFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetNotiFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetSessionFragmnet;
import com.veeritsolution.android.anivethub.fragment.vet.VetSubsFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.GeneralSettingsModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by veerk on 4/1/2017.
 */

public class PractiseHomeFragment extends Fragment implements OnClickEvent, DataObserver,
        OnBackPressedEvent, NavigationView.OnNavigationItemSelectedListener {

    // xml components
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectClientProfilePhoto, imgSelectClientBannerPhoto, imgNavProfilePhoto;
    private GridView gvdHomeCategory;
    private TextView tvHeader, tvName, tvUserName, navHeaderName, navHeaderLocation;
    private View rootView;
    private ArrayList<String> prefDeleteList;

    // object and variable declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private Bundle bundle;
    private int homeImages[] = {R.drawable.img_home_my_profile, R.drawable.img_home_session_summery,
            R.drawable.img_home_my_appoint, R.drawable.img_home_my_subscription};
    private String homeCategory[] = {"My Profile", "My Sessions", "My Appointments", "My Subscriptions"};

    private GeneralSettingsModel generalSettingsModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();

        //  setHasOptionsMenu(true);
        SignInActivity.initFacebookSdk();
        SignInActivity.initGoogleSdk();
        SignInActivity.mGoogleApiClient.connect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_home, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // toolbar.setTitle(getString(R.string.app_name));
        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.app_name_header));
        drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header);

        navHeaderName = (TextView) navHeaderView.findViewById(R.id.tv_navHeaderName);
        navHeaderName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        navHeaderLocation = (TextView) navHeaderView.findViewById(R.id.tv_navHeaderLocation);
        navHeaderLocation.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        imgNavProfilePhoto = (ImageView) navHeaderView.findViewById(R.id.img_navHeaderProfilePhoto);

        gvdHomeCategory = (GridView) rootView.findViewById(R.id.gv_home);

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);

        imgSelectClientProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectClientProfilePhoto.setVisibility(View.INVISIBLE);

        imgSelectClientBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectClientBannerPhoto.setVisibility(View.INVISIBLE);


        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setProfileImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgNavProfilePhoto);

        Utils.setProfileImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);

        Utils.setBannerImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        tvName.setText(PractiseLoginModel.getPractiseCredentials().getVetName());
        tvUserName.setText(PractiseLoginModel.getPractiseCredentials().getUserName());

        navHeaderName.setText(PractiseLoginModel.getPractiseCredentials().getVetName());
        navHeaderLocation.setText(PractiseLoginModel.getPractiseCredentials().getCountry());

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemTextColor(ResourcesCompat.getColorStateList(getResources(), R.color.homeCategory, null));
            navigationView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        Utils.setTypeFace(subMenuItem, MyApplication.getInstance().FONT_ROBOTO_LIGHT);
                    }
                }
                //the method we have create in activity
                Utils.setTypeFace(mi, MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            }
        }

        gvdHomeCategory.setAdapter(new AdpGridView(homeImages, homeCategory, getActivity()));

        PractiseLoginModel vetLoginModel = PractiseLoginModel.getPractiseCredentials();

        if (vetLoginModel.getIsVetProfile() == 0) {
            homeActivity.pushFragment(new UpdatePracBasicInfoFragment(), true, false, null);
        } else if (vetLoginModel.getIsVetAccount() == 0) {
            homeActivity.pushFragment(new UpdatePracAccInfoFragment(), true, false, null);
        }
        getGeneralSettings();
    }


    @Override
    public void onStop() {
        super.onStop();
        CustomDialog.getInstance().dismiss();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_notification:
                homeActivity.pushFragment(new VetNotiFragment(), true, false, null);
                break;

            case R.id.nav_terms_of_use:
                homeActivity.pushFragment(new TermsAndConditionsFragment(), true, false, null);
                break;

            case R.id.nav_settings:
                homeActivity.pushFragment(new SettingFragment(), true, false, null);
                break;

            case R.id.nav_howItWorks:
                homeActivity.pushFragment(new VetHIWFragment(), true, false, null);
                break;

            case R.id.nav_logout:

                String registerBy = null;

                if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {
                    registerBy = ClientLoginModel.getClientCredentials().getRegisteredBy();
                } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.VET_LOGIN) {
                    registerBy = VetLoginModel.getVetCredentials().getRegisterBy();
                } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLINIC_LOGIN) {
                    registerBy = PractiseLoginModel.getPractiseCredentials().getRegisterBy();
                }

                if (registerBy.equals(RegisterBy.APP.getRegisterBy())) {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, false);

                } else if (registerBy.equals(RegisterBy.GOOGLE.getRegisterBy())) {
                    SignInActivity.signOutToGoogle();
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, false);
                } else if (registerBy.equals(RegisterBy.FACEBOOK.getRegisterBy())) {
                    SignInActivity.logoutToFacebook();
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, false);
                } else {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, false);
                }

                prefDeleteList = new ArrayList<>();
                // prefDeleteList.add(PrefHelper.FIRST_TIME_APP_OPEN);
                prefDeleteList.add(PrefHelper.FIREBASE_DEVICE_TOKEN);
                PrefHelper.getInstance().deletePreferencesExcept(prefDeleteList);

                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                homeActivity.finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetGeneralSettings:

                if (PractiseLoginModel.getPractiseCredentials() != null)
                    setVetToken();
                generalSettingsModel = (GeneralSettingsModel) mObject;
                GeneralSettingsModel.saveGeneralSettings(generalSettingsModel);
                break;

            case SetVetTokenId:

                //    ToastHelper.getInstance().showMessage("token updated!");
                break;

            case SetVetLocation:

                //  ToastHelper.getInstance().showMessage("Location updated!");

                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {

        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                homeActivity.popBackFragment();
            }
        } else {
            homeActivity.popBackFragment();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_homeGridView:
                Utils.buttonClickEffect(view);
                int position = (int) view.getTag(R.integer.int_key);
                // Intent intent;
                switch (position) {

                    case 0:

                        bundle = new Bundle();
                        bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
                        homeActivity.pushFragment(new PractiseProfileFragment(), true, false, null);
                        break;

                    case 1:

                        homeActivity.pushFragment(new VetSessionFragmnet(), true, false, null);
                        break;

                    case 2:

                        homeActivity.pushFragment(new VetAppFragment(), true, false, null);
                        break;

                    case 3:

                        homeActivity.pushFragment(new VetSubsFragment(), true, false, null);
                        break;
                }
                break;

            case R.id.img_cancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                // homeActivity.finish();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            // homeActivity.popBackFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getGeneralSettings() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_GENERAL_SETTINGS);
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_GENERAL_SETTINGS, false, RequestCode.GetGeneralSettings, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setVetToken() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.SET_VET_TOKEN_ID);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            params.put("AcTokenId", PrefHelper.getInstance().getString(PrefHelper.FIREBASE_DEVICE_TOKEN, ""));
            params.put("DeviceType", Constants.DEFAULT_VALUE_DEVICE_TYPE_ANDROID);
            params.put("DeviceBrand", Build.BRAND);
            params.put("DeviceModel", Build.DEVICE + " " + Build.MODEL);
            params.put("DeviceProduct", Build.PRODUCT);
            params.put("DeviceSDKVersion", Build.VERSION.SDK_INT);

            RestClient.getInstance().post(MyApplication.getInstance(), Request.Method.POST, params,
                    ApiList.SET_VET_TOKEN_ID, false, RequestCode.SetVetTokenId, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

