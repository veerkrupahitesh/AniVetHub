package com.veeritsolution.android.anivethub.fragment.client;

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
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpNotifications;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.NotificationType;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.NotificationModel;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veerk on 3/22/2017.
 */

public class ClientNotiFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    //xml components
    private View rootView;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoNotification;
    private ListView lvNotifications;
    private CoordinatorLayout snackBarView;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private ArrayList<NotificationModel> notificationList;
    private AdpNotifications adpNotifications;
    private int totalRecords = 0;
    private int page = 2;
    private int totalPage;
    private NotificationModel notificationModel;
    private List<AppointmentRejectModel> rejectModelList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_client_notifications, container, false);
        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);

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
        tvHeader.setText(getString(R.string.str_notifications));

        lvNotifications = (ListView) rootView.findViewById(R.id.lv_notifications);

        notificationList = new ArrayList<>();
        adpNotifications = new AdpNotifications(homeActivity, notificationList);
        lvNotifications.setAdapter(adpNotifications);

        tvNoNotification = (TextView) rootView.findViewById(R.id.tv_noNotificationInfo);
        tvNoNotification.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        lvNotifications.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                if (totalItemCount <= totalRecords && page <= totalPage) {

                    getNotificationInfo(page, false);
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

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNotificationInfo(1, true);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetNotifications:
                Utils.dismissSnackBar();
                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        //  ToastHelper.getInstance().showMessage(errorModel.getError());
                        if (notificationList.isEmpty()){
                            tvNoNotification.setVisibility(View.VISIBLE);
                            lvNotifications.setVisibility(View.GONE);
                        }

                    } else {

                        List<NotificationModel> list = (List<NotificationModel>) mObject;
                        if (!list.isEmpty()) {
                            notificationList.addAll(list);
                            if (!notificationList.isEmpty()) {

                                adpNotifications.notifyDataSetChanged();
                                totalRecords = notificationList.get(0).getTotalRowNo();
                                totalPage = notificationList.get(0).getTotalPage();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientAppointmentConfirm:

                ToastHelper.getInstance().showMessage("Be ready to get video call from client!");
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

                ToastHelper.getInstance().showMessage("You have canceled your appointment of video call");
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        Utils.dismissSnackBar();
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_notification:
                notificationModel = (NotificationModel) view.getTag();
                setProperFragment(notificationModel);
                break;

            case R.id.btn_actionOk:
                readyForVideoCall();
                break;

            case R.id.btn_actionCancel:

                getRejectReasonInfo();
                break;

            case R.id.btn_submit_rejection:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                Object object = view.getTag();

                if (object != null) {
                    AppointmentRejectModel appointmentRejectModel = (AppointmentRejectModel) object;
                    ClientVideoCallReject(appointmentRejectModel);
                }
                break;

            case R.id.img_close:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }

    private void ClientVideoCallReject(AppointmentRejectModel appointmentRejectModel) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_APPOINTMENT_CANCEL);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetAppointmentId", notificationModel.getVetAppointmentId());
            params.put("VetCancelReasonId", appointmentRejectModel.getRejectReasonId());
            params.put("VetCancelReasonRemarks", appointmentRejectModel.getRejectionRemarks());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_APPOINTMENT_CANCEL,
                    true, RequestCode.ClientAppointmentCancel, this);

        } catch (JSONException e) {
            e.printStackTrace();
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

    private void getNotificationInfo(int pageNo, boolean isDialogRequired) {
        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_CLIENT_NOTIFICATIONS);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("PageNumber", pageNo);
            params.put("Records", 10);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_NOTIFICATIONS,
                    isDialogRequired, RequestCode.GetNotifications, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setProperFragment(NotificationModel notificationModel) {

        int notificationType = notificationModel.getNotificationType();

        if (notificationType == NotificationType.AppointmentRequest.getType()
                || notificationType == NotificationType.AppointmentApprove.getType()
                || notificationType == NotificationType.AppointmentReject.getType()
                || notificationType == NotificationType.AppointmentClientConfirm.getType()
                || notificationType == NotificationType.AppointmentVetConfirm.getType()
                || notificationType == NotificationType.AppointmentClientCancel.getType()
                || notificationType == NotificationType.AppointmentVetCancel.getType()
                || notificationType == NotificationType.AppointmentClientBoth.getType()
                || notificationType == NotificationType.AppointmentVetBoth.getType()) {

            homeActivity.pushFragment(new ClientAppFragment(), true, false, null);
        } /*else if (notificationType == NotificationType.AppointmentClientBoth.getType()
                || notificationType == NotificationType.AppointmentVetBoth.getType()) {
            CustomDialog.getInstance().showActionDialog(getActivity(), "Video call confirmation",
                    "Are you ready for video call?", true);
        }*/
    }

    private void readyForVideoCall() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_APPOINTMENT_CONFIRM);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetAppointmentId", notificationModel.getVetAppointmentId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_APPOINTMENT_CONFIRM,
                    true, RequestCode.ClientAppointmentConfirm, this);

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
}
