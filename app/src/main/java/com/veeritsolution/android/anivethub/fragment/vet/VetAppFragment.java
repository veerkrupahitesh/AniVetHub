package com.veeritsolution.android.anivethub.fragment.vet;

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
import com.veeritsolution.android.anivethub.adapter.AdpVetAppointment;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.AppointmentDetailFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jaymin on 2/9/2017.
 */

public class VetAppFragment extends Fragment implements OnClickEvent, DataObserver, OnBackPressedEvent {

    // xml components
    private Toolbar toolbar;
    private TextView tvHeader, tvNoAppointment;
    private ListView lvAppointments;
    private View rootView;
    private CoordinatorLayout snackBarView;

    // object and variable declaration
    private AdpVetAppointment adapter;
    private List<AppointmentModel> appointmentList;
    private List<TimeSlotModel> listTimeSlot;
    private List<AppointmentRejectModel> rejectModelList;
    private JSONObject params;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private AppointmentModel appointmentModel;
    private int totalRecords = 0;
    private int totalPage;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_appointment, container, false);

        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);
        // rootView.setVisibility(View.INVISIBLE);

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
        tvHeader.setText(getString(R.string.str_appointments_summary));
        //footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_item, null, false);

        lvAppointments = (ListView) rootView.findViewById(R.id.lstappointment);
        // lvAppointments.addFooterView(footerView);

        appointmentList = new ArrayList<>();
        adapter = new AdpVetAppointment(getActivity(), appointmentList);
        lvAppointments.setAdapter(adapter);
        lvAppointments.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                if (totalItemCount <= totalRecords && page <= totalPage) {

                    getVetAppointmentInfoAll(page, false);
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
        tvNoAppointment = (TextView) rootView.findViewById(R.id.tv_noAppointmentInfo);
        tvNoAppointment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        /*lvAppointments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

                if (loadMore) {
                    if (totalItemCount <= totalRecords&& page <= totalPage) {

                        GetVetAppointmentInfoAll(page, false);

                        Utils.showSnackBar(homeActivity, snackBarView);

                        page++;

                    } else if (totalItemCount > totalRecords) {

                        Utils.dismissSnackBar();

                    }

                }

            }
        });*/

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getVetAppointmentInfoAll(1, true);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetAppointmentInfoAll:

                rootView.setVisibility(View.VISIBLE);
                Utils.dismissSnackBar();

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
                            totalRecords = list.get(0).getTotalRowNo();
                            totalPage = list.get(0).getTotalPage();
                            appointmentList.addAll(list);

                            if (adapter == null) {
                                adapter = new AdpVetAppointment(getActivity(), appointmentList);
                                lvAppointments.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetTimeSubslotInfo:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    listTimeSlot = (List<TimeSlotModel>) mObject;
                    if (!listTimeSlot.isEmpty()) {
                        CustomDialog.getInstance().showTimeSubSlot(getActivity(), true, listTimeSlot);
                    }
                }
                break;

            case GetRejectReasonInfo:

                if (mObject instanceof ErrorModel) {
                    ToastHelper.getInstance().showMessage(((ErrorModel) mObject).getError());
                } else {
                    rejectModelList = (List<AppointmentRejectModel>) mObject;
                    if (!rejectModelList.isEmpty()) {
                        CustomDialog.getInstance().showRejectAppointment(getActivity(), true, rejectModelList, appointmentModel);
                    }
                }
                break;

            case VetAppointmentReject:
                appointmentList = new ArrayList<>();
                getVetAppointmentInfoAll(1, true);
                break;

            case VetAppointmentApprove:
                appointmentList = new ArrayList<>();
                getVetAppointmentInfoAll(1, true);
                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {
            case GetVetAppointmentInfoAll:
                Utils.dismissSnackBar();
                ToastHelper.getInstance().showMessage(mError);
                break;
            case GetTimeSubslotInfo:
                ToastHelper.getInstance().showMessage(mError);
                break;
            case GetRejectReasonInfo:
                ToastHelper.getInstance().showMessage(mError);
                break;
            case VetAppointmentReject:
                ToastHelper.getInstance().showMessage(mError);
                break;
            case VetAppointmentApprove:
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

            case R.id.lin_appointment:

                Utils.buttonClickEffect(view);
                appointmentModel = (AppointmentModel) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.APPOINTMENT_DATA, appointmentModel);
                homeActivity.pushFragment(new AppointmentDetailFragment(), true, true, bundle);
                break;

            case R.id.btn_statusOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                int pos = (int) view.getTag();
                if (pos == 1) {
                    getTimeSubSlotInfo();
                } else {
                    getRejectReasonInfo();
                }
                break;

            case R.id.btn_cancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.tv_appointmentStatus:

                appointmentModel = (AppointmentModel) view.getTag();
                if (appointmentModel.getAppointmentStatus() == Constants.APPOINTMENT_APPROVE_STATUS) {
                    Utils.buttonClickEffect(view);
                    CustomDialog.getInstance().showAppointmentStatus(homeActivity, false);
                }
                break;

            case R.id.btn_timeSlotOk:

                Utils.buttonClickEffect(view);
                Object object = view.getTag();
                if (object != null) {
                    int subSlotId = (int) object;
                    CustomDialog.getInstance().dismiss();
                    vetAppointmentApprove(subSlotId);
                }
                break;

            case R.id.btn_submit_rejection:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                object = view.getTag();

                if (object != null) {
                    AppointmentRejectModel appointmentRejectModel = (AppointmentRejectModel) object;
                    vetAppointmentReject(appointmentRejectModel);
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
        //  if (!fromSignUp) {
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
            if (loginUser == Constants.VET_LOGIN) {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
            } else {
                homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getVetAppointmentInfoAll(int pageNo, boolean isDialogRequired) {
        //  int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
        try {
            if (loginUser == Constants.VET_LOGIN) {
                params = new JSONObject();
                params.put("op", "GetVetAppointmentInfo_Vet");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                params.put("PageNumber", pageNo);
                params.put("Records", 10);

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_APPOINTMENT_INFO_VET,
                        isDialogRequired, RequestCode.GetVetAppointmentInfoAll, this);
            } else {
                params = new JSONObject();
                params.put("op", "GetVetAppointmentInfo_VetPractise");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetPractiseId", PractiseLoginModel.getPractiseCredentials().getVetId());
                params.put("PageNumber", pageNo);
                params.put("Records", 10);

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_APPOINTMENT_INFO_VET_PRACTISE,
                        isDialogRequired, RequestCode.GetVetAppointmentInfoAll, this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTimeSubSlotInfo() {
        try {
            params = new JSONObject();
            params.put("op", "getTimeSubSlotInfo");
            params.put("TimeSlotId", appointmentModel.getTimeSlotId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_TIME_SUBSLOT_INFO,
                    true, RequestCode.GetTimeSubslotInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vetAppointmentApprove(int subSlotId) {
        try {
            params = new JSONObject();
            params.put("op", "VetAppointment_Approve");
            params.put("VetAppointmentId", appointmentModel.getVetAppointmentId());
            params.put("VetTimeSubSlotId", subSlotId);
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_APPOINTMENT_APPROVE,
                    true, RequestCode.VetAppointmentApprove, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vetAppointmentReject(AppointmentRejectModel appointmentRejectModel) {
        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_APPOINTMENT_REJECT);
            params.put("VetAppointmentId", appointmentModel.getVetAppointmentId());
            params.put("RejectReasonId", appointmentRejectModel.getRejectReasonId());
            params.put("RejectReasonRemarks", appointmentRejectModel.getRejectReasonName());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_APPOINTMENT_REJECT,
                    true, RequestCode.VetAppointmentReject, this);

        } catch (Exception e) {
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
