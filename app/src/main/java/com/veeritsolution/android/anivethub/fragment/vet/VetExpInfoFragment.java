package com.veeritsolution.android.anivethub.fragment.vet;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by admin on 12/17/2016.
 */

public class VetExpInfoFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvFromDate, tvToDate, tvClinicHint;
    private Toolbar toolbar;
    private ImageView backButton, imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private EditText edtTitle, edtRemarks;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private String title, remarks, fromDate, toDate;
    private VetExperience vetExpertise;
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog dialog;
    private Calendar calFromTime, CalToTime;
    private long frmTime, toTime;
    private int addEditSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            //      fromSignUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
            addEditSignUp = bundle.getInt(Constants.ADD_EDIT_SINGUP);
            vetExpertise = (VetExperience) bundle.getSerializable(Constants.VET_EXPERIENCE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_experience_info, container, false);

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
                tvHeader.setText(getString(R.string.str_add_experience_info));
                break;
            case Constants.FROM_EDIT:
                tvHeader.setText(getString(R.string.str_edit_experience_info));
                break;
            case Constants.FROM_SIGN_UP:
                tvHeader.setText(getString(R.string.str_add_experience_info));
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

        edtTitle = (EditText) rootView.findViewById(R.id.edt_vet_clinic_name);
        edtTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvClinicHint = (TextView) rootView.findViewById(R.id.tv_clinicHint);
        tvClinicHint.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvFromDate = (TextView) rootView.findViewById(R.id.tv_vet_fromdate);
        tvFromDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvToDate = (TextView) rootView.findViewById(R.id.tv_vet_todate);
        tvToDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtRemarks = (EditText) rootView.findViewById(R.id.edt_vet_remarks);
        edtRemarks.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Utils.setupOutSideTouchHideKeyboard(rootView);

        if (addEditSignUp == Constants.FROM_EDIT) {
            getExperienceInfo();
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetExperienceInfo:

                rootView.setVisibility(View.VISIBLE);
                vetExpertise = (VetExperience) mObject;
                if (vetExpertise != null) {
                    setData(vetExpertise);
                }
                break;

            case VetExperienceInsert:
                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetExperience(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));
                switch (addEditSignUp) {
                    case Constants.FROM_ADD:
                        homeActivity.popBackFragment();
                        break;
                    case Constants.FROM_SIGN_UP:
                        bundle = new Bundle();
                        bundle.putInt(Constants.ADD_EDIT_SINGUP, Constants.FROM_SIGN_UP);
                        homeActivity.pushFragment(new VetSpecInfoFragment(), true, true, bundle);
                        break;
                }
                break;

            case VetExperienceUpdate:

                homeActivity.popBackFragment();
                break;
        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetVetExperienceInfo:

                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetExperienceInsert:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetExperienceUpdate:

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

            case R.id.tv_vet_fromdate:
                Utils.buttonClickEffect(view);
                dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                        // fromDate = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;
                        String format = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;
                        //tvFromDate.setText((1 + selectedmonth) + "/" + selectedday + "/" + selectedyear);

                        calFromTime = Calendar.getInstance();
                        calFromTime.set(selectedyear, selectedmonth,
                                selectedday);
                        frmTime = calFromTime.getTimeInMillis();
                        tvFromDate.setText("");
                        tvFromDate.setText(format);
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tvFromDate.setText("");
                            //onBackPressed();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.tv_vet_todate:
                Utils.buttonClickEffect(view);
                dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {

                        //   toDate = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;

                        String format = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;

                        CalToTime = Calendar.getInstance();
                        CalToTime.set(selectedyear, selectedmonth,
                                selectedday);
                        toTime = CalToTime.getTimeInMillis();
                        tvToDate.setText("");
                        if (toTime > frmTime) {
                            tvToDate.setText(format);
                        } else {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_to_date));
                        }
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tvToDate.setText("");
                            //onBackPressed();
                        }
                    }
                });
                dialog.show();
                dialog.show();
                break;

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
        }
    }

    private void saveData() {
        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("Title", title);
            params.put("FromDate", fromDate);
            params.put("ToDate", toDate);
            params.put("Description", remarks);

            switch (addEditSignUp) {

                case Constants.FROM_ADD:
                    params.put("op", "VetExperienceInsert");
                    params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERIENCE_INSERT, true,
                            RequestCode.VetExperienceInsert, this);
                    break;

                case Constants.FROM_EDIT:
                    params.put("op", "VetExperienceUpdate");
                    params.put("VetExperienceId", vetExpertise.getVetExperienceId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERIENCE_UPDATE, true,
                            RequestCode.VetExperienceUpdate, this);
                    break;

                case Constants.FROM_SIGN_UP:
                    params.put("op", "VetExperienceInsert");
                    params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERIENCE_INSERT, true,
                            RequestCode.VetExperienceInsert, this);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getExperienceInfo() {
        try {
            params = new JSONObject();
            params.put("op", "GetVetExperienceInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetExperienceId", vetExpertise.getVetExperienceId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_EXPERIENCE_INFO, true,
                    RequestCode.GetVetExperienceInfo, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(VetExperience vetExpertise) {

        edtTitle.setText(vetExpertise.getTitle());
        //  fromDate = Utils.formatDate(vetExpertise.getFromDate(), Constants.DATE_DD_MM_YYYY, Constants.DATE_MM_DD_YYYY);
        tvFromDate.setText(vetExpertise.getFromDate());
        //  toDate = Utils.formatDate(vetExpertise.getToDate(), Constants.DATE_DD_MM_YYYY, Constants.DATE_MM_DD_YYYY);
        tvToDate.setText(vetExpertise.getToDate());
        edtRemarks.setText(vetExpertise.getDescription());
    }

    private boolean validateForm() {

        title = edtTitle.getText().toString().trim();
        fromDate = tvFromDate.getText().toString().trim();
        toDate = tvToDate.getText().toString().trim();
        remarks = edtRemarks.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_clinic_title));
            return false;
        } else if (fromDate.isEmpty()) {
            tvFromDate.requestFocus();
            ToastHelper.getInstance().showMessage("Select FROM date");
            return false;
        } else if (toDate.isEmpty()) {
            tvToDate.requestFocus();
            ToastHelper.getInstance().showMessage("Select TO date");
            return false;
        } else {
            return true;
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
}
