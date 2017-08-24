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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpManagePracticeVet;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PracticeModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hitesh on 8/22/2017.
 */

public class ManagePracticeVetFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    //private Button btnAddNewEduInfo;
    private TextView tvHeader/*, tvName, tvUserName, tvNoEducationInfo, tvPassingYear, navHeaderName, navHeaderLocation*/;
    private Toolbar toolbar;
    // private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private ListView listViewVetInfo;
    // private View headerView;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private ArrayList<PracticeModel> practiceList;
    private AdpManagePracticeVet adpManagePracticeVet;
    private HomeActivity homeActivity;
    private PracticeModel practiceModel;
    // private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_manage_practice_vet, container, false);
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
        tvHeader.setText(getString(R.string.str_vet));
        //rootView.setVisibility(View.INVISIBLE);

        listViewVetInfo = (ListView) rootView.findViewById(R.id.listView_vetInfo);

        getVetData();
        return rootView;
    }

    private void getVetData() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_PRACTICE_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetPractiseId", PractiseLoginModel.getPractiseCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PRACTICE_VET,
                    true, RequestCode.GetPracticeVet, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPracticeVet:
                if (mObject instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) mObject;
                    ToastHelper.getInstance().showMessage(errorModel.getError());
                    //listViewEducation.setVisibility(View.GONE);
                    //tvNoEducationInfo.setVisibility(View.VISIBLE);
                } else {
                    practiceList = (ArrayList<PracticeModel>) mObject;
                    if (practiceList != null) {

                        adpManagePracticeVet = new AdpManagePracticeVet(practiceList);
                        listViewVetInfo.setAdapter(adpManagePracticeVet);
                    }
                }
                break;

            case AcceptVet:
                practiceModel.setFlag(1);
                practiceList.set(practiceModel.getPosition(), practiceModel);
                adpManagePracticeVet.refreshList(practiceList);
                break;

            case RejectVet:
                practiceModel.setFlag(2);
                practiceList.set(practiceModel.getPosition(), practiceModel);
                adpManagePracticeVet.refreshList(practiceList);
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_acceptVet:
                Object object = view.getTag();
                if (object != null) {
                    practiceModel = (PracticeModel) object;
                    doAcceptVet(practiceModel);
                }
                break;

            case R.id.img_rejectVet:
                object = view.getTag();
                if (object != null) {
                    practiceModel = (PracticeModel) object;
                    doRejectVet(practiceModel);
                }
                break;
        }
    }

    private void doRejectVet(PracticeModel practiceModel) {
        try {
            params = new JSONObject();
            params.put("op", ApiList.REJECT_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetPractiseId", PractiseLoginModel.getPractiseCredentials().getVetId());
            params.put("VetId", practiceModel.getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.REJECT_VET,
                    true, RequestCode.RejectVet, this);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void doAcceptVet(PracticeModel practiceModel) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.ACCEPT_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetPractiseId", PractiseLoginModel.getPractiseCredentials().getVetId());
            params.put("VetId", practiceModel.getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.ACCEPT_VET,
                    true, RequestCode.AcceptVet, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // if (!fromSignUp) {
        inflater.inflate(R.menu.other_menu, menu);
        // }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            // if (!fromSignUp) {
            homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            //  }
        }

        return super.onOptionsItemSelected(item);
    }
}
