package com.veeritsolution.android.anivethub.fragment.client;

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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpSupportCenter;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.models.SupportCenterModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veerk on 3/15/2017.
 */

public class ClientSupportCenterFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    //xml components
    private Button btnCreateTicket;
    private View rootView;
    private ListView lvSupportCenter;
    private Toolbar toolbar;
    private TextView tvHeader;

    // object and variable declaration
    private JSONObject params;
    private HomeActivity homeActivity;
    private ArrayList<SupportCenterModel> supportCenterList;
    private AdpSupportCenter adpSupportCenter;
    private Bundle bundle;
    private SessionModel sessionModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        bundle = getArguments();

        if (bundle != null) {
            sessionModel = (SessionModel) bundle.getSerializable(Constants.SESSION_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_client_support_center, container, false);
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
        tvHeader.setText(R.string.str_support_center);

        lvSupportCenter = (ListView) rootView.findViewById(R.id.lv_supportCenter);

        btnCreateTicket = (Button) rootView.findViewById(R.id.btn_createTicket);
        btnCreateTicket.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSupportCenterInfo();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case SupportCenter:

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        List<SessionModel> list = (List<SessionModel>) mObject;

                        if (!list.isEmpty()) {

                            supportCenterList = (ArrayList<SupportCenterModel>) mObject;

                            if (!supportCenterList.isEmpty()) {

                                adpSupportCenter = new AdpSupportCenter(homeActivity, supportCenterList);
                                lvSupportCenter.setAdapter(adpSupportCenter);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case VetSessionTicketInsert:
                getSupportCenterInfo();
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

           /* case R.id.img_back_header:

                homeActivity.popBackFragment();
                break;*/
            case R.id.btn_createTicket:

                CustomDialog.getInstance().showTicketDialog(homeActivity, false, "Ticket Reason");

                break;

            case R.id.img_cancel:

                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_submit_ticket:


                Object object = view.getTag();

                if (object != null) {
                    CustomDialog.getInstance().dismiss();
                    insertTicket(object);
                } else {
                    ToastHelper.getInstance().showMessage("Please enter your message");
                }
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

    private void getSupportCenterInfo() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.CLIENT_TICKET_INFO);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionModel.getVetSessionId());

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CLIENT_TICKET_INFO,
                    true, RequestCode.SupportCenter, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTicket(Object object) {

        String remarks = (String) object;

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_SESSION_TICKET_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionModel.getVetSessionId());
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("VetId", 0);
            params.put("LoginId", 0);
            params.put("Remarks", remarks);
            params.put("Flag", Constants.CLIENT_TICKET_FLAG);

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                    ApiList.VET_SESSION_TICKET_INSERT, true, RequestCode.VetSessionTicketInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
