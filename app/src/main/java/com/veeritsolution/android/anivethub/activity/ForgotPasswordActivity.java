package com.veeritsolution.android.anivethub.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Veer on 03/10/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private EditText edtuser;
    private Button btnsubmit;
    private Toolbar toolbar;
    private TextView tvHeader;

    // object and variable declaration
    private String userName, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_forgot_password));

        edtuser = (EditText) findViewById(R.id.edt_userName);
        edtuser.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnsubmit = (Button) findViewById(R.id.btn_save);
        btnsubmit.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        Utils.setupOutSideTouchHideKeyboard(findViewById(R.id.activity_forgot_password));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:
                Utils.buttonClickEffect(view);
                userName = edtuser.getText().toString();
                if (userName.isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_username));

                } else {
                    getForgotPassword(userName);
                    edtuser.setText("");
                }
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();

                break;
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {
            case ForgotPassword:
                try {

                    if (!(mObject instanceof ErrorModel)) {
                        JSONArray jsonArray = (JSONArray) mObject;
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        password = jsonObject.getString("Password");
                        Debug.trace("password", password);
                    }

                    // CustomDialog.getInstance().showAlert(ForgotPasswordActivity.this, "Your password is: " + password + "\n please note it down", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    public void getForgotPassword(String user) {
        try {
            JSONObject params = new JSONObject();
            params.put("op", "ForgotPassword");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("UserName", user);
            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.FORGOT_PASSWORD, true, RequestCode.ForgotPassword,
                    this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
