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
import com.veeritsolution.android.anivethub.adapter.AdpExperienceList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hitesh} on 1/5/2017.
 */

public class VetExpFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Button btnAddNewExpInfo;
    private ListView listViewExpertise;
    private TextView tvHeader, tvName, tvUserName, tvNoExperienceInfo, tvPassingYear, navHeaderName, navHeaderLocation;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private View headerView;
    private View rootView;

   // object and variable declaration
    private JSONObject params;
    private List<VetExperience> vetExpertiseList;
    private AdpExperienceList adpExpertise;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private VetExperience vetExpertise;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            params = new JSONObject();
            params.put("op", "GetVetExperienceInfoAll");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EXPERIENCE_INFO_ALL,
                    true, RequestCode.VetVetExperienceInfoAll, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_experience, container, false);

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
        tvHeader.setText(getString(R.string.str_experience));

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

        listViewExpertise = (ListView) getView().findViewById(R.id.listView_experience);

        tvNoExperienceInfo = (TextView) getView().findViewById(R.id.tv_noExperienceInfo);
        tvNoExperienceInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnAddNewExpInfo = (Button) rootView.findViewById(R.id.btn_addNewExpInfo);
        btnAddNewExpInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetVetExperienceInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                        listViewExpertise.setVisibility(View.GONE);
                        tvNoExperienceInfo.setVisibility(View.VISIBLE);

                    } else {

                        vetExpertiseList = (ArrayList<VetExperience>) mObject;

                        if (!vetExpertiseList.isEmpty()) {
                            tvNoExperienceInfo.setVisibility(View.GONE);
                            adpExpertise = new AdpExperienceList(getActivity(), vetExpertiseList);
                            listViewExpertise.setAdapter(adpExpertise);
                        } else {
                            tvNoExperienceInfo.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case VetExperienceDelete:

                try {
                    if (mObject instanceof ErrorModel) {
                        vetExpertiseList.remove(vetExpertise);
                        adpExpertise.notifyDataSetChanged();

                        if (!vetExpertiseList.isEmpty()) {
                            tvNoExperienceInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                        } else {
                            tvNoExperienceInfo.setVisibility(View.VISIBLE);
                            listViewExpertise.setVisibility(View.GONE);
                        }
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());

                    } else {
                        vetExpertiseList.remove(vetExpertise);
                        adpExpertise.notifyDataSetChanged();

                        if (!vetExpertiseList.isEmpty()) {
                            tvNoExperienceInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
                        } else {
                            tvNoExperienceInfo.setVisibility(View.GONE);
                            listViewExpertise.setVisibility(View.VISIBLE);
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

            case R.id.img_Edit:
                Utils.buttonClickEffect(view);
                vetExpertise = (VetExperience) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.VET_EXPERIENCE, vetExpertise);
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_EDIT);
                homeActivity.pushFragment(new VetExpInfoFragment(), true, false, bundle);
                break;

            case R.id.img_Delete:
                Utils.buttonClickEffect(view);
                vetExpertise = (VetExperience) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_experience),
                        getString(R.string.str_want_to_delete), false);

                break;

            case R.id.btn_actionOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();

                try {

                    params = new JSONObject();
                    params.put("op", "VetExperienceDelete");
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("VetExperienceId", vetExpertise.getVetExperienceId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERIENCE_DELETE,
                            true, RequestCode.VetExperienceDelete, this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_actionCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_addNewExpInfo:
                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_ADD);
                homeActivity.pushFragment(new VetExpInfoFragment(), true, false, bundle);
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
            // homeActivity.popBackFragment();
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            // homeActivity.pushFragment(new ClientHomeFragment(), true, true, null);
        }

        return super.onOptionsItemSelected(item);
    }
}
