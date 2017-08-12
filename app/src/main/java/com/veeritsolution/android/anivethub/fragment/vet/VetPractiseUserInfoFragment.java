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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.SpinnerUserAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/31/2017.
 */

public class VetPractiseUserInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private View rootView;
    private HomeActivity homeActivity;
    private Toolbar toolbar;
    private TextView tvHeader;
    private Spinner spVetPractiseUser;

    // object and variable declaration
    private SpinnerUserAdapter adapter;
    private ArrayList<VetPracticeUserModel> vetPractiselist;
    private JSONObject params;
    private VetPracticeUserModel vetPracticeUserModel;
    //int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_insert_vet_practise, container, false);

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

        spVetPractiseUser = (Spinner) rootView.findViewById(R.id.sp_vetPracticeUser);

        spVetPractiseUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvSpinnerItem = (TextView) view.findViewById(R.id.spinnerItem);
                if (tvSpinnerItem != null) {
                    vetPracticeUserModel = (VetPracticeUserModel) tvSpinnerItem.getTag();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_vet_practise_user_detail));

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetVetPractiseInfoAll();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetVetPractiseInfo:

                if (mObject instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) mObject;
                    ToastHelper.getInstance().showMessage(errorModel.getError());
                } else {
                    vetPractiselist = (ArrayList<VetPracticeUserModel>) mObject;
                    VetPracticeUserModel vetPracticeUserModel = new VetPracticeUserModel();
                    vetPracticeUserModel.setVetPractiseName("Select Vet Practice");
                    vetPractiselist.add(0, vetPracticeUserModel);
                    adapter = new SpinnerUserAdapter(getActivity(), vetPractiselist);
                    spVetPractiseUser.setAdapter(adapter);

                }
                break;

            case VetPractiseInsert:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    homeActivity.popBackFragment();
                }
                break;


        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        switch (mRequestCode) {

            case GetVetPractiseInfo:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetPractiseInsert:

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

            case R.id.btn_save:
                if (spVetPractiseUser.getSelectedItemPosition() == 0) {
                    spVetPractiseUser.requestFocus();
                    ToastHelper.getInstance().showMessage("Select Vet Practice Name");
                } else {
                    VetPractiseInsert();
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
        if (id == R.id.action_home) {
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetVetPractiseInfoAll() {
        params = new JSONObject();
        try {
            params.put("op", "GetVetPractiseInfo");
            // params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_PRACTICE_INFO,
                    true, RequestCode.GetVetPractiseInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void VetPractiseInsert() {
        try {
            params.put("op", "VetPractiseInsert");
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("VetPractiseId", vetPracticeUserModel.getVetPractiseId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_PRACTICE_INSERT,
                    true, RequestCode.VetPractiseInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
