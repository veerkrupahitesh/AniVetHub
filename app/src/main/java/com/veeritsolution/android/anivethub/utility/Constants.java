package com.veeritsolution.android.anivethub.utility;

import com.veeritsolution.android.anivethub.MyApplication;

/**
 * Created by admin on 12/14/2015
 */
public class Constants {

    /**
     * Vet session rate add, edit related keys and flags
     */
    public static final String VET_SESSION_RATE_DATA = "vetSessionRateData";
    public static final int VET_SESSION_RATE_FLAG_ADD = 1;
    public static final int VET_SESSION_RATE_FLAG_EDIT = 2;
    public static final String SESSION_DATA = "sessionData";


    /**
     * for LOGIN state check eg. CLIENT,VET and CLINIC login in app
     */
    public static final int CLIENT_LOGIN = 1;
    public static final int VET_LOGIN = 2;
    public static final int CLINIC_LOGIN = 3;


    /**
     * for manging state of All fragments
     */
    public static final int FROM_SIGN_UP = 1;
    public static final int FROM_EDIT = 2;
    public static final int FROM_ADD = 3;
    public static final String ADD_EDIT_SINGUP = "addEditSignUp";


    /**
     * Error code if date not found in database in from API
     */
    public static final int NO_DATA_FOUND = 999;

    /**
     * Constant used in the location settings dialog.
     */
    public static final int REQUEST_CHECK_SETTINGS = 1;
    /*The desired interval for location updates. Inexact. Updates may be more or less frequent.*/
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000/* * 60*/;
    /*The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.*/
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /*for checking location permission*/
    public static final int REQUEST_LOCATION = 9;

    /*Appointment status*/
    public static final int REQUESTED = 0;
    public static final int APPROVED = 1;
    public static final int REJECTED = 2;
    public static final int CANCELLED = 3;
    public static final int READY_TO_CALL = 4;
    public static final int ARCHIVED = 5;
    public static final int APPOINTMENT_CANCEL = 6;
    /**
     * Taking photos from gallery and camera with different request code
     */
    public static final int REQUEST_CAMERA_PROFILE = 1;
    public static final int REQUEST_CAMERA_BANNER = 2;
    public static final int REQUEST_FILE_PROFILE = 3;
    public static final int REQUEST_FILE_BANNER = 4;

    /**
     * to save the loginUser type
     */
    public static final int VET_DATA_ID = 1;
    public static final int CLINIC_DATA_ID = 1;
//    public static final int CLIENT_DATA_ID = 2;

    /**
     * social login keys for getting data
     */
    public static final String USERNAME = "UserName";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
//    public static final String MOBILE_NUMBER = "mobileNumber";
//    public static final String SELECT_ROLE = "selectRole";


    public static final String REGISTER_BY = "registerBy";

    public static final int DATA_EXISTS = 1;

    public static final String IS_FROM_SIGN_UP = "IsFromSignUp";


    /*Vet related data*/
    public static final String VET_ID = "vetId";
    public static final String VET_EDUCATION = "vetEducation";
    public static final String VET_EXPERTISE = "vetExpertise";
    public static final String VET_EXPERIENCE = "vetExperience";

//    public static final String VET_CLINIC = "vetClinic";

    public static final String PET_DATA = "petData";

    /**
     * Vet online status
     */
    public static final int OFFLINE_STATUS = 0;
    public static final int ONLINE_STATUS = 1;
    public static final int BUSY_STATUS = 2;

//    public static final String VET_CLINIC_TIME = "vetClinicTime";

    public static final String IS_FROM_VIDEO_CALL = "isFromVideoCall";

    public static final String VET_DATA = "vetData";


//    public static final String CLINIC_DATA = "clinicData";

    public static final String PET_SYMPTOMS_DATA = "petSymptomsData";

//    public static final String GENERAL_SETTING_DATA = "generalSettingData";


    /*Type face constants in whole project */
    public static final String FONT_ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    public static final String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String FONT_ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
//    public static final String FONT_ROBOTO_BOLD_CONDENSE = "fonts/Roboto-BoldCondensed.ttf";
//    public static final String FONT_ROBOTO_BOLD_CONDENSE_ITALIC = "fonts/Roboto-BoldCondensedItalic.ttf";
//    public static final String FONT_ROBOTO_BOLD_ITALIC = "fonts/Roboto-BoldItalic.ttf";
//    public static final String FONT_ROBOTO_CONDENSE = "fonts/Roboto-Condensed.ttf";
//    public static final String FONT_ROBOTO_CONDENSE_ITALIC = "fonts/Roboto-CondensedItalic.ttf";
//    public static final String FONT_ROBOTO_ITALIC = "fonts/Roboto-Italic.ttf";
//    public static final String FONT_ROBOTO_LIGHT_ITALIC = "fonts/Roboto-LightItalic.ttf";
//    public static final String FONT_ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
//    public static final String FONT_ROBOTO_MEDIUM_ITALIC = "fonts/Roboto-MediumItalic.ttf";
//    public static final String FONT_ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
//    public static final String FONT_ROBOTO_THIN_ITALIC = "fonts/Roboto-ThinItalic.ttf";
//    public static final String FONT_ROBOTO_BLACK = "fonts/Roboto-Black.ttf";
//    public static final String FONT_ROBOTO_BLACK_ITALIC = "fonts/Roboto-BlackItalic.ttf";

