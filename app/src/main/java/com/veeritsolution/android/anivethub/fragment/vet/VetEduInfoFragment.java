package com.veeritsolution.android.anivethub.fragment.vet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpVetEducation;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.DegreeModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.UniversityModel;
import com.veeritsolution.android.anivethub.models.VetEducation;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 12/17/2016.
 */

public class VetEduInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvPassingYear, tvUniversity, tvDegree;
    private Toolbar toolbar;
    private ImageView backButton, imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    //  private Spinner spDegree, spUniversity;
    private EditText edtDescription;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private int universityId = 0, degreeId = 0;
    private VetEducation vetEducation;

    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog dialog;
    private int addEditSignUp;
    private Dialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            addEditSignUp = bundle.getInt(Constants.ADD_EDIT_SINGUP);
            //  fromSignUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
            vetEducation = (VetEducation) bundle.getSerializable(Constants.VET_EDUCATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_educationinfo, container, false);

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

        // backButton = (ImageView) rootView.findViewById(R.id.img_back_header);
        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        switch (addEditSignUp) {
            case Constants.FROM_ADD:
                tvHeader.setText(getString(R.string.str_add_education_info));
                break;
            case Constants.FROM_EDIT:
                tvHeader.setText(getString(R.string.str_edit_education_info));
                break;
            case Constants.FROM_SIGN_UP:
                tvHeader.setText(getString(R.string.str_add_education_info));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                break;
        }

        tvName = (TextView) rootView.findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        tvUserName = (TextView) rootView.findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        Utils.setProfileImage(getActivity(), VetLoginModel.getVetCredentials().getProfilePic(),
                R.drawable.img_vet_profile, imgVetProfilePhoto);

        imgVetBannerPhoto = (ImageView) rootView.findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(getActivity(), VetLoginModel.getVetCredentials().getBannerPic(),
                R.drawable.img_vet_banner, imgVetBannerPhoto);

        imgSelectBannerPhoto = (ImageView) rootView.findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) rootView.findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvUniversity = (TextView) rootView.findViewById(R.id.tv_university);
        tvUniversity.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvDegree = (TextView) rootView.findViewById(R.id.tv_degree);
        tvDegree.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPassingYear = (TextView) rootView.findViewById(R.id.tv_passingYear);
        tvPassingYear.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtDescription = (EditText) rootView.findViewById(R.id.edt_vet_desc);
        edtDescription.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);

        if (addEditSignUp == Constants.FROM_EDIT) {
            getEducationInfo();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetEducationInfo:

                rootView.setVisibility(View.VISIBLE);
                vetEducation = (VetEducation) mObject;
                if (vetEducation != null) {
                    setData(vetEducation);
                    // GetUniversityInfo();
                }
                break;

            case GetUniversityInfo:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        ArrayList<UniversityModel> universityList = (ArrayList<UniversityModel>) mObject;

                        showEducationDialog(getActivity(), universityList, "Select University");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //setUniversityAdapter(mObject);
                // GetDegreeInfo();
                break;

            case GetDegreeInfo:

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {
                        ArrayList<DegreeModel> degreeModelList = (ArrayList<DegreeModel>) mObject;
                        showEducationDialog(getActivity(), degreeModelList, "Select Degree");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // setDegreeAdapter(mObject);

                break;

            case VetEducationInsert:

                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetEducation(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));

                switch (addEditSignUp) {

                    case Constants.FROM_ADD:
                        homeActivity.popBackFragment();
                        break;

                    case Constants.FROM_SIGN_UP:
                        bundle = new Bundle();
                        bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_SIGN_UP);
                        homeActivity.pushFragment(new VetExpInfoFragment(), true, true, bundle);
                        break;
                }
                break;

            case VetEducationUpdate:

                homeActivity.popBackFragment();
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetDegreeInfo:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetUniversityInfo:

                ToastHelper.getInstance().showMessage(mError);
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
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:

                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    saveData();
                }
                break;

            case R.id.tv_passingYear:

                Utils.buttonClickEffect(view);
                dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                        tvPassingYear.setText(String.valueOf(selectedyear));
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tvPassingYear.setText("");
                            //onBackPressed();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.tv_university:
                getUniversityInfo();
                break;

            case R.id.tv_degree:
                getDegreeInfo();
                break;
        }

    }

    private void getEducationInfo() {
        try {
            params = new JSONObject();
            params.put("op", "GetVetEducationInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetEducationId", vetEducation.getVetEducationId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EDUCATION_INFO, true,
                    RequestCode.GetVetEducationInfo, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(VetEducation vetEducation) {
        tvPassingYear.setText(vetEducation.getPassingYear());
        edtDescription.setText(vetEducation.getDescription());
        tvUniversity.setText(vetEducation.getUniversityName());
        tvDegree.setText(vetEducation.getDegreeName());
    }

    private void getUniversityInfo() {
        try {
            params = new JSONObject();
            params.put("op", "GetUniversityInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_UNIVERSITY_INFO, true, RequestCode.GetUniversityInfo, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDegreeInfo() {

        try {
            params = new JSONObject();
            params.put("op", "GetDegreeInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.GET_DEGREE_INFO, true, RequestCode.GetDegreeInfo, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        if (tvUniversity.getText().toString().isEmpty()) {
            tvUniversity.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_university));
            return false;
        } else if (tvDegree.getText().toString().isEmpty()) {
            tvDegree.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_degree));

            return false;
        } else if (tvPassingYear.getText().toString().trim().equals(getString(R.string.str_passing_year)) || tvPassingYear.getText().toString().trim().isEmpty()) {
            tvPassingYear.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_passing_year));
            return false;
        } else {

            return true;
        }
    }

    private void saveData() {
        try {

            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);

            if (degreeId == 0) {
                params.put("DegreeId", vetEducation.getDegreeId());
            } else {
                params.put("DegreeId", degreeId);
            }

            if (universityId == 0) {
                params.put("UniversityId", vetEducation.getUniversityId());
            } else {
                params.put("UniversityId", universityId);
            }
            params.put("PassingYear", tvPassingYear.getText().toString().trim());
            params.put("Description", edtDescription.getText().toString().trim());

            switch (addEditSignUp) {

                case Constants.FROM_ADD:

                    params.put("op", "VetEducationInsert");
                    params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                            ApiList.VET_EDUCATION_INSERT, true, RequestCode.VetEducationInsert, this);
                    break;

                case Constants.FROM_EDIT:

                    params.put("op", "VetEducationUpdate");
                    params.put("VetEducationId", vetEducation.getVetEducationId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                            ApiList.VET_EDUCATION_UPDATE, true, RequestCode.VetEducationUpdate, this);
                    break;

                case Constants.FROM_SIGN_UP:

                    params.put("op", "VetEducationInsert");
                    params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                            ApiList.VET_EDUCATION_INSERT, true, RequestCode.VetEducationInsert, this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // homeActivity.popBackFragment();
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            // homeActivity.pushFragment(new ClientHomeFragment(), true, true, null);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method show the datePickerDialog with list of locations and having functionality of search
     *
     * @param context(Context)        : context
     * @param listLocation(ArrayList) : list of locations with countries, states and cities
     */

    public void showEducationDialog(final Context context, final ArrayList<?> listLocation, String dialogTitle) {

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
                AdpVetEducation adpVetEducation = new AdpVetEducation(context, listLocation);
                listViewLocation.setAdapter(adpVetEducation);
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

                        if (object instanceof UniversityModel) {

                            UniversityModel universityModel = (UniversityModel) object;
                            tvUniversity.setText(universityModel.getUniversityName());
                            universityId = universityModel.getUniversityId();

                        } else if (object instanceof DegreeModel) {

                            DegreeModel degreeModel = (DegreeModel) object;
                            tvDegree.setText(degreeModel.getDegreeName());
                            degreeId = degreeModel.getDegreeId();
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
