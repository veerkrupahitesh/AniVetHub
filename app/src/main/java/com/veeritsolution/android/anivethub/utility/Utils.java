package com.veeritsolution.android.anivethub.utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.enums.ImageUpload;
import com.veeritsolution.android.anivethub.helper.PrefHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {

    static Snackbar snackbar;
    static long cacheProfileFlag;
    static long cacheBannerFlag;
//    public static float round(float d, int decimalPlace) {
//        BigDecimal bd = new BigDecimal(Float.toString(d));
//        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
//        return bd.floatValue();
//    }

    public static void printFbKeyHash() {
        // Add code to print out the key hash

        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = MyApplication.getInstance().getPackageManager().getPackageInfo(
                    MyApplication.getInstance().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Debug.trace("KeyHash:", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Debug.trace("error", "Error while printing facebook hash");
        }
    }

    public static boolean isInternetAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public static void hideKeyBoard(View view) {
//        // Check if no view has focus:
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }

//    public static Bitmap StringToBitMap(String encodedString) {
//        try {
//            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//
//    }

//    public static Bitmap compressImage(String imagePath) {
//
//        Bitmap bm;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imagePath, options);
//        final int REQUIRED_SIZE = 100;
//        int scale = 1;
//        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//            scale *= 2;
//        options.inSampleSize = scale;
//        options.inJustDecodeBounds = false;
//        bm = BitmapFactory.decodeFile(imagePath, options);
//
//        return bm;
//    }

    public static String getStringImage(String imagePath, ImageUpload imageUpload) {

        // File sd = Environment.getExternalStorageDirectory();
        File image = new File(imagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        if (imageUpload == ImageUpload.ClientProfile)
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        else if (imageUpload == ImageUpload.ClientBanner)
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        // Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;

    }

    /**
     * Return Date in specified format. This method convert date
     * from millisecond to date format e.g. 1478180961448 to 3/11/2016
     *
     * @param milliSeconds (long) : Date in milliseconds e.g. 1478180961448
     * @param dateFormat   (String) :   Date format e.g. dd/MM/yyyy
     * @return (String) : representing Date in specified format e.g. 3/11/2016
     * @see SimpleDateFormat#format(Object)
     */
    public static String dateFormat(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying Date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        return formatter.format(milliSeconds);
    }

//    public static int getScreenHeight(Context context) {
//
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager()
//                .getDefaultDisplay()
//                .getMetrics(displaymetrics);
//
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
//
//        return height;
//    }

    public static int getScreenWidth(Context context) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay()
                .getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        return width;
    }

    public static String formatDate(String inputDate, String InputDateFormat, String outputDateFormat) {

        SimpleDateFormat originalFormat = new SimpleDateFormat(InputDateFormat);
        //   originalFormat.applyPattern("dd/MM/yyyy HH:mm:ss a");
        SimpleDateFormat targetFormat = new SimpleDateFormat(outputDateFormat);
        Date outPutDate = null, toDate = null;
        try {
            outPutDate = originalFormat.parse(inputDate);
            toDate = originalFormat.parse(inputDate);
            //   System.out.println("Old Format :   " + originalFormat.format(fromDate));
            //   System.out.println("New Format :   " + targetFormat.format(fromDate));

        } catch (ParseException ex) {
            // Handle Exception.
        }

        return targetFormat.format(outPutDate);
    }

//    public static Date formatDateWithDate(String inputDate) {
//
//        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
//        //   originalFormat.applyPattern("dd/MM/yyyy HH:mm:ss a");
//        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date outPutDate = null, date1 = null;
//        try {
//            outPutDate = originalFormat.parse(inputDate);
//            String newDate = targetFormat.format(outPutDate);
//
//            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
//            date1 = formatter1.parse(newDate);
//
//        } catch (ParseException ex) {
//            // Handle Exception.
//        }
//
//        return date1;
//    }

    public static Date stringToDate(String date, String dateFormat) {

        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date returnDate = null;
        try {
            returnDate = format.parse(date);
            //System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDate;
    }

//    public static String dateToString(String date, String dateFormat) {
//
//        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat);
//        String returnDate;
//        //  Date date = new Date();
//        String datetime = dateformat.format(date);
//        //  System.out.println("Current Date Time : " + datetime);
//        return datetime;
//    }

    /**
     * This method identify touch event. If user touches outside of keyboard
     * on screen instead of input control then call hide Keyboard method to hide keyboard.
     *
     * @param view(View) :parent view
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void setupOutSideTouchHideKeyboard(final View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(view);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                setupOutSideTouchHideKeyboard(innerView);
            }
        }
    }

    /**
     * This method hide keyboard
     *
     * @param view contains view, on which touch event has been performed.
     */
    private static void hideKeyboard(View view) {

        InputMethodManager mgr = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void buttonClickEffect(final View v) {
        // v.setEnabled(false);

        AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
        obja.setDuration(5);
        obja.setFillAfter(false);
        v.startAnimation(obja);

    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Kilo Meters
     */
//    public static double getDistance(double lat1, double lat2, double lon1,
//                                     double lon2, double el1, double el2) {
//
//        final int R = 6371; // Radius of the earth
//
//        Double latDistance = Math.toRadians(lat2 - lat1);
//        Double lonDistance = Math.toRadians(lon2 - lon1);
//        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c * 1000; // convert to meters
//
//        double height = el1 - el2;
//
//        distance = Math.pow(distance, 2) + Math.pow(height, 2);
//
//        return Math.sqrt(distance) / 1000;
//    }
    public static String getMinutes(long milliSeconds) {

        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.valueOf(minutes);
    }

    public static String getSeconds(long milliSeconds) {

        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.valueOf(seconds);
    }

    public static String formatTimeSpan(long milliSeconds) {
        long totalSeconds = milliSeconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.UK, "%02d:%02d", minutes, seconds);
    }


    /**
     * This method convert object into a string
     *
     * @param obj(Serializable) : object to be converted in string
     */
    public static String objectToString(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method convert string into object
     *
     * @param str(String) : string to be converted into object
     */
    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
//
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
//                .getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        final float roundPx = pixels;
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//
//        return output;
//    }

    public static void setTypeFace(MenuItem menuItem, Typeface typeface) {

        SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(mNewTitle);
    }

    public static void setProfileImage(Context context, String imagePath, int placeholder, ImageView imgProfilePhoto) {

        if (PrefHelper.getInstance().containKey(PrefHelper.IMAGE_CACHE_FLAG_PROFILE)) {
            cacheProfileFlag = PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, 0);
        } else {
            cacheProfileFlag = 0;
        }


        Glide.with(MyApplication.getInstance()).load(imagePath)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop()
                .signature(new StringSignature(String.valueOf(cacheProfileFlag)))
                .bitmapTransform(new CropCircleTransformation(context))
                //.centerCrop()
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgProfilePhoto);
    }

    public static void setProfileImage(Context context, String imagePath, int placeholder, ImageView imgProfilePhoto,
                                       final ProgressBar progressBar) {
        if (PrefHelper.getInstance().containKey(PrefHelper.IMAGE_CACHE_FLAG_PROFILE)) {
            cacheProfileFlag = PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, 0);
        } else {
            cacheProfileFlag = 0;
        }

        progressBar.setVisibility(View.VISIBLE);
        Glide.with(MyApplication.getInstance()).load(imagePath)
                .signature(new StringSignature(String.valueOf(cacheProfileFlag)))
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //   .override(imgProfilePhoto.getLayoutParams().width, imgProfilePhoto.getLayoutParams().height)
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .bitmapTransform(new CropCircleTransformation(MyApplication.getInstance()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgProfilePhoto);
    }

    public static void setProfileImage(Context context, int resourceId, int placeholder, ImageView imgProfilePhoto) {

        if (PrefHelper.getInstance().containKey(PrefHelper.IMAGE_CACHE_FLAG_PROFILE)) {
            cacheProfileFlag = PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG_PROFILE, 0);
        } else {
            cacheProfileFlag = 0;
        }

        Glide.with(MyApplication.getInstance()).load(resourceId)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(MyApplication.getInstance()))
                .signature(new StringSignature(String.valueOf(cacheProfileFlag)))
                //    .override(imgProfilePhoto.getLayoutParams().width, imgProfilePhoto.getLayoutParams().height)
                // .centerCrop()
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgProfilePhoto);
    }


    public static void setBannerImage(Context context, String imagePath, int placeholder, ImageView imgBannerPhoto) {

        if (PrefHelper.getInstance().containKey(PrefHelper.IMAGE_CACHE_FLAG_BANNER)) {
            cacheBannerFlag = PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG_BANNER, 0);
        } else {
            cacheBannerFlag = 0;
        }

        Glide.with(MyApplication.getInstance()).load(imagePath)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop()
                .signature(new StringSignature(String.valueOf(cacheBannerFlag)))
                //  .override(imgBannerPhoto.getMeasuredWidth(), imgBannerPhoto.getMeasuredHeight())
                // .centerCrop()
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgBannerPhoto);
    }

    public static void setBannerImage(Context context, String imagePath, int placeholder, ImageView imgBannerPhoto,
                                      final ProgressBar progressBar) {

        if (PrefHelper.getInstance().containKey(PrefHelper.IMAGE_CACHE_FLAG_BANNER)) {
            cacheBannerFlag = PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG_BANNER, 0);
        } else {
            cacheBannerFlag = 0;
        }

        progressBar.setVisibility(View.VISIBLE);
        Glide.with(MyApplication.getInstance()).load(imagePath)
                .placeholder(placeholder)
                .error(placeholder)
                .centerCrop()
                .signature(new StringSignature(String.valueOf(cacheBannerFlag)))
                //  .override(imgBannerPhoto.getMeasuredWidth(), imgBannerPhoto.getMeasuredHeight())
                // .centerCrop()
                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgBannerPhoto);
    }

    public static void showSnackBar(Context context, CoordinatorLayout parentView) {

        //  View containerLayout = LayoutInflater.from(context).inflate(R.layout.footer_item, null, false);
        // Create the Snackbar
        snackbar = Snackbar.make(parentView, context.getText(R.string.str_loading), Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        View layout = snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        // textView.setVisibility(View.INVISIBLE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        // Inflate our custom view
        // View snackView = LayoutInflater.from(context).inflate(R.layout.footer_item, null);

        // Add the view to the Snackbar's layout
        // layout.addView(snackView, 0);
        // Show the Snackbar
        if (snackbar != null && !snackbar.isShown())
            snackbar.show();

    }

    public static void dismissSnackBar() {

        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(SystemClock.currentThreadTimeMillis()), null);
        return Uri.parse(path);
    }

    /*public static void UploadPhoto(final Context context, final String SoapMethodName, final HashMap<String, String> params, final String url) {

        // image64Base = Utils.getStringImage(Crop.getOutput(result).getPath());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... param) {

                SoapObject request = new SoapObject(Constants.NAMESPACE, SoapMethodName);

                for (Map.Entry<String, String> e : params.entrySet()) {
                    request.addProperty(e.getKey(), e.getValue());
                    // Object key = e.getKey();
                    // Object value = e.getValue();
                }

               *//* for (int i = 0; i <= params.size(); i++) {
                    request.addProperty(params., ApiList.CLIENT_PROFILE_PIC_UPDATE);
                }
                request.addProperty("op", ApiList.CLIENT_PROFILE_PIC_UPDATE);
                request.addProperty("AuthKey", ApiList.AUTH_KEY);
                request.addProperty("ClientId", String.valueOf(ClientLoginModel.getClientCredentials().getClientId()));
                request.addProperty("ProfilePic", image64Base);*//*

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
                try {
                    androidHttpTransport.call(Constants.SOAP_ACTION + SoapMethodName, envelope);
                    SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
                    String str = result1.toString();
                    Debug.trace("Response", str);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    // ToastHelper.getInstance().showMessage("Failed! Please try again Later");
                }
                // getUserInfo();
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                CustomDialog.getInstance().showProgress(context, "Image Uploading...", false);
            }
        }.execute();
    }*/
}
