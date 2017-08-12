package com.veeritsolution.android.anivethub.customdialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
import com.veeritsolution.android.anivethub.adapter.SpinnerRejectAdapter;
import com.veeritsolution.android.anivethub.enums.CalenderDateSelection;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.models.AppointmentRejectModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ${hitesh} on 12/6/2016.
 */
public class CustomDialog {

    private static final int PREV_MONTH = 1;
    private static CustomDialog ourInstance;
    private Dialog mDialog;
    private float minRate = 0;
    private float maxRate = 0;
    private int position = 0;
    private int sortBy, sortType;
    private int mSelectedYear, mSelectedMonth, mSelectedDay;

    private CustomDialog() {
    }

    public static CustomDialog getInstance() {

        if (ourInstance == null) {
            ourInstance = new CustomDialog();
        }
        return ourInstance;
    }

    public void showProgress(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        // @SuppressLint("InflateParams")
        //  View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_progress_update, null, false);
        mDialog.setContentView(R.layout.custom_dialog_progress_update);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        /* Set Dialog width match parent */
        // mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

       /* TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_customProgressBarTitle);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        mDialogTitle.setText(mTitle);*/

        mDialog.setCancelable(mIsCancelable);

        try {
            if (mDialog != null) {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlert(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_alert);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_alert);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        mDialogTitle.setText(mTitle);

        TextView tvOk = (TextView) mDialog.findViewById(R.id.tv_ok);
        tvOk.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDialog.setCancelable(mIsCancelable);

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }

    public void showTermAndConditions(Context mContext, String mTitle, boolean mIsCancelable, boolean isClient) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
       /* @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_terms_conditions, null, false);*/
        mDialog.setContentView(R.layout.custom_dialog_terms_conditions);

        /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //  mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        Button btnAgree = (Button) mDialog.findViewById(R.id.btn_agree);
        btnAgree.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_termsTitle);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        mDialogTitle.setText(mTitle);

        TextView tvTermsConditions = (TextView) mDialog.findViewById(R.id.tv_terms_conditions);
        tvTermsConditions.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvTermsAndConditions = (TextView) mDialog.findViewById(R.id.tv_termsAndConditions);
        tvTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);
        //TextView tvSpecialTerms = (TextView) view.findViewById(R.id.tv_specialTerms);

        TextView tvPrivacyPolicy = (TextView) mDialog.findViewById(R.id.tv_privacyPolicy);
        tvPrivacyPolicy.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

        if (isClient) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvPrivacyPolicy.setText(Html.fromHtml("<a href=\"http://www.anivethub.com/privacy-policy-client\">Our Privacy Policy</a>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvPrivacyPolicy.setText(Html.fromHtml("<a href=\"http://www.anivethub.com/privacy-policy-client\">Our Privacy Policy</a>"));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvPrivacyPolicy.setText(Html.fromHtml("<a href=\"http://www.anivethub.com/privacy-policy-vet\">Our Privacy Policy</a>", Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvPrivacyPolicy.setText(Html.fromHtml("<a href=\"http://www.anivethub.com/privacy-policy-vet\">Our Privacy Policy</a>"));
            }
        }
        tvPrivacyPolicy.setLinkTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimaryDark, null));
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        tvTermsAndConditions.setLinkTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimaryDark, null));
        tvTermsAndConditions.setText(Html.fromHtml("<a href=\"http://www.anivethub.com/terms-and-conditions\">Terms and Conditions</a>"));
        tvTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_close);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }

    /**
     * This method open Date picker dialog to select Date.
     * This method manages future, past and all Date selection in calender
     * e.g. (1) set future Date limit e.g. event - pass Date[1-31], month [1-12], year[2016-2099]
     * (2) set past Date limit e.g. birthDate, age limit - pass Date[1-31], month [1-12], year[1970-2016]
     *
     * @param context               (Context)  : context
     * @param textView              (TextView)   : to show selected Date
     * @param calenderDateSelection (enum) :  e.g. CALENDER_WITH_PAST_DATE
     * @param year                  (int)     : year e.g. 2016
     * @param month                 (int)     : month e.g. 9
     * @param day                   (int)     : day   e.g. 20
     */
    public void showDatePickerDialog(final Context context, final TextView textView, final CalenderDateSelection calenderDateSelection, int year, int month, int day) {

        final Calendar mCurrentDate = Calendar.getInstance();

        final Calendar minDate = Calendar.getInstance();

        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                mSelectedYear = selectedyear;
                mSelectedMonth = selectedmonth;
                mSelectedDay = selectedday;

                mCurrentDate.set(mSelectedYear, mSelectedMonth, mSelectedDay);
                textView.setText(Utils.dateFormat(mCurrentDate.getTimeInMillis(), Constants.DATE_MM_DD_YYYY));
                /* it is used to pass selected Date in millisecond*/
                textView.setTag(mCurrentDate.getTimeInMillis());
            }
        }, mYear, mMonth, mDay);


        switch (calenderDateSelection) {

            case CALENDER_WITH_ALL_DATE:

                break;
            case CALENDER_WITH_PAST_DATE:

                minDate.set(Calendar.YEAR, year);
                minDate.set(Calendar.MONTH, month - PREV_MONTH);
                minDate.set(Calendar.DAY_OF_MONTH, day);

                mDatePicker.getDatePicker().setMinDate(minDate.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                break;
            case CALENDER_WITH_FUTURE_DATE:

                minDate.set(Calendar.YEAR, year);
                minDate.set(Calendar.MONTH, month - PREV_MONTH);
                minDate.set(Calendar.DAY_OF_MONTH, day);

                mCurrentDate.set(Calendar.DAY_OF_MONTH, mDay);

                mDatePicker.getDatePicker().setMinDate(mCurrentDate.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(minDate.getTimeInMillis());
                break;
        }

        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                    textView.setText("");
                    //onBackPressed();
                }
            }
        });
       /* mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    dialog.dismiss();
                    mCurrentDate.set(mSelectedYear, mSelectedMonth, mSelectedDay);
                    textView.setText(Utils.dateFormat(mCurrentDate.getTimeInMillis(), Constants.DATE_MM_DD_YYYY));
                *//* it is used to pass selected Date in millisecond*//*
                    textView.setTag(mCurrentDate.getTimeInMillis());

                }
            }
        });*/
