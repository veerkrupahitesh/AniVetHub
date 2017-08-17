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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpVetSpecialisation;
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
import com.veeritsolution.android.anivethub.models.VetSpecialization;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veerk on 1/5/2017.
 */

public class VetSpecFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Button btnNewSpecialization;
    private TextView tvHeader, tvName, tvUserName, tvNoSpecializationInfo;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private ListView listViewExpertise;
    private View headerView;

    // object and variable declaration
    private JSONObject params;
    private List<VetSpecialization> vetSpecializationList;
    private AdpVetSpecialisation adpExpertise;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private VetSpecialization vetSpecialization;
    private View rootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        vetSpecializationList = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_expertise, container, false);

        // rootView.setVisibility(View.INVISIBLE);
        return rootView;
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
        tvHeader.setText(getString(R.string.str_specialization));

        //backButton = (ImageView) getView().findViewById(R.id.img_back_header);

        headerView = getView().findViewById(R.id.headerView);
        headerView.setVisibility(View.GONE);

        tvName = (TextView) getView().findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) getView().findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) getView().findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) getView().findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);


        imgSelectBannerPhoto = (ImageView) getView().findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) getView().findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        listViewExpertise = (ListView) getView().findViewById(R.id.listView_expertise);
        tvNoSpecializationInfo = (TextView) getView().findViewById(R.id.tv_noExpertiseInfo);
        tvNoSpecializationInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnNewSpecialization = (Button) getView().findViewById(R.id.btn_addMoreSpecialization);
        btnNewSpecialization.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        getVetSpecialisationData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetExpertiseInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                        listViewExpertise.setVisibility(View.GONE);
                    } else {

                        vetSpecializationList = (ArrayList<VetSpecialization>) mObject;

                        if (!vetSpecializationList.isEmpty()) {
                            tvNoSpecializationInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                            adpExpertise = new AdpVetSpecialisation(getActivity(), vetSpecializationList, false);
                            listViewExpertise.setAdapter(adpExpertise);
                        } else {
                            tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                            listViewExpertise.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case VetExpertiseDelete:

                try {
                    if (mObject instanceof ErrorModel) {

                        vetSpecializationList.remove(vetSpecialization);
                        adpExpertise.notifyDataSetChanged();

                        if (!vetSpecializationList.isEmpty()) {
                            tvNoSpecializationInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                        } else {
                            tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                            listViewExpertise.setVisibility(View.GONE);
                        }
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());

                    } else {

                        vetSpecializationList.remove(vetSpecialization);
                        adpExpertise.notifyDataSetChanged();

                        if (!vetSpecializationList.isEmpty()) {
                            tvNoSpecializationInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                        } else {
                            tvNoSpecializationInfo.setVisibility(View.VISIBLE);
                            listViewExpertise.setVisibility(View.GONE);
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

        switch (mRequestCode) {

            case GetVetExpertiseInfoAll:

                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetExpertiseDelete:

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

            case R.id.img_Edit:
                Utils.buttonClickEffect(view);
                vetSpecialization = (VetSpecialization) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.VET_EXPERTISE, vetSpecialization);
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_EDIT);
                homeActivity.pushFragment(new VetSpecInfoFragment(), true, false, bundle);
                break;

            case R.id.img_Delete:
                Utils.buttonClickEffect(view);
                vetSpecialization = (VetSpecialization) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_specialization),
                        getString(R.string.str_want_to_delete), false);

                break;

            case R.id.btn_actionOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();

                try {
                    params = new JSONObject();
                    params.put("op", "VetExpertiseDelete");
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("VetExpertiseId", vetSpecialization.getVetExpertiseId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERTISE_DELETE,
                            true, RequestCode.VetExpertiseDelete, this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_actionCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_addMoreSpecialization:
                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_ADD);
                homeActivity.pushFragment(new VetSpecInfoFragment(), true, false, bundle);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
            // homeActivity.popBackFragment();
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            // homeActivity.pushFragment(new ClientHomeFragment(), true, true, null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getVetSpecialisationData() {
        try {
            params = new JSONObject();
            params.put("op", "GetVetExpertiseInfoAll");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EXPERTISE_INFO_ALL,
                    true, RequestCode.GetVetExpertiseInfoAll, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
