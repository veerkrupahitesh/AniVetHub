package com.veeritsolution.android.anivethub.fragment.practise;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpLocation;
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
import com.veeritsolution.android.anivethub.models.CityModel;
import com.veeritsolution.android.anivethub.models.CountryModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.StateModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.PermissionClass;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by veerk on 3/27/2017.
 */

public class UpdatePracBasicInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private EditText edtAddress1, edtAddress2, edtSessionRate, edtPoBox, edtBiography, edtPhoneNumber, edtEmail;
    private TextView tvHeader, tvName, tvUserName, tvCountry, tvState, tvCity;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto;
    private View rootView;
    private ProgressBar progressBar, progressBar1;

    // object and variable declaration
    private JSONObject params;
    private String addressOne, addressTwo, pincode, state, country, city, sessionRate, bioGraphy, phone, email,
            countryId = "", stateId = "", cityId = "";
    private HomeActivity homeActivity;
    private Dialog mDialog;
    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList;
    private int loginUser;
    private boolean uploadProfilePhoto;
    private int apiType;
    private String image64Base;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
        homeActivity = (HomeActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_basicinfo, container, false);

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

        // imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        // imgBackHeader.setVisibility(View.INVISIBLE);

        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_add_basic_information));

        tvName.setText(PractiseLoginModel.getPractiseCredentials().getVetName());
        tvUserName.setText(PractiseLoginModel.getPractiseCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);

        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), PractiseLoginModel.getPractiseCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        progressBar = (ProgressBar) rootView.findViewById(R.id.prg_imageUpload);
        //  progressBar.setVisibility(View.VISIBLE);

        progressBar1 = (ProgressBar) rootView.findViewById(R.id.prg_imageUpload1);
        //  progressBar1.setVisibility(View.VISIBLE);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        edtAddress1 = (EditText) rootView.findViewById(R.id.edt_addressOne);
        edtAddress1.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtAddress2 = (EditText) rootView.findViewById(R.id.edt_addressTwo);
        edtAddress2.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtPhoneNumber = (EditText) rootView.findViewById(R.id.edt_vet_phone);
        edtPhoneNumber.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtEmail = (EditText) rootView.findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtSessionRate = (EditText) rootView.findViewById(R.id.edt_sessionRate);
        edtSessionRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvCountry = (TextView) rootView.findViewById(R.id.tv_country);
        tvCountry.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvState = (TextView) rootView.findViewById(R.id.tv_state);
        tvState.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvCity = (TextView) rootView.findViewById(R.id.tv_city);
        tvCity.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtPoBox = (EditText) rootView.findViewById(R.id.edt_pinCode);
        edtPoBox.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtBiography = (EditText) rootView.findViewById(R.id.edt_biography);
        edtBiography.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);
        edtPhoneNumber.setText(PractiseLoginModel.getPractiseCredentials().getPhoneNo());
        edtEmail.setText(PractiseLoginModel.getPractiseCredentials().getEmailId());
        edtEmail.setEnabled(false);
        edtPhoneNumber.setEnabled(false);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetUpdate:
                PractiseLoginModel practiseLoginModel = PractiseLoginModel.getPractiseCredentials();
                practiseLoginModel.setIsVetProfile(1);
                PractiseLoginModel.savePractiseCredentials(RestClient.getGsonInstance().toJson(practiseLoginModel));
                homeActivity.popBackFragment();
                break;

            case GetCountry:

                rootView.setVisibility(View.VISIBLE);


                countryList = (ArrayList<CountryModel>) mObject;
                showLocationListDialog(getActivity(), countryList, tvCountry, "Select Country");
                break;

            case GetState:

                stateList = (ArrayList<StateModel>) mObject;
                showLocationListDialog(getActivity(), stateList, tvState, "Select State");
                break;

            case GetCity:

                cityList = (ArrayList<CityModel>) mObject;
                showLocationListDialog(getActivity(), cityList, tvCity, "Select City");
                break;

            case PractiseProfilePhotoUpload:

                try {
                    JSONObject jsonObject = (JSONObject) mObject;
                    String photoPath = jsonObject.getString("Error");
                    PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, System.currentTimeMillis());
                    // progressBar.setVisibility(View.VISIBLE);
                    Utils.setProfileImage(getActivity(), photoPath, R.drawable.img_vet_profile,
                            imgVetProfilePhoto, progressBar);
                    practiseLoginModel = PractiseLoginModel.getPractiseCredentials();
                    practiseLoginModel.setProfilePic(photoPath);
                    PractiseLoginModel.savePractiseCredentials(RestClient.getGsonInstance().toJson(practiseLoginModel));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PractiseBannerPhotoUpload:

                try {
                    JSONObject jsonObject = (JSONObject) mObject;
                    String photoPath = jsonObject.getString("Error");
                    PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG_BANNER, System.currentTimeMillis());
                    // progressBar.setVisibility(View.VISIBLE);
                    Utils.setBannerImage(getActivity(), photoPath, R.drawable.img_vet_banner,
                            imgVetBannerPhoto, progressBar1);
                    practiseLoginModel = PractiseLoginModel.getPractiseCredentials();
                    practiseLoginModel.setBannerPic(photoPath);
                    PractiseLoginModel.savePractiseCredentials(RestClient.getGsonInstance().toJson(practiseLoginModel));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetCountry:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetState:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetCity:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetUpdate:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case PractiseProfilePhotoUpload:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case PractiseBannerPhotoUpload:
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        homeActivity.finish();
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

            case R.id.tv_country:

                getCountryInfo(true);
                break;

            case R.id.tv_state:

                if (!countryId.equals("")) {
                    getStateInfo(countryId);
                } else {
                    ToastHelper.getInstance().showMessage("please select country first");
                }
                break;

            case R.id.tv_city:

                if (!stateId.equals("")) {
                    getCityInfo(stateId);
                } else {
                    ToastHelper.getInstance().showMessage("please select state first");
                }
                break;

            case R.id.img_selectProfilePhoto:

                Utils.buttonClickEffect(view);
                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);
                    uploadProfilePhoto = true;
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setAspectRatio(1, 1)
                            .setRequestedSize(200, 200)
                            .start(getContext(), this);
                    uploadProfilePhoto = true;
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;

            case R.id.img_selectBannerPhoto:

                Utils.buttonClickEffect(view);
                if (Build.VERSION.SDK_INT > 22) {
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);
                    uploadProfilePhoto = false;
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setAspectRatio(2, 1)
                            .setRequestedSize(400, 200)
                            .start(getContext(), this);
                    uploadProfilePhoto = false;
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;
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

                case Constants.REQUEST_CAMERA_BANNER:

                    apiType = Constants.API_REQUEST_BANNER_CAMERA;
                    thumbnail = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
                    beginCrop(selectedImageUri);
                    // cropBannerPhotoFromCamera(data);
                    break;

                case Constants.REQUEST_FILE_BANNER:

                    apiType = Constants.API_REQUEST_BANNER_FILE;
                    selectedImageUri = data.getData();
                    beginCrop(selectedImageUri);
                    //cropBannerPhotoFromGallery(data);
                    break;

               /* case Crop.REQUEST_CROP:

                    handleCrop(resultCode, data);
                    break;*/

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
    }

    private void handleCrop(int resultCode, final String result) {

        if (uploadProfilePhoto) {
            uploadProfilePhoto(result);
        } else {
            uploadBannerPhoto(result);
        }
    }

    private void uploadBannerPhoto(String result) {
        // CustomDialog.getInstance().showProgress(getActivity(), "Image Uploading...", false);
        image64Base = Utils.getStringImage(result, ImageUpload.ClientBanner);
        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.VET_BANNER_PIC_UPDATE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("VetId", String.valueOf(PractiseLoginModel.getPractiseCredentials().getVetId()));
        params.put("sBannerPic", image64Base);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_BANNER_PIC_UPDATE,
                true, RequestCode.PractiseBannerPhotoUpload, this);
    }

    private void uploadProfilePhoto(String result) {

        // CustomDialog.getInstance().showProgress(getActivity(), "Image Uploading...", false);
        image64Base = Utils.getStringImage(result, ImageUpload.ClientProfile);

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.VET_PROFILE_PIC_UPDATE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("VetId", String.valueOf(PractiseLoginModel.getPractiseCredentials().getVetId()));
        params.put("sProfilePic", image64Base);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_PROFILE_PIC_UPDATE,
                true, RequestCode.PractiseProfilePhotoUpload, this);
    }


    private void saveData() {
        try {

            params = new JSONObject();

            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                params.put("VetName", VetLoginModel.getVetCredentials().getVetName());
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
                params.put("VetName", PractiseLoginModel.getPractiseCredentials().getVetName());
            }

            params.put("op", ApiList.VET_UPDATE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("Address1", addressOne);
            params.put("Address2", addressTwo);
            params.put("PhoneNo", phone);
            params.put("EmailId", email);
            params.put("City", cityId);
            params.put("State", stateId);
            params.put("Country", countryId);
            params.put("POBox", pincode);
            params.put("SessionRate", sessionRate);
            params.put("AcTokenId", "");
            params.put("Biography", bioGraphy);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_UPDATE, true,
                    RequestCode.VetUpdate, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        addressOne = edtAddress1.getText().toString().trim();
        addressTwo = edtAddress2.getText().toString().trim();
        pincode = edtPoBox.getText().toString().trim();
        sessionRate = edtSessionRate.getText().toString().trim();
        bioGraphy = edtBiography.getText().toString().trim();
        phone = edtPhoneNumber.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        country = tvCountry.getText().toString().trim();
        state = tvState.getText().toString().trim();
        city = tvCity.getText().toString().trim();


        if (addressOne.isEmpty()) {
            edtAddress1.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_address));
            return false;
        } else if (addressTwo.isEmpty()) {
            edtAddress2.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_address));
            return false;
        } else if (phone.isEmpty()) {
            edtPhoneNumber.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_phoneno));
            return false;
        } else if (phone.length() < Constants.PHONE_LENGTH) {
            edtPhoneNumber.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_phoneno));
            return false;
        } else if (!TextUtils.isDigitsOnly(phone)) {
            edtPhoneNumber.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_phoneno));
            return false;
        } else if (email.isEmpty()) {
            edtEmail.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_email));
            return false;
        } else if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_email_address));
            return false;
        } else if (country.equals("select country") || country.isEmpty()) {
            tvCountry.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_country));
            return false;
        } else if (state.equals("select state") || state.isEmpty()) {
            tvState.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_state));
            return false;
        } else if (city.equals("select city") || city.isEmpty()) {
            tvCity.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_city));
            return false;
        } else if (pincode.isEmpty()) {
            edtPoBox.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_pincode));
            return false;
        } else if (pincode.length() < Constants.PINCODE_LENGTH) {
            edtPoBox.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_pincode));
            return false;
        } else if (!TextUtils.isDigitsOnly(pincode)) {
            edtPoBox.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_pincode));
            return false;
        } else if (sessionRate.isEmpty()) {
            edtSessionRate.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_sessionrate));
            return false;
        } else if (!TextUtils.isDigitsOnly(sessionRate)) {
            edtSessionRate.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_sessionrate));
            return false;
        } else {
            return true;
        }
    }


    private void getCountryInfo(boolean isDialogRequired) {

        try {
            params = new JSONObject();
            params.put("op", "GetCountryInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_COUNTRY_INFO, isDialogRequired, RequestCode.GetCountry, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getCityInfo(String stateId) {

        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", "GetCityInfo");
            params.put("StateId", stateId);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CITY_INFO, true,
                    RequestCode.GetCity, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getStateInfo(String countryId) {

        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", "GetStateInfo");
            params.put("CountryId", countryId);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_STATE_INFO,
                    true, RequestCode.GetState, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method show the dialog with list of locations and having functionality of search
     *
     * @param context(Context)        : context
     * @param listLocation(ArrayList) : list of locations with countries, states and cities
     * @param textView(TextView)      : to show selected location e.g country-United Kingdom,
     *                                state- new york, city- london
     */

    public void showLocationListDialog(final Context context, final ArrayList<?> listLocation,
                                       final TextView textView, String dialogTitle) {

        try {

            mDialog = new Dialog(context, R.style.dialogStyle);
            // View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_location_list, null);
            mDialog.setContentView(R.layout.custom_dialog_location_list);

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

            LinearLayout linSearch = (LinearLayout) mDialog.findViewById(R.id.lin_search);
            Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
            btnCancel.setText("Cancel");

            final EditText edtSearchLocation = (EditText) mDialog.findViewById(R.id.edt_searchLocation);
            edtSearchLocation.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            if (listLocation != null && listLocation.size() > 0) {

                listViewLocation.setVisibility(View.VISIBLE);
                AdpLocation adpLocationList = new AdpLocation(context, listLocation, "");
                listViewLocation.setAdapter(adpLocationList);
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

                        if (object instanceof CountryModel) {

                            CountryModel countryModel = (CountryModel) object;

                            countryId = String.valueOf(countryModel.getCountryId());

                            textView.setText(countryModel.getCountryName());
                            textView.setTag(countryModel);
                            tvState.setText("");
                            tvState.performClick();

                        } else if (object instanceof StateModel) {

                            StateModel stateModel = (StateModel) object;

                            stateId = String.valueOf(stateModel.getStateId());

                            textView.setText(stateModel.getStateName());
                            textView.setTag(stateModel);
                            tvCity.performClick();
                            tvCity.setText("");

                        } else if (object instanceof CityModel) {

                            CityModel cityModel = (CityModel) object;
                            cityId = String.valueOf(cityModel.getCityId());

                            textView.setText(cityModel.getCityName());
                            textView.setTag(cityModel);


                        }
                    }

                }
            });

            edtSearchLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    /** Show clear icon on editext length greater than zero */
                    String searchStr = edtSearchLocation.getText().toString().trim();

                    ArrayList<Object> filterList = new ArrayList<>();

                    for (int i = 0; i < listLocation.size(); i++) {
                        try {

                            Object object = listLocation.get(i);

                            if (object instanceof CountryModel) {

                                CountryModel countryModel = (CountryModel) object;

                                if (countryModel.getCountryName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                                textView.setText(countryModel.getCountryName());
                                textView.setTag(countryModel);

                            } else if (object instanceof StateModel) {

                                StateModel stateModel = (StateModel) object;

                                if (stateModel.getStateName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                            } else if (object instanceof CityModel) {

                                CityModel cityModel = (CityModel) object;

                                if (cityModel.getCityName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (filterList.size() > 0) {
                        txtNoRecord.setVisibility(View.GONE);
                        listViewLocation.setVisibility(View.VISIBLE);

                        AdpLocation adpLocationList = new
                                AdpLocation(context, filterList, searchStr/*, locationType*/);
                        listViewLocation.setAdapter(adpLocationList);

                    } else {
                        txtNoRecord.setVisibility(View.VISIBLE);
                        listViewLocation.setVisibility(View.GONE);
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
                if (uploadProfilePhoto) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.REQUEST_CAMERA_BANNER);
                }
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (uploadProfilePhoto) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Constants.REQUEST_FILE_PROFILE);
                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Constants.REQUEST_FILE_BANNER);
                }
            }
        });
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                if (PermissionClass.verifyPermission(grantResults)) {
                    if (uploadProfilePhoto) {
                        // start cropping activity for pre-acquired image saved on the device
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle("Crop")
                                .setAspectRatio(1, 1)
                                .setRequestedSize(200, 200)
                                .start(getContext(), this);
                    } else {
                        // start cropping activity for pre-acquired image saved on the device
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(2, 1)
                                .setActivityTitle("Crop")
                                .setRequestedSize(400, 200)
                                .start(getContext(), this);
                    }

                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //if (shouldShowRequestPermissionRationale(permissions))
                    requestPermissions(permissions, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION);
                    // PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                }
            }
        }
    }
}

