package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpSearchVetList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchVetPractiseActivity extends AppCompatActivity implements OnClickEvent, DataObserver, View.OnClickListener {

    // xml components
    private TextView /*tvSearchResult,*/ tvHeader;
    private ListView lvSearchVet;
    private Toolbar toolbar;

    // object and variable declaration
    private JSONObject params;
    private ArrayList<SearchVetModel> normalVetList;
    private AdpSearchVetList adpSearchVetList;
    private SearchVetModel searchVetModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vet_practise);

        init();

        Intent intent = getIntent();

        if (intent != null) {
            searchVetModel = (SearchVetModel) intent.getSerializableExtra(Constants.VET_ID);

            if (searchVetModel != null) {
                getVetPractiseData();
            }
        }
    }

    private void getVetPractiseData() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.SEARCH_VET_PRACTISE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetPractiseId", searchVetModel.getVetId());
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());

            RestClient.getInstance().post(this, Request.Method.POST, params,
                    ApiList.SEARCH_VET_PRACTISE, true, RequestCode.SearchVetPractise, this);

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
        tvHeader.setText(getString(R.string.str_vet_practise));

        normalVetList = new ArrayList<>();
        lvSearchVet = (ListView) findViewById(R.id.lv_vetPractiseSearch);

        adpSearchVetList = new AdpSearchVetList(this, normalVetList, this);
        lvSearchVet.setAdapter(adpSearchVetList);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case SearchVetPractise:

                if (mObject instanceof ErrorModel) {
                    ErrorModel errorModel = (ErrorModel) mObject;
                    ToastHelper.getInstance().showMessage(errorModel.getError());
                } else {
                    normalVetList = (ArrayList<SearchVetModel>) mObject;
                    searchVetModel = normalVetList.get(0);
                    adpSearchVetList = new AdpSearchVetList(this, normalVetList, this);
                    lvSearchVet.setAdapter(adpSearchVetList);
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case SearchVetPractise:

                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
           /* case R.id.img_back_header:

                Utils.buttonClickEffect(view);
                finish();
                break;*/

            case R.id.lin_vetSearch:

                Utils.buttonClickEffect(view);
                SearchVetModel searchVetModel = (SearchVetModel) view.getTag();

                if (searchVetModel != null) {
                    Intent intent = new Intent(this, VetDetailActivity.class);
                    searchVetModel.setIsVetPractise(1);
                    intent.putExtra(Constants.VET_ID, searchVetModel);
                    intent.putExtra(Constants.VET_PRACTICE_ID, this.searchVetModel.getVetId());
                    startActivity(new Intent(intent));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                // finish();
                break;

            case R.id.img_sortCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.img_filterCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.action_filterBy:

                if (searchVetModel != null)
                    CustomDialog.getInstance().showFilterDialog(this, getString(R.string.str_filterby), false, searchVetModel, this, true);
                break;

            case R.id.action_sortBy:

                if (searchVetModel != null)
                    CustomDialog.getInstance().showSortDialog(this, getString(R.string.str_sortby), false, searchVetModel, this, true);
                break;

            default:

                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }
}
