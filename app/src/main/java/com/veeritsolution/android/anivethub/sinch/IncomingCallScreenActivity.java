package com.veeritsolution.android.anivethub.sinch;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.VetCallFeedbackActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.models.GeneralSettingsModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class IncomingCallScreenActivity extends BaseActivity implements DataObserver, OnClickListener, VideoCallListener, MessageClientListener {

    //xml components
    private TextView remoteUser, tvMinutesRemains;
    private Button answer, decline;
    private ProgressBar prgCallDuration;
    private LinearLayout linCallingView, linRemoteUser;
    private RelativeLayout relVideoView, relLocalUser;
    private View rootView;

    // object and variable declaration
    private String CALL_START_TIME = "callStartTime";
    private String ADDED_LISTENER = "addedListener";
    private String mCallId;
    private JSONObject params;
    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
    private long mCallStart = 0, mCallEnd = 0;
    private boolean mAddedListener = false;
    private int sessionId = 0;
    private Call call;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_incoming_call, null);
        setContentView(rootView);

        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }
        mCallId = getIntent().getStringExtra(Constants.CALL_ID);

        init();
    }

    private void init() {

        answer = (Button) findViewById(R.id.answerButton);
        answer.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        answer.setEnabled(false);

        decline = (Button) findViewById(R.id.declineButton);
        decline.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        decline.setEnabled(false);

        remoteUser = (TextView) findViewById(R.id.remoteUser);
        remoteUser.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();

        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
    }

    @Override
    public void onServiceConnected() {

        answer.setEnabled(true);
        decline.setEnabled(true);

        ToastHelper.getInstance().showMessage("Please accept the Call");

        call = getSinchServiceInterface().getCall(mCallId);

        if (call != null) {

            call.addCallListener(this);
            getSinchServiceInterface().getClient().getMessageClient().addMessageClientListener(this);
            // Map<String, String> map = call.getHeaders();
            // Debug.trace("vetName", map.get("vetName"));
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Debug.trace("Started with invalid callId, aborting");
            finish();
        }
    }

    @Override
    public void onServiceDisconnected() {

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
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetStatusUpdate:
                //   ToastHelper.getInstance().showMessage("Vet Surgeon is busy now!");
                break;

        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        //ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.answerButton:

                answerClicked();
                break;

            case R.id.declineButton:

                declineClicked();
                break;

            case R.id.hangupButton:
                if (call != null) {
                    call.hangup();

                    Intent intent = new Intent(this, VetCallFeedbackActivity.class);
                    // intent.putExtra("sessionId", sessionId);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onVideoTrackAdded(Call call) {
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
        Debug.trace("Call progressing");
        mAudioPlayer.playRingtone();
    }

    @Override
    public void onCallEstablished(Call call) {
        Debug.trace("Call established");
        Debug.trace("Call offered video: " + call.getDetails().isVideoOffered());

        vetStatusUpdate(Constants.BUSY_STATUS);
        mTimer.schedule(mDurationTask, 0, 500);
        mAudioPlayer.stopRingtone();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        AudioController audioController = getSinchServiceInterface().getAudioController();
        audioController.enableSpeaker();
        mCallStart = System.currentTimeMillis();
    }

    @Override
    public void onCallEnded(Call call) {

        mCallEnd = System.currentTimeMillis();
        Debug.trace("callEndTime", "" + Utils.formatTimeSpan(call.getDetails().getEndedTime()));
        Debug.trace("callStartTime", "" + Utils.formatTimeSpan(call.getDetails().getStartedTime()));
        vetStatusUpdate(Constants.ONLINE_STATUS);
        mAudioPlayer.stopRingtone();
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        CallEndCause cause = call.getDetails().getEndCause();

        switch (cause) {

            case TIMEOUT:
                ToastHelper.getInstance().showMessage("Due to weak network connection call time out. please try again");
                break;

            case DENIED:

                ToastHelper.getInstance().showMessage("Call Denied");
                break;

            case NO_ANSWER:

                ToastHelper.getInstance().showMessage("Didn't answered");
                break;

            case CANCELED:
                ToastHelper.getInstance().showMessage("Call Canceled");
                break;

            case FAILURE:
                ToastHelper.getInstance().showMessage("Due to uncertain reason call failed");
                break;

            case HUNG_UP:
                ToastHelper.getInstance().showMessage("Call Hang up");
                removeVideoViews();
                Intent intent = new Intent(this, VetCallFeedbackActivity.class);
                // intent.putExtra("sessionId", sessionId);
                startActivity(intent);
                break;
        }
        finish();
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list) {

    }

    private void vetStatusUpdate(int status) {

        try {
            params = new JSONObject();
            params.put("op", "VetStatusUpdate");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            params.put("OnlineStatus", status);

            RestClient.getInstance().post(this, Request.Method.POST, params,
                    ApiList.VET_STATUS_UPDATE, false, RequestCode.VetStatusUpdate, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIncomingMessage(MessageClient messageClient, Message message) {

        PrefHelper.getInstance().setInt(PrefHelper.SESSION_ID, Integer.parseInt(message.getTextBody()));

        //ToastHelper.getInstance().showMessage(message.getTextBody());
    }

    @Override
    public void onMessageSent(MessageClient messageClient, Message message, String s) {

    }

    @Override
    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {

    }

    @Override
    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {

    }

    @Override
    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

    }

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            IncomingCallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
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
            //Snackbar.make(rootView, "Two minutes remain", Snackbar.LENGTH_LONG).show();
            // tvMinutesRemains.setText("Two minutes remains");
        }

        if (TimeUnit.MINUTES.toSeconds(GeneralSettingsModel.getGeneralSettings().getMinutePerSession()) == TimeUnit.MILLISECONDS.toSeconds(callDuration)) {
            if (call != null) {
                call.hangup();
            }
            // endCall();
        }
    }


    private void addVideoViews() {

        linCallingView.setVisibility(View.GONE);
        relVideoView.setVisibility(View.VISIBLE);

        final VideoController vc = getSinchServiceInterface().getClient().getVideoController();

        if (vc != null) {

            relLocalUser.addView(vc.getLocalView());
            relLocalUser.setOnClickListener(new OnClickListener() {
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

        VideoController vc = getSinchServiceInterface().getClient().getVideoController();

        if (vc != null) {

            //LinearLayout view = (LinearLayout) findViewById(R.id.linRemoteUser);
            relLocalUser.removeAllViews();
            // RelativeLayout localView = (RelativeLayout) findViewById(R.id.relLocalUser);
            linRemoteUser.removeAllViews();
        }
    }

    private void answerClicked() {

        mAudioPlayer.stopRingtone();

        if (call != null) {
            call.addCallListener(this);
            initVideoViews();
            call.answer();
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private void initVideoViews() {

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_callscreen, null);
        setContentView(rootView);

        relVideoView = (RelativeLayout) findViewById(R.id.rel_videoView);
        linCallingView = (LinearLayout) findViewById(R.id.lin_callingView);

        relLocalUser = (RelativeLayout) findViewById(R.id.relLocalUser);
        linRemoteUser = (LinearLayout) findViewById(R.id.linRemoteUser);

        tvMinutesRemains = (TextView) findViewById(R.id.tv_minutesRemains);

        Button endCallButton = (Button) findViewById(R.id.hangupButton);
        endCallButton.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        prgCallDuration = (ProgressBar) findViewById(R.id.prg_callDuration);
        prgCallDuration.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.drw_custom_progressbar, null));
        prgCallDuration.setMax((int) TimeUnit.MINUTES.toSeconds(GeneralSettingsModel.getGeneralSettings().getMinutePerSession()));

    }
}
