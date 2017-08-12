package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.RegisterBy;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Veer on 15/09/2016.
 */
public class SignUpActivity extends AppCompatActivity implements OnClickEvent, DataObserver, AdapterView.OnItemSelectedListener {

    // xml components
    private Button btnSignUp;
    private EditText edtName, edtUserName, edtMobileNumber, edtEmail, edtPassword, edtReTypePassword;
    private Spinner spRole;
    private LinearLayout linPassword, linRetypePassword;
    private Toolbar toolbar;
    private TextView tvHeader;

    // object and variable declaration
    private ArrayList<String> role;
    private int selectRole;
    private String mName, mUserName, mEmailAddress, mMObileNumber, mPassword, mRetypePassword, registerBy;
    private JSONObject params;
    private Intent intent;
    private SpinnerAdapter adpRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        intent = getIntent();

        init();

        setUserData(intent);
    }

    private void setUserData(Intent intent) {

        if (intent != null) {

            Bundle bundle = intent.getBundleExtra(Constants.USER_DATA);

            mName = bundle.getString(Constants.NAME);
            mUserName = bundle.getString(Constants.USERNAME);
            mEmailAddress = bundle.getString(Constants.EMAIL);
            registerBy = bundle.getString(Constants.REGISTER_BY);

            if (!registerBy.equals(RegisterBy.APP.getRegisterBy())) {

                linPassword.setVisibility(View.GONE);
                linRetypePassword.setVisibility(View.GONE);
            }

            if (mName != null && !mName.isEmpty()) {

                edtName.setText(mName);
            }
            if (mEmailAddress != null && !mEmailAddress.isEmpty()) {

                edtEmail.setText(mEmailAddress);
            }
            if (mUserName != null && !mUserName.isEmpty()) {

                edtUserName.setText(mUserName);
            }
        }
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_register));

        edtName = (EditText) findViewById(R.id.edt_name);
        edtName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtUserName = (EditText) findViewById(R.id.edt_userName);
        edtUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtMobileNumber = (EditText) findViewById(R.id.edt_phone);
        edtMobileNumber.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        spRole = (Spinner) findViewById(R.id.sp_role);

        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPassword.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtReTypePassword = (EditText) findViewById(R.id.edt_reTypePassword);
        edtReTypePassword.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        linPassword = (LinearLayout) findViewById(R.id.lin_password);
        linRetypePassword = (LinearLayout) findViewById(R.id.lin_retypePassword);

        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        btnSignUp.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        role = new ArrayList<>();
        role.add("Select Role");
        role.add("Client");
        role.add("Veterinary Surgeon");
        role.add("Veterinary Surgeon Practice");
        adpRole = new SpinnerAdapter(this, R.layout.spinner_row_list, role);
        spRole.setAdapter(adpRole);
        Utils.setupOutSideTouchHideKeyboard(findViewById(R.id.activity_sign_up));
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case ClientInsert:

                ClientLoginModel clientLoginModel = (ClientLoginModel) mObject;

                if (clientLoginModel.getIsDataExists() == Constants.DATA_EXISTS) {

                    ToastHelper.getInstance().showMessage(getString(R.string.str_user_already_exists));
                } else {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                    PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLIENT_LOGIN);
                    intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            case VetInsert:

                VetLoginModel vetLoginModel = (VetLoginModel) mObject;

                if (vetLoginModel.getIsDataExists() == Constants.DATA_EXISTS) {

                    ToastHelper.getInstance().showMessage(getString(R.string.str_user_already_exists));
                } else {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                    PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.VET_LOGIN);
                    intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            case PracInsert:

                PractiseLoginModel practiseLoginModel = (PractiseLoginModel) mObject;

                if (practiseLoginModel.getIsDataExists() == Constants.DATA_EXISTS) {

                    ToastHelper.getInstance().showMessage(getString(R.string.str_user_already_exists));
                } else {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                    PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLINIC_LOGIN);
                    intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_signUp:
                Utils.buttonClickEffect(view);
                try {
                    if (validateForm()) {

                        if (selectRole == 1) {
                            CustomDialog.getInstance().showTermAndConditions(this, getString(R.string.str_terms_and_conditions), false, true);
                        } else {
                            CustomDialog.getInstance().showTermAndConditions(this, getString(R.string.str_terms_and_conditions), false, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_agree:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                SignUpUser();
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }

    private void SignUpUser() {

        try {
            if (selectRole == 1) {

                params = new JSONObject();
                params.put("op", "ClientInsert");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("ClientName", mName);
                params.put("PhoneNo", mMObileNumber);
                params.put("EmailId", mEmailAddress);
                params.put("UserName", mUserName);
                params.put("Password", mPassword);
                params.put("AcTokenId", " ");
                params.put("RegisteredBy", registerBy);

                RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CLIENT_INSERT,
                        true, RequestCode.ClientInsert, this);

            } else if (selectRole == 2) {

                params = new JSONObject();
                params.put("op", "VetInsert");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetName", mName);
                params.put("PhoneNo", mMObileNumber);
                params.put("EmailId", mEmailAddress);
                params.put("UserName", mUserName);
                params.put("Password", mPassword);
                params.put("AcTokenId", " ");
                params.put("RegisteredBy", registerBy);
                params.put("IsVetPractiseUser", 0);

                RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_INSERT,
                        true, RequestCode.VetInsert, this);

            } else if (selectRole == 3) {

                params = new JSONObject();
                params.put("op", "VetInsert");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetName", mName);
                params.put("PhoneNo", mMObileNumber);
                params.put("EmailId", mEmailAddress);
                params.put("UserName", mUserName);
                params.put("Password", mPassword);
                params.put("AcTokenId", " ");
                params.put("RegisteredBy", registerBy);
                params.put("IsVetPractiseUser", 1);

                RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_INSERT,
                        true, RequestCode.PracInsert, this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        mName = edtName.getText().toString().trim();
        mUserName = edtUserName.getText().toString().trim();
        mEmailAddress = edtEmail.getText().toString().trim();
        mMObileNumber = edtMobileNumber.getText().toString().trim();
        mPassword = edtPassword.getText().toString().trim();
        mRetypePassword = edtReTypePassword.getText().toString().trim();
        selectRole = spRole.getSelectedItemPosition();

        if (mName.isEmpty()) {
            edtName.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_name));
            return false;
        } else if (mEmailAddress.isEmpty()) {
            edtEmail.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_email));
            return false;
        } else if (!mEmailAddress.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_email_address));
            return false;
        } else if (mMObileNumber.isEmpty()) {
            edtMobileNumber.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_mobile));
            return false;
        } else if (mMObileNumber.length() < Constants.PHONE_LENGTH) {
            edtMobileNumber.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_phoneno));
            return false;
        } else if (selectRole == 0) {
            spRole.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_role));
            return false;
        } else if (mUserName.isEmpty()) {
            edtUserName.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_username));
            return false;
        } else if (registerBy.equals(RegisterBy.APP.getRegisterBy())) {
            if (mPassword.isEmpty()) {
                edtPassword.requestFocus();
                ToastHelper.getInstance().showMessage(getString(R.string.str_enter_password));
                return false;
            } else if (mRetypePassword.isEmpty()) {
                edtReTypePassword.requestFocus();
                ToastHelper.getInstance().showMessage(getString(R.string.str_enter_retype_password));
                return false;
            } else if (!mPassword.equals(mRetypePassword)) {
                edtReTypePassword.requestFocus();
                ToastHelper.getInstance().showMessage(getString(R.string.str_password_matches));
                return false;
            } else {
                return true;
            }
        } else {

            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        selectRole = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        CustomDialog.getInstance().dismiss();
    }
}
