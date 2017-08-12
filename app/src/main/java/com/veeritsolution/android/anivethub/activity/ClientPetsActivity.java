package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpPetList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientPetsActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private ImageView imgClientProfilePhoto, imgClientBannerPhoto, imgSelectBannerPhoto, imgSelectProfilePhoto;
    private Button btnAddPets;
    private TextView tvHeader, tvClientName, tvClientUserName, tvNoPetsFound;
    private View headerView;
    private Toolbar toolbar;

    // object and variable declaration
    private AdpPetList adapter;
    private JSONObject params;
    private List<PetDetailsModel> list;
    private Intent intent;
    private boolean isFromVideoCall;
    private VetLoginModel vetLoginModel;
    private ListView listViewClientPets;
    private AppointmentModel appointmentModel;
    private View rootView;
    private int vetPractiseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_client_pets, null, false);
        rootView.setVisibility(View.INVISIBLE);

        setContentView(rootView);

        intent = getIntent();
        if (intent != null) {
            isFromVideoCall = intent.getBooleanExtra(Constants.IS_FROM_VIDEO_CALL, false);
            vetLoginModel = (VetLoginModel) intent.getSerializableExtra(Constants.VET_DATA);
            appointmentModel = (AppointmentModel) intent.getSerializableExtra(Constants.APPOINTMENT_DATA);
            vetPractiseId = intent.getIntExtra(Constants.VET_PRACTICE_ID, 0);
        }
        init();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getAllPets();
    }

    private void getAllPets() {

        try {
            params = new JSONObject();
            params.put("op", "GetClientPetInfoAll");
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_CLIENT_PET_INFO_ALL, true,
                    RequestCode.GetClientPetInfoAll, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tvHeader.setText(getString(R.string.str_select_pet));

        headerView = findViewById(R.id.headerView);
        headerView.setVisibility(View.GONE);

        //backButton = (ImageView) findViewById(R.id.img_back_header);

        tvClientName = (TextView) findViewById(R.id.tv_clientName);
        tvClientUserName = (TextView) findViewById(R.id.tv_clientUserName);
        // btnAddPets = (Button) findViewById(R.id.btn_addMorePets);
        tvNoPetsFound = (TextView) findViewById(R.id.tv_noPetsFound);

        imgClientProfilePhoto = (ImageView) findViewById(R.id.img_clientProfilePic);
        imgClientBannerPhoto = (ImageView) findViewById(R.id.img_client_banner);

        // imgSelectClientProfilePhoto = (ImageView) findViewById(R.id.img_selectProfilePhoto);
        //  imgSelectClientBannerPhoto = (ImageView) findViewById(R.id.img_selectBannerPhoto);
        Utils.setProfileImage(this, ClientLoginModel.getClientCredentials().getProfilePic(),
                R.drawable.img_client_profile, imgClientProfilePhoto);


        Utils.setBannerImage(this, ClientLoginModel.getClientCredentials().getBannerPic(),
                R.drawable.img_client_banner, imgClientBannerPhoto);


        tvClientName.setText(ClientLoginModel.getClientCredentials().getClientName());
        tvClientUserName.setText(ClientLoginModel.getClientCredentials().getUserName());

        imgSelectBannerPhoto = (ImageView) findViewById(R.id.img_selectBannerPhoto);
        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);

        imgSelectProfilePhoto = (ImageView) findViewById(R.id.img_selectProfilePhoto);
        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        // recyclerView = (RecyclerView) findViewById(R.id.recycler_petList);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        btnAddPets = (Button) findViewById(R.id.btn_addMorePets);
        btnAddPets.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnAddPets.setVisibility(View.GONE);

        listViewClientPets = (ListView) findViewById(R.id.listView_clientPets);
        // params = new JSONObject();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_petList:
                Utils.buttonClickEffect(view);
                PetDetailsModel petDetailsModel = (PetDetailsModel) view.getTag();
                Intent intent = new Intent(this, SymptomsActivity.class);
                intent.putExtra(Constants.IS_FROM_VIDEO_CALL, isFromVideoCall);
                intent.putExtra(Constants.PET_DATA, petDetailsModel);
                intent.putExtra(Constants.VET_DATA, vetLoginModel);
                intent.putExtra(Constants.APPOINTMENT_DATA, appointmentModel);
                intent.putExtra(Constants.VET_PRACTICE_ID, vetPractiseId);
                //    intent.putExtra(Constants.GENERAL_SETTING_DATA,generalSettingsModel);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                finish();
                break;


        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientPetInfoAll:

                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = new ArrayList();

                        list = (ArrayList<PetDetailsModel>) mObject;

                        if (!list.isEmpty()) {
                            adapter = new AdpPetList(this, list, true);
                            listViewClientPets.setAdapter(adapter);
                        } else {
                            tvNoPetsFound.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case ClientPetDelete:

                list = new ArrayList();
                list = (List<PetDetailsModel>) mObject;
                if (!list.isEmpty()) {
                    adapter.refreshList(list);
                } else {
                    tvNoPetsFound.setVisibility(View.VISIBLE);
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
}
