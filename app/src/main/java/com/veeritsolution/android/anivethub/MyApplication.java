package com.veeritsolution.android.anivethub;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.MapsInitializer;
import com.veeritsolution.android.anivethub.utility.Constants;

/**
 * Created by ${hitesh} on 12/6/2016.
 */

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    public static boolean isAppRunnning;
    private static MyApplication mInstance;
    public final int VOLLEY_TIMEOUT = 60000;
    public Typeface /*FONT_ROBOTO_BLACK, FONT_ROBOTO_BLACK_ITALIC, */FONT_ROBOTO_BOLD,/* FONT_ROBOTO_BOLD_CONDENSE,
            FONT_ROBOTO_BOLD_CONDENSE_ITALIC, FONT_ROBOTO_BOLD_ITALIC,*/
    // FONT_ROBOTO_CONDENSE,
    /* FONT_ROBOTO_CONDENSE_ITALIC, FONT_ROBOTO_ITALIC,*/ FONT_ROBOTO_LIGHT,/* FONT_ROBOTO_LIGHT_ITALIC,
            FONT_ROBOTO_MEDIUM, FONT_ROBOTO_MEDIUM_ITALIC,*/
            FONT_ROBOTO_REGULAR /*, FONT_ROBOTO_THIN, FONT_ROBOTO_THIN_ITALIC*/;
    private RequestQueue mRequestQueue;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MapsInitializer.initialize(this);
        //  FONT_ROBOTO_BLACK = getTypeFace(Constants.FONT_ROBOTO_BLACK);
        //  FONT_ROBOTO_BLACK_ITALIC = getTypeFace(Constants.FONT_ROBOTO_BLACK_ITALIC);
        FONT_ROBOTO_BOLD = getTypeFace(Constants.FONT_ROBOTO_BOLD);
        //  FONT_ROBOTO_BOLD_CONDENSE = getTypeFace(Constants.FONT_ROBOTO_BOLD_CONDENSE);
        //  FONT_ROBOTO_BOLD_CONDENSE_ITALIC = getTypeFace(Constants.FONT_ROBOTO_BOLD_CONDENSE_ITALIC);
        //  FONT_ROBOTO_BOLD_ITALIC = getTypeFace(Constants.FONT_ROBOTO_BOLD_ITALIC);
        //  FONT_ROBOTO_CONDENSE = getTypeFace(Constants.FONT_ROBOTO_CONDENSE);
        //  FONT_ROBOTO_CONDENSE_ITALIC = getTypeFace(Constants.FONT_ROBOTO_CONDENSE_ITALIC);
        //  FONT_ROBOTO_ITALIC = getTypeFace(Constants.FONT_ROBOTO_ITALIC);
        FONT_ROBOTO_LIGHT = getTypeFace(Constants.FONT_ROBOTO_LIGHT);
        //  FONT_ROBOTO_LIGHT_ITALIC = getTypeFace(Constants.FONT_ROBOTO_LIGHT_ITALIC);
        //  FONT_ROBOTO_MEDIUM = getTypeFace(Constants.FONT_ROBOTO_MEDIUM);
        //  FONT_ROBOTO_MEDIUM_ITALIC = getTypeFace(Constants.FONT_ROBOTO_MEDIUM_ITALIC);
        FONT_ROBOTO_REGULAR = getTypeFace(Constants.FONT_ROBOTO_REGULAR);
        //  FONT_ROBOTO_THIN = getTypeFace(Constants.FONT_ROBOTO_THIN);
        //  FONT_ROBOTO_THIN_ITALIC = getTypeFace(Constants.FONT_ROBOTO_THIN_ITALIC);
    }

    public Typeface getTypeFace(String typeFaceName) {
        return Typeface.createFromAsset(getAssets(), typeFaceName);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
