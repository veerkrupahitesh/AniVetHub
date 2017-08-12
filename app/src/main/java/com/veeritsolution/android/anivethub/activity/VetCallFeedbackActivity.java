package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;

import org.json.JSONObject;


/**
 * Created by Admin on 5/17/2017.
 */

public class VetCallFeedbackActivity extends AppCompatActivity implements DataObserver, OnClickEvent {

    private View rootView;
    private EditText edtVetCallFeedback;
    private TextView tvHeader;
    private Toolbar toolbar;

    //   private int sessionId;
    private String remark;
    private JSONObject params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_vet_call_feedback, null);
        edtVetCallFeedback = (EditText) rootView.findViewById(R.id.edt_vet_call_feedback);
        setContentView(rootView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VetCallFeedbackActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                finish();
            }
        });

        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_feedback));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VetCallFeedbackActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetSessionUpdateVideoConsultSummary:

                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:

                vetSessionFeedbackInsert();
                break;

        }

    }

    private void vetSessionFeedbackInsert() {

        remark = edtVetCallFeedback.getText().toString();

        try {
            params = new JSONObject();
            params.put("op", "VetSessionUpdateVideoConsultSummary");
            params.put("AuthKey", ApiList.AUTH_KEY);
            if (PrefHelper.getInstance().containKey(PrefHelper.SESSION_ID))
                params.put("VetSessionId", PrefHelper.getInstance().getInt(PrefHelper.SESSION_ID, 0));
            else
                params.put("VetSessionId", 0);
            params.put("VideoConsultSummary", remark);

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_SESSION_UPDATE_VIDEO_CONSULT_SUMMARY,
                    true, RequestCode.VetSessionUpdateVideoConsultSummary, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
