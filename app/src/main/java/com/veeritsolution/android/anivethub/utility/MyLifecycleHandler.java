package com.veeritsolution.android.anivethub.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.veeritsolution.android.anivethub.MyApplication;


/*
* This class use to maintain application state that application is in running mode or not.
* There are so many cases handle like running state, destroy state,hibernate,sleep and more.
* it will be use to manage action like push notification.
*
* refer below link to understand activity life cycle
* @link : https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle
*
* refer below link to understand activity state like visible or invisible state
* @link : https://developer.android.com/training/basics/activity-lifecycle/starting.html
*
* */
public class MyLifecycleHandler implements ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
//    private int resumed;
//    private int paused;
//    private int started;
//    private int stopped;

    @SuppressLint("StaticFieldLeak")
    public static Activity currentActivity;
    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static boolean isApplicationInBackGround() {
        return paused > resumed;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        currentActivity = activity;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Debug.trace("test", "onActivityDestroyed: started" + started + "resumed: " + resumed + " paused: " + paused + " stopped" + stopped);

        if (resumed == paused && resumed == started && resumed == stopped) {
            Debug.trace("MyLifecycleHandler", "App is closed");
            MyApplication.isAppRunnning = false;

            /*On back press app is close but MyApplication variables are not reset, So reset varible here*/
            /*if(MyApplication.appInstance!=null)
                MyApplication.appInstance.showRecFollowers=true;*/

//            ToastHelper.displayInfo("App is closed", Gravity.CENTER);
            //Toast.makeText(activity,"App is closed",Toast.LENGTH_SHORT).show();

            /*Clean Temp Directory, on close app */
            //cleanImageDirectories();
        } else
            MyApplication.isAppRunnning = true;

    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        MyApplication.isAppRunnning = true;
        Debug.trace("MyActivityLifeCycle", "onActivityResumed isApplicationInForeground() " + isApplicationInForeground());

    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        Debug.trace("test", "application is in foreground: " + (resumed > paused));
        Debug.trace("MyActivityLifeCycle", "onActivityPaused isApplicationInForeground() " + isApplicationInForeground());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;

    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        Debug.trace("test", "application is visible: " + (started > stopped));
        Debug.trace("MyActivityLifeCycle", "onActivityStopped isApplicationInForeground() " + isApplicationInForeground());
    }
}
