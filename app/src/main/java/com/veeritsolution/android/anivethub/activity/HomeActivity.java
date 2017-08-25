package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.client.ClientBasicInfoFragment;
import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseBasicInfo;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetBasicInfoFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HomeActivity extends AppCompatActivity implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // object and variable declaration
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private Intent intent;
    private OnBackPressedEvent onBackPressedEvent;
    //private Bundle bundle;
    private JSONObject params;
    private OnClickEvent onClickEvent;
    private Map<String, String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        intent = getIntent();
        // bundle = intent.getExtras();
        fragmentManager = getSupportFragmentManager();

        PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, System.currentTimeMillis());
        PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG_BANNER, System.currentTimeMillis());

        if (intent.getBooleanExtra(Constants.IS_FROM_SIGN_UP, false)) {
            Bundle bundle = new Bundle();
            //  bundle.putBoolean(Constants.IS_FROM_SIGN_UP, true);
            bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_SIGN_UP);

            if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {

                loadFirstFragment(new ClientBasicInfoFragment(), bundle);
            } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.VET_LOGIN) {

                loadFirstFragment(new VetBasicInfoFragment(), bundle);
            } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLINIC_LOGIN) {

                loadFirstFragment(new PractiseBasicInfo(), bundle);
            }

        } else {

            if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {

                loadFirstFragment(new ClientHomeFragment(), null);
                // loadFirstFragment(new UpdateClientPetInfo(), null);

            } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.VET_LOGIN) {
                // if (Utils.isInternetAvailable()) {
                //     vetStatusUpdate(Constants.ONLINE_STATUS);
                // }
                loadFirstFragment(new VetHomeFragment(), null);
            } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLINIC_LOGIN) {

                loadFirstFragment(new PractiseHomeFragment(), null);
                //loadFirstFragment(new PractiseHomeFragment(), null);
            }
        }
//        Bundle bundle = intent.getExtras();
//        if (intent.hasExtra(Constants.KEY_FROM_NOTIFICATION)) {
//            values = bundle.getParcelable(Constants.KEY_NOTIFICATION_MESSAGE_DATA);
//        }
    }

    private void loadFirstFragment(Fragment fragment, Bundle bundle) {

        pushFragment(fragment, true, false, bundle);

    }


    @Override
    public void onBackPressed() {

        if (onBackPressedEvent != null)
            onBackPressedEvent.onBackPressed();

    }

    public void pushFragment(Fragment fragment, boolean addToBackStack, boolean shouldAnimate, Bundle bundle) {

        currentFragment = fragment;
        onClickEvent = (OnClickEvent) fragment;
        onBackPressedEvent = (OnBackPressedEvent) fragment;

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        transaction = fragmentManager.beginTransaction();

        if (shouldAnimate) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        }
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getCanonicalName());
        }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.frameLayout_home, fragment, fragment.getClass().getCanonicalName());

        //currentFragment = (Fragment) onClickEvent;

        // Commit the transaction
        transaction.commit();
    }

    public void popBackFragment() {

        try {

            int backStackCount = fragmentManager.getBackStackEntryCount();
            if (backStackCount > 1) {

                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount - 2);
                String str = backEntry.getName();
                Fragment fragment = fragmentManager.findFragmentByTag(str);

                currentFragment = fragment;
                onClickEvent = (OnClickEvent) fragment;
                onBackPressedEvent = (OnBackPressedEvent) fragment;

                fragmentManager.popBackStack();
            } else {
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Remove all fragments from back stack*/
    public void removeAllFragment() {

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if (fragmentsCount > 0) {
            // MyApplication.disableFragmentAnimations = true;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //		manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();

            // MyApplication.disableFragmentAnimations = false;
            //fragmentManager.popBackStack("myfancyname", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //removeFragmentUntil(DrinkListFragment);
    }

    /*Remove Fragments until provided Fragment class*/
    public void removeFragmentUntil(Class<?> fragmentClass) {

        try {
            int backStackCountMain = fragmentManager.getBackStackEntryCount();
            if (backStackCountMain > 1) {
                /*Note: To eliminate pop menu fragments and push base menu fragment animation effect at a same times*/
                //MyApplication.disableFragmentAnimations = true;
                int backStackCount = backStackCountMain;
                for (int i = 0; i < backStackCountMain; i++) {
                    FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount - 1);
                    String str = backEntry.getName();
                    Fragment fragment = fragmentManager.findFragmentByTag(str);
                    if (fragment.getClass().getCanonicalName().equals(fragmentClass.getCanonicalName())) {
                        currentFragment = fragment;
                        onClickEvent = (OnClickEvent) fragment;
                        onBackPressedEvent = (OnBackPressedEvent) fragment;
                        break;
                    } else
                        fragmentManager.popBackStack();

                    backStackCount--;
                }

            } else
                finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        onClickEvent.onClick(view);
        switch (view.getId()) {
            case R.id.btn_alertOk:
                CustomDialog.getInstance().dismiss();
                // finish();
                break;

            /*case R.id.btn_actionOk:

                int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
                switch (loginUser) {

                    case Constants.CLIENT_LOGIN:

                        sendClientConfirmation();
                        break;

                    case Constants.VET_LOGIN:
                        sendVetConfirmation();
                        break;

                    case Constants.CLINIC_LOGIN:

                        break;
                }
                break;

            case R.id.btn_actionCancel:


                break;*/
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomDialog.getInstance().dismiss();

        /*int userLogin = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);

        switch (userLogin) {
            case Constants.CLIENT_LOGIN:

                break;
            case Constants.VET_LOGIN:
                vetStatusUpdate(Constants.OFFLINE_STATUS);
                break;
            case Constants.CLINIC_LOGIN:

                break;
        }*/
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetStatusUpdate:
                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int status = jsonObject.getInt("OnlineStatus");
                    if (status == 1) {
                        // ToastHelper.getInstance().showMessage(getString(R.string.str_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CHECK_SETTINGS) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void vetStatusUpdate(int status) {

        try {
            params = new JSONObject();
            params.put("op", "VetStatusUpdate");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("OnlineStatus", status);

            RestClient.getInstance().post(this, Request.Method.POST, params,
                    ApiList.VET_STATUS_UPDATE, false, RequestCode.VetStatusUpdate, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
