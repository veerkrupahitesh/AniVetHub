package com.veeritsolution.android.anivethub.fragment.vet;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpVetSubscription;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetSubscriptionModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ${hitesh} on 2/1/2017.
 */

public class VetSubsFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    //xml components
    private Button btnAddMoreCredits;
    private ListView lvSubscription;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoSubscription;

    //object and variable declaration
    private JSONObject params;
    private List<VetSubscriptionModel> vetSubscriptionList;
    private AdpVetSubscription adpVetSubscriptionList;
    private HomeActivity homeActivity;
    private View rootView;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_subscription, container, false);

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

        tvHeader = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_subscription_info));

        lvSubscription = (ListView) rootView.findViewById(R.id.listView_subscription);

        btnAddMoreCredits = (Button) rootView.findViewById(R.id.btn_buyCredits);
        btnAddMoreCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvNoSubscription = (TextView) rootView.findViewById(R.id.tv_noSubscriptionInfo);
        tvNoSubscription.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSubScriptingData();

    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetSubscriptionInfoAll:

                rootView.setVisibility(View.VISIBLE);
                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        lvSubscription.setVisibility(View.GONE);
                        tvNoSubscription.setVisibility(View.VISIBLE);
                    } else {

                        List<SessionModel> list = (List<SessionModel>) mObject;

                        if (!list.isEmpty()) {

                            vetSubscriptionList = (List<VetSubscriptionModel>) mObject;

                            if (!vetSubscriptionList.isEmpty()) {
                                adpVetSubscriptionList = new AdpVetSubscription(getActivity(), vetSubscriptionList);
                                lvSubscription.setAdapter(adpVetSubscriptionList);
                            } else {

                            }
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
        // homeActivity.popBackFragment();
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case com.veeritsolution.android.anivethub.R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case com.veeritsolution.android.anivethub.R.id.btn_buyCredits:
                //homeActivity.pushFragment(new VetSubsInfoFragment(), true, true, null);
                homeActivity.pushFragment(new VetSubscriptionInfo(), true, true, null);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(com.veeritsolution.android.anivethub.R.menu.other_menu, menu);
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

    private void getSubScriptingData() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_SUBSCRIPTION_INFO_ALL);
            params.put("AuthKey", ApiList.AUTH_KEY);
            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            } else {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            }

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_SUBSCRIPTION_INFO_ALL,
                    true, RequestCode.GetVetSubscriptionInfoAll, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
