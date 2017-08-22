package com.veeritsolution.android.anivethub.fragment.client;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.soundcloud.android.crop.Crop;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpPetPics;
import com.veeritsolution.android.anivethub.adapter.AdpPetTypeList;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.ImageUpload;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetBreedModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetPicsModel;
import com.veeritsolution.android.anivethub.models.PetTypeModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.DecimalDigitsInputFilter;
import com.veeritsolution.android.anivethub.utility.PermissionClass;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Veer on 06/10/2016.
 */
public class ClientPetInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvPetBirthDate, tvPetType, tvPetBreed;
    private Toolbar toolbar;
    private EditText edtPetName, edtPetWeight;
    private Spinner spPetGender;
    private ImageView imgClientProfilePhoto, imgClientBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private View rootView;
    private RecyclerView recyclerPetPics;

    // object and variable declaration
    private String petName, petBirthdate, petWeight, petType, petBreedName;
    private int petTypeId, petBreedId, petGender;
    private Bundle bundle;
    private JSONObject params;
    private HomeActivity homeActivity;
    private ArrayList<String> genderList;
    private PetDetailsModel petDetailsModel;
    private SpinnerAdapter adpGender;
    private ArrayList<PetTypeModel> petTypeList;
    private ArrayList<PetBreedModel> petBreedList;
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog datePickerDialog;
    private Dialog mDialog;
    private int addEditSignUp;
    private ArrayList<PetPicsModel> petPicsList;
    private AdpPetPics adpPetPics;
    private int apiType;
    private String image64Base;
    private PetPicsModel petPicsModel;
    // private int uploadImage = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            //fromSignUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
            if (petDetailsModel != null) {
                petTypeId = Integer.parseInt(petDetailsModel.getPetTypeId());

            }
            addEditSignUp = bundle.getInt(Constants.ADD_EDIT_SINGUP);
        }
        genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male entire");
        genderList.add("Female entire");
        genderList.add("Male neutered");
        genderList.add("Female neutered");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_client_pet_info, container, false);

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

        switch (addEditSignUp) {
            case Constants.FROM_ADD:
                tvHeader.setText(getString(R.string.str_add_pet_information));
                break;
            case Constants.FROM_EDIT:
                tvHeader.setText(getString(R.string.str_edit_pets_info));
                break;
            case Constants.FROM_SIGN_UP:
                tvHeader.setText(getString(R.string.str_add_pet_information));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                break;
        }

        imgClientProfilePhoto = (ImageView) rootView.findViewById(R.id.img_clientProfilePic);
        Utils.setProfileImage(getActivity(), ClientLoginModel.getClientCredentials().getProfilePic(),
                R.drawable.img_client_profile, imgClientProfilePhoto);

        imgClientBannerPhoto = (ImageView) rootView.findViewById(R.id.img_client_banner);
        Utils.setBannerImage(getActivity(), ClientLoginModel.getClientCredentials().getBannerPic(),
                R.drawable.img_client_banner, imgClientBannerPhoto);

        tvName = (TextView) rootView.findViewById(R.id.tv_clientName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(ClientLoginModel.getClientCredentials().getClientName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_clientUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(ClientLoginModel.getClientCredentials().getUserName());

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        recyclerPetPics = (RecyclerView) rootView.findViewById(R.id.recycler_PetPics);
        recyclerPetPics.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        edtPetName = (EditText) rootView.findViewById(R.id.edt_pet_name);
        edtPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetType = (TextView) rootView.findViewById(R.id.tv_petType);
        tvPetType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetBreed = (TextView) rootView.findViewById(R.id.tv_petBreed);
        tvPetBreed.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetBirthDate = (TextView) rootView.findViewById(R.id.tv_petBirthDate);
        tvPetBirthDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        //  edtPetAge = (EditText) getView().findViewById(R.id.edt_petAge);
        edtPetWeight = (EditText) rootView.findViewById(R.id.edt_petWeight);
        edtPetWeight.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        edtPetWeight.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});

        spPetGender = (Spinner) rootView.findViewById(R.id.sp_petGender);
        adpGender = new SpinnerAdapter(getActivity(), R.layout.spinner_row_list, genderList);
        spPetGender.setAdapter(adpGender);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        //  scrollViewParent = (ScrollView) rootView.findViewById(R.id.scrollViewParent);
        // scrollViewChild = (ScrollView) rootView.findViewById(R.id.scrollViewChild);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);
        petPicsList = new ArrayList<>();

        if (addEditSignUp == Constants.FROM_EDIT) {
            //CustomDialog.getInstance().showProgress(getActivity(), getString(R.string.str_please_wait), false);
            getClientPetInfo();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPetInfo:

                rootView.setVisibility(View.VISIBLE);
                petDetailsModel = (PetDetailsModel) mObject;
                if (petDetailsModel != null) {
                    setData(petDetailsModel);
                    getPetPicsInfo();
                }
                break;

            case GetPetTypeInfo:
                rootView.setVisibility(View.VISIBLE);
                // setPetTypeInfoSpinner(mObject);
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

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        petBreedList = (ArrayList<PetBreedModel>) mObject;
                        showPetTypeAndBreedDialog(homeActivity, petBreedList, "Select Pet Breed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientPetInsert:

                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int dataId = jsonObject.getInt("DataId");
                    ClientLoginModel clientLoginModel = ClientLoginModel.getClientCredentials();
                    clientLoginModel.setIsClientPetProfile(1);
                    ClientLoginModel.saveClientCredentials(RestClient.getGsonInstance().toJson(clientLoginModel));

                    for (int i = 0; i < petPicsList.size(); i++) {

                        Map<String, String> params = new HashMap<>();
                        params.put("op", ApiList.CLIENT_PET_PICS_INSERT);
                        params.put("AuthKey", ApiList.AUTH_KEY);
                        params.put("ClientPetId", String.valueOf(dataId));
                        params.put("PicTitle", String.valueOf(System.currentTimeMillis()));
                        params.put("sPic", petPicsList.get(i).getBase64image());

                        RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                                ApiList.CLIENT_PET_PICS_INSERT, false, RequestCode.ClientPetPicInsert, this);
                    }
                    switch (addEditSignUp) {
                        case Constants.FROM_ADD:
                            homeActivity.popBackFragment();
                            break;
                        case Constants.FROM_EDIT:
                            homeActivity.popBackFragment();
                            break;
                        case Constants.FROM_SIGN_UP:
                            homeActivity.removeAllFragment();
                            //PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                            homeActivity.pushFragment(new ClientHomeFragment(), true, false, null);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ClientPetUpdate:

                homeActivity.popBackFragment();
                break;

            case GetPetPicsInfo:

                PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, System.currentTimeMillis());
                CustomDialog.getInstance().dismiss();
                if (!(mObject instanceof ErrorModel)) {
                    petPicsList = (ArrayList<PetPicsModel>) mObject;
                    adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                    recyclerPetPics.setAdapter(adpPetPics);
                }
                break;

            case PetPicDelete:

                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.has("DataId")) {
                        if (jsonObject.optInt("DataId") != 0) {
                            petPicsList.remove(petPicsModel);
                            adpPetPics.notifyDataSetChanged();
                            ToastHelper.getInstance().showMessage("Deleted successfully!");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ClientPetPicInsert:

                switch (addEditSignUp) {

                    case Constants.FROM_ADD:

                        break;

                    case Constants.FROM_EDIT:
                        getPetPicsInfo();
                        break;

                    case Constants.FROM_SIGN_UP:

                        break;
                }
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

            case ClientPetInsert:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case ClientPetUpdate:

                ToastHelper.getInstance().showMessage(mError);
                homeActivity.popBackFragment();
                break;
        }
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

            case R.id.tv_petBirthDate:

                Utils.buttonClickEffect(view);
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                        // fromDate = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;
                        String format = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedmonth + 1, selectedday, selectedyear);
                        tvPetBirthDate.setText(format);
                        //tvPetBirthDate.setText((1 + selectedmonth) + "/" + selectedday + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.tv_petType:
                Utils.buttonClickEffect(view);
                getPetTypeInfo();
                break;

            case R.id.tv_petBreed:
                Utils.buttonClickEffect(view);
                if (petTypeId != 0) {
                    getPetBreedInfo(petTypeId);
                } else {
                    ToastHelper.getInstance().showMessage("Select first pet's Type");
                }
                break;

            case R.id.lin_addPetPhoto:

                Utils.buttonClickEffect(view);
                /*if (petDetailsModel == null) {

                    CustomDialog.getInstance().showAlert(getActivity(), "First insert pet then after add pet photos", false);
                } else {*/
                if (petPicsList.size() == 3) {
                    CustomDialog.getInstance().showAlert(getActivity(), "Maximum three photos of pet allowed to upload", true);
                } else {
                    if (PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION,
                            Arrays.asList(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA))) {
                        showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);
                    }
                }
                // }
                break;

            case R.id.img_petPicDelete:
                Utils.buttonClickEffect(view);
                petPicsModel = (PetPicsModel) view.getTag();
                if (petPicsModel != null) {

                    CustomDialog.getInstance().showActionDialog(getActivity(), "Delete pet photo", getString(R.string.str_want_to_delete), false);
                }
                break;

            case R.id.btn_actionOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                switch (addEditSignUp) {

                    case Constants.FROM_ADD:
                        petPicsList.remove(petPicsModel);
                        adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                        recyclerPetPics.setAdapter(adpPetPics);
                        break;

                    case Constants.FROM_EDIT:
                        deletePetPic(petPicsModel);
                        break;

                    case Constants.FROM_SIGN_UP:
                        petPicsList.remove(petPicsModel);
                        adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                        recyclerPetPics.setAdapter(adpPetPics);
                        break;
                }

                break;

            case R.id.btn_actionCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.img_close:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_submit_addBreed:
                Utils.buttonClickEffect(view);
                Object tag = view.getTag();
                if (tag == null) {
                    ToastHelper.getInstance().showMessage("Please enter pet's breed name");
                } else {
                    CustomDialog.getInstance().dismiss();
                    String petBreed = (String) tag;
                    tvPetBreed.setText(petBreed);
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (addEditSignUp != Constants.FROM_SIGN_UP)
            homeActivity.popBackFragment();
        else
            homeActivity.finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (addEditSignUp != Constants.FROM_SIGN_UP) {
            inflater.inflate(R.menu.other_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            if (addEditSignUp != Constants.FROM_SIGN_UP) {
                homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void deletePetPic(PetPicsModel petPicsModel) {
        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_PET_PICS_DELETE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientPetPicsId", petPicsModel.getClientPetPicsId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_PICS_DELETE,
                    true, RequestCode.PetPicDelete, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getClientPetInfo() {
        try {
            params = new JSONObject();
            params.put("op", "GetClientPetInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientPetId", petDetailsModel.getClientPetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_PET_INFO, true,
                    RequestCode.GetClientPetInfo, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(PetDetailsModel petDetailsModel) {

        edtPetName.setText(petDetailsModel.getPetName());
        tvPetType.setText(petDetailsModel.getPetTypeName());
        tvPetBreed.setText(petDetailsModel.getPetBreedName());
        tvPetBirthDate.setText(Utils.formatDate(petDetailsModel.getBirthDate(), Constants.MM_DD_YYYY_HH_MM_SS_A,
                Constants.DATE_MM_DD_YYYY));
        spPetGender.setSelection(petDetailsModel.getGender());
        edtPetWeight.setText(String.valueOf(petDetailsModel.getWeight()));
    }

    private void saveData() {

        try {

            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(ClientLoginModel.getClientCredentials().getClientId()));
            //  params.put("ClientName", ClientLoginModel.getClientCredentials().getClientName());
            params.put("PetName", petName);
            params.put("PetTypeId", petTypeId);
            params.put("PetBreedId", petBreedId);
            params.put("PetBreedName", petBreedName);

            switch (petGender) {

                case Constants.MALE_ENTIRE:
                    params.put("Gender", Constants.MALE_ENTIRE);
                    break;

                case Constants.FEMALE_ENTIRE:
                    params.put("Gender", Constants.FEMALE_ENTIRE);
                    break;

                case Constants.MALE_NEUTERED:
                    params.put("Gender", Constants.MALE_NEUTERED);
                    break;

                case Constants.FEMALE_NEUTERED:
                    params.put("Gender", Constants.FEMALE_NEUTERED);
                    break;
            }

            params.put("Status", 0);
            params.put("BirthDate", petBirthdate);
            params.put("Weight", petWeight);
            params.put("Description", "");

            switch (addEditSignUp) {

                case Constants.FROM_ADD:
                    params.put("op", "ClientPetInsert");
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_INSERT, true,
                            RequestCode.ClientPetInsert, this);
                    break;
                case Constants.FROM_EDIT:
                    params.put("op", "ClientPetUpdate");
                    params.put("ClientPetId", petDetailsModel.getClientPetId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_UPDATE, true,
                            RequestCode.ClientPetUpdate, this);
                    break;
                case Constants.FROM_SIGN_UP:
                    params.put("op", "ClientPetInsert");
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_INSERT, true,
                            RequestCode.ClientPetInsert, this);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private boolean validateForm() {

        petName = edtPetName.getText().toString().trim();
        petBirthdate = tvPetBirthDate.getText().toString().trim();
        petGender = spPetGender.getSelectedItemPosition();
        petWeight = edtPetWeight.getText().toString().trim();
        petType = tvPetType.getText().toString().trim();
        petBreedName = tvPetBreed.getText().toString().trim();

        if (petName.isEmpty()) {
            edtPetName.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_petname));
            return false;
        } else if (petType.isEmpty() || petType.equals("select pet type")) {
            tvPetType.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_pettype));
            return false;
        } else if (petBirthdate.isEmpty()) {
            tvPetBirthDate.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_petage));
            return false;
        } else if (petGender == 0) {
            spPetGender.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_gender));
            return false;
        } else if (petWeight.isEmpty()) {
            edtPetWeight.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_petweight));
            return false;
        } else {
            return true;
        }
    }

    private void getPetTypeInfo() {
        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", "GetPetTypeInfo");

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PET_TYPE_INFO, true,
                    RequestCode.GetPetTypeInfo, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPetBreedInfo(int petTypeId) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_PET_BREED_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("PetTypeId", petTypeId);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PET_BREED_INFO, true,
                    RequestCode.GetPetBreedInfo, this);

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
                AdpPetTypeList adpPetTypeList = new AdpPetTypeList(context, listLocation);
                listViewLocation.setAdapter(adpPetTypeList);
            } else {

                listViewLocation.setVisibility(View.GONE);
                txtNoRecord.setVisibility(View.VISIBLE);
            }

            listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Utils.buttonClickEffect(view);
                    mDialog.dismiss();

                    TextView txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);

                    Object object = txtLocationName.getTag();

                    if (object != null) {

                        if (object instanceof PetTypeModel) {

                            PetTypeModel petTypeModel = (PetTypeModel) object;
                            petTypeId = petTypeModel.getPetTypeId();

                            tvPetType.setText(petTypeModel.getPetTypeName());
                            tvPetBreed.setText("");
                            //  tvPetBreed.performClick();

                        } else if (object instanceof PetBreedModel) {

                            PetBreedModel petBreedModel = (PetBreedModel) object;

                            petBreedId = petBreedModel.getPetBreedId();

                            if (petBreedId == 99999) {
                                CustomDialog.getInstance().showAddBreed(getActivity(), false);
                            } else {
                                tvPetBreed.setText(petBreedModel.getPetBreedName());
                            }

                        }
                    }

                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.buttonClickEffect(view);
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

    public void showImageSelect(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_select_image, null, false);
        mDialog.setContentView(R.layout.custom_dialog_select_image);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle, tvCamera, tvGallery;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_selectImageTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        tvCamera = (TextView) mDialog.findViewById(R.id.tv_camera);
        tvCamera.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvGallery = (TextView) mDialog.findViewById(R.id.tv_gallery);
        tvGallery.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        LinearLayout linCamera, linGallery;

        linCamera = (LinearLayout) mDialog.findViewById(R.id.lin_camera);
        linGallery = (LinearLayout) mDialog.findViewById(R.id.lin_gallery);

        linCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Utils.buttonClickEffect(v);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.buttonClickEffect(v);
                dismiss();
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        Constants.REQUEST_FILE_PROFILE);
            }
        });
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.buttonClickEffect(v);
                dismiss();
            }
        });
        try {
            if (mDialog != null) {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:
                    apiType = Constants.API_REQUEST_PROFILE_CAMERA;
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
                    beginCrop(selectedImageUri);
                    //  cropProfilePhotoFromCamera(data);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    apiType = Constants.API_REQUEST_PROFILE_FILE;
                    selectedImageUri = data.getData();
                    //  Uri imageUri = data.getData();
                    beginCrop(selectedImageUri);
                    //cropProfilePhotoFromGallery(data);
                    break;

                case Crop.REQUEST_CROP:

                    handleCrop(resultCode, data);
                    break;
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        switch (apiType) {

            case Constants.API_REQUEST_PROFILE_CAMERA:

                Crop.of(source, destination)
                        .asSquare()
                        .start(getActivity(), this);
                break;

            case Constants.API_REQUEST_PROFILE_FILE:

                Crop.of(source, destination)
                        .asSquare()
                        .start(getActivity(), this);
                break;
        }
    }

    private void handleCrop(int resultCode, final Intent result) {

        switch (apiType) {

            case Constants.API_REQUEST_PROFILE_CAMERA:

                uploadPetPhoto(result);
                break;

            case Constants.API_REQUEST_PROFILE_FILE:

                uploadPetPhoto(result);
                break;
        }
    }

    private void uploadPetPhoto(Intent result) {

        image64Base = Utils.getStringImage(Crop.getOutput(result).getPath(), ImageUpload.ClientProfile);

        switch (addEditSignUp) {

            case Constants.FROM_ADD:

                PetPicsModel petPicsModel = new PetPicsModel();
                petPicsModel.setPicPath(Crop.getOutput(result).getPath());
                petPicsModel.setBase64image(image64Base);
                petPicsList.add(petPicsModel);
                adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                recyclerPetPics.setAdapter(adpPetPics);
                break;

            case Constants.FROM_EDIT:

                Map<String, String> params = new HashMap<>();
                params.put("op", ApiList.CLIENT_PET_PICS_INSERT);
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("ClientPetId", String.valueOf(petDetailsModel.getClientPetId()));
                params.put("PicTitle", String.valueOf(System.currentTimeMillis()));
                params.put("sPic", image64Base);

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                        ApiList.CLIENT_PET_PICS_INSERT, true, RequestCode.ClientPetPicInsert, this);
                break;

            case Constants.FROM_SIGN_UP:
                petPicsModel = new PetPicsModel();
                petPicsModel.setPicPath(Crop.getOutput(result).getPath());
                petPicsModel.setBase64image(image64Base);
                petPicsList.add(petPicsModel);
                adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                recyclerPetPics.setAdapter(adpPetPics);
                break;
        }

        /*new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                CustomDialog.getInstance().showProgress(getActivity(), "Image Uploading...", false);
            }

            @Override
            protected Void doInBackground(Void... params) {

                SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.CLIENT_PET_METHOD_NAME);
                request.addProperty("op", "ClientPetPicsInsert");
                request.addProperty("AuthKey", ApiList.AUTH_KEY);
                request.addProperty("ClientPetId", petDetailsModel.getClientPetId());
                request.addProperty("PicTitle", "myPet");
                request.addProperty("Pic", image64Base);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE("http://admin.anivethub.com/WebService/VetHubData.asmx?op=ClientPetPicsInsert");
                try {
                    androidHttpTransport.call(Constants.SOAP_ACTION + Constants.CLIENT_PET_METHOD_NAME, envelope);
                    SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
                    String str = result1.toString();
                    Debug.trace("Response", str);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                getPetPicsInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // CustomDialog.getInstance().dismiss();
            }
        }.execute();*/
    }

    private void getPetPicsInfo() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_CLIENT_PET_PICS_INFO_ALL);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientPetId", petDetailsModel.getClientPetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_PET_PICS_INFO_ALL,
                    false, RequestCode.GetPetPicsInfo, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
