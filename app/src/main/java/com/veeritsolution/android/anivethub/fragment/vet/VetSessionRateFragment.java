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
import com.veeritsolution.android.anivethub.adapter.AdpVetSessionRate;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetSessionRateModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.List;

/**
 * 0
 * Created by admin on 3/16/2017.
 */

public class VetSessionRateFragment extends Fragment implements DataObserver, OnClickEvent, OnBackPressedEvent {

    //xml components
    private TextView tvHeader, tvNoSessionRateInfo;
    private ListView lvSessionRate;
    private Toolbar toolbar;
    private View rootView;
    private Button btnAddSessionRate;

    //variables and object declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private AdpVetSessionRate adapter;
    private List<VetSessionRateModel> vetSessionRateModelArrayList;
    private Bundle bundle;
    private VetSessionRateModel vetSessionRateModel;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        setHasOptionsMenu(true);

        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_session_list, container, false);
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
        tvHeader.setText(getString(R.string.str_session_rate));

        lvSessionRate = (ListView) rootView.findViewById(R.id.lstsessionrate);
        tvNoSessionRateInfo = (TextView) rootView.findViewById(R.id.tv_noSessionRateInfo);
        tvNoSessionRateInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnAddSessionRate = (Button) rootView.findViewById(R.id.btn_addSessionRate);
        btnAddSessionRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getVetRateInfoAll();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetSessionRateInfoAll:

                rootView.setVisibility(View.VISIBLE);
                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        lvSessionRate.setVisibility(View.GONE);
                        tvNoSessionRateInfo.setVisibility(View.VISIBLE);
                    } else {

                        vetSessionRateModelArrayList = (List<VetSessionRateModel>) mObject;
                        adapter = new AdpVetSessionRate(getActivity(), vetSessionRateModelArrayList);
                        lvSessionRate.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case VetRateDelete:

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        lvSessionRate.setVisibility(View.GONE);
                        tvNoSessionRateInfo.setVisibility(View.VISIBLE);

                    } else {

                        if (!vetSessionRateModelArrayList.isEmpty()) {
                            vetSessionRateModelArrayList.remove(vetSessionRateModel);
                            adapter.notifyDataSetChanged();

                            if (vetSessionRateModelArrayList.isEmpty()) {
                                lvSessionRate.setVisibility(View.GONE);
                                tvNoSessionRateInfo.setVisibility(View.VISIBLE);
                            } else {
                                lvSessionRate.setVisibility(View.VISIBLE);
                                tvNoSessionRateInfo.setVisibility(View.GONE);
                            }
                        } else {
                            lvSessionRate.setVisibility(View.VISIBLE);
                            tvNoSessionRateInfo.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case VetRateUpdate:

                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {
            case GetVetSessionRateInfoAll:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetRateDelete:

                ToastHelper.getInstance().showMessage(mError);
                break;
            case VetRateUpdate:

                ToastHelper.getInstance().showMessage(mError);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_addSessionRate:

                bundle = new Bundle();
                bundle.putSerializable(Constants.VET_SESSION_RATE_DATA, vetSessionRateModel);
                bundle.putInt("flag", Constants.VET_SESSION_RATE_FLAG_ADD);
                homeActivity.pushFragment(new VetSessionRateInfoFragment(), true, false, bundle);
                break;

            case R.id.img_delete:
                Utils.buttonClickEffect(view);
                vetSessionRateModel = (VetSessionRateModel) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_session_rate),
                        getString(R.string.str_want_to_delete), false);
                break;

            case R.id.img_edit:

                Utils.buttonClickEffect(view);
                vetSessionRateModel = (VetSessionRateModel) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.VET_SESSION_RATE_DATA, vetSessionRateModel);
                bundle.putInt("flag", Constants.VET_SESSION_RATE_FLAG_EDIT);
                homeActivity.pushFragment(new VetSessionRateInfoFragment(), true, false, bundle);
                break;

            case R.id.btn_actionCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_actionOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                vetRateDelete();
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

            if (loginUser == Constants.CLINIC_LOGIN) {
                homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
                return true;
            } else if (loginUser == Constants.VET_LOGIN) {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
                return true;
            }
        }
        return true;
    }

    private void getVetRateInfoAll() {
        params = new JSONObject();
        try {
            params.put("op", "GetVetRateInfoAll");
            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            }
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_RATE_INFO_ALL,
                    true, RequestCode.GetVetSessionRateInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void vetRateDelete() {
        params = new JSONObject();
        try {
            params.put("op", "VetRateDelete");
            params.put("VetRateId", vetSessionRateModel.getVetRateId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_RATE_DELETE,
                    true, RequestCode.VetRateDelete, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
