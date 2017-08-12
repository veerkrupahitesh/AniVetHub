package com.veeritsolution.android.anivethub.fragment.vet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpPackageList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.PackageModel;
import com.veeritsolution.android.anivethub.models.PackageSetting;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.VetCredit;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${Hitesh} on 20-Apr-17.
 */

public class VetSubscriptionInfo extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    // xml components
    private Toolbar toolbar;
    private TextView tvHeader, tvCredits, tvSelectPackage, tvCurrentCredits;
    private RecyclerView recyclerView;
    private View rootView;

    // object and variable declaration
    private HomeActivity homeActivity;
    private JSONObject params;
    private AdpPackageList adpPackageList;
    private PackageSetting packageSetting;
    private int loginUser;
    // private boolean _paypalLibraryInit;
    private CheckoutButton launchPayPalButton;
    private LinearLayout linParentView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(com.veeritsolution.android.anivethub.R.layout.fragment_vet_subscription_info, container, false);

        linParentView = (LinearLayout) rootView.findViewById(R.id.lin_parentView);
        toolbar = (Toolbar) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(com.veeritsolution.android.anivethub.R.string.str_subscription_info));

        tvCurrentCredits = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_currentCredits);
        tvCurrentCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvCredits = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_credits);
        tvCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvSelectPackage = (TextView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.tv_select_package);
        tvSelectPackage.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        recyclerView = (RecyclerView) rootView.findViewById(com.veeritsolution.android.anivethub.R.id.recyler_subscription);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getSubscriptionPackage();
        payPalSetUp();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {
        switch (mRequestCode) {

            case GetPackageSettings:

                rootView.setVisibility(View.VISIBLE);

                setData(mObject);
                break;

            case VetSubscriptionInsert:

                try {
                    JSONArray jsonArray = (JSONArray) mObject;
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    if (!jsonObject.has("DataId")) {
                        ToastHelper.getInstance().showMessage("payment is not verified");
                        homeActivity.popBackFragment();
                    } else {
                        getSubscriptionPackage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case com.veeritsolution.android.anivethub.R.id.btn_alertOk:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case com.veeritsolution.android.anivethub.R.id.lin_package_settings:

                ToastHelper.getInstance().showMessage("Proceed with below PayPal button");
                packageSetting = (PackageSetting) view.getTag();
                if (launchPayPalButton == null || !launchPayPalButton.isShown())
                    showPayPalButton();
                break;
        }
    }

    private void payPalSetUp() {

        PayPal pp = PayPal.getInstance();

        if (pp == null) {  // Test to see if the library is already initialized

            // This main initialization call takes your Context, AppID, and target server
            pp = PayPal.initWithAppID(getActivity(), Constants.PAYPAL_APP_ID_LIVE,
                    PayPal.ENV_LIVE);

            // Required settings:
            // Set the language for the library
            pp.setLanguage("en_US");

            // Some Optional settings:
            // Sets who pays any transaction fees. Value is:
            // FEEPAYER_SENDER, FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY
            pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
            pp.setDynamicAmountCalculationEnabled(true);
            // true = transaction requires shipping
            //pp.setShippingEnabled(true);

            // _paypalLibraryInit = true;
        }

    }

    private void showPayPalButton() {

        // Generate the PayPal checkout button and save it for later use
        PayPal pp = PayPal.getInstance();
        launchPayPalButton = pp.getCheckoutButton(getActivity(), PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);

        // The OnClick listener for the checkout button
        launchPayPalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseCredits(packageSetting);
            }
        });

        // Add the listener to the layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 10;
        launchPayPalButton.setLayoutParams(params);
        // launchPayPalButton.setId(Constants.PAYPAL_BUTTON_ID);
        linParentView.addView(launchPayPalButton);
        linParentView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void purchaseCredits(PackageSetting packageSetting) {
        /* PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         Change PAYMENT_INTENT_SALE to
           - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
           - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
             later via calls from your server.*/

         /*Create a basic PayPal payment*/
        PayPalPayment payment = new PayPalPayment();

        /* Set the currency type*/
        payment.setCurrencyType("USD");

        /* Set the recipient for the payment (can be a phone number)*/
        // payment.setRecipient("+919033304750");
        payment.setRecipient("jaymin@veerkrupainfotechs.com");

        /* Set the payment amount, excluding tax and shipping costs*/
        payment.setSubtotal(new BigDecimal(1));

        /* Set the payment type--his can be PAYMENT_TYPE_GOODS,
         PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE*/
        payment.setPaymentType(PayPal.PAYMENT_TYPE_NONE);

        // Use checkout to create our Intent.
        Intent checkoutIntent = PayPal.getInstance()
                .checkout(payment, getActivity() /*, new ResultDelegate()*/);
        // Use the android's startActivityForResult() and pass in our
        // Intent.
        // This will start the library.
        startActivityForResult(checkoutIntent, 1);
        /* PayPalInvoiceData can contain tax and shipping amounts, and an
         ArrayList of PayPalInvoiceItem that you can fill out.
         These are not required for any transaction.*/
        //PayPalInvoiceData invoice = new PayPalInvoiceData();

        /* Set the tax amount*/
        //invoice.setTax(new BigDecimal(_taxAmount));
    }

    private void getSubscriptionPackage() {

        try {
            params = new JSONObject();
            params.put("op", "GetPackageSettings");
            params.put("AuthKey", ApiList.AUTH_KEY);
            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            } else {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            }
            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PACKAGE_SETTINGS,
                    true, RequestCode.GetPackageSettings, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(Object mObject) {

        List<PackageModel> packageModelList = (List<PackageModel>) mObject;

        VetCredit vetCredit = packageModelList.get(0).getVetCredit().get(0);

        tvCredits.setText(String.valueOf(vetCredit.getSessionCredit()));
        List<PackageSetting> packageSettingList = packageModelList.get(1).getPackageSettings();

        if (!packageSettingList.isEmpty()) {
            adpPackageList = new AdpPackageList(getActivity(), packageSettingList);
            recyclerView.setAdapter(adpPackageList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        removePayPalButton();
        switch (resultCode) {
            // The payment succeeded
            case Activity.RESULT_OK:
                String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                ToastHelper.getInstance().showMessage("Purchase succeed \n pay key :" + payKey);
                confirmPayment();
                break;

            // The payment was canceled
            case Activity.RESULT_CANCELED:
                ToastHelper.getInstance().showMessage("Purchase canceled");
                break;

            // The payment failed, get the error from the EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
            case PayPalActivity.RESULT_FAILURE:
                String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                Debug.trace("ErrorID", "" + errorID);
                String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                ToastHelper.getInstance().showMessage("Purchase Failed " + errorMessage);
                break;
        }
    }

    /* this method removes the PayPal button from the view
     */
    private void removePayPalButton() {
        // Avoid an exception for setting a parent more than once
        if (launchPayPalButton != null) {
            linParentView.removeView(launchPayPalButton);
        }
    }

    private void confirmPayment() {
        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_SUBSCRIPTION_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            if (loginUser == Constants.VET_LOGIN) {
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
            }
            params.put("SessionBuy", packageSetting.getSessionCount());
            params.put("PaymentAmount", packageSetting.getPackageAmount());
            //   params.put("op",ApiList.VET_SUBSCRIPTION_INSERT);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_SUBSCRIPTION_INSERT,
                    true, RequestCode.VetSubscriptionInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.other_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            switch (loginUser) {
                case Constants.CLIENT_LOGIN:
                    homeActivity.removeFragmentUntil(ClientHomeFragment.class);
                    break;
                case Constants.CLINIC_LOGIN:
                    homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
                    break;
                case Constants.VET_LOGIN:
                    homeActivity.removeFragmentUntil(VetHomeFragment.class);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
