package com.veeritsolution.android.anivethub.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.enums.RegisterBy;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by ${hitesh} on 08/08/2016.
 */
public class SignInActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // class object declaration
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 9001;
    public static GoogleApiClient mGoogleApiClient;
    private static CallbackManager callbackManager;
    // private final int DEFAULT_AUTH_FACEBOOK_REQUEST_CODE = 64206;
    private final String FB_REQUEST_PARAMETER = "id, first_name, last_name, email,gender, birthday, location";
    private final String FIELDS = "fields";

    // xml components
    private EditText edtUserName, edtpwd;
    private Button btnLogin, btnSignup/*, gmailLogin, fblogin*/;
    private TextView forgotpwd, tvHeader;
    private CheckBox chkRememberMe;
    private Toolbar toolbar;

  //  private Map<String, String> params;
    //private ClientLoginModel clientLoginModel;
    private Intent intent;
    private Bundle bundle;
    private String password, userName;
    /*
     * Extended permissions give access to more sensitive information and allows the app to publish and delete data.
     * Provides the ability to publish Posts, Open Graph actions, achievements, scores and other activity on behalf of the user.
     * */
    private List<String> permissionNeeds = Collections.singletonList("publish_actions");
    private List<String> accessUserDetailPermission = Arrays.asList("public_profile", "email");
    private AccessToken FBAccessToken;

    public static void initGoogleSdk() {
        // [START configure_signin]
        // Configure sign-in to request the userName's ID, email address, and basic
        // fragment_vet_profile. ID and basic fragment_vet_profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                // .requestServerAuthCode(getString(R.string.str_google_server_key))
                .requestIdToken(MyApplication.getInstance().getString(R.string.str_google_server_web_key))
                .requestProfile()
                //  .requestScopes(new Scope(Constants.SCOPE))
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.getInstance())
                // .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]
    }

    public static void initFacebookSdk() {
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(MyApplication.getInstance());
        AppEventsLogger.activateApp(MyApplication.getInstance());
        callbackManager = CallbackManager.Factory.create();
        Utils.printFbKeyHash();
    }
    // [END signInToGoogle]

    // [START signOut]
    public static void signOutToGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);/*.setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        if (status.isSuccess()) {

                        }
                    }
                });*/
    }

    /**
     * This method destroy the current access token.
     */
    public static void logoutToFacebook() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebookSdk();
        initGoogleSdk();
        setContentView(R.layout.activity_sign_in);

        init();

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText("Login");

        edtUserName = (EditText) findViewById(R.id.edt_userName);
        edtUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        edtpwd = (EditText) findViewById(R.id.edt_password);
        edtpwd.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        btnSignup = (Button) findViewById(R.id.btn_signUp);
        btnSignup.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        //gmailLogin = (Button) findViewById(R.id.btn_google);
        //fblogin = (Button) findViewById(R.id.btn_faceBook);

        forgotpwd = (TextView) findViewById(R.id.tv_forgotPassword);
        forgotpwd.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        chkRememberMe = (CheckBox) findViewById(R.id.chk_rememberMe);
        chkRememberMe.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

       // params = new HashMap<>();
      //  params.put("AuthKey", ApiList.AUTH_KEY);
        Utils.setupOutSideTouchHideKeyboard(findViewById(R.id.activity_sign_in));
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetUser:

                if (chkRememberMe.isChecked()) {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                } else {
                    PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, false);
                }

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        ToastHelper.getInstance().showMessage("UserName or Password Is wrong");
                    } else if (mObject instanceof ClientLoginModel) {
                        PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLIENT_LOGIN);
                        intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.putExtra(Constants.IS_FROM_SIGN_UP, false);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else if (mObject instanceof PractiseLoginModel) {
                        PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.CLINIC_LOGIN);
                        intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.putExtra(Constants.IS_FROM_SIGN_UP, false);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else if (mObject instanceof VetLoginModel) {
                        PrefHelper.getInstance().setInt(PrefHelper.LOGIN_USER, Constants.VET_LOGIN);
                        intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.putExtra(Constants.IS_FROM_SIGN_UP, false);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        switch (mRequestCode) {

            case GetUser:

                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_login:

                Utils.buttonClickEffect(view);

                try {
                    userName = edtUserName.getText().toString().trim();
                    password = edtpwd.getText().toString().trim();

                    if (userName.isEmpty()) {
                        edtUserName.requestFocus();
                        ToastHelper.getInstance().showMessage(getString(R.string.str_enter_username));
                        return;
                    } else if (password.isEmpty()) {
                        edtpwd.requestFocus();
                        ToastHelper.getInstance().showMessage(getString(R.string.str_enter_password));
                        return;
                    } else {

                        JSONObject params = new JSONObject();
                        params.put("op", ApiList.GET_USER);
                        params.put("AuthKey", ApiList.AUTH_KEY);
                        params.put("UserName", userName);
                        params.put("Password", password);


                        RestClient.getInstance().post(this, Request.Method.POST, params,
                                ApiList.GET_USER, true, RequestCode.GetUser, this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.tv_forgotPassword:
                Utils.buttonClickEffect(view);
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.btn_signUp:

                Utils.buttonClickEffect(view);
                intent = new Intent(this, SignUpActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.REGISTER_BY, RegisterBy.APP.getRegisterBy());
                intent.putExtra(Constants.USER_DATA, bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.btn_google:
                Utils.buttonClickEffect(view);
                if (Utils.isInternetAvailable()) {
                    signInToGoogle();
                } else {
                    CustomDialog.getInstance().showAlert(this, getString(R.string.str_no_internet_connection_available), true);
                }
                break;

            case R.id.btn_faceBook:
                Utils.buttonClickEffect(view);
                if (Utils.isInternetAvailable()) {
                    loginToFaceBook(this, this);
                    //LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                } else {
                    CustomDialog.getInstance().showAlert(this, getString(R.string.str_no_internet_connection_available), true);
                }
                break;
        }
    }
    // [END signOut]

    // [START signInToGoogle]
    private void signInToGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    /**
     * This method open facebook login page and ask for permission to access user's basic information.
     * With this method you do not need to use facebook login button.
     *
     * @param activityContext (Context)   : context
     * @param dataObserver    (DataObserver) : dataObserver instance
     */
    public void loginToFaceBook(final Activity activityContext, final DataObserver dataObserver) {

        LoginManager.getInstance().logInWithReadPermissions(activityContext, accessUserDetailPermission);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getFBUserDetails(activityContext, dataObserver);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                ToastHelper.getInstance().showMessage(activityContext.getString(R.string.str_login_failed));
            }
        });
    }

    /**
     * This method gives the user's information that GraphRequest requested and
     * request approved by user like id,first_name,last_name,email and etc.
     *
     * @param activityContext (Activity)  : activity context
     * @param dataObserver    (DataObserver) : dataObserver instance
     */
    private void getFBUserDetails(final Activity activityContext, final DataObserver dataObserver) {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        String firstName = object.optString("first_name");
                        String lastName = object.optString("last_name");
                        String email = object.optString("email");
                        String socialMediaUserId = object.optString("id");
                        String birthday = object.optString("birthday ");

                        bundle = new Bundle();
                        bundle.putString(Constants.NAME, firstName);
                        bundle.putString(Constants.EMAIL, email);
                        bundle.putString(Constants.USERNAME, firstName + lastName);
                        bundle.putString(Constants.REGISTER_BY, RegisterBy.FACEBOOK.getRegisterBy());

                        intent = new Intent(SignInActivity.this, SignUpActivity.class);
                        intent.putExtra(Constants.USER_DATA, bundle);
                        startActivity(intent);
                        //finish();
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, FB_REQUEST_PARAMETER);
        request.setParameters(parameters);
        request.executeAsync();

    }

    /**
     * This method check user logged in with facebook id than it will return it's access token
     *
     * @return FBAccessToken (boolean)  : it return current logged in user's active access token
     */
    public boolean isLoggedInWithFacebook() {
        FBAccessToken = AccessToken.getCurrentAccessToken();
        return FBAccessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                registerClient(result);
            } else {
                ToastHelper.getInstance().showMessage(getString(R.string.str_login_failed));
            }
        }
    }

    private void registerClient(GoogleSignInResult result) {

        bundle = new Bundle();
        bundle.putString(Constants.NAME, result.getSignInAccount().getGivenName());
        bundle.putString(Constants.EMAIL, result.getSignInAccount().getEmail());
        bundle.putString(Constants.USERNAME, result.getSignInAccount().getGivenName() + result.getSignInAccount().getFamilyName());
        bundle.putString(Constants.REGISTER_BY, RegisterBy.GOOGLE.getRegisterBy());

        intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Constants.USER_DATA, bundle);
        startActivity(intent);
        //finish();
    }


}
