package com.veeritsolution.android.anivethub.fragment;

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
import android.widget.EditText;
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
import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jaymin on 1/9/2017.
 */

public class ChangePwdFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // xml components
    private Toolbar toolbar;
    private TextView tvHeader;
    private Button btnSave;
    private EditText edtOldPwd, edtNewPwd, edtConfirmPwd;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();

        setHasOptionsMenu(true);
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_pwd, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_change_password));

        edtOldPwd = (EditText) getView().findViewById(R.id.edt_old_pwd);
        edtOldPwd.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtNewPwd = (EditText) getView().findViewById(R.id.edt_new_pwd);
        edtNewPwd.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtConfirmPwd = (EditText) getView().findViewById(R.id.edt_confirm_pwd);
        edtConfirmPwd.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnSave = (Button) getView().findViewById(R.id.btnSave);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        Utils.setupOutSideTouchHideKeyboard(getView().findViewById(R.id.activity_change_password));
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case ChangePassword:
                ToastHelper.getInstance().showMessage(getString(R.string.str_password_changed_success));
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
        homeActivity.popBackFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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

            if (loginUser == Constants.CLIENT_LOGIN) {
                homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            } else if (loginUser == Constants.VET_LOGIN) {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSave:

                Utils.buttonClickEffect(view);

                String oldPass = edtOldPwd.getText().toString().trim();
                String newPass = edtNewPwd.getText().toString().trim();
                String confirmPass = edtConfirmPwd.getText().toString().trim();


                if (edtOldPwd.getText().toString().isEmpty()) {
                    edtOldPwd.requestFocus();
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_old_password));
                    return;
                }

                if (loginUser == Constants.CLIENT_LOGIN) {
                    if (!oldPass.equals(ClientLoginModel.getClientCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("Old password is wrong");
                        edtOldPwd.requestFocus();
                        return;
                    }
                } else if (loginUser == Constants.VET_LOGIN) {
                    if (!oldPass.equals(VetLoginModel.getVetCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("Old password is wrong");
                        edtOldPwd.requestFocus();
                        return;
                    }
                } else if (loginUser == Constants.CLINIC_LOGIN) {
                    if (!oldPass.equals(PractiseLoginModel.getPractiseCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("Old password is wrong");
                        edtOldPwd.requestFocus();
                        return;
                    }
                }
                if (newPass.isEmpty()) {
                    edtNewPwd.requestFocus();
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_password));
                    return;
                }

                if (confirmPass.isEmpty()) {
                    edtConfirmPwd.requestFocus();
                    ToastHelper.getInstance().showMessage(getString(R.string.str_enter_password));
                    return;
                }

                if (!confirmPass.equals(newPass)) {
                    edtConfirmPwd.requestFocus();
                    ToastHelper.getInstance().showMessage("password and confirm password not matched");
                    return;
                }

                if (loginUser == Constants.CLIENT_LOGIN) {
                    if (newPass.equals(ClientLoginModel.getClientCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("New password should not be old password");
                        edtConfirmPwd.requestFocus();
                        return;
                    }
                } else if (loginUser == Constants.VET_LOGIN) {
                    if (newPass.equals(VetLoginModel.getVetCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("New password should not be old password");
                        edtConfirmPwd.requestFocus();
                        return;
                    }
                } else if (loginUser == Constants.CLINIC_LOGIN) {
                    if (newPass.equals(PractiseLoginModel.getPractiseCredentials().getPassword())) {
                        ToastHelper.getInstance().showMessage("New password should not be old password");
                        edtConfirmPwd.requestFocus();
                        return;
                    }
                }
                changePassword();
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }

    private void changePassword() {
        try {
            params = new JSONObject();
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("Password", edtNewPwd.getText().toString());

            //int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);

            if (loginUser == Constants.CLIENT_LOGIN) {

                params.put("op", "ClientChangePassword");
                params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_CHANGE_PASSWORD,
                        true, RequestCode.ChangePassword, this);

            } else if (loginUser == Constants.VET_LOGIN) {

                params.put("op", "VetChangePassword");
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_CHANGE_PASSWORD,
                        true, RequestCode.ChangePassword, this);

            } else if (loginUser == Constants.CLINIC_LOGIN) {

                params.put("op", "VetChangePassword");
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_CHANGE_PASSWORD,
                        true, RequestCode.ChangePassword, this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
