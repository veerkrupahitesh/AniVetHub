package com.veeritsolution.android.anivethub.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpSymptoms;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.sinch.PlaceCallActivity;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.PermissionClass;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jaymin on 12/31/2016.
 */

public class SymptomsActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private ListView lvSymptoms;
    private Toolbar toolbar;
    private TextView tvHeader;
    private EditText edtSend;

    // object and variable declaration
    private AdpSymptoms adapter;
    private List<PetSymptomsModel> list;
    private PetDetailsModel petDetailsModel;
    private Intent intent;
    private boolean isFromVideoCall;
    private VetLoginModel vetLoginModel;
    private JSONObject params;
    private ArrayList<String> callPermissionList;
    private PetSymptomsModel petSymptomsModel;
    private AppointmentModel appointmentModel;
    private View rootView;
    private int vetPractiseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_symptoms, null, false);
        rootView.setVisibility(View.INVISIBLE);

        setContentView(rootView);

        intent = getIntent();

        if (intent != null) {
            petDetailsModel = (PetDetailsModel) intent.getSerializableExtra(Constants.PET_DATA);
            isFromVideoCall = intent.getBooleanExtra(Constants.IS_FROM_VIDEO_CALL, false);
            vetLoginModel = (VetLoginModel) intent.getSerializableExtra(Constants.VET_DATA);
            appointmentModel = (AppointmentModel) intent.getSerializableExtra(Constants.APPOINTMENT_DATA);
            vetPractiseId = intent.getIntExtra(Constants.VET_PRACTICE_ID, 0);
            //generalSettingsModel = (GeneralSettingsModel) intent.getSerializableExtra(Constants.GENERAL_SETTING_DATA);
        }


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
        tvHeader.setText(getString(R.string.str_select_symptoms));
        lvSymptoms = (ListView) findViewById(R.id.lstsympotoms);
        edtSend = (EditText) findViewById(R.id.edt_symp);
        edtSend.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        Utils.setupOutSideTouchHideKeyboard(findViewById(R.id.activity_symptoms));

        params = new JSONObject();
        try {
            params.put("op", "GetPetSymptomsInfo");
            params.put("PetTypeId", petDetailsModel.getPetTypeId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_PET_SYMPTOMS_INFO,
                    true, RequestCode.GetPetSymptomsInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        callPermissionList = new ArrayList<>();
        callPermissionList.add(Manifest.permission.INTERNET);
        callPermissionList.add(Manifest.permission.RECORD_AUDIO);
        callPermissionList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        callPermissionList.add(Manifest.permission.READ_PHONE_STATE);
        callPermissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        callPermissionList.add(Manifest.permission.CAMERA);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetPetSymptomsInfo:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = new ArrayList();

                        list = (List<PetSymptomsModel>) mObject;
                        // mcontext = SymptomsActivity.this;
                        adapter = new AdpSymptoms(this, list);
                        lvSymptoms.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
          /*  case GetGeneralSettings:

                generalSettingsModel = (GeneralSettingsModel) mObject;

                break;*/
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
        //finish();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

           /* case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                finish();
                break;*/

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                finish();
                break;

            case R.id.lin_symptoms:
                Utils.buttonClickEffect(view);
                petSymptomsModel = (PetSymptomsModel) view.getTag();

                if (isFromVideoCall) {
                    if (PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, callPermissionList)) {

                        openPlaceCallActivity();
                        // loginClicked();
                    }
                } else {
                    intent = new Intent(this, AppointmentsSelectionActivity.class);
                    intent.putExtra(Constants.VET_DATA, vetLoginModel);
                    intent.putExtra(Constants.PET_DATA, petDetailsModel);
                    intent.putExtra(Constants.PET_SYMPTOMS_DATA, petSymptomsModel);
                    intent.putExtra(Constants.VET_PRACTICE_ID, vetPractiseId);

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            case R.id.img_send:

                Utils.buttonClickEffect(view);
                if (edtSend.getText().toString().isEmpty()) {
                    ToastHelper.getInstance().showMessage("Please enter symptoms");
                    return;
                }

                petSymptomsModel = new PetSymptomsModel();
                petSymptomsModel.setSymptomsName(edtSend.getText().toString().trim());

                if (isFromVideoCall) {
                    if (PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, callPermissionList)) {
                        openPlaceCallActivity();
                        //  loginClicked();

                    }
                } else {
                    intent = new Intent(this, AppointmentsSelectionActivity.class);
                    intent.putExtra(Constants.VET_DATA, vetLoginModel);
                    intent.putExtra(Constants.PET_DATA, petDetailsModel);
                    intent.putExtra(Constants.PET_SYMPTOMS_DATA, petSymptomsModel);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {

                for (int i = 0; i < grantResults.length; i++) {
                    //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                    shouldPermit.add(permissions[i]);
                }
            }
        }
    }


    @Override
    protected void onPause() {

        CustomDialog.getInstance().dismiss();

        super.onPause();
    }

    private void openPlaceCallActivity() {

        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        mainActivity.putExtra(Constants.VET_DATA, vetLoginModel);
        mainActivity.putExtra(Constants.PET_DATA, petDetailsModel);
        mainActivity.putExtra(Constants.PET_SYMPTOMS_DATA, petSymptomsModel);
        mainActivity.putExtra(Constants.APPOINTMENT_DATA, appointmentModel);
        mainActivity.putExtra(Constants.VET_PRACTICE_ID, vetPractiseId);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
