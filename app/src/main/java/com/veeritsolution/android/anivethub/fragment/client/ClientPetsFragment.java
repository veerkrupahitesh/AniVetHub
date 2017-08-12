package com.veeritsolution.android.anivethub.fragment.client;

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
import com.veeritsolution.android.anivethub.adapter.AdpPetList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/30/2016.
 */

public class ClientPetsFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Toolbar toolbar;
    private ImageView imgClientProfilePhoto, imgClientBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private TextView tvHeader, tvName, tvUserName, tvNoPetsFound;
    private Button btnAddPets;
    private ListView listViewClientPets;
    private View headerView;

    private JSONObject params;
    private List<PetDetailsModel> petList;
    private PetDetailsModel petDetailsModel;
    private int position;
    private AdpPetList adpPetList;
    private Bundle bundle;
    private boolean fromSingUp;
    private HomeActivity homeActivity;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            fromSingUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_client_pets, container, false);
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
        tvHeader.setText(getString(R.string.str_my_pets));

        headerView = getView().findViewById(R.id.headerView);
        headerView.setVisibility(View.GONE);

        imgClientProfilePhoto = (ImageView) getView().findViewById(R.id.img_clientProfilePic);
        Utils.setProfileImage(getActivity(), ClientLoginModel.getClientCredentials().getProfilePic(),
                R.drawable.img_client_profile, imgClientProfilePhoto);

        imgClientBannerPhoto = (ImageView) getView().findViewById(R.id.img_client_banner);
        Utils.setBannerImage(getActivity(), ClientLoginModel.getClientCredentials().getBannerPic(),
                R.drawable.img_client_banner, imgClientBannerPhoto);

        tvName = (TextView) getView().findViewById(R.id.tv_clientName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvUserName = (TextView) getView().findViewById(R.id.tv_clientUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvName.setText(ClientLoginModel.getClientCredentials().getClientName());
        tvUserName.setText(ClientLoginModel.getClientCredentials().getUserName());

        imgSelectBannerPhoto = (ImageView) getView().findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) getView().findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        tvNoPetsFound = (TextView) getView().findViewById(R.id.tv_noPetsFound);
        tvNoPetsFound.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvNoPetsFound.setVisibility(View.GONE);

        btnAddPets = (Button) getView().findViewById(R.id.btn_addMorePets);
        btnAddPets.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        listViewClientPets = (ListView) getView().findViewById(R.id.listView_clientPets);

        petList = new ArrayList();
        adpPetList = new AdpPetList(getActivity(), petList, false);
        listViewClientPets.setAdapter(adpPetList);

        getAllPets();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPetInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        tvNoPetsFound.setVisibility(View.VISIBLE);
                        listViewClientPets.setVisibility(View.GONE);
                    } else {

                        petList = (ArrayList<PetDetailsModel>) mObject;

                        if (!petList.isEmpty()) {
                            listViewClientPets.setVisibility(View.VISIBLE);
                            tvNoPetsFound.setVisibility(View.GONE);
                            adpPetList.notifyDataSetChanged();

                            if (adpPetList == null) {
                                adpPetList = new AdpPetList(getActivity(), petList, false);
                                listViewClientPets.setAdapter(adpPetList);
                            } else {
                                adpPetList.refreshList(petList);
                            }
                        } else {
                            tvNoPetsFound.setVisibility(View.VISIBLE);
                            listViewClientPets.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientPetDelete:

                if (mObject instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) mObject;
                    // ToastHelper.getInstance().showMessage(errorModel.getError());
                    if (petList != null && !petList.isEmpty()) {
                        petList.remove(petDetailsModel);
                        adpPetList.refreshList(petList);
                    }

                    if (!petList.isEmpty()) {
                        tvNoPetsFound.setVisibility(View.GONE);
                    } else {
                        tvNoPetsFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (petList != null && !petList.isEmpty()) {
                        petList.remove(petDetailsModel);
                        adpPetList.refreshList(petList);
                    }

                    if (!petList.isEmpty()) {
                        tvNoPetsFound.setVisibility(View.GONE);
                    } else {
                        tvNoPetsFound.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case ClientPetDelete:

                ToastHelper.getInstance().showMessage(mError);

                break;

            case GetClientPetInfoAll:

                ToastHelper.getInstance().showMessage(mError);
                // homeActivity.popBackFragment();
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

            /*case R.id.img_back_header:

                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();

                break;*/

            case R.id.btn_addMorePets:

                Utils.buttonClickEffect(view);
                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_ADD);
                homeActivity.pushFragment(new ClientPetInfoFragment(), true, false, bundle);

                break;

            case R.id.img_delete:

                Utils.buttonClickEffect(view);
                position = (int) view.getTag();
                petDetailsModel = petList.get(position);

                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_pet),
                        getString(R.string.str_want_to_delete), false);
                break;

            case R.id.btn_actionOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                try {
                    params.put("op", "ClientPetDelete");
                    params.put("ClientPetId", petDetailsModel.getClientPetId());
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_DELETE,
                            true, RequestCode.ClientPetDelete, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_actionCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.img_edit:

                Utils.buttonClickEffect(view);
                position = (int) view.getTag();
                petDetailsModel = petList.get(position);

                bundle = new Bundle();
                bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_EDIT);
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                homeActivity.pushFragment(new ClientPetInfoFragment(), true, false, bundle);
                break;

            case R.id.img_graph:

                Utils.buttonClickEffect(view);
                position = (int) view.getTag();
                petDetailsModel = petList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                homeActivity.pushFragment(new ClientPetGraphFragment(), true, true, bundle);
                break;

            case R.id.img_treatment:

                Utils.buttonClickEffect(view);
                position = (int) view.getTag();
                petDetailsModel = petList.get(position);
                bundle = new Bundle();
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                homeActivity.pushFragment(new ClientPetTreatFragment(), true, true, bundle);
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.lin_petList:

                Utils.buttonClickEffect(view);
                position = (int) view.getTag();
                petDetailsModel = petList.get(position);
                bundle = new Bundle();
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                homeActivity.pushFragment(new ClientPetDetailFragment(), true, true, bundle);
                break;

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
            //    if (!fromSignUp) {
            homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            //    }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllPets() {
        try {
            params = new JSONObject();
            params.put("op", "GetClientPetInfoAll");
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_CLIENT_PET_INFO_ALL, true, RequestCode.GetClientPetInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
