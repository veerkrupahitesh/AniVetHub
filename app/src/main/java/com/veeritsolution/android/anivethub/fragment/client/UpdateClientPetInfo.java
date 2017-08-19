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
import com.veeritsolution.android.anivethub.adapter.AdpPetTypeGroupList;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.ImageUpload;
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
 * Created by veer on 3/22/2017.
 */

public class UpdateClientPetInfo extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvPetBirthDate, tvPetType, tvPetBreed;
    private Toolbar toolbar;
    private EditText edtPetName, edtPetWeight;
    private Spinner spPetGender;
    private ImageView imgClientProfilePhoto, imgClientBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto, imgBack;
    private View rootView;
    private RecyclerView recyclerPetPics;

    // object and variable declaration
    private String petName, petBirthdate, petWeight, petType;
    // private Map<String, Integer> petTypeMap, petBreedMap;
    private int petTypeId = 0, petBreedId, petGender;
    // private boolean fromSignUp;
    // private Bundle bundle;
    //  private long clientPetId;
    private JSONObject params;
    private HomeActivity homeActivity;
    private ArrayList<String> genderList;
    // private String genderList[] = {"Select Gender", "Male", "Female"};
    private PetDetailsModel petDetailsModel;
    private SpinnerAdapter adpGender/*, adpPetType, adpPetBreed*/;
    // private ArrayList<String> selectPetType, selectPetBreed;
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog dialog;
    private Dialog mDialog;
    private ArrayList<PetTypeModel> petTypeList;
    private ArrayList<PetBreedModel> petBreedList;
    private ArrayList<PetPicsModel> petPicsList;
    private PetPicsModel petPicsModel;
    private AdpPetPics adpPetPics;
    private int apiType;
    private String image64Base;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* bundle = getArguments();
        if (bundle != null) {
            fromSignUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
        }*/
        genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Male neutered");
        genderList.add("Female neutered");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_client_pet_info, container, false);

        homeActivity = (HomeActivity) getActivity();
        //  rootView.setVisibility(View.INVISIBLE);
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
        // tvHeader.setGravity(Gravity.START);
        // if (fromSignUp) {
        tvHeader.setText(getString(R.string.str_add_pet));
        // } else {
        //      tvHeader.setText(getString(R.string.str_edit_pet));
        //  }

        // imgBack = (ImageView) rootView.findViewById(R.id.img_back_header);
        //  imgBack.setVisibility(View.GONE);

        imgClientProfilePhoto = (ImageView) rootView.findViewById(R.id.img_clientProfilePic);
        Utils.setProfileImage(getActivity(), ClientLoginModel.getClientCredentials().getProfilePic(),
                R.drawable.img_client_profile, imgClientProfilePhoto);

        recyclerPetPics = (RecyclerView) rootView.findViewById(R.id.recycler_PetPics);
        recyclerPetPics.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        imgClientBannerPhoto = (ImageView) rootView.findViewById(R.id.img_client_banner);
        Utils.setBannerImage(getActivity(), ClientLoginModel.getClientCredentials().getBannerPic(),
                R.drawable.img_client_banner, imgClientBannerPhoto);

        tvName = (TextView) rootView.findViewById(R.id.tv_clientName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(ClientLoginModel.getClientCredentials().getClientName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_clientUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(ClientLoginModel.getClientCredentials().getUserName());

        tvPetType = (TextView) rootView.findViewById(R.id.tv_petType);
        tvPetType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetBreed = (TextView) rootView.findViewById(R.id.tv_petBreed);
        tvPetBreed.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        edtPetName = (EditText) rootView.findViewById(R.id.edt_pet_name);
        edtPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

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

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setupOutSideTouchHideKeyboard(rootView);
        petPicsList = new ArrayList<>();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPetTypeInfo:
                //rootView.setVisibility(View.VISIBLE);
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
                    homeActivity.popBackFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetPetTypeInfo:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetPetBreedInfo:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case ClientPetInsert:
                ToastHelper.getInstance().showMessage(mError);
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
                dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                        // fromDate = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;
                        String format = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedmonth + 1, selectedday, selectedyear);
                        tvPetBirthDate.setText(format);
                        //tvPetBirthDate.setText((1 + selectedmonth) + "/" + selectedday + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                break;

            case R.id.tv_petType:

                getPetTypeInfo();
                break;

            case R.id.tv_petBreed:

                if (petTypeId != 0) {
                    getPetBreedInfo(petTypeId);
                } else {
                    ToastHelper.getInstance().showMessage("Select first Pet Type");
                }
                break;

            case R.id.lin_addPetPhoto:
                Utils.buttonClickEffect(view);
               /* if (petDetailsModel == null) {

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
                petPicsList.remove(petPicsModel);
                adpPetPics = new AdpPetPics(getActivity(), petPicsList);
                recyclerPetPics.setAdapter(adpPetPics);
                //deletePetPic(petPicsModel);
                break;

            case R.id.btn_actionCancel:
                CustomDialog.getInstance().dismiss();
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
        PetPicsModel petPicsModel = new PetPicsModel();
        petPicsModel.setPicPath(Crop.getOutput(result).getPath());
        petPicsModel.setBase64image(image64Base);
        petPicsList.add(petPicsModel);
        adpPetPics = new AdpPetPics(getActivity(), petPicsList);
        recyclerPetPics.setAdapter(adpPetPics);
    }

    private void saveData() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_PET_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(ClientLoginModel.getClientCredentials().getClientId()));
            // params.put("ClientName", ClientLoginModel.getClientCredentials().getClientName());
            params.put("PetName", petName);
            params.put("PetTypeId", petTypeId);
            params.put("PetBreedId", petBreedId);

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

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.CLIENT_PET_INSERT, true, RequestCode.ClientPetInsert, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getPetTypeInfo() {
        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", "GetPetTypeInfo");

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PET_TYPE_INFO, true, RequestCode.GetPetTypeInfo, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        petName = edtPetName.getText().toString().trim();
        petType = tvPetType.getText().toString().trim();
        petBirthdate = tvPetBirthDate.getText().toString().trim();
        petGender = spPetGender.getSelectedItemPosition();
        petWeight = edtPetWeight.getText().toString().trim();


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

    private void getPetBreedInfo(int petTypeId) {

        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("op", "GetPetBreedInfo");
            params.put("PetTypeId", petTypeId);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_PET_BREED_INFO, true, RequestCode.GetPetBreedInfo, this);

        } catch (Exception e) {
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
                            tvPetBreed.setText("");
                            //  tvPetBreed.performClick();

                        } else if (object instanceof PetBreedModel) {

                            PetBreedModel petBreedModel = (PetBreedModel) object;
                            tvPetBreed.setText(petBreedModel.getPetBreedName());

                            petBreedId = petBreedModel.getPetBreedId();

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onBackPressed() {
        // homeActivity.popBackFragment();
        homeActivity.finish();
    }
}

