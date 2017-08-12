package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.Calendar;

/**
 * Created by hitesh on 1/3/2016
 */
public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Utils.printFbKeyHash();
        Debug.trace("tokenID", PrefHelper.getInstance().getString(PrefHelper.FIREBASE_DEVICE_TOKEN, ""));
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int hours = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);

        Debug.trace("currentDate", "" + hours + ":" + minutes);

        runnable = new Runnable() {

            @Override
            public void run() {
               /* if (!PrefHelper.getInstance().getBoolean(PrefHelper.FIRST_TIME_APP_OPEN, false)) {
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {*/

                if (PrefHelper.getInstance().getBoolean(PrefHelper.IS_LOGIN, false)) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra(Constants.IS_FROM_SIGN_UP, false);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                // }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}
