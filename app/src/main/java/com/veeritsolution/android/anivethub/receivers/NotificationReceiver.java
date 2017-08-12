package com.veeritsolution.android.anivethub.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.utility.Constants;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ${Hitesh} on 18-Apr-17.
 */

public class NotificationReceiver extends BroadcastReceiver implements DataObserver {

    private JSONObject params;
    private Map<String, String> values;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Bundle bundle = intent.getExtras();

        values = (Map<String, String>) bundle.getSerializable(Constants.KEY_FROM_NOTIFICATION);

        String action = intent.getAction();
        int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);

        if (action.equals(context.getString(R.string.str_yes))) {

            switch (loginUser) {

                case Constants.CLIENT_LOGIN:

                    // sendClientConfirmation();
                    break;

                case Constants.VET_LOGIN:
                    //  sendVetConfirmation();
                    break;

                case Constants.CLINIC_LOGIN:

                    break;
            }
        } else if (action.equals(context.getString(R.string.str_no))) {
            switch (loginUser) {

                case Constants.CLIENT_LOGIN:

                    break;

                case Constants.VET_LOGIN:

                    break;

                case Constants.CLINIC_LOGIN:

                    break;
            }
        }
    }

    /*private void sendVetConfirmation() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.APPOINTMENT_VET_CONFIRM);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetAppointmentId", values.get(Constants.KEY_VET_APPOINTMENT_ID));

            RestClient.getInstance().post(context, Request.Method.POST,params,ApiList.APPOINTMENT_VET_CONFIRM,
                    false, RequestCode.AppointmentVetConfirm,this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendClientConfirmation() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.APPOINTMENT_CLIENT_CONFIRM);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetAppointmentId", values.get(Constants.KEY_VET_APPOINTMENT_ID));

            RestClient.getInstance().post(context, Request.Method.POST,params,ApiList.APPOINTMENT_CLIENT_CONFIRM,
                    false, RequestCode.AppointmentClientConfirm,this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }
}
