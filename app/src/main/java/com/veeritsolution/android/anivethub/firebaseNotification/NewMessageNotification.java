package com.veeritsolution.android.anivethub.firebaseNotification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;

import java.util.concurrent.ExecutionException;

/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class NewMessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewMessage";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of new message notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context, final String title, final int number,
                              final String details, final PendingIntent pendingIntent, String picPath) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.img_notification_icon);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.img_notification_icon)
                .setContentTitle(title)
                .setContentText(details)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(title)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(pendingIntent);

        if (picPath != null && !picPath.isEmpty()) {
            builder.setStyle(getStyle(picPath, title, details));
        } else {
            // Show expanded text content on devices running Android 4.1 or
            // later.
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(details)
                    .setBigContentTitle(title)
                    .setSummaryText(details));
        }

        // Example additional actions for this notification. These will
        // only show on devices running Android 4.1 or later, so you
        // should ensure that the activity in this notification's
        // content intent provides access to the same actions in
        // another way.
        builder.addAction(
                R.drawable.img_yes,
                res.getString(R.string.str_yes),
                pendingIntent)
                .addAction(
                        R.drawable.img_close,
                        res.getString(R.string.str_no),
                        pendingIntent)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int, String, PendingIntent, String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }

    private static NotificationCompat.BigPictureStyle getStyle(String picPath, String title, String details) {

        //String picPath = body.get(Constants.KEY_IMAGEPATH);
        Bitmap theBitmap = null;
        //if (picPath != null && picPath.length() != 0) {
        try {
            theBitmap = Glide.
                    with(MyApplication.getInstance()).
                    load(picPath).
                    asBitmap().
                    into(200, 200). // Width and height
                    get();
            // notificationView.setImageViewBitmap(R.id.img_notification, theBitmap);

        } catch (InterruptedException | ExecutionException e) {
            // notificationView.setViewVisibility(R.id.img_notification, View.GONE);
            e.printStackTrace();
        }
        //  }
        // Create the style object with BigPictureStyle subclass.
        android.support.v7.app.NotificationCompat.BigPictureStyle notiStyle = new
                android.support.v7.app.NotificationCompat.BigPictureStyle();
        if (theBitmap != null)
            notiStyle.bigPicture(theBitmap)
                    .setBigContentTitle(title)
                    .setSummaryText(details);

        return notiStyle;
    }
}
