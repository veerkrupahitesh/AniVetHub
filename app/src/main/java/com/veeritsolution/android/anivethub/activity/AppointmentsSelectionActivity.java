package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.veeritsolution.android.anivethub.enums.CalenderDateSelection;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetTimeSlotModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsSelectionActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName, tvAppointmentDate;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private Spinner spTimeSlot;

    // object and variable declaration
    private JSONObject params;
    private Intent intent;
    private PetDetailsModel petDetailsModel;
    private VetLoginModel vetLoginModel;
    private PetSymptomsModel petSymptomsModel;
    private SpinnerAdapter adpTimeLot;
    private List<String> vetTimeSlotList;
    private ArrayList<VetTimeSlotModel> timeSlotList;
    private int timeSlotId = 0;
    private View rootView;
    private int vetPractiseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_appoitments_selelection, null, false);
        rootView.setVisibility(View.INVISIBLE);

        setContentView(rootView);

        intent = getIntent();

        if (intent != null) {
            petDetailsModel = (PetDetailsModel) intent.getSerializableExtra(Constants.PET_DATA);
            vetLoginModel = (VetLoginModel) intent.getSerializableExtra(Constants.VET_DATA);
            petSymptomsModel = (PetSymptomsModel) intent.getSerializableExtra(Constants.PET_SYMPTOMS_DATA);
            vetPractiseId = intent.getIntExtra(Constants.VET_PRACTICE_ID, 0);
        }
        init();

        getVetTimeSlotData();

    }


    private void init() {

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
        tvHeader.setText(getString(R.string.str_place_appointment));

        //backButton = (ImageView) getView().findViewById(R.id.img_back_header);

        tvName = (TextView) findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvName.setText(vetLoginModel.getVetName());

        tvUserName = (TextView) findViewById(R.id.tv_vetUserName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvUserName.setText(vetLoginModel.getUserName());

        imgVetProfilePhoto = (ImageView) findViewById(R.id.img_vetProfilePic);

        Utils.setProfileImage(this, vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePhoto);

        imgVetBannerPhoto = (ImageView) findViewById(R.id.img_vet_bannerPic);
        Utils.setBannerImage(this, vetLoginModel.getBannerPic(), R.drawable.img_vet_banner, imgVetBannerPhoto);

        imgSelectBannerPhoto = (ImageView) findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        spTimeSlot = (Spinner) findViewById(R.id.sp_vetTimeSlot);
        tvAppointmentDate = (TextView) findViewById(R.id.tv_timeSlotDate);
        tvAppointmentDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        vetTimeSlotList = new ArrayList<>();

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetTimeSlotInfoAll:

                rootView.setVisibility(View.VISIBLE);

                setSpinnerData(mObject);

                break;
            case VetAppointmentInsert:
                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.has("DataId")) {

                        ToastHelper.getInstance().showMessage("Your Appointment successfully Placed");
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        ToastHelper.getInstance().showMessage("Error in placing Appointment\nPlease try again later");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
        // finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_timeSlotDate:

                CustomDialog.getInstance().showDatePickerDialog(this, tvAppointmentDate,
                        CalenderDateSelection.CALENDER_WITH_FUTURE_DATE, 2050, 1, 1);
                break;

            case R.id.btn_save:

                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    saveData();
                }
                break;
        }
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
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getVetTimeSlotData() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_VET_TIME_SLOT_INFO_ALL);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_VET_TIME_SLOT_INFO_ALL,
                    true, RequestCode.GetVetTimeSlotInfoAll, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerData(Object mObject) {

        if (mObject instanceof ErrorModel) {
            vetTimeSlotList.add("No time slot found");
        } else {
            timeSlotList = (ArrayList<VetTimeSlotModel>) mObject;
            if (!timeSlotList.isEmpty()) {

                VetTimeSlotModel timeSlotModel = new VetTimeSlotModel();
                timeSlotModel.setTimeSlotName("Select time slot");
                timeSlotList.add(0, timeSlotModel);
                for (int i = 0; i < timeSlotList.size(); i++) {

                    vetTimeSlotList.add(timeSlotList.get(i).getTimeSlotName());
                }
            }
        }

        adpTimeLot = new SpinnerAdapter(this, R.layout.spinner_row_list, vetTimeSlotList);
        spTimeSlot.setAdapter(adpTimeLot);
        spTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    timeSlotId = timeSlotList.get(position).getVetTimeSlotId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveData() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_APPOINTMENT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());
            params.put("VetPractiseId", vetPractiseId);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("ClientPetId", petDetailsModel.getClientPetId());
            params.put("SymptomsDescription", petSymptomsModel.getSymptomsName());
            params.put("VetTimeSlotId", timeSlotId);
            params.put("AppointmentDate", tvAppointmentDate.getText().toString().trim());

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_APPOINTMENT_INSERT,
                    true, RequestCode.VetAppointmentInsert, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        if (tvAppointmentDate.getText().toString().isEmpty()) {
            ToastHelper.getInstance().showMessage(getString(R.string.str_select_date_of_appointment));
            return false;
        } else if (timeSlotId == 0) {
            ToastHelper.getInstance().showMessage("Select appointment time slot");
            return false;
        } else {
            return true;
        }
    }
}
