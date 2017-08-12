package com.veeritsolution.android.anivethub.fragment.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpPetTreatment;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.TreatmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/25/2017.
 */

public class ClientPetDetailFragment extends android.support.v4.app.Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    private View rootView;
    private TextView tvPetName, tvPetType, tvPetBreed, tvPetBirthday, tvPetGender, tvPetWeight, tvHeader, tvSympTreat,
            lblPetType, lblPetBreed, lblPetBirhtday, lblPetGender, lblPetWeight, tvNoTreatment;
    private ImageView imgProfilePic;
    private Toolbar toolbar;
    private ListView lvTreatment;

    private PetDetailsModel petDetailsModel;
    private Bundle bundle;
    private HomeActivity homeActivity;
    private JSONObject params;
    private AdpPetTreatment adpPetTreatment;
    private TreatmentModel treatmentModel;
    private ArrayList<TreatmentModel> treatmenList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        if (bundle != null) {
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_client_pet_detail, container, false);
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
        tvHeader.setText(getString(R.string.str_pet_detail));

        lblPetType = (TextView) rootView.findViewById(R.id.lbl_PetType);
        lblPetType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lblPetBreed = (TextView) rootView.findViewById(R.id.lbl_PetBreed);
        lblPetBreed.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lblPetBirhtday = (TextView) rootView.findViewById(R.id.lbl_PetBirthday);
        lblPetBirhtday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lblPetGender = (TextView) rootView.findViewById(R.id.lbl_PetGender);
        lblPetGender.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lblPetWeight = (TextView) rootView.findViewById(R.id.lbl_PetWeight);
        lblPetWeight.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetName = (TextView) rootView.findViewById(R.id.tvPetName);
        tvPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetType = (TextView) rootView.findViewById(R.id.tvPetType);
        tvPetType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetBreed = (TextView) rootView.findViewById(R.id.tvPetBreed);
        tvPetBreed.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetBirthday = (TextView) rootView.findViewById(R.id.tvPetBirthday);
        tvPetBirthday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetGender = (TextView) rootView.findViewById(R.id.tvGender);
        tvPetGender.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetWeight = (TextView) rootView.findViewById(R.id.tvPetWeight);
        tvPetWeight.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSympTreat = (TextView) rootView.findViewById(R.id.tv_symptomsAndTreatment);
        tvSympTreat.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lvTreatment = (ListView) rootView.findViewById(R.id.lv_petTreatment);

        imgProfilePic = (ImageView) rootView.findViewById(R.id.img_petProfilePic);

        tvPetName.setText(petDetailsModel.getPetName());
        tvPetType.setText(petDetailsModel.getPetTypeName());
        tvPetBreed.setText(petDetailsModel.getPetBreedName());
        tvPetBirthday.setText(Utils.formatDate(petDetailsModel.getBirthDate(), Constants.MM_DD_YYYY_HH_MM_SS_A,
                Constants.DATE_MM_DD_YYYY));
        if (petDetailsModel.getGender() == 1) {

            tvPetGender.setText("Male");
        } else {
            tvPetGender.setText("Female");
        }
        tvPetWeight.setText(String.valueOf(petDetailsModel.getWeight()));

        tvNoTreatment = (TextView) rootView.findViewById(R.id.tv_noTreatmentInfo);
        tvNoTreatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String picPath = petDetailsModel.getClientPetPicPath();
        if (picPath.length() != 0) {

            Utils.setProfileImage(getActivity(), petDetailsModel.getClientPetPicPath(),
                    R.drawable.img_pets, imgProfilePic);

        } else {
            Utils.setProfileImage(getActivity(), R.drawable.img_pets, R.drawable.img_pets, imgProfilePic);
        }
        getClientTreatmentInfoAll();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPetTreatmentInfoAll:

                try {
                    if (mObject instanceof ErrorModel) {

                        lvTreatment.setVisibility(View.GONE);
                        tvNoTreatment.setVisibility(View.VISIBLE);

                    } else {
                        lvTreatment.setVisibility(View.VISIBLE);
                        tvNoTreatment.setVisibility(View.GONE);

                        treatmenList = (ArrayList<TreatmentModel>) mObject;
                        adpPetTreatment = new AdpPetTreatment(getActivity(), treatmenList, true);
                        lvTreatment.setAdapter(adpPetTreatment);
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
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

          /*  case R.id.img_back_header:

                homeActivity.popBackFragment();
                break;*/
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
            homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            //  }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getClientTreatmentInfoAll() {

        params = new JSONObject();

        try {
            params.put("op", "GetClientPetTreatmentInfoAll");
            params.put("ClientPetId", petDetailsModel.getClientPetId());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_INFO_ALL,
                    true, RequestCode.GetClientPetTreatmentInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