    /**
     * gender distribution constants
     */
    public static final int MALE_ENTIRE = 1;
    public static final int FEMALE_ENTIRE = 2;
    public static final int MALE_NEUTERED = 3;
    public static final int FEMALE_NEUTERED = 4;

    /**
     * date and time formats
     */
    public static final String MM_DD_YYYY_HH_MM_SS_A = "MM/dd/yyyy HH:mm:ss a";
    public static final String DATE_YYYY = "yyyy";
    public static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
//    public static final String HH_MM_SS = "HH:mm:ss";
//    public static final String DATE_DD_MM_YYYY = "dd/MM/yyyy";
//    public static final String DATE_FORMAT = "dd/MM/yyyy";
//    public static final int DEFAULT_CALENDER_YEAR = 1970;
//    public static final int DEFAULT_CALENDER_MONTH = 1;
//    public static final int DEFAULT_CALENDER_DATE = 1;


    public static final String APPOINTMENT_DATA = "appointmentData";
    public static final int APPOINTMENT_APPROVE_STATUS = 0;
//    public static final String VET_APPOINTMENT_ID = "vetAppointmentId";

    public static final String DATA_ID = "DataId";

    public static final String USER_DATA = "userData";

    public static final String TERMS_AND_POLICY = "http://www.anivethub.com/terms-and-conditions";

    public static final int DEFAULT_VALUE_DEVICE_TYPE_ANDROID = 1;

    /**
     * filter search vet with following criteria
     */
    public static final int NORMAL_SEARCH_VET = 1;
    public static final int FILTER_SEARCH_VET = 2;
    public static final int SORT_SEARCH_VET = 3;


    /**
     * API request code to upload photos to the server
     */
    public static final int API_REQUEST_PROFILE_CAMERA = 1;
    public static final int API_REQUEST_PROFILE_FILE = 2;
    public static final int API_REQUEST_BANNER_CAMERA = 3;
    public static final int API_REQUEST_BANNER_FILE = 4;

    /**
     * Support center flags
     */
    public static final int CLIENT_TICKET_FLAG = 1;
    public static final int VET_TICKET_FLAG = 2;
    public static final int GENERATE_TICKET = 1;


    public static final String FLAG = "flag";

    public static final String PET_TREATMENT_DATA = "petTreatmentData";

    /**
     * Sinch environment credentials
     */
    public static final String APP_KEY = "78030c9f-ddfb-4b8a-9f10-4651ecfca958";
    public static final String APP_SECRET = "zK4SAegqcE6+7bkgu/OqEA==";
    public static final String ENVIRONMENT = "sandbox.sinch.com";
    public static final String CALL_ID = "CALL_ID";


    /**
     * reply by vet , client or admin in support center listing
     */
    public static final int CLIENT_SUPPORT_CENTER_ID = 1;
    public static final int VET_SUPPORT_CENTER_ID = 2;
    public static final int ADMIN_SUPPORT_CENTER_ID = 3;

    public static final String VET_PRACTICE_ID = "vetPractiseId";

    /**
     * Soap request parameters
     */
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String CLIENT_PROFILE_METHOD_NAME = "ClientProfilePicUpdate";
    public static final String CLIENT_BANNER_METHOD_NAME = "ClientBannerPicUpdate";
    public static final String SOAP_ACTION = "http://tempuri.org/";
    public static final String VET_PROFILE_METHOD_NAME = "VetProfilePicUpdate";
    public static final String VET_BANNER_METHOD_NAME = "VetBannerPicUpdate";
    public static final String CLIENT_PET_METHOD_NAME = "ClientPetPicsInsert";

    /**
     * notification related parameters
     */
    public static final String MESSAGE = "message";
    //    public static final String KEY_NOTIFICATION_MESSAGE = "message";
    public static final String KEY_NOTIFICATION_MESSAGE_DATA = "notificationMessageData";
    public static final String KEY_SHOW_POP_UP = "showPopUp";
    public static final String KEY_VET_APPOINTMENT_ID = "VetAppointmentId";
    public static final String KEY_APP_ICON_PATH = "AppIconPath";
    public static final String KEY_IMAGEPATH = "ImagePath";
    public static final String KEY_NOTIFICATION_TYPE = "NotificationType";
    public static final String KEY_FROM_NOTIFICATION = "fromNotification";
    public static final int NOTIFICATION_REQUEST_CODE = 1;
    public static final String DEBUG_KEY_FIREBASE_DEVICE_TOKEN = "firebaseTokenKey";

    /**
     * Paypal related constants
     */
    // public static final int PAYPAL_BUTTON_ID = R.id.btn_save;
    public static final String PAYPAL_APP_ID_SANDBOX = "APP-80W284485P519543T";
    public static final String PAYPAL_APP_ID_LIVE = "APP-9W545554N98801148";

    //public static final String PAYPAL_CLIENT_ID = "AUOSNTEHZP_8TcZhN2huz2de15-0g7Ah3HHtGYt-wFHQE0JDN0-qJaoXRzSSngnk1Id8PyMeamjuKCqV";

    public static final int PHONE_LENGTH = 10;
    public static final int PINCODE_LENGTH = 4;
    public static final java.lang.String TAG = MyApplication.TAG;
    public static int VET_ACCEPTED = 1;
    public static int VET_REJECTED = 2;
}
