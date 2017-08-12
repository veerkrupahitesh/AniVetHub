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
import com.veeritsolution.android.anivethub.adapter.AdpVetPracticeUser;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/30/2017.
 */

public class VetPracticeUserFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent {

    // xml components
    private Button btnAddNewPracInfo;
    private View rootView;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoUser;
    private ListView lstVetPracticeUser;

    private HomeActivity homeActivity;
    private ArrayList<VetPracticeUserModel> vetPracticeUserModelList;
    private AdpVetPracticeUser adapter;
    private VetPracticeUserModel vetPracticeUserModel;
    private JSONObject params;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_practise_user, container, false);

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
        tvHeader.setText(getString(R.string.str_vet_practise_user));

        lstVetPracticeUser = (ListView) rootView.findViewById(R.id.listView_vetPractiseUser);

        tvNoUser = (TextView) rootView.findViewById(R.id.tv_noUserInfo);
        tvNoUser.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnAddNewPracInfo = (Button) rootView.findViewById(R.id.btn_addNewPracInfo);
        btnAddNewPracInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vetPracticeUserModelList = new ArrayList<>();
        GetVetPractiseInfoAll();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {
            case GetVetPractiseInfoAll:
                if (mObject != null) {

                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        tvNoUser.setVisibility(View.VISIBLE);
                        lstVetPracticeUser.setVisibility(View.GONE);
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        tvNoUser.setVisibility(View.GONE);
                        lstVetPracticeUser.setVisibility(View.VISIBLE);

                        vetPracticeUserModelList = (ArrayList<VetPracticeUserModel>) mObject;

                        if (!vetPracticeUserModelList.isEmpty()) {
                            // tvNoUser.setVisibility(View.GONE);

                            adapter = new AdpVetPracticeUser(getActivity(), vetPracticeUserModelList);
                            lstVetPracticeUser.setAdapter(adapter);
                        } else {
                            //tvNoUser.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;

            case GetVetPractiseDelete:

                if (mObject instanceof ErrorModel) {
                    vetPracticeUserModelList.remove(vetPracticeUserModel);
                    adapter.notifyDataSetChanged();
                    if (!vetPracticeUserModelList.isEmpty()) {
                        tvNoUser.setVisibility(View.GONE);
                        lstVetPracticeUser.setVisibility(View.VISIBLE);
                    } else {
                        tvNoUser.setVisibility(View.VISIBLE);
                        lstVetPracticeUser.setVisibility(View.GONE);
                    }
                } else {
                    vetPracticeUserModelList.remove(vetPracticeUserModel);
                    adapter.notifyDataSetChanged();
                    if (!vetPracticeUserModelList.isEmpty()) {
                        tvNoUser.setVisibility(View.GONE);
                        lstVetPracticeUser.setVisibility(View.VISIBLE);
                    } else {
                        tvNoUser.setVisibility(View.VISIBLE);
                        lstVetPracticeUser.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetVetPractiseInfoAll:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetVetPractiseDelete:

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

            case R.id.img_Delete:

                Utils.buttonClickEffect(view);
                vetPracticeUserModel = (VetPracticeUserModel) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_vetpractiseuser),
                        getString(R.string.str_want_to_delete), false);
                break;

            case R.id.btn_actionOk:

                CustomDialog.getInstance().dismiss();
                vetPractiseDelete();
                break;

            case R.id.btn_actionCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_addNewPracInfo:

                Utils.buttonClickEffect(view);
                homeActivity.pushFragment(new VetPractiseUserInfoFragment(), true, false, null);
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

        try {
            params = new JSONObject();
            params.put("op", "GetVetPractiseInfoAll");
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_PRACTICE_INFO_ALL,
                    true, RequestCode.GetVetPractiseInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void vetPractiseDelete() {

        try {
            params = new JSONObject();
            params.put("op", "VetPractiseDelete");
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("VetPractiseId", vetPracticeUserModel.getVetPractiseId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_PRACTICE_DELETE,
                    true, RequestCode.GetVetPractiseDelete, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
