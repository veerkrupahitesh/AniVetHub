package com.veeritsolution.android.anivethub.fragment.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.ClientPetsActivity;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpClientAppointments;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.AppointmentDetailFragment;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hitesh} on 2/10/2017.
 */

public class ClientAppFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // xml components
    private TextView tvHeader, tvNoAppointment;
    private Toolbar toolbar;
    private ListView lvAppointments;
    //  private View footerView;
    private View rootView;
    private CoordinatorLayout snackBarView;

    private HomeActivity homeActivity;
    private JSONObject params;
    private AdpClientAppointments adpClientAppointment;
    private List<AppointmentModel> appointmentList;
    private AppointmentModel appointmentModel;
    private Bundle bundle;
    private List<AppointmentRejectModel> rejectModelList;
    private int totalRecords = 0;
    // private int page = 2;
    private int totalPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_appointment, container, false);

        //rootView.setVisibility(View.INVISIBLE);
        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        homeActivity.setSupportActionBar(toolbar);
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
        tvHeader.setText(getString(R.string.str_appointments_summary));

        // footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_item, null, false);

        lvAppointments = (ListView) rootView.findViewById(R.id.lv_clientAppointment);
        //  lvAppointments.addFooterView(footerView, null, false);

        appointmentList = new ArrayList<>();
        adpClientAppointment = new AdpClientAppointments(appointmentList, homeActivity);
        lvAppointments.setAdapter(adpClientAppointment);

        tvNoAppointment = (TextView) rootView.findViewById(R.id.tv_noAppointmentInfo);
        tvNoAppointment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lvAppointments.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                if (totalItemCount <= totalRecords && page <= totalPage) {

                    getAppointmentInfo(page, false);
                    Utils.showSnackBar(homeActivity, snackBarView);
                    //  page++;
                    return true;

                } else if (totalItemCount > totalRecords) {

                    Utils.dismissSnackBar();
                    return false;
                } else {
                    return false;
                }
            }
        });

        getAppointmentInfo(1, true);

        return rootView;
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientAppointmentInfo:

                Utils.dismissSnackBar();

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                        if (appointmentList.isEmpty()) {
                            lvAppointments.setVisibility(View.GONE);
                            tvNoAppointment.setVisibility(View.VISIBLE);
                        }

                    } else {

                        List<AppointmentModel> list = (List<AppointmentModel>) mObject;

                        if (!list.isEmpty()) {
                            appointmentList.addAll(list);
                            if (!appointmentList.isEmpty()) {

                                totalRecords = list.get(0).getTotalRowNo();
                                totalPage = list.get(0).getTotalPage();
                                adpClientAppointment.notifyDataSetChanged();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetRejectReasonInfo:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    rejectModelList = (List<AppointmentRejectModel>) mObject;
                    if (!rejectModelList.isEmpty()) {
                        CustomDialog.getInstance().showVideoCallRejectDialog(getActivity(), true, rejectModelList);
                    }
                }
                break;

            case ClientAppointmentCancel:
                appointmentList = new ArrayList<>();
                adpClientAppointment = new AdpClientAppointments(appointmentList, homeActivity);
                lvAppointments.setAdapter(adpClientAppointment);
                getAppointmentInfo(1, true);
                // ToastHelper.getInstance().showMessage("You have canceled your appointment of video call");
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetClientAppointmentInfo:

                Utils.dismissSnackBar();
                ToastHelper.getInstance().showMessage(mError);
                break;

            case GetRejectReasonInfo:

                Utils.dismissSnackBar();
                ToastHelper.getInstance().showMessage(mError);
                break;

            case ClientAppointmentCancel:

                Utils.dismissSnackBar();
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_cancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

          /*  case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;*/

            case R.id.lin_clientAppointment:

                Utils.buttonClickEffect(view);
                appointmentModel = (AppointmentModel) view.getTag();

                appointmentModel = (AppointmentModel) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.APPOINTMENT_DATA, appointmentModel);
                homeActivity.pushFragment(new AppointmentDetailFragment(), true, true, bundle);
                break;

            case R.id.tv_appointmentStatus:
                Utils.buttonClickEffect(view);
                appointmentModel = (AppointmentModel) view.getTag();

                if (appointmentModel.getAppointmentStatus() == Constants.READY_TO_CALL) {
                    Utils.buttonClickEffect(view);
                    Intent intent = new Intent(homeActivity, ClientPetsActivity.class);

                    VetLoginModel vetLoginModel = new VetLoginModel();
                    // vetLoginModel.setUserName();
                    vetLoginModel.setProfilePic(appointmentModel.getVetProfilePic());
                    vetLoginModel.setVetId(appointmentModel.getVetId());

                    intent.putExtra(Constants.VET_DATA, vetLoginModel);
                    intent.putExtra(Constants.APPOINTMENT_DATA, appointmentModel);
                    intent.putExtra(Constants.IS_FROM_VIDEO_CALL, true);
                    // intent.putExtra(Constants.GENERAL_SETTING_DATA, generalSettingsModel);
                    startActivity(intent);
                    homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            case R.id.img_requestCancel:
                Utils.buttonClickEffect(view);
                appointmentModel = (AppointmentModel) view.getTag();
                getRejectReasonInfo();
                break;

            case R.id.btn_submit_rejection:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                AppointmentRejectModel appointmentRejectModel = (AppointmentRejectModel) view.getTag();

                if (appointmentRejectModel != null) {
                    cancelAppointment(appointmentRejectModel);
                }
                break;

            case R.id.img_close:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }

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
            homeActivity.removeFragmentUntil(ClientHomeFragment.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelAppointment(AppointmentRejectModel appointmentRejectModel) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_APPOINTMENT_CANCEL);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetAppointmentId", appointmentModel.getVetAppointmentId());
            params.put("ClientCancelReasonId", appointmentRejectModel.getRejectReasonId());
            params.put("ClientCancelReasonRemarks", appointmentRejectModel.getRejectionRemarks());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_APPOINTMENT_CANCEL,
                    true, RequestCode.ClientAppointmentCancel, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRejectReasonInfo() {
        try {
            params = new JSONObject();
            params.put("op", "GetRejectReasonInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_REJECT_REASON_INFO,
                    true, RequestCode.GetRejectReasonInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAppointmentInfo(int pageNo, boolean isDialogRequired) {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_CLIENT_APPOINTMENT_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("PageNumber", pageNo);
            params.put("Records", 10);

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CLIENT_APPOINTMENT_INFO,
                    isDialogRequired, RequestCode.GetClientAppointmentInfo, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
