package com.veeritsolution.android.anivethub.fragment.vet;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.GeneralSettingsModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.sinch.SinchService;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.PermissionClass;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/17/2016.
 */

public class VetHomeFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>, ServiceConnection, OnClickEvent, DataObserver,
        OnBackPressedEvent, NavigationView.OnNavigationItemSelectedListener {

    // location related
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double lat, lon, altitude;

    // xml components
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectClientProfilePhoto, imgSelectClientBannerPhoto, imgNavProfilePhoto;
    private GridView gvdHomeCategory;
    private TextView tvHeader, tvName, tvUserName, navHeaderName, navHeaderLocation, tvOnlineStatus;
    private View rootView;
    private ArrayList<String> prefDeleteList;

    // object and variable declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private Bundle bundle;
    private int homeImages[] = {R.drawable.img_home_my_profile, R.drawable.img_home_session_summery,
            R.drawable.img_home_my_appoint, R.drawable.img_home_my_subscription,
            /*R.drawable.img_home_cam, R.drawable.img_home_my_appoint, R.drawable.img_home_session_summery*/};
    private String homeCategory[] = {"My Profile", "My Sessions", "My Appointments", "My Subscriptions"/*, "Experience", "My Appointments", "Session Summery"*/};

    private SinchService.SinchServiceInterface mSinchServiceInterface;
    private ArrayList<String> callPermissionList;
    private GeneralSettingsModel generalSettingsModel;
    private Dialog mDialog;
    private boolean serviceBind = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();

        //  setHasOptionsMenu(true);
        SignInActivity.initFacebookSdk();
        SignInActivity.initGoogleSdk();
        SignInActivity.mGoogleApiClient.connect();

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        serviceBind = MyApplication.getInstance().
                bindService(new Intent(MyApplication.getInstance(), SinchService.class), this, Activity.BIND_AUTO_CREATE);

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

        tvOnlineStatus = (TextView) rootView.findViewById(R.id.tv_onLineStatus);
        tvOnlineStatus.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

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

        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgNavProfilePhoto);

        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);

        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        tvName.setText(VetLoginModel.getVetCredentials().getVetName());
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        navHeaderName.setText(VetLoginModel.getVetCredentials().getVetName());
        navHeaderLocation.setText(VetLoginModel.getVetCredentials().getCountry());

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

        VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();

        if (vetLoginModel.getIsVetProfile() == 0) {
            homeActivity.pushFragment(new UpdateVetBasicInfoFragment(), true, false, null);
        } else if (vetLoginModel.getIsVetAccount() == 0) {
            homeActivity.pushFragment(new UpdateVetAccInfoFragment(), true, false, null);
        } else if (vetLoginModel.getIsVetEducation() == 0) {
            homeActivity.pushFragment(new UpdateVetEduInfoFragment(), true, false, null);
        } else if (vetLoginModel.getIsVetExperience() == 0) {
            homeActivity.pushFragment(new UpdateVetExpInfoFragment(), true, false, null);
        } else if (vetLoginModel.getIsVetExpertise() == 0) {
            homeActivity.pushFragment(new UpdateVetSpecInfoFragment(), true, false, null);
        }

        tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drw_circle_gradient_green, null));
        vetStatusUpdate(Constants.ONLINE_STATUS);
        getGeneralSettings();
    }

    @Override
    public void onStart() {
        super.onStart();

        callPermissionList = new ArrayList<>();
        callPermissionList.add(Manifest.permission.INTERNET);
        callPermissionList.add(Manifest.permission.RECORD_AUDIO);
        callPermissionList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        callPermissionList.add(Manifest.permission.READ_PHONE_STATE);
        callPermissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        callPermissionList.add(Manifest.permission.CAMERA);
        callPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        callPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, callPermissionList);

        mGoogleApiClient.connect();

        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient.isConnected() && mGoogleApiClient != null) {

            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
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

                vetStatusUpdate(Constants.OFFLINE_STATUS);

                prefDeleteList = new ArrayList<>();
                prefDeleteList.add(PrefHelper.LOGIN_USER);
                // prefDeleteList.add(PrefHelper.VET_LOGIN_CREDENTIALS);
                prefDeleteList.add(PrefHelper.FIREBASE_DEVICE_TOKEN);
                PrefHelper.getInstance().deletePreferencesExcept(prefDeleteList);

                if (serviceBind) {
                    MyApplication.getInstance().unbindService(this);
                }
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                homeActivity.finish();
                homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

        // DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetGeneralSettings:

                if (VetLoginModel.getVetCredentials() != null) {
                    setVetToken();
                }

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        generalSettingsModel = (GeneralSettingsModel) mObject;
                        GeneralSettingsModel.saveGeneralSettings(generalSettingsModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
// DrawerLayout drawer = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
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
                        homeActivity.pushFragment(new VetProfileFragment(), true, false, null);
                        break;

                    case 1:
                        bundle = new Bundle();
                        bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
                        homeActivity.pushFragment(new VetSessionFragmnet(), true, false, null);
                        //  intent = new Intent(getActivity(), ClientPetsActivity.class);
                        //  startActivity(intent);
                        break;

                    case 2:
                        bundle = new Bundle();
                        bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
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
                //  homeActivity.finish();
                break;

            case R.id.tv_onLineStatus:
                Utils.buttonClickEffect(view);
                ShowOnlineStatusDialog(homeActivity, false);
                break;

            case R.id.img_dialogCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {

                for (int i = 0; i < grantResults.length; i++) {
                    //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                    shouldPermit.add(permissions[i]);
                }
            }
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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) service;
        }
        if (!mSinchServiceInterface.isStarted() && mSinchServiceInterface != null) {
            if (VetLoginModel.getVetCredentials() != null) {
                mSinchServiceInterface.startClient(VetLoginModel.getVetCredentials().getUserName());
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (VetLoginModel.getVetCredentials() != null) {
            vetStatusUpdate(Constants.OFFLINE_STATUS);
        }
    }


    /* protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }*/

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
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        /* If the initial location was never previously requested, we use
         FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
         its value in the Bundle and check for it in onCreate(). We
         do not request it again unless the user specifically requests location updates by pressing
         the Start Updates button.

         Because we cache the value of the initial location in the Bundle, it means that if the
         user launches the activity,
         moves to a new location, and then changes the device orientation, the original location
         is displayed as the activity is re-created.*/

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

        // checkLocationSettings();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        buildGoogleApiClient();
        // checkLocationSettings();
        //showLocationsettings();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        //  mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        //  updateLocationUI();
        if (mLastLocation != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            altitude = location.getAltitude();

            Debug.trace("Location", lat + "," + lon + "," + altitude);

            if (VetLoginModel.getVetCredentials() != null)
                updateVetLocation();

        } else {
            checkLocationSettings();
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:

                Debug.trace(Constants.TAG, "All location settings are satisfied.");
                if (mGoogleApiClient.isConnected()) {
                    startLocationUpdates();
                }
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                Debug.trace(Constants.TAG, "LocationModel settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), Constants.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Debug.trace(Constants.TAG, "PendingIntent unable to execute request.");
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Debug.trace(Constants.TAG, "LocationModel settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case Constants.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //  Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        ToastHelper.getInstance().showMessage("You will be not available to client search list");
                        //   Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }


    private void updateVetLocation() {

        try {
            params = new JSONObject();
            params.put("op", "SetVetLocation");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("Latitude", lat);
            params.put("Longitude", lon);
            params.put("Altitude", altitude);

            if (Utils.isInternetAvailable()) {
                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.SET_VET_LOCATION,
                        false, RequestCode.SetVetLocation, this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void vetStatusUpdate(int status) {

        try {
            params = new JSONObject();
            params.put("op", "VetStatusUpdate");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("OnlineStatus", status);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.VET_STATUS_UPDATE, false, RequestCode.VetStatusUpdate, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(homeActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused LocationModel Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {

        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        ArrayList<String> permission = new ArrayList<>();
        permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (PermissionClass.checkPermission(homeActivity, Constants.REQUEST_LOCATION, permission)) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            mRequestingLocationUpdates = true;

                            if (!status.getStatus().isSuccess()) {

                            }
                            //  setButtonsEnabledState();
                        }
                    });

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {
                checkLocationSettings();
            }

        }
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            mRequestingLocationUpdates = false;
                            //  setButtonsEnabledState();
                        }
                    });
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    public void ShowOnlineStatusDialog(Context context, boolean mIsCancelable) {
        mDialog = new Dialog(context, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        // View view = LayoutInflater.from(mContext).inflate(R.layout.activity_custom_filter_dialog, null, false);
        mDialog.setContentView(R.layout.custom_dialog_vet_online_status);
      /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout llonline = (LinearLayout) mDialog.findViewById(R.id.lin_online);
        LinearLayout lloffline = (LinearLayout) mDialog.findViewById(R.id.lin_offline);
        LinearLayout llbusy = (LinearLayout) mDialog.findViewById(R.id.lin_busy);


        llonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                vetStatusUpdate(Constants.ONLINE_STATUS);
                tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drw_circle_gradient_green, null));
                dismiss();

            }
        });
        lloffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                vetStatusUpdate(Constants.OFFLINE_STATUS);
                tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drw_circle_gradient_red, null));
                dismiss();

            }
        });

        llbusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);
                vetStatusUpdate(Constants.BUSY_STATUS);
                tvOnlineStatus.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drw_circle_gradient_grey, null));
                dismiss();

            }
        });

        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_dialogCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDialog.setCancelable(mIsCancelable);
        try {
            if (mDialog != null) {
                if (!isDialogShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowing() {

        return mDialog != null && mDialog.isShowing();
    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (isDialogShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
