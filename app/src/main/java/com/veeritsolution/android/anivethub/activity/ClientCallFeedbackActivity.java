package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientCallFeedbackModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/22/2017.
 */

public class ClientCallFeedbackActivity extends AppCompatActivity implements DataObserver, OnClickEvent {

    //XML COMPONENTS
    private Button btnSave;
    private TextView tvFeedbackCompetence, tvFeedbackFriendliness, tvFeedbackProfessionalism,
            tvFeedbackRespectForAnimal, tvFeedbackValueForMoney, tvNotifyOthers;
    private RatingBar rbCompetence, rbFriendliness, rbFeedbackProfessionalism,
            rbFeedbackRespectForAnimal, rbFeedbackValueForMoney;
    private Toolbar toolbar;
    private TextView tvHeader;
    private EditText edtNotifyOthers;
    private View rootView;

    //VARIABLES AND OBJECT DECLARATION
    private ArrayList<ClientCallFeedbackModel> clientCallFeedbackModelList;
    private JSONObject params;
    private ArrayList<Integer> ratingList;
    private int i = 0;
    private int sessionId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_call_feedback, null);
        setContentView(rootView);

        Intent intent = getIntent();
        if (intent != null) {
            sessionId = intent.getIntExtra("sessionId", 0);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientCallFeedbackActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                finish();
            }
        });
        ratingList = new ArrayList<>();
        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_feedback));

        tvFeedbackCompetence = (TextView) findViewById(R.id.lbl_feedback_type_competence);
        tvFeedbackCompetence.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvFeedbackFriendliness = (TextView) findViewById(R.id.lbl_feedback_type_Friendliness);
        tvFeedbackFriendliness.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvFeedbackProfessionalism = (TextView) findViewById(R.id.lbl_feedback_type_Professionalism);
        tvFeedbackProfessionalism.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvFeedbackRespectForAnimal = (TextView) findViewById(R.id.lbl_feedback_type_RespectForAnimal);
        tvFeedbackRespectForAnimal.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvFeedbackValueForMoney = (TextView) findViewById(R.id.lbl_feedback_type_ValueForMoney);
        tvFeedbackValueForMoney.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvNotifyOthers = (TextView) findViewById(R.id.lbl_notify_others);
        tvNotifyOthers.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtNotifyOthers = (EditText) findViewById(R.id.edt_notify_others);
        edtNotifyOthers.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        rbCompetence = (RatingBar) findViewById(R.id.rating_feedback_competence);
        rbFriendliness = (RatingBar) findViewById(R.id.rating_feedback_Friendliness);
        rbFeedbackProfessionalism = (RatingBar) findViewById(R.id.rating_feedback_Professionalism);
        rbFeedbackRespectForAnimal = (RatingBar) findViewById(R.id.rating_feedback_RespectForAnimal);
        rbFeedbackValueForMoney = (RatingBar) findViewById(R.id.rating_feedback_ValueForMoney);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        Utils.setupOutSideTouchHideKeyboard(rootView);
        getFeedbackTypeInfo();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {


        switch (mRequestCode) {

            case GetFeedbackTypeInfo:

                if (!(mObject instanceof ErrorModel)) {
                    clientCallFeedbackModelList = (ArrayList<ClientCallFeedbackModel>) mObject;

                    tvFeedbackCompetence.setText(clientCallFeedbackModelList.get(0).getFeedbackTypeName());
                    tvFeedbackFriendliness.setText(clientCallFeedbackModelList.get(1).getFeedbackTypeName());
                    tvFeedbackProfessionalism.setText(clientCallFeedbackModelList.get(2).getFeedbackTypeName());
                    tvFeedbackRespectForAnimal.setText(clientCallFeedbackModelList.get(3).getFeedbackTypeName());
                    tvFeedbackValueForMoney.setText(clientCallFeedbackModelList.get(4).getFeedbackTypeName());
                }
                break;

            case VetSessionFeedbackInsert:

                i++;
                if (i < clientCallFeedbackModelList.size()) {
                    vetSessionFeedbackInsert(i);
                } else {
                    VetReviewUpdate();
                }
                break;

            case VetSessionUpdateReview:
                i = 0;
                CustomDialog.getInstance().dismiss();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

    }

    private void VetReviewUpdate() {

        try {
            params = new JSONObject();
            params.put("op", "VetSessionUpdateReview");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionId);
            params.put("Review", edtNotifyOthers.getText().toString().trim());

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_VET_SESSION_UPDATE_REVIEW,
                    false, RequestCode.VetSessionUpdateReview, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetFeedbackTypeInfo:
                // Debug.trace(mError);
                ToastHelper.getInstance().showMessage(mError);
                break;
            case VetSessionFeedbackInsert:
                //Debug.trace(mError);
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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

            Intent intent = new Intent(ClientCallFeedbackActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:

                Utils.buttonClickEffect(view);

                CustomDialog.getInstance().showProgress(this, this.getString(R.string.str_please_wait), false);
                ratingList.add(0, (int) rbCompetence.getRating());
                ratingList.add(1, (int) rbFriendliness.getRating());
                ratingList.add(2, (int) rbFeedbackProfessionalism.getRating());
                ratingList.add(3, (int) rbFeedbackRespectForAnimal.getRating());
                ratingList.add(4, (int) rbFeedbackValueForMoney.getRating());

                vetSessionFeedbackInsert(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ClientCallFeedbackActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void getFeedbackTypeInfo() {

        params = new JSONObject();
        try {
            params.put("op", "GetFeedbackTypeInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_FEEDBACK_TYPE_INFO,
                    true, RequestCode.GetFeedbackTypeInfo, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vetSessionFeedbackInsert(int i) {

        params = new JSONObject();
        try {
            params.put("op", "VetSessionFeedbackInsert");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionId);
            params.put("FeedbackTypeId", clientCallFeedbackModelList.get(i).getFeedbackTypeId());
            params.put("Rating", ratingList.get(i));

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_SESSION_FEEDBACK_INSERT,
                    false, RequestCode.VetSessionFeedbackInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
