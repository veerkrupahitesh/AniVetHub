package com.veeritsolution.android.anivethub.fragment.vet;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by veerk on 3/27/2017.
 */

public class UpdateVetExpInfoFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvFromDate, tvToDate, tvClinicHint;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto, backButton;
    private EditText edtTitle, edtRemarks;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private String title, remarks, fromDate, toDate;
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog dialog;
    private Calendar calFromTime, CalToTime;
    private long frmTime, toTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //    setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_experience_info, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvHeader.setText(getString(R.string.str_add_experience_info));

        //  backButton = (ImageView) rootView.findViewById(R.id.img_back_header);
        //  backButton.setVisibility(View.INVISIBLE);

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

        Utils.setupOutSideTouchHideKeyboard(getView().findViewById(R.id.activity_vet_experience));
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetExperienceInsert:
                VetLoginModel vetLoginModel = VetLoginModel.getVetCredentials();
                vetLoginModel.setIsVetExperience(1);
                VetLoginModel.saveVetCredentials(RestClient.getGsonInstance().toJson(vetLoginModel));
                homeActivity.popBackFragment();
                break;

        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
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

                        String format = (1 + selectedmonth) + "/" + selectedday + "/" + selectedyear;
                        //  tvFromDate.setText((1 + selectedmonth) + "/" + selectedday + "/" + selectedyear);

                        calFromTime = Calendar.getInstance();
                        calFromTime.set(selectedyear, selectedmonth,
                                selectedday);
                        frmTime = calFromTime.getTimeInMillis();
                        tvFromDate.setText("");
                        tvFromDate.setText(format);
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tvFromDate.setText("");
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
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tvToDate.setText("");
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btn_save:
                Utils.buttonClickEffect(view);
                if (validateForm()) {

                    try {
                        params = new JSONObject();
                        params.put("op", "VetExperienceInsert");
                        params.put("AuthKey", ApiList.AUTH_KEY);
                        params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                        params.put("Title", title);
                        params.put("FromDate", fromDate);
                        params.put("ToDate", toDate);
                        params.put("Description", remarks);

                        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_EXPERIENCE_INSERT, true,
                                RequestCode.VetExperienceInsert, this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
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
}

