package com.veeritsolution.android.anivethub.fragment.client;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.SpinnerSymptomsAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.TreatmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by admin on 3/2/2017.
 */

public class ClientPetTreatInfoFragment extends Fragment implements DataObserver, OnClickEvent, OnBackPressedEvent {

    // xml components
    private Button btnSave;
    private View rootView;
    private Toolbar toolbar;
    private TextView tvHeader, txvFromTime, txvToTime;
    private EditText edtTreatment;
    private Spinner spDiseases;

    // object and variable declaration
    private JSONObject params;
    private Bundle bundle;
    private TreatmentModel petTreatmentModel;
    private HomeActivity homeActivity;
    private ArrayList<PetSymptomsModel> petSymptomsModelList;
    private PetSymptomsModel petSymptomsModel;
    private PetDetailsModel petDetailsModel;
    private DatePickerDialog dialog;
    private int val;
    private Calendar mCurrentDate = Calendar.getInstance();
    private int mYear = mCurrentDate.get(Calendar.YEAR);
    private int mMonth = mCurrentDate.get(Calendar.MONTH);
    private int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);
    Calendar calFromTime, calToTime;
    private long fromTime, toTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();

        if (bundle != null) {
            petTreatmentModel = (TreatmentModel) bundle.getSerializable(Constants.PET_TREATMENT_DATA);

            val = bundle.getInt(Constants.FLAG);
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
        }
        // 0  means edit and 1 means add
        if (val == 0) {
            getClientTreatmentInfo();

        } else {
            getSymptoms();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pet_treatment, container, false);
        return rootView;
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
        tvHeader.setText(getString(R.string.str_treatment_detail));
        txvFromTime = (TextView) getView().findViewById(R.id.tv_frmtime);
        edtTreatment = (EditText) getView().findViewById(R.id.edt_treatment);
        edtTreatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        txvFromTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        txvToTime = (TextView) getView().findViewById(R.id.tv_totime);
        txvToTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        spDiseases = (Spinner) getView().findViewById(R.id.sp_disease);
        btnSave = (Button) getView().findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case ClientPetTreatmentInfo:

                rootView.setVisibility(View.VISIBLE);

                if (val == 0) {

                    // edtDescription.setText(petTreatmentModel.);
                    txvFromTime.setText(petTreatmentModel.getFromDate());
                    txvToTime.setText(petTreatmentModel.getToDate());
                    edtTreatment.setText(petTreatmentModel.getTreatment());

                    getSymptoms();
                }

                break;

            case ClientPetTreatmentUpdate:

                //  Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_LONG).show();
                homeActivity.popBackFragment();
                break;

            case GetPetSymptomsInfo:
                //  try {
                petSymptomsModelList = (ArrayList<PetSymptomsModel>) mObject;
                PetSymptomsModel newPetSymptomsModel = new PetSymptomsModel();
                newPetSymptomsModel.setSymptomsName("Select Symptoms");

                petSymptomsModelList.add(0, newPetSymptomsModel);
                SpinnerSymptomsAdapter spinner = new SpinnerSymptomsAdapter(getActivity(), R.layout.spinner_row_list, petSymptomsModelList);
                spDiseases.setAdapter(spinner);

                if (val == 0) {

                    int pos;
                    for (int i = 0; i < petSymptomsModelList.size(); i++) {

                        if (petTreatmentModel.getSymptomsName().equalsIgnoreCase(petSymptomsModelList.get(i).getSymptomsName())) {
                            pos = i;
                            spDiseases.setSelection(pos);
                            break;
                        }
                    }

                }
                spDiseases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                        //petSymptomsModel = (PetSymptomsModel) adapterView.getTag();

                        petSymptomsModel = petSymptomsModelList.get(pos);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                break;

            // } catch (Exception e) {
            //  }
            case ClientPetTreatmentInsert:

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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_frmtime:

                dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {
                        String format = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedmonth + 1, selectedday, selectedyear);
                        calFromTime = Calendar.getInstance();
                        calFromTime.set(selectedyear, selectedmonth,
                                selectedday);
                        fromTime = calFromTime.getTimeInMillis();
                        txvFromTime.setText("");
                        txvFromTime.setText(format);
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            txvFromTime.setText("");
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.tv_totime:
                dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedyear, int selectedmonth, int selectedday) {
                        String format = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedmonth + 1, selectedday, selectedyear);
                        calToTime = Calendar.getInstance();
                        calToTime.set(selectedyear, selectedmonth,
                                selectedday);
                        toTime = calToTime.getTimeInMillis();
                        txvToTime.setText("");
                        if (toTime > fromTime) {

                            txvToTime.setText(format);
                        } else {
                            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_valid_pet_treatment_to_time));
                        }
                    }
                }, mYear, mMonth, mDay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            txvToTime.setText("");
                        }
                    }
                });
                dialog.show();
                break;


            case R.id.btn_save:

                validation();
                break;


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // if (!fromSignUp) {
        inflater.inflate(R.menu.other_menu, menu);
        // }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            // if (!fromSignUp) {
            homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            //  }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getClientTreatmentInfo() {

        params = new JSONObject();
        try {
            params.put("op", "GetClientPetTreatmentInfo");
            params.put("ClientPetTreatmentId", petTreatmentModel.getClientPetTreatmentId());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_INFO,
                    true, RequestCode.ClientPetTreatmentInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClientPetTreatmentUpdate() {

        params = new JSONObject();
        try {
            params.put("op", "ClientPetTreatmentUpdate");
            params.put("ClientPetTreatmentId", petTreatmentModel.getClientPetTreatmentId());
            params.put("SymptomsId", petSymptomsModel.getSymptomsId());
            params.put("Treatment", edtTreatment.getText().toString());
            params.put("FromDate", txvFromTime.getText().toString());
            params.put("ToDate", txvToTime.getText().toString());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_UPDATE,
                    true, RequestCode.ClientPetTreatmentUpdate, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClientPetTreatmentInsert() {

        params = new JSONObject();
        try {
            params.put("op", "ClientPetTreatmentInsert");
            params.put("ClientPetId", petDetailsModel.getClientPetId());
            params.put("SymptomsId", petSymptomsModel.getSymptomsId());
            params.put("Treatment", edtTreatment.getText().toString());
            params.put("FromDate", txvFromTime.getText().toString());
            params.put("ToDate", txvToTime.getText().toString());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_INSERT,
                    true, RequestCode.ClientPetTreatmentInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSymptoms() {

        try {
            params = new JSONObject();
            params.put("op", "GetPetSymptomsInfo");
            params.put("PetTypeId", petDetailsModel.getPetTypeId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PET_SYMPTOMS_INFO,
                    true, RequestCode.GetPetSymptomsInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validation() {

        petSymptomsModel = (PetSymptomsModel) spDiseases.getSelectedItem();

        if (petSymptomsModel.getSymptomsName().equals("") || petSymptomsModel.getSymptomsName().equals("Select Symptoms")) {
            spDiseases.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_pet_symptomps));

        } else if (txvFromTime.getText().toString().equalsIgnoreCase("")) {
            txvFromTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_pet_treatment_From_time));

        } else if (txvToTime.getText().toString().equalsIgnoreCase("")) {
            txvToTime.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_pet_treatment_to_time));

        } else if (edtTreatment.getText().toString().equalsIgnoreCase("")) {
            edtTreatment.requestFocus();
            ToastHelper.getInstance().showMessage(getString(R.string.str_enter_pet_treatment));

        } else {
            if (val == 0) {
                ClientPetTreatmentUpdate();
            } else {
                ClientPetTreatmentInsert();
            }
        }
    }

}
