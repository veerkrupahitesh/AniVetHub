package com.veeritsolution.android.anivethub.fragment.vet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpPetTypeGroupList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetBreedModel;
import com.veeritsolution.android.anivethub.models.PetTypeModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by veerk on 3/27/2017.
 */

public class UpdateVetSpecInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvPetType;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto, backButton;
    private View rootView;
    //private Spinner spProficiency;

    // object and variable declaration
    private HomeActivity homeActivity;
    // private Bundle bundle;
    private JSONObject params;
    private int petTypeId = 0, proficiencyId;
    private ArrayAdapter<String> adpProficiency;
    private ArrayList<String> proficiency;
    // private VetExperience vetExpertise;
    // private boolean fromSignUp;
    private Dialog mDialog;
    private ArrayList<PetTypeModel> petTypeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();

        proficiency = new ArrayList<>();
        proficiency.add("Select Expertise");
        proficiency.add("Basic");
        proficiency.add("Intermediate");
        proficiency.add("Expert");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_expertise_info, container, false);

        // rootView.setVisibility(View.INVISIBLE);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.finish();
            }
        });

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvHeader.setText(getString(R.string.str_add_specialization_info));

        //   backButton = (ImageView) rootView.findViewById(R.id.img_back_header);
        //   backButton.setVisibility(View.INVISIBLE);

        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        tvPetType = (TextView) rootView.findViewById(R.id.tv_petType);
        tvPetType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);


        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

       // spProficiency = (Spinner) rootView.findViewById(R.id.sp_vet_proficiency);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       /* adpProficiency = new SpinnerAdapter(getActivity(), R.layout.spinner_row_list, proficiency);
        spProficiency.setAdapter(adpProficiency);

        spProficiency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                proficiencyId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        Utils.setupOutSideTouchHideKeyboard(getView().findViewById(R.id.activity_vet_expertise));
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPetTypeInfo:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        petTypeList = (ArrayList<PetTypeModel>) mObject;
                        showPetTypeAndBreedDialog(homeActivity, petTypeList, "Select Pet Type");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case GetPetBreedInfo:


                break;

            case VetExpertiseInsert:
                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetExpertise(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));
                homeActivity.popBackFragment();
                break;

        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetClientPetInfo:

                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;
            case GetPetTypeInfo:

                ToastHelper.getInstance().showMessage(mError);
                break;
            case GetPetBreedInfo:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetExpertiseInsert:
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        homeActivity.finish();
        // homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:
                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    saveData();
                }

                break;
            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
            case R.id.tv_petType:

                getPetTypeInfo();
                break;
        }
    }

    private boolean validateForm() {

        String petType = tvPetType.getText().toString().trim();
        // String petBreed = spPetBreed.getSelectedItem().toString().trim();
        //String proficiency = spProficiency.getSelectedItem().toString().trim();

        if (petType.isEmpty() || petType.equals("select pet type")) {
            tvPetType.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_pet_type));
            return false;
        }/* else if (petBreed.isEmpty() || petBreed.equals("select pet breed")) {
            ToastHelper.getInstance().showMessage(getActivity(), getString(R.string.str_pet_breed));
            return false;
        }*//* else if (spProficiency.getSelectedItemPosition() == 0) {
            spProficiency.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_expertise));
            return false;
        }*/ else {
            return true;
        }
    }

    private void getPetTypeInfo() {
        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", ApiList.GET_PET_TYPE_INFO);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_PET_TYPE_INFO, true, RequestCode.GetPetTypeInfo, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {

        try {
            params = new JSONObject();
            params.put("op", "VetExpertiseInsert");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("PetTypeGroupId", petTypeId);
           // params.put("Proficiency", proficiencyId);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.VET_EXPERTISE_INSERT, true, RequestCode.VetExpertiseInsert, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method show the datePickerDialog with list of locations and having functionality of search
     *
     * @param context(Context)        : context
     * @param listLocation(ArrayList) : list of locations with countries, states and cities
     */

    public void showPetTypeAndBreedDialog(final Context context, final ArrayList<?> listLocation, String dialogTitle) {

        try {

            mDialog = new Dialog(context, R.style.dialogStyle);
            // View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_location_list, null);
            mDialog.setContentView(R.layout.custom_dialog_pet_type_breed);

            /* Set Dialog width match parent */
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            /*Set out side touch hide keyboard*/
            Utils.setupOutSideTouchHideKeyboard(mDialog.findViewById(R.id.parentDialog));

            TextView txtTitleDialog = (TextView) mDialog.findViewById(R.id.tv_titleDialog);
            txtTitleDialog.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);
            // Set Dialog title
            txtTitleDialog.setText(dialogTitle);

            final ListView listViewLocation = (ListView) mDialog.findViewById(R.id.lv_location);

            final TextView txtNoRecord = (TextView) mDialog.findViewById(R.id.tv_noRecord);
            txtNoRecord.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            txtNoRecord.setText("No Records Found");

            Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
            btnCancel.setText("Cancel");

            if (listLocation != null && listLocation.size() > 0) {

                listViewLocation.setVisibility(View.VISIBLE);
                AdpPetTypeGroupList adppetTypeGroupList = new AdpPetTypeGroupList(context, listLocation);
                listViewLocation.setAdapter(adppetTypeGroupList);
            } else {

                listViewLocation.setVisibility(View.GONE);
                txtNoRecord.setVisibility(View.VISIBLE);
            }

            listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mDialog.dismiss();

                    TextView txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);

                    Object object = txtLocationName.getTag();

                    if (object != null) {

                        if (object instanceof PetTypeModel) {

                            PetTypeModel petTypeModel = (PetTypeModel) object;
                            petTypeId = petTypeModel.getPetTypeId();

                            tvPetType.setText(petTypeModel.getPetTypeName());
                            // tvPetBreed.setText("");
                            //  tvPetBreed.performClick();

                        } else if (object instanceof PetBreedModel) {

                            PetBreedModel petBreedModel = (PetBreedModel) object;
                            // tvPetBreed.setText(petBreedModel.getPetBreedName());

                            // petBreedId = petBreedModel.getPetBreedId();

                        }
                    }

                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });

            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