//        mDatePicker.setTitle(context.getString(R.string.str_select_date));
        mDatePicker.setCanceledOnTouchOutside(false);

        try {
            if (!mDatePicker.isShowing()) {
                mDatePicker.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showActionDialog(Context mContext, String mTitle, String messaage, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        // @SuppressLint("InflateParams")
        // View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_action_dialog, null, false);
        mDialog.setContentView(R.layout.custom_dialog_action_dialog);

        /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_actionTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        TextView tvMessage = (TextView) mDialog.findViewById(R.id.tv_actionMessage);
        tvMessage.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvMessage.setText(messaage);

        TextView tvOK = (TextView) mDialog.findViewById(R.id.btn_actionOk);
        tvOK.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView tvCancel = (TextView) mDialog.findViewById(R.id.btn_actionCancel);
        tvCancel.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {
            if (mDialog != null) {
                if (!isDialogShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void showImageSelect(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_select_image, null, false);
        mDialog.setContentView(R.layout.custom_dialog_select_image);

         *//* Set Dialog width match parent *//*
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle, tvCamera, tvGallery;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_selectImageTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        tvCamera = (TextView) mDialog.findViewById(R.id.tv_camera);
        tvCamera.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvGallery = (TextView) mDialog.findViewById(R.id.tv_gallery);
        tvGallery.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        try {
            if (mDialog != null) {
                if (!isDialogShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void showSortDialog(final Context mContext, String mTitle, boolean mIsCancelable,
                               final SearchVetModel searchVetModel, View.OnClickListener onClickListener, boolean isVet) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        // @SuppressLint("InflateParams")
        // View view = LayoutInflater.from(mContext).inflate(R.layout.activity_custom_sort_dialog, null, false);
        mDialog.setContentView(R.layout.activity_custom_sort_dialog);
       /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //  mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);
        mDialog.getWindow().setGravity(Gravity.CENTER);

        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_sortCancel);
        imgCancel.setOnClickListener(onClickListener);

        TextView tvTitle, tvSortBy, tvSortType;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_search_sortBy);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        tvSortBy = (TextView) mDialog.findViewById(R.id.tvSortBy);
        tvSortType = (TextView) mDialog.findViewById(R.id.tvSortType);

        tvSortBy.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvSortType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        Spinner spSortBy, spSortType;

        spSortBy = (Spinner) mDialog.findViewById(R.id.sp_sortBy);
        spSortType = (Spinner) mDialog.findViewById(R.id.sp_sortType);

        SpinnerAdapter adpSortBy, adpSortType;
        List<String> sortBtyList, sortTypeList;

        sortBtyList = new ArrayList<>();
        sortBtyList.add("Select sort by");
        if (isVet) {
            sortBtyList.add("Veterinary Surgeon Name");
        } else {
            sortBtyList.add("Veterinary Surgeon Practice Name");
        }

        sortBtyList.add("Session Rate");
        sortBtyList.add("Rating");
        sortBtyList.add("Distance");

        sortTypeList = new ArrayList<>();
        sortTypeList.add("Select sort type");
        sortTypeList.add("Ascending");
        sortTypeList.add("Descending");

        adpSortBy = new SpinnerAdapter(mContext, R.layout.spinner_row_list, sortBtyList);
        spSortBy.setAdapter(adpSortBy);

        adpSortType = new SpinnerAdapter(mContext, R.layout.spinner_row_list, sortTypeList);
        spSortType.setAdapter(adpSortType);

        searchVetModel.setSortBy(0);
        searchVetModel.setSortType(0);

        spSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBy = position;
                searchVetModel.setSortBy(sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortType = 0;
                    searchVetModel.setSortType(0);
                } else if (position == 1) {
                    sortType = 0;
                    searchVetModel.setSortType(0);
                } else {
                    sortType = 1;
                    searchVetModel.setSortType(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnSortSearch = (Button) mDialog.findViewById(R.id.btn_sortSearch);
        btnSortSearch.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnSortSearch.setOnClickListener(onClickListener);
        btnSortSearch.setTag(searchVetModel);

        try {
            if (mDialog != null) {
                if (!isDialogShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFilterDialog(final Context mContext, String mTitle, boolean mIsCancelable,
                                 final SearchVetModel searchVetModel, View.OnClickListener onClickListener, boolean isVet) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_custom_filter_dialog, null, false);
        mDialog.setContentView(view);
      /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //  mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);
        Utils.setupOutSideTouchHideKeyboard(view);
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_filterCancel);
        imgCancel.setOnClickListener(onClickListener);

        TextView tvTitle, tvStatus, tvOnline, tvBusy, tvOffline, tvSessionRate, tvDistance, tvLabelVetName;
        final EditText edtVetName;

        LinearLayout linOnlineStatus = (LinearLayout) mDialog.findViewById(R.id.lin_onlineStatus);

        edtVetName = (EditText) mDialog.findViewById(R.id.edt_vetName);
        edtVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvLabelVetName = (TextView) mDialog.findViewById(R.id.tv_labelVetName);
        tvLabelVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (isVet) {
            tvLabelVetName.setText(mContext.getString(R.string.str_vet_name));
            linOnlineStatus.setVisibility(View.VISIBLE);
        } else {
            tvLabelVetName.setText(mContext.getString(R.string.str_vet_practise_name));
            linOnlineStatus.setVisibility(View.GONE);
        }
        tvTitle = (TextView) mDialog.findViewById(R.id.tv_search_filterBy);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        tvStatus = (TextView) mDialog.findViewById(R.id.tv_status);
        tvStatus.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvOnline = (TextView) mDialog.findViewById(R.id.tv_online);
        tvOnline.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvBusy = (TextView) mDialog.findViewById(R.id.tv_busy);
        tvBusy.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvOffline = (TextView) mDialog.findViewById(R.id.tv_offline);
        tvOffline.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionRate = (TextView) mDialog.findViewById(R.id.tv_session_rate);
        tvSessionRate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvDistance = (TextView) mDialog.findViewById(R.id.tv_distance);
        tvDistance.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        // tvCityTown = (TextView) mDialog.findViewById(R.id.tvCityTown);
        // tvCityTown.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        // EditText edtVetName = (EditText) view.findViewById(R.id.edt_vetName);

        final CheckBox chkOnLine, chkOffLine, chkBusy, chkIsVet, chkIsVetPractise;
        chkOnLine = (CheckBox) mDialog.findViewById(R.id.chk_online);
        chkOffLine = (CheckBox) mDialog.findViewById(R.id.chk_Offline);
        chkBusy = (CheckBox) mDialog.findViewById(R.id.chk_busy);
      /*  chkIsVet = (CheckBox) mDialog.findViewById(R.id.chk_isVet);
        chkIsVet.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        chkIsVetPractise = (CheckBox) mDialog.findViewById(R.id.chk_isVetPractise);
        chkIsVetPractise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
*/
        final TextView tvMinPrice, tvMaxPrice, tvMinDistance, tvMaxDistance;
        tvMinDistance = (TextView) mDialog.findViewById(R.id.tv_mimDistance);
        tvMinDistance.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvMaxDistance = (TextView) mDialog.findViewById(R.id.tv_maxDistance);
        tvMaxDistance.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvMinPrice = (TextView) mDialog.findViewById(R.id.tv_mimPrice);
        tvMinPrice.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvMaxPrice = (TextView) mDialog.findViewById(R.id.tv_maxPrice);
        tvMaxPrice.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        CrystalRangeSeekbar rangPrice, rangeDistance;
        rangPrice = (CrystalRangeSeekbar) mDialog.findViewById(R.id.range_price);

        if (searchVetModel != null) {

            if (searchVetModel.getIsOnline() == 0) {
                chkOnLine.setChecked(false);
            } else {
                chkOnLine.setChecked(true);
            }

            if (searchVetModel.getIsBusy() == 0) {
                chkBusy.setChecked(false);
            } else {
                chkBusy.setChecked(true);
            }

            if (searchVetModel.getIsOffline() == 0) {
                chkOffLine.setChecked(false);
            } else {
                chkOffLine.setChecked(true);
            }

            rangPrice.setMinValue(searchVetModel.getMinSessionRate());
            rangPrice.setMaxValue(searchVetModel.getMaxSessionRate());
            rangPrice.setLeft((int) searchVetModel.getMinRate());
            rangPrice.setRight((int) searchVetModel.getMaxRate());

            minRate = (int) searchVetModel.getMinRate();
            maxRate = (int) searchVetModel.getMaxRate();
        }
        rangPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minRate = minValue.floatValue();
                tvMinPrice.setText(String.valueOf(minRate));
                maxRate = maxValue.floatValue();
                tvMaxPrice.setText(String.valueOf(maxRate));
            }
        });

        rangeDistance = (CrystalRangeSeekbar) mDialog.findViewById(R.id.range_kilometer);
        rangeDistance.setMinValue((float) searchVetModel.getMinDistance());
        rangeDistance.setMaxValue((float) searchVetModel.getMaxDistance());

        rangeDistance.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                tvMinDistance.setText(String.valueOf(minValue.floatValue()));
                tvMaxDistance.setText(String.valueOf(maxValue.floatValue()));
                searchVetModel.setMinDistance(minValue.floatValue());
                searchVetModel.setMaxDistance(maxValue.floatValue());

            }
        });

        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radioGroup_distance);
        RadioButton rbKilometer/*, rbMeter*/;

        rbKilometer = (RadioButton) mDialog.findViewById(R.id.rb_kilometer);
        rbKilometer.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        // rbMeter = (RadioButton) mDialog.findViewById(R.id.rb_meter);
        // rbMeter.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        Button btnFilterSearch = (Button) mDialog.findViewById(R.id.btn_filterSearch);
        btnFilterSearch.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnFilterSearch.setOnClickListener(onClickListener);

        chkOnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchVetModel.setIsOnline(1);
                } else {
                    searchVetModel.setIsOnline(0);
                }
            }
        });

        chkBusy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchVetModel.setIsBusy(1);
                } else {
                    searchVetModel.setIsBusy(0);
                }
            }
        });

        chkOffLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchVetModel.setIsOffline(1);
                } else {
                    searchVetModel.setIsOffline(0);
                }
            }
        });

       /* chkIsVet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchVetModel.setIsVet(1);
                } else {
                    searchVetModel.setIsVet(0);
                }
            }
        });

        chkIsVetPractise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchVetModel.setIsVetPractise(1);
                } else {
                    searchVetModel.setIsVetPractise(0);
                }
            }
        });*/

        edtVetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchVetModel.setVetName(edtVetName.getText().toString().trim());
            }
        });

        btnFilterSearch.setTag(searchVetModel);

        try {
            if (mDialog != null) {
                if (!isDialogShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ShowTimePickerDialog(Context context, final TextView textView) {

        Calendar mCurrentTime = Calendar.getInstance();

        final int[] hour = new int[1];
        final int[] minute = new int[1];

        hour[0] = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        minute[0] = mCurrentTime.get(Calendar.MINUTE);

        final TimePickerDialog mTimePicker = new TimePickerDialog(context, R.style.DatePickerDialogTheme,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hour[0] = selectedHour;
                minute[0] = selectedMinute;
                textView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour[0], minute[0], true);//Yes 24 hour time

        // mTimePicker.setTitle("Select Time");

        /*mTimePicker.setButton(DialogInterface.BUTTON_POSITIVE, "SET", new DialogInterface.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mTimePicker.dismiss();
                    textView.setText(String.format("%02d:%02d", hour[0], minute[0]));
                    // textView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                }
            }
        });*/
        mTimePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    mTimePicker.dismiss();
                    textView.setText("");
                }
            }
        });

        mTimePicker.setCanceledOnTouchOutside(false);
        try {
            if (!mTimePicker.isShowing()) {
                mTimePicker.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAppointmentStatus(Context mContext, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        // @SuppressLint("InflateParams")
        // View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_appointment_status, null, false);
        mDialog.setContentView(R.layout.custom_dialog_appointment_status);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_actionTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        RadioGroup rg = (RadioGroup) mDialog.findViewById(R.id.rbgroup_status);
        final RadioButton rbapprove = (RadioButton) mDialog.findViewById(R.id.rbApprove);
        rbapprove.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        final RadioButton rbreject = (RadioButton) mDialog.findViewById(R.id.rbReject);
        rbreject.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        final Button ok = (Button) mDialog.findViewById(R.id.btn_statusOk);
        ok.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        ok.setTag(1);

        final Button cancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        cancel.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == rbapprove.getId()) {
                    ok.setTag(1);
                } else {
                    ok.setTag(2);
                }
            }
        });

        //TextView mDialogTitle = (TextView) view.findViewById(R.id.tv_timeSlot1);

        mDialog.setCancelable(mIsCancelable);

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }

    public void showTimeSubSlot(Context mContext, boolean mIsCancelable, final List<TimeSlotModel> listTimeslot) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        // @SuppressLint("InflateParams")
        // View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_appointment_time_slot, null, false);
        mDialog.setContentView(R.layout.custom_dialog_appointment_time_slot);
       /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //  mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_actionTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        RadioGroup rg = (RadioGroup) mDialog.findViewById(R.id.rg);

        RadioButton timeslotfirst = (RadioButton) mDialog.findViewById(R.id.timeslot1);
        timeslotfirst.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        RadioButton timeslotsec = (RadioButton) mDialog.findViewById(R.id.timeslot2);
        timeslotsec.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        RadioButton timeslotthird = (RadioButton) mDialog.findViewById(R.id.timeslot3);
        timeslotthird.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        RadioButton timeslotforth = (RadioButton) mDialog.findViewById(R.id.timeslot4);
        timeslotforth.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        timeslotfirst.setText(listTimeslot.get(0).getTimeSlotName());
        timeslotsec.setText(listTimeslot.get(1).getTimeSlotName());
        timeslotthird.setText(listTimeslot.get(2).getTimeSlotName());
        timeslotforth.setText(listTimeslot.get(3).getTimeSlotName());

        final Button btnok = (Button) mDialog.findViewById(R.id.btn_timeSlotOk);
        btnok.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        Button btncancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        btncancel.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.timeslot1) {
                    btnok.setTag(listTimeslot.get(0).getTimeSlotId());
                } else if (i == R.id.timeslot2) {
                    btnok.setTag(listTimeslot.get(1).getTimeSlotId());
                } else if (i == R.id.timeslot3) {
                    btnok.setTag(listTimeslot.get(2).getTimeSlotId());
                } else if (i == R.id.timeslot4) {
                    btnok.setTag(listTimeslot.get(3).getTimeSlotId());
                }

            }
        });
        mDialog.setCancelable(mIsCancelable);

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }

    }

    public void showRejectAppointment(Context mContext, boolean mIsCancelable, List<AppointmentRejectModel> rejectModelList,
                                      AppointmentModel appointmentModel) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_appointment_rejection, null, false);

        Utils.setupOutSideTouchHideKeyboard(view);

        mDialog.setContentView(R.layout.custom_dialog_appointment_rejection);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        ImageView imgClientProfilePic = (ImageView) mDialog.findViewById(R.id.img_clientProfilePic);

        Utils.setProfileImage(mContext, appointmentModel.getClientProfilePic(), R.drawable.img_client_profile, imgClientProfilePic);

        Spinner sp = (Spinner) mDialog.findViewById(R.id.sp_Rejection);
        final Button submit = (Button) mDialog.findViewById(R.id.btn_submit_rejection);
        submit.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView tvClientName = (TextView) mDialog.findViewById(R.id.tv_clientname_reject);
        tvClientName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvAppointmentDate = (TextView) mDialog.findViewById(R.id.tv_appointment_date);
        tvAppointmentDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvAppointmentTime = (TextView) mDialog.findViewById(R.id.tv_Appointment_time);
        tvAppointmentTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvReasonOfCallRejection = (TextView) mDialog.findViewById(R.id.tv_reason_of_call_rejection);
        tvReasonOfCallRejection.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvOtherReason = (TextView) mDialog.findViewById(R.id.tv_other_reason);
        tvOtherReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvClientUserCode = (TextView) mDialog.findViewById(R.id.tv_appointmentCode_reject);
        tvClientUserCode.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvStatus = (TextView) mDialog.findViewById(R.id.tv_status_reject);
        tvStatus.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvDateTime = (TextView) mDialog.findViewById(R.id.tv_appointmentDate);
        tvDateTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvTime = (TextView) mDialog.findViewById(R.id.tv_appointmentTime);
        tvTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        EditText edtOtherResason = (EditText) mDialog.findViewById(R.id.edt_rejection_remark);
        edtOtherResason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvClientName.setText(appointmentModel.getClientName());
        tvClientUserCode.setText(appointmentModel.getAppointmentNo());
        tvStatus.setText(String.valueOf(appointmentModel.getAppointmentStatus()));
        tvDateTime.setText(appointmentModel.getAppointmentDate());
        tvTime.setText(appointmentModel.getFromTime() + "-" + appointmentModel.getToTime());

        int Status = appointmentModel.getAppointmentStatus();

        if (Status == Constants.APPROVED) {

            String dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                    + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
            tvDateTime.setText(appointmentModel.getAppointmentDateDisp());
            tvTime.setText(appointmentModel.getTimeSlotName());
            tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
            tvStatus.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.green, null));

        } else if (Status == Constants.REJECTED) {

            tvDateTime.setText(appointmentModel.getAppointmentDateDisp());
            tvTime.setText("");
            tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
            tvStatus.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.orange, null));

        } else if (Status == Constants.CANCELLED) {

            tvDateTime.setText(appointmentModel.getAppointmentDateDisp());
            String dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                    + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
            tvTime.setText(appointmentModel.getTimeSlotName());

            tvStatus.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.red, null));

        } else if (Status == Constants.REQUESTED) {

            tvDateTime.setText(appointmentModel.getAppointmentDateDisp());
            tvTime.setText(appointmentModel.getTimeSlotName());
            tvStatus.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, null));
            tvStatus.setText(mContext.getString(R.string.str_new));

        } else if (Status == Constants.READY_TO_CALL) {

            tvDateTime.setText(appointmentModel.getAppointmentDateDisp());
            tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
            tvStatus.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));

        }
        AppointmentRejectModel appointmentRejectModel = new AppointmentRejectModel();
        appointmentRejectModel.setRejectReasonName("Select Reject Reason");
        rejectModelList.add(0, appointmentRejectModel);

        SpinnerRejectAdapter spAdapter = new SpinnerRejectAdapter(mContext, R.layout.spinner_row_list, rejectModelList);
        sp.setAdapter(spAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                AppointmentRejectModel appointmentRejectModel = (AppointmentRejectModel) view.getTag();
                submit.setTag(appointmentRejectModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // mDialog.setCancelable(mIsCancelable);

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }


    public void showVideoCallRejectDialog(Context mContext, boolean mIsCancelable, List<AppointmentRejectModel> rejectModelList) {
        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_videocall_rejection, null, false);
        mDialog.setContentView(view);
        Utils.setupOutSideTouchHideKeyboard(view);
         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        Spinner sp = (Spinner) mDialog.findViewById(R.id.sp_Rejection);
        // final AppointmentRejectModel[] appointmentRejectModel = new AppointmentRejectModel[1];

        final Button submit = (Button) mDialog.findViewById(R.id.btn_submit_rejection);
        submit.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView tvReasonOfCallRejection = (TextView) mDialog.findViewById(R.id.tv_reason_of_call_rejection);
        tvReasonOfCallRejection.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvOtherReason = (TextView) mDialog.findViewById(R.id.tv_other_reason);
        tvOtherReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        SpinnerRejectAdapter spAdapter = new SpinnerRejectAdapter(mContext, R.layout.spinner_row_list, rejectModelList);
        sp.setAdapter(spAdapter);

        final AppointmentRejectModel appointmentRejectModel = rejectModelList.get(sp.getSelectedItemPosition());
        final EditText edtOtherReason = (EditText) mDialog.findViewById(R.id.edt_rejection_remark);
        edtOtherReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        edtOtherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                appointmentRejectModel.setRejectionRemarks(edtOtherReason.getText().toString().trim());
            }
        });
        //appointmentRejectModel.setRejectionRemarks(edtOtherReason.getText().toString().trim());
        submit.setTag(appointmentRejectModel);
        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }

    public void showAddBreed(Context mContext, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_add_breed, null, false);
        mDialog.setContentView(view);
        Utils.setupOutSideTouchHideKeyboard(view);
         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        final Button submit = (Button) mDialog.findViewById(R.id.btn_submit_addBreed);
        submit.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        final EditText edtOtherReason = (EditText) mDialog.findViewById(R.id.edt_addPetBreed);
        edtOtherReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        edtOtherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String msg = edtOtherReason.getText().toString().trim();
                if (msg.isEmpty()) {
                    submit.setTag(null);
                } else {
                    submit.setTag(msg);
                }
                submit.setTag(edtOtherReason.getText().toString().trim());
            }
        });
        //appointmentRejectModel.setRejectionRemarks(edtOtherReason.getText().toString().trim());
        //submit.setTag(appointmentRejectModel);
        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }
    }

    public void showTicketDialog(Context mContext, boolean mIsCancelable, String title) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        mDialog.setContentView(R.layout.custom_dialog_cretae_ticket);

        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = (TextView) mDialog.findViewById(R.id.tv_dialogTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(title);

       /* TextView tvReason = (TextView) mDialog.findViewById(R.id.tv_reason);
        tvReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);*/

        final Button btnSubmit = (Button) mDialog.findViewById(R.id.btn_submit_ticket);
        btnSubmit.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        final EditText edtTicketReason = (EditText) mDialog.findViewById(R.id.edt_ticket_reason);
        edtTicketReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtTicketReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String msg = edtTicketReason.getText().toString().trim();
                if (msg.isEmpty()) {
                    btnSubmit.setTag(null);
                } else {
                    btnSubmit.setTag(msg);
                }

            }
        });

        mDialog.setCancelable(mIsCancelable);

        if (mDialog != null) {
            if (!isDialogShowing()) {
                mDialog.show();
            }
        }

    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (isDialogShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return (boolean) : return true or false, if the dialog is showing or not
     */
    public boolean isDialogShowing() {

        return mDialog != null && mDialog.isShowing();
    }

}
