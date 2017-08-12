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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.adapter.SpinnerTimeSlotAdapter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.veeritsolution.android.anivethub.R.id.sp_vetTimeSlot;

/**
 * Created by ${hitesh} on 2/16/2017.
 */

public class VetTimeSlotInfoFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Button btnSave;
    private TextView tvHeader, tvName, tvUserName;
    private Toolbar toolbar;
    private ImageView imgVetProfilePhoto, imgVetBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private Spinner spVetTimeSlot;
    private Spinner spDay;

    private HomeActivity homeActivity;
    private JSONObject params;
    private List<TimeSlotModel> timeSlotModelList;
    private TimeSlotModel timeSlotModel;
    private View rootView;
    private ArrayList<String> spDayList;
    private int dayNo;
    private SpinnerAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_timeslot_info, container, false);

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
        tvHeader.setText(getString(R.string.str_add_time_slot));

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

        spVetTimeSlot = (Spinner) rootView.findViewById(sp_vetTimeSlot);

        spDay = (Spinner) rootView.findViewById(R.id.sp_day);

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
                dayNo = pos;

                /*setEnableCheckbox();
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
                }*/
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

        getTimeSlotData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetTimeSlotInfo:

                rootView.setVisibility(View.VISIBLE);
                setSpinnerData(mObject);
                break;

            case VetTimeSlotInsert:

                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    if (jsonArray.length() != 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        if (jsonObject.has(Constants.DATA_ID)) {
                            homeActivity.popBackFragment();

                        } else {

                            ToastHelper.getInstance().showMessage(getString(R.string.str_server_error));
                        }
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.str_server_error));
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
        // homeActivity.popBackFragment();
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_save:
                Utils.buttonClickEffect(view);
                if (spVetTimeSlot.getSelectedItemPosition() == 0) {
                    spVetTimeSlot.requestFocus();
                    ToastHelper.getInstance().showMessage("please select time slot");
                    return;
                } else {
                    insetTimeSlot();
                }
                break;


        }
    }

    private void getTimeSlotData() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_TIME_SLOT_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_TIME_SLOT_INFO,
                    true, RequestCode.GetTimeSlotInfo, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSpinnerData(Object mObject) {

        timeSlotModelList = (List<TimeSlotModel>) mObject;

        if (!timeSlotModelList.isEmpty()) {

            timeSlotModel = new TimeSlotModel();
            timeSlotModel.setTimeSlotName("Select time slot");
            timeSlotModelList.add(0, timeSlotModel);

            spVetTimeSlot.setAdapter(new SpinnerTimeSlotAdapter(getActivity(), R.layout.spinner_row_list, timeSlotModelList));
        }

        spVetTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                timeSlotModel = (TimeSlotModel) view.getTag();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insetTimeSlot() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_TIME_SLOT_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("TimeSlotId", timeSlotModel.getTimeSlotId());

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.VET_TIME_SLOT_INSERT,
                    true, RequestCode.VetTimeSlotInsert, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
