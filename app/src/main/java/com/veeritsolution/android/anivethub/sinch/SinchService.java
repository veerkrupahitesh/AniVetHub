package com.veeritsolution.android.anivethub.sinch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.video.VideoController;
import com.sinch.android.rtc.video.VideoScalingType;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;

import java.util.List;

public class SinchService extends Service implements MessageClientListener {

    //public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();
    private static final String APP_KEY = "78030c9f-ddfb-4b8a-9f10-4651ecfca958";
    private static final String APP_SECRET = "zK4SAegqcE6+7bkgu/OqEA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;

    private StartFailedListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {

        if (mSinchClient == null) {

            mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(MyApplication.getInstance()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.setSupportMessaging(true);

            mSinchClient.setSupportCalling(true);

            mSinchClient.getVideoController().setResizeBehaviour(VideoScalingType.ASPECT_FILL);

            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getMessageClient().addMessageClientListener(this);
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public void onIncomingMessage(MessageClient messageClient, Message message) {

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


    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    public class SinchServiceInterface extends Binder {

       /* public Call callUserVideo(String userId) {
            if (PrefHelper.getInstance().getBoolean(PrefHelper.LOGIN_USER, true)) {
                return mSinchClient.getCallClient().callUserVideo(userId);
            } else {
                return null;
            }
        }*/

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }

            return mSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getAudioController();
        }

        public SinchClient getClient() {

            return mSinchClient;
        }
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Debug.trace(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Debug.trace(TAG, "SinchClient stopped");
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

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

            Debug.trace(TAG, "Incoming call");

            Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
            intent.putExtra(Constants.CALL_ID, call.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchService.this.startActivity(intent);
        }
    }

}
