package com.veeritsolution.android.anivethub.sinch;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.messaging.WritableMessage;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.sinch.android.rtc.video.VideoScalingType;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.ClientCallFeedbackActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.GeneralSettingsModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlaceCallActivity extends AppCompatActivity implements OnClickEvent, SinchClientListener,
        DataObserver, VideoCallListener {

    // xml components
    private Button mCallButton, stopButton, endCallButton;
    private TextView mCallName, tvUserName, tvHeader, mCallState, tvMinutesRemains;
    private Toolbar toolbar;
    private ImageView imgVetProfilePic;
    private ProgressBar prgCallDuration;
    private LinearLayout linCallingView, linRemoteUser;
    private RelativeLayout relVideoView, relLocalUser;

    // object and variable declaration
    private PetSymptomsModel petSymptomsModel;
    private VetLoginModel vetLoginModel;
    private Intent intent;
    private PetDetailsModel petDetailsModel;
    private AppointmentModel appointmentModel;

    // sinch variables
    private SinchClient mSinchClient;
    private String mUserId;
    final String TAG = PlaceCallActivity.class.getSimpleName();
    final String CALL_START_TIME = "callStartTime";
    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
    private long mCallStart = 0, mCallEnd = 0;
    private JSONObject params;
    private int sessionId, vetPractiseId;
    private Call call;


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        // mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_place_call);

        startClientForVideo();

        intent = getIntent();

        if (intent != null) {
            vetLoginModel = (VetLoginModel) intent.getSerializableExtra(Constants.VET_DATA);
            petSymptomsModel = (PetSymptomsModel) intent.getSerializableExtra(Constants.PET_SYMPTOMS_DATA);
            petDetailsModel = (PetDetailsModel) intent.getSerializableExtra(Constants.PET_DATA);
            appointmentModel = (AppointmentModel) intent.getSerializableExtra(Constants.APPOINTMENT_DATA);
            vetPractiseId = intent.getIntExtra(Constants.VET_PRACTICE_ID, 0);
        }


        init();
    }

    private void startClientForVideo() {

        if (mSinchClient == null) {

            mUserId = ClientLoginModel.getClientCredentials().getClientName();

            mSinchClient = Sinch.getSinchClientBuilder().context(this)
                    .userId(mUserId)
                    .applicationKey(Constants.APP_KEY)
                    .applicationSecret(Constants.APP_SECRET)
                    .environmentHost(Constants.ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.getVideoController().setResizeBehaviour(VideoScalingType.ASPECT_FILL);
            mSinchClient.addSinchClientListener(this);
            mSinchClient.setSupportMessaging(true);
            //  mSinchClient.getCallClient().addCallClientListener(new SinchService.SinchCallClientListener());
            mSinchClient.start();
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
        tvHeader.setText(getString(R.string.str_call_now));

        imgVetProfilePic = (ImageView) findViewById(R.id.img_vetProfilePic);

        mCallName = (TextView) findViewById(R.id.callName);
        mCallName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvUserName = (TextView) findViewById(R.id.tv_userName);
        tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        mCallButton.setEnabled(false);
        //  mCallButton.setOnClickListener(buttonClickListener);

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        stopButton.setVisibility(View.INVISIBLE);
        //  stopButton.setOnClickListener(buttonClickListener);

        if (vetLoginModel != null) {
            Utils.setProfileImage(this, vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePic);
            mCallName.setText(vetLoginModel.getUserName());
            tvUserName.setText(vetLoginModel.getVetName());
        }

        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
    }

    @Override
    public void onStop() {

        mDurationTask.cancel();
        mTimer.cancel();
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.callButton:

                if (mSinchClient.isStarted()) {
                    callButtonClicked();
                } else {
                    ToastHelper.getInstance().showMessage("Please wait!");
                    startClientForVideo();
                }
                break;

            case R.id.hangupButton:
                //Call call = mSinchClient.getCallClient().getCall(mUserId);
                if (call != null) {
                    call.hangup();
                }
                // endCall();
                break;

            case R.id.stopButton:
                //endCall();
                //stopButtonClicked();
                break;

        }
    }


    @Override
    public void onClientStarted(SinchClient sinchClient) {

        ToastHelper.getInstance().showMessage("You can call now!");
        mCallButton.setEnabled(true);
    }

    @Override
    public void onClientStopped(SinchClient sinchClient) {
        startClientForVideo();
    }

    @Override
    public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {

        //ToastHelper.getInstance().showMessage(sinchError.getMessage());
        startClientForVideo();
    }

    @Override
    public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

    }

    @Override
    public void onLogMessage(int level, String area, String message) {

        switch (level) {

            case Log.DEBUG:
                Debug.trace(area, message);

                break;
            case Log.ERROR:
                Debug.trace(area, message);
                break;

            case Log.INFO:
                Debug.trace(area, message);
                break;

            case Log.VERBOSE:
                Debug.trace(area, message);
                break;

            case Log.WARN:
                Debug.trace(area, message);
                break;
        }
    }

    private void callButtonClicked() {

        initVideoViews();

        call = mSinchClient.getCallClient().callUserVideo(vetLoginModel.getUserName());

        if (call != null) {

            //call.getHeaders().put("vetName", vetLoginModel.getVetName());
            call.addCallListener(this);
            mCallState.setText(getString(R.string.str_calling));
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetSessionInsert:
                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    sessionId = jsonObject.getInt("DataId");

                    WritableMessage msg = new WritableMessage(vetLoginModel.getUserName(), String.valueOf(sessionId));
                    mSinchClient.getMessageClient().send(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case VetSessionUpdate:

                JSONArray jsonArray = (JSONArray) mObject;

                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        //ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onVideoTrackAdded(Call call) {
        Debug.trace(TAG, "Video track added");
        addVideoViews();
    }

    @Override
    public void onVideoTrackPaused(Call call) {

        call.pauseVideo();
    }

    @Override
    public void onVideoTrackResumed(Call call) {
        call.resumeVideo();
    }

    @Override
    public void onCallProgressing(Call call) {
        Debug.trace(TAG, "Call progressing");
        mAudioPlayer.playProgressTone();
    }

    @Override
    public void onCallEstablished(Call call) {

        mTimer.schedule(mDurationTask, 0, 500);
        mCallStart = System.currentTimeMillis();

        insertSession();

        mCallState.setVisibility(View.GONE);
        imgVetProfilePic.setVisibility(View.GONE);
        endCallButton.setVisibility(View.VISIBLE);

        Debug.trace(TAG, "Call established");
        mAudioPlayer.stopProgressTone();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        AudioController audioController = mSinchClient.getAudioController();
        audioController.enableSpeaker();

        Debug.trace(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
    }

    @Override
    public void onCallEnded(Call call) {

        CallEndCause cause = call.getDetails().getEndCause();
        Debug.trace(TAG, "Call ended. Reason: " + cause.toString());
        mAudioPlayer.stopProgressTone();
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        String endMsg = "Call ended: " + call.getDetails().toString();

        Debug.trace("callEndTime", "" + Utils.formatTimeSpan(call.getDetails().getEndedTime()));
        Debug.trace("callStartTime", "" + Utils.formatTimeSpan(call.getDetails().getStartedTime()));

        switch (cause) {

            case TIMEOUT:
                ToastHelper.getInstance().showMessage("Due to weak network connection call time out. please try again");

                calEndUncertainReason();
                break;

            case DENIED:
                calEndUncertainReason();
                ToastHelper.getInstance().showMessage("Call denied");
                break;

            case NO_ANSWER:
                calEndUncertainReason();
                ToastHelper.getInstance().showMessage("No answer");
                break;

            case CANCELED:
                calEndUncertainReason();
                ToastHelper.getInstance().showMessage("Call Canceled");
                break;

            case FAILURE:
                calEndUncertainReason();
                ToastHelper.getInstance().showMessage("Due to uncertain reason call failed");
                break;

            case HUNG_UP:
                mCallEnd = System.currentTimeMillis();
                updateSession();
                removeVideoViews();
                ToastHelper.getInstance().showMessage("Call Hang up");
                intent = new Intent(this, ClientCallFeedbackActivity.class);
                intent.putExtra("sessionId", sessionId);
                startActivity(intent);
                break;

            case NONE:

                break;
        }
        mSinchClient.stop();
    }

    private void calEndUncertainReason() {

        setContentView(R.layout.activity_place_call);
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
        tvHeader.setText(getString(R.string.str_call_now));

        if (vetLoginModel != null) {
            imgVetProfilePic = (ImageView) findViewById(R.id.img_vetProfilePic);
            Utils.setProfileImage(this, vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePic);
            mCallName = (TextView) findViewById(R.id.callName);
            mCallName.setText(vetLoginModel.getUserName());
            tvUserName = (TextView) findViewById(R.id.tv_userName);
            tvUserName.setText(vetLoginModel.getVetName());
        }
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list) {

    }

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            PlaceCallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    private void initVideoViews() {

        setContentView(R.layout.activity_callscreen);

        mAudioPlayer = new AudioPlayer(this);
        mCallState = (TextView) findViewById(R.id.callState);
        TextView userName = (TextView) findViewById(R.id.remoteUser);
        imgVetProfilePic = (ImageView) findViewById(R.id.img_vetProfilePic);

        if (vetLoginModel != null) {

            Utils.setProfileImage(this, vetLoginModel.getProfilePic(), R.drawable.img_vet_profile, imgVetProfilePic);
            userName.setText(vetLoginModel.getVetName());
        }

        relVideoView = (RelativeLayout) findViewById(R.id.rel_videoView);
        linCallingView = (LinearLayout) findViewById(R.id.lin_callingView);

        relLocalUser = (RelativeLayout) findViewById(R.id.relLocalUser);
        linRemoteUser = (LinearLayout) findViewById(R.id.linRemoteUser);

        tvMinutesRemains = (TextView) findViewById(R.id.tv_minutesRemains);

        endCallButton = (Button) findViewById(R.id.hangupButton);
        endCallButton.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        endCallButton.setVisibility(View.INVISIBLE);

        prgCallDuration = (ProgressBar) findViewById(R.id.prg_callDuration);
        prgCallDuration.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drw_custom_progressbar, null));
        prgCallDuration.setMax((int) TimeUnit.MINUTES.toSeconds(GeneralSettingsModel.getGeneralSettings().getMinutePerSession()));
    }

    private void updateCallDuration() {

        if (mCallStart > 0) {
            prgCallDuration.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - mCallStart));
            tvMinutesRemains.setText(Utils.getMinutes(System.currentTimeMillis() - mCallStart)
                    + "m" + " " + Utils.getSeconds(System.currentTimeMillis() - mCallStart) + "sec");
        }
        long timeRemain = (GeneralSettingsModel.getGeneralSettings().getMinutePerSession() - 1);
        long callDuration = System.currentTimeMillis() - mCallStart;

        if (TimeUnit.MINUTES.toSeconds(timeRemain) == TimeUnit.MILLISECONDS.toSeconds(callDuration)) {

            ToastHelper.getInstance().showMessage(getString(R.string.str_two_minutes_remains));
            // tvMinutesRemains.setText("Two minutes remains");
        }

        if (TimeUnit.MINUTES.toSeconds(GeneralSettingsModel.getGeneralSettings().getMinutePerSession())
                == TimeUnit.MILLISECONDS.toSeconds(callDuration)) {
            Call call = mSinchClient.getCallClient().getCall(mUserId);
            if (call != null) {
                call.hangup();
            }
            //endCall();
            intent = new Intent(this, ClientCallFeedbackActivity.class);
            intent.putExtra("sessionId", sessionId);
            startActivity(intent);
            finish();
        }
    }


    private String getCurrentTimeFromMilliSeconds(long milliSeconds) {

        int seconds = (int) (milliSeconds / 1000) % 60;
        int minutes = (int) ((milliSeconds / (1000 * 60)) % 60);
        int hours = (int) ((milliSeconds / (1000 * 60 * 60)) % 24);

        return String.format(Locale.UK, "%02d:%02d:%02d", hours, minutes, seconds);

     /*  return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));*/
    }

    private void addVideoViews() {

        linCallingView.setVisibility(View.GONE);
        relVideoView.setVisibility(View.VISIBLE);

        final VideoController vc = mSinchClient.getVideoController();

        if (vc != null) {

            relLocalUser.addView(vc.getLocalView());
            relLocalUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vc.toggleCaptureDevicePosition();
                }
            });

            linRemoteUser.addView(vc.getRemoteView());
        }
    }

    private void removeVideoViews() {

        linCallingView.setVisibility(View.VISIBLE);
        relVideoView.setVisibility(View.GONE);

        VideoController vc = mSinchClient.getVideoController();

        if (vc != null) {

            //LinearLayout view = (LinearLayout) findViewById(R.id.linRemoteUser);
            relLocalUser.removeAllViews();
            // RelativeLayout localView = (RelativeLayout) findViewById(R.id.relLocalUser);
            linRemoteUser.removeAllViews();
        }
    }

    private void updateSession() {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int hours = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_SESSION_UPDATE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionId);
            params.put("SessionEndOn", String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_SESSION_UPDATE,
                    false, RequestCode.VetSessionUpdate, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertSession() {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int hours = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_SESSION_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", vetLoginModel.getVetId());
            params.put("VetPractiseId", vetPractiseId);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            //params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("ClientPetId", petDetailsModel.getClientPetId());
            params.put("SymptomsDescription", petSymptomsModel.getSymptomsName());
            params.put("SessionStartOn", String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
            params.put("PaymentAmount", GeneralSettingsModel.getGeneralSettings().getRatePerTotalSession1());

            if (appointmentModel == null) {
                params.put("VetAppointmentId", 0);
            } else {
                params.put("VetAppointmentId", appointmentModel.getVetAppointmentId());
            }
            params.put("PaymentTime", "");
            params.put("PaymentId", "");
            params.put("PaymentStatus", "");
            params.put("PaymentResponse", "");

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.VET_SESSION_INSERT,
                    false, RequestCode.VetSessionInsert, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
