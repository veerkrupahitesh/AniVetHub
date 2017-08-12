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
import com.veeritsolution.android.anivethub.adapter.AdpPetTreatment;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.TreatmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by admin on 3/2/2017.
 */

public class ClientPetTreatFragment extends Fragment implements DataObserver, OnClickEvent, OnBackPressedEvent {

    // xml components
    private ListView lvTreatment;
    private View rootView;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoTreatment;
    private Button btnAddTreatment;

    // object and variable declaration
    private PetDetailsModel petDetailsModel;
    private AdpPetTreatment adapter;
    private JSONObject params;
    private Bundle bundle;
    private TreatmentModel petTreatmentModel;
    private HomeActivity homeActivity;
    private ArrayList<TreatmentModel> treatmentList;
    private int flag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        bundle = getArguments();
        if (bundle != null) {
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pet_treatment_list, container, false);
        //  rootView.setVisibility(View.INVISIBLE);
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
        tvHeader.setText(getString(R.string.str_treatment));

        lvTreatment = (ListView) rootView.findViewById(R.id.lv_petTreatment);

        btnAddTreatment = (Button) rootView.findViewById(R.id.btn_addTreatments);
        btnAddTreatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        //  if (fromSignUp) {
        tvNoTreatment = (TextView) rootView.findViewById(R.id.tv_noTreatmentInfo);
        tvNoTreatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getClientTreatmentInfoAll();
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPetTreatmentInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                        lvTreatment.setVisibility(View.GONE);
                        tvNoTreatment.setVisibility(View.VISIBLE);
                    } else {
                        lvTreatment.setVisibility(View.VISIBLE);
                        tvNoTreatment.setVisibility(View.GONE);
                        treatmentList = (ArrayList<TreatmentModel>) mObject;
                        adapter = new AdpPetTreatment(getActivity(), treatmentList, false);
                        lvTreatment.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientPetTreatmentDelete:

                try {

                    if (mObject instanceof ErrorModel) {
                        treatmentList.remove(petTreatmentModel);
                        adapter.notifyDataSetChanged();

                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());

                    } else {
                        treatmentList.remove(petTreatmentModel);
                        adapter.notifyDataSetChanged();

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

            case GetClientPetTreatmentInfoAll:

                ToastHelper.getInstance().showMessage(mError);
                break;

            case ClientPetTreatmentDelete:
                ToastHelper.getInstance().showMessage(mError);

                break;
        }
    }


    public void onClick(View view) {

        switch (view.getId()) {

            /*case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;*/

            case R.id.img_edit:

                flag = 0;
                Utils.buttonClickEffect(view);
                petTreatmentModel = (TreatmentModel) view.getTag();

                rootView.setVisibility(View.VISIBLE);
                //int position = (int) view.getTag();
                //petTreatmentModel = list.get(position);

                bundle = new Bundle();
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                bundle.putSerializable(Constants.PET_TREATMENT_DATA, petTreatmentModel);
                bundle.putInt(Constants.FLAG, flag);
                homeActivity.pushFragment(new ClientPetTreatInfoFragment(), true, false, bundle);
                break;


            case R.id.lin_treatment:

/*                petTreatmentModel = (TreatmentModel) view.getTag();
                rootView.setVisibility(View.VISIBLE);
                //int position = (int) view.getTag();
                //petTreatmentModel = list.get(position);

                bundle = new Bundle();
                bundle.putSerializable(Constants.PET_TREATMENT_DATA,petTreatmentModel);
                homeActivity.pushFragment(new ClientPetTreatInfoFragment(), true, false, bundle);
                break;*/

                break;
            case R.id.img_delete:
                Utils.buttonClickEffect(view);
                petTreatmentModel = (TreatmentModel) view.getTag();
                CustomDialog.getInstance().showActionDialog(getActivity(), "Delete Treatment",
                        getString(R.string.str_want_to_delete), false);
                break;

            case R.id.btn_addTreatments:
                Utils.buttonClickEffect(view);

                flag = 1;
                rootView.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putBoolean(Constants.IS_FROM_SIGN_UP, false);
                bundle.putSerializable(Constants.PET_DATA, petDetailsModel);
                bundle.putSerializable(Constants.PET_TREATMENT_DATA, petTreatmentModel);
                bundle.putInt(Constants.FLAG, flag);
                homeActivity.pushFragment(new ClientPetTreatInfoFragment(), true, false, bundle);
                break;

            case R.id.btn_actionOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                ClientPetTreatmentDelete();
                break;

            case R.id.btn_actionCancel:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;
        }

    }


    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();

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

    private void ClientPetTreatmentDelete() {

        try {
            params = new JSONObject();
            params.put("op", "ClientPetTreatmentDelete");
            params.put("ClientPetTreatmentId", petTreatmentModel.getClientPetTreatmentId());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_DELETE,
                    true, RequestCode.ClientPetTreatmentDelete, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getClientTreatmentInfoAll() {

        params = new JSONObject();

        try {
            params.put("op", "GetClientPetTreatmentInfoAll");
            params.put("ClientPetId", petDetailsModel.getClientPetId());
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_PET_TREATMENT_INFO_ALL,
                    true, RequestCode.GetClientPetTreatmentInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
