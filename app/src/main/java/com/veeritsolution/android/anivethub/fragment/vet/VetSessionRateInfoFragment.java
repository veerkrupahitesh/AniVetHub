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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetSessionRateModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/15/2017.
 */

public class VetSessionRateInfoFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    //xml Components
    private Button btnSave;
    private TextView txvNormalFromTime, txvNormalToTime, txvSpecialFromTime, txvSpecialToTime,
            tvLabelNormalRate, tvLabelSpecialRate, tvLabelOtherDay;
    private EditText edtNormalRate, edtSpecialRate;
    private CheckBox chkMonday, chkTuesday, chkWednesday, chkThursday, chkFriday, chkSaturday, chkSunday;
    private TextView tvHeader;
    private Spinner spDay;
    private View rootView;
    private Toolbar toolbar;

    //Variables and objects declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private ArrayList<String> spDayList;
    private SpinnerAdapter adapter;
    private int position, addEditFlag;
    private String other;
    private Bundle bundle;
    private ArrayList<String> chkString;
    private VetSessionRateModel vetSessionRateModel;
    private int loginUser;
    // Calendar mCurrentTime = Calendar.getInstance();
    // int hour = mCurrentTime.get(Calendar.HOUR);
    //  int minute = mCurrentTime.get(Calendar.MINUTE);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
        bundle = getArguments();
        if (bundle != null) {
            vetSessionRateModel = (VetSessionRateModel) bundle.getSerializable(Constants.VET_SESSION_RATE_DATA);
            addEditFlag = bundle.getInt("flag");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_session_rate, container, false);


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
        tvHeader.setText(getString(R.string.str_session_rate_information));

        spDay = (Spinner) rootView.findViewById(R.id.sp_day);

        tvLabelNormalRate = (TextView) rootView.findViewById(R.id.tv_normal_hours);
        tvLabelNormalRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvLabelSpecialRate = (TextView) rootView.findViewById(R.id.tv_special_hours);
        tvLabelSpecialRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvLabelOtherDay = (TextView) rootView.findViewById(R.id.tv_label_otherDays);
        tvLabelOtherDay.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        txvNormalFromTime = (TextView) rootView.findViewById(R.id.tv_normalFromDate);
        txvNormalFromTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        txvNormalToTime = (TextView) rootView.findViewById(R.id.tv_normalToDate);
        txvNormalToTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        txvSpecialFromTime = (TextView) rootView.findViewById(R.id.tv_specialFromDate);
        txvSpecialFromTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        txvSpecialToTime = (TextView) rootView.findViewById(R.id.tv_specialToDate);
        txvSpecialToTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtNormalRate = (EditText) rootView.findViewById(R.id.edt_normalSessionRate);
        edtNormalRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtSpecialRate = (EditText) rootView.findViewById(R.id.edt_specialSessionRate);
        edtSpecialRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkMonday = (CheckBox) rootView.findViewById(R.id.chk_Monday);
        chkMonday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkTuesday = (CheckBox) rootView.findViewById(R.id.chk_Tuesday);
        chkTuesday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkWednesday = (CheckBox) rootView.findViewById(R.id.chk_Wednesday);
        chkWednesday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkThursday = (CheckBox) rootView.findViewById(R.id.chk_Thrusday);
        chkThursday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkFriday = (CheckBox) rootView.findViewById(R.id.chk_friday);
        chkFriday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkSaturday = (CheckBox) rootView.findViewById(R.id.chk_saturday);
        chkSaturday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkSunday = (CheckBox) rootView.findViewById(R.id.chk_sunday);
        chkSunday.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;

                setEnableCheckbox();
                switch (pos) {
                    case 1:
                        chkMonday.setEnabled(false);
                        break;
                    case 2:
                        chkTuesday.setEnabled(false);
                        break;
                    case 3:
                        chkWednesday.setEnabled(false);
                        break;
                    case 4:
                        chkThursday.setEnabled(false);
                        break;
                    case 5:
                        chkFriday.setEnabled(false);
                        break;
                    case 6:
                        chkSaturday.setEnabled(false);
                        break;
                    case 7:
                        chkSunday.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDayList = new ArrayList<>();
        spDayList.add(0, "Select Day");
        spDayList.add(1, "Monday");
        spDayList.add(2, "Tuesday");
        spDayList.add(3, "Wednesday");
        spDayList.add(4, "Thursday");
        spDayList.add(5, "Friday");
        spDayList.add(6, "Saturday");
        spDayList.add(7, "Sunday");
        adapter = new SpinnerAdapter(getActivity(), R.layout.spinner_row_list, spDayList);
        spDay.setAdapter(adapter);

        if (addEditFlag == Constants.VET_SESSION_RATE_FLAG_EDIT) {

            // GetSessionRateInfo();
            txvNormalFromTime.setText(vetSessionRateModel.getNormalFromTime());
            txvNormalToTime.setText(vetSessionRateModel.getNormalToTime());
            txvSpecialFromTime.setText(vetSessionRateModel.getSpecialFromTime());
            txvSpecialToTime.setText(vetSessionRateModel.getSpecialToTime());
            edtSpecialRate.setText(String.valueOf(vetSessionRateModel.getSpecialSessionRate()));
            edtNormalRate.setText(String.valueOf(vetSessionRateModel.getNormalSessionRate()));

            int pos = vetSessionRateModel.getDayNo();
            spDay.setSelection(pos);
        }
    }


    private void setEnableCheckbox() {

        chkMonday.setEnabled(true);
        chkTuesday.setEnabled(true);
        chkWednesday.setEnabled(true);
        chkThursday.setEnabled(true);
        chkFriday.setEnabled(true);
        chkSaturday.setEnabled(true);
        chkSunday.setEnabled(true);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case VetRateInsert:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    homeActivity.popBackFragment();
                }
                break;

            case VetRateUpdate:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    homeActivity.popBackFragment();
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case VetRateInsert:
                ToastHelper.getInstance().showMessage(mError);
                break;

            case VetRateUpdate:
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_normalFromDate:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().ShowTimePickerDialog(getActivity(), txvNormalFromTime);
                break;

            case R.id.tv_normalToDate:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().ShowTimePickerDialog(getActivity(), txvNormalToTime);
                break;

            case R.id.tv_specialFromDate:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().ShowTimePickerDialog(getActivity(), txvSpecialFromTime);
                break;

            case R.id.tv_specialToDate:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().ShowTimePickerDialog(getActivity(), txvSpecialToTime);
                break;

            case R.id.btn_save:

                validateCheckbox();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
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
            if (loginUser == Constants.CLINIC_LOGIN) {
                homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            } else if (loginUser == Constants.VET_LOGIN) {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void validateCheckbox() {
        chkString = new ArrayList<>();
        if (chkMonday.isChecked()) {
            other = "1";
            chkString.add(other);
        }
        if (chkTuesday.isChecked()) {
            other = "2";
            chkString.add(other);
        }
        if (chkWednesday.isChecked()) {
            other = "3";
            chkString.add(other);
        }
        if (chkThursday.isChecked()) {
            other = "4";
            chkString.add(other);
        }
        if (chkFriday.isChecked()) {
            other = "5";
            chkString.add(other);
        }
        if (chkSaturday.isChecked()) {
            other = "6";
            chkString.add(other);
        }
        if (chkSunday.isChecked()) {
            other = "7";
            chkString.add(other);
        }
        StringBuilder sb = new StringBuilder();

        for (String str : chkString) {
            sb.append(str.toString());
            sb.append(",");
        }

        String sel_cat = sb.toString();
        validateForm(sel_cat);
    }

    private void validateForm(String sel_cat) {

        if (spDay.getSelectedItemPosition() == 0) {
            spDay.requestFocus();
            ToastHelper.getInstance().showMessage("Select Day");
        } else if (txvNormalFromTime.getText().toString().isEmpty()) {
            txvNormalFromTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_normal_from_time));
        } else if (txvNormalToTime.getText().toString().isEmpty()) {
            txvNormalToTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_normal_to_time));
        } else if (edtNormalRate.getText().toString().isEmpty()) {
            edtNormalRate.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_normal_session_rate));
        } else if (txvSpecialFromTime.getText().toString().isEmpty()) {
            txvSpecialFromTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_special_from_time));
        } else if (txvSpecialToTime.getText().toString().isEmpty()) {
            txvSpecialToTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_special_to_time));
        } else if (edtSpecialRate.getText().toString().isEmpty()) {
            edtSpecialRate.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_special_session_rate));
        } else {
            if (addEditFlag == Constants.VET_SESSION_RATE_FLAG_EDIT) {
                vetRateUpdate();
            } else {
                vetRateInsert(sel_cat);
            }
        }
    }

    private void vetRateInsert(String sel_cat) {
        try {
            params = new JSONObject();
            params.put("op", "VetRateInsert");
            params.put("AuthKey", ApiList.AUTH_KEY);

            if (loginUser == Constants.CLINIC_LOGIN) {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            } else if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            }

            params.put("DayNo", position);
            params.put("Normal_FromTime", txvNormalFromTime.getText().toString());
            params.put("Normal_ToTime", txvNormalToTime.getText().toString());
            params.put("Normal_SessionRate", edtNormalRate.getText().toString());
            params.put("Special_FromTime", txvSpecialFromTime.getText().toString());
            params.put("Special_ToTime", txvSpecialToTime.getText().toString());
            params.put("Special_SessionRate", edtSpecialRate.getText().toString());
            params.put("OtherDays", sel_cat);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_VET_RATE_INSERT,
                    true, RequestCode.VetRateInsert, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void vetRateUpdate() {

        try {
            params = new JSONObject();
            params.put("op", "VetRateUpdate");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetRateId", vetSessionRateModel.getVetRateId());
            params.put("DayNo", position);
            params.put("Normal_FromTime", txvNormalFromTime.getText().toString());
            params.put("Normal_ToTime", txvNormalToTime.getText().toString());
            params.put("Normal_SessionRate", edtNormalRate.getText().toString());
            params.put("Special_FromTime", txvSpecialFromTime.getText().toString());
            params.put("Special_ToTime", txvSpecialToTime.getText().toString());
            params.put("Special_SessionRate", edtSpecialRate.getText().toString());
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_RATE_UPDATE,
                    true, RequestCode.VetRateUpdate, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
