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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpVetTimeSlot;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.models.VetTimeSlotModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ${hitesh} on 2/15/2017.
 */

public class VetTimeSlotFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private Button btnAddTimeSlot;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoTimeSlot;
    private ListView lvTimeSlot;
    private View rootView;

    // object and variable declaration
    private JSONObject params;
    private List<VetTimeSlotModel> vetTimeSlotModelList;
    private VetTimeSlotModel vetTimeSlotModel;
    private AdpVetTimeSlot adpVetTimeSlot;
    private HomeActivity homeActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_time_slot, container, false);

        //  rootView.setVisibility(View.INVISIBLE);
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
        tvHeader.setText(getString(R.string.str_time_slot_information));

        tvNoTimeSlot = (TextView) getView().findViewById(R.id.tv_noTimeSlotFound);
        tvNoTimeSlot.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvNoTimeSlot.setVisibility(View.GONE);

        btnAddTimeSlot = (Button) getView().findViewById(R.id.btn_addNewTimeSlot);
        btnAddTimeSlot.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        lvTimeSlot = (ListView) getView().findViewById(R.id.lv_vetTimeSlot);

        getTimeSlotData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetVetTimeSlotInfoAll:

                rootView.setVisibility(View.VISIBLE);
                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        tvNoTimeSlot.setVisibility(View.VISIBLE);
                        lvTimeSlot.setVisibility(View.GONE);
                    } else {

                        vetTimeSlotModelList = (List<VetTimeSlotModel>) mObject;

                        if (!vetTimeSlotModelList.isEmpty()) {
                            tvNoTimeSlot.setVisibility(View.GONE);
                            lvTimeSlot.setVisibility(View.VISIBLE);
                            // if (adpPetList == null) {
                            adpVetTimeSlot = new AdpVetTimeSlot(getActivity(), vetTimeSlotModelList);
                            lvTimeSlot.setAdapter(adpVetTimeSlot);
                            // } else {
                            //     adpPetList.refreshList(list);
                            // }
                        } else {
                            tvNoTimeSlot.setVisibility(View.VISIBLE);
                            lvTimeSlot.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case VetTimeSlotDelete:

                try {
                    if (mObject instanceof ErrorModel) {
                        vetTimeSlotModelList.remove(vetTimeSlotModel);
                        adpVetTimeSlot.setList(vetTimeSlotModelList);
                        ErrorModel errorModel = (ErrorModel) mObject;
                        // ToastHelper.getInstance().showMessage(errorModel.getError());
                        if (vetTimeSlotModelList.isEmpty()) {
                            lvTimeSlot.setVisibility(View.GONE);
                            tvNoTimeSlot.setVisibility(View.VISIBLE);
                        } else {
                            lvTimeSlot.setVisibility(View.VISIBLE);
                            tvNoTimeSlot.setVisibility(View.GONE);
                        }

                    } else {
                        vetTimeSlotModelList.remove(vetTimeSlotModel);
                        adpVetTimeSlot.setList(vetTimeSlotModelList);

                        if (vetTimeSlotModelList.isEmpty()) {
                            lvTimeSlot.setVisibility(View.GONE);
                            tvNoTimeSlot.setVisibility(View.VISIBLE);
                        } else {
                            lvTimeSlot.setVisibility(View.VISIBLE);
                            tvNoTimeSlot.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetVetTimeSlotInfoAll:

                rootView.setVisibility(View.VISIBLE);
                // vetTimeSlotModelList = new ArrayList();
                ToastHelper.getInstance().showMessage(mError);

                break;

            case VetTimeSlotDelete:

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

            case R.id.btn_addNewTimeSlot:

                Utils.buttonClickEffect(view);
                homeActivity.pushFragment(new VetTimeSlotInfoFragment(), true, false, null);
                break;

            case R.id.img_delete:

                Utils.buttonClickEffect(view);
                vetTimeSlotModel = (VetTimeSlotModel) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), getString(R.string.str_delete_time_slot),
                        getString(R.string.str_want_to_delete), false);
                break;

            case R.id.btn_actionOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                try {
                    params.put("op", "VetTimeSlotDelete");
                    params.put("AuthKey", ApiList.AUTH_KEY);
                    params.put("VetTimeSlotId", vetTimeSlotModel.getVetTimeSlotId());


                    RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_TIME_SLOT_DELETE,
                            true, RequestCode.VetTimeSlotDelete, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_actionCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }
    }

    private void getTimeSlotData() {
        try {
            params = new JSONObject();
            params.put("op", "GetVetTimeSlotInfoAll");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_TIME_SLOT_INFO_ALL,
                    true, RequestCode.GetVetTimeSlotInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
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
            // homeActivity.popBackFragment();
            homeActivity.removeFragmentUntil(VetHomeFragment.class);
            // homeActivity.pushFragment(new ClientHomeFragment(), true, true, null);
        }

        return super.onOptionsItemSelected(item);
    }
}


