//package com.veeritsolution.android.anivethub.fragment.vet;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//import com.veeritsolution.android.anivethub.MyApplication;
//import com.veeritsolution.android.anivethub.R;
//import com.veeritsolution.android.anivethub.activity.HomeActivity;
//import com.veeritsolution.android.anivethub.adapter.AdpPackageList;
//import com.veeritsolution.android.anivethub.api.ApiList;
//import com.veeritsolution.android.anivethub.api.DataObserver;
//import com.veeritsolution.android.anivethub.api.RequestCode;
//import com.veeritsolution.android.anivethub.api.RestClient;
//import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
//import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
//import com.veeritsolution.android.anivethub.fragment.vetpractise.PractiseHomeFragment;
//import com.veeritsolution.android.anivethub.helper.PrefHelper;
//import com.veeritsolution.android.anivethub.helper.ToastHelper;
//import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
//import com.veeritsolution.android.anivethub.listener.OnClickEvent;
//import com.veeritsolution.android.anivethub.models.PackageModel;
//import com.veeritsolution.android.anivethub.models.PackageSetting;
//import com.veeritsolution.android.anivethub.models.VetCredit;
//import com.veeritsolution.android.anivethub.models.VetLoginModel;
//import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
//import com.veeritsolution.android.anivethub.utility.Constants;
//import com.veeritsolution.android.anivethub.utility.Debug;
//import com.veeritsolution.android.anivethub.utility.Utils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * Created by ${hitesh} on 2/1/2017.
// */
//
//public class VetSubsInfoFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {
//
//    // xml components
//    private Toolbar toolbar;
//    private TextView tvHeader, tvCredits, tvSelectPackage, tvCurrentCredits;
//    private RecyclerView recyclerView;
//    private View rootView;
//
//    // object and variable declaration
//    private PayPalConfiguration config;
//    private HomeActivity homeActivity;
//    private JSONObject params;
//    private AdpPackageList adpPackageList;
//    private PackageSetting packageSetting;
//    private int loginUser;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setHasOptionsMenu(true);
//        homeActivity = (HomeActivity) getActivity();
//        payapalSetUp();
//        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
//    }
//
//    private void payapalSetUp() {
//
//        config = new PayPalConfiguration()
//
//                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
//                // or live (ENVIRONMENT_PRODUCTION)
//                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//                //.acceptCreditCards(true)
//                .defaultUserEmail(VetLoginModel.getVetCredentials().getPaypalAccountId())
//                .clientId(Constants.PAYPAL_CLIENT_ID);
//
//        Intent intent = new Intent(getActivity(), PayPalService.class);
//
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        getActivity().startService(intent);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        rootView = inflater.inflate(com.veeritsolution.android.anivethub.R.layout.fragment_vet_subscription_info, container, false);
//
//        // rootView.setVisibility(View.INVISIBLE);
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        toolbar = (Toolbar) getView().findViewById(com.veeritsolution.android.anivethub.R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                homeActivity.popBackFragment();
//            }
//        });
//
//        tvHeader = (TextView) getView().findViewById(com.veeritsolution.android.anivethub.R.id.tv_headerTitle);
//        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
//        tvHeader.setText(getString(com.veeritsolution.android.anivethub.R.string.str_subscription_info));
//
//        tvCurrentCredits = (TextView) getView().findViewById(com.veeritsolution.android.anivethub.R.id.tv_currentCredits);
//        tvCurrentCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
//
//        tvCredits = (TextView) getView().findViewById(com.veeritsolution.android.anivethub.R.id.tv_credits);
//        tvCredits.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
//
//        tvSelectPackage = (TextView) getView().findViewById(com.veeritsolution.android.anivethub.R.id.tv_select_package);
//        tvSelectPackage.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
//
//        recyclerView = (RecyclerView) getView().findViewById(com.veeritsolution.android.anivethub.R.id.recyler_subscription);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//
//        getSubscriptionPackage();
//
//    }
//
//
//    @Override
//    public void onSuccess(RequestCode mRequestCode, Object mObject) {
//
//        switch (mRequestCode) {
//
//            case getPackageSettings:
//
//                rootView.setVisibility(View.VISIBLE);
//
//                setData(mObject);
//                break;
//
//            case vetSubscriptionInsert:
//
//                try {
//                    JSONArray jsonArray = (JSONArray) mObject;
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                    if (!jsonObject.has("DataId")) {
//                        ToastHelper.getInstance().showMessage("payment is not verified");
//                        homeActivity.popBackFragment();
//                    } else {
//                        getSubscriptionPackage();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onFailure(RequestCode mRequestCode, String mError) {
//        ToastHelper.getInstance().showMessage(mError);
//        // homeActivity.popBackFragment();
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        homeActivity.popBackFragment();
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//
//            case com.veeritsolution.android.anivethub.R.id.btn_alertOk:
//                Utils.buttonClickEffect(view);
//                CustomDialog.getInstance().dismiss();
//                break;
//
//            case com.veeritsolution.android.anivethub.R.id.lin_package_settings:
//                packageSetting = (PackageSetting) view.getTag();
//                purchaseCredits(packageSetting);
//                break;
//        }
//    }
//
//    private void getSubscriptionPackage() {
//
//        try {
//            params = new JSONObject();
//            params.put("op", "GetPackageSettings");
//            params.put("AuthKey", ApiList.AUTH_KEY);
//            if (loginUser == Constants.VET_LOGIN) {
//                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
//            } else {
//                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
//            }
//            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_PACKAGE_SETTINGS,
//                    true, RequestCode.getPackageSettings, this);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setData(Object mObject) {
//
//        List<PackageModel> packageModelList = (List<PackageModel>) mObject;
//
//        VetCredit vetCredit = packageModelList.get(0).getVetCredit().get(0);
//
//        tvCredits.setText(String.valueOf(vetCredit.getSessionCredit()));
//        List<PackageSetting> packageSettingList = packageModelList.get(1).getPackageSettings();
//
//        if (!packageSettingList.isEmpty()) {
//            adpPackageList = new AdpPackageList(getActivity(), packageSettingList);
//            recyclerView.setAdapter(adpPackageList);
//        }
//    }
//
//    private void purchaseCredits(PackageSetting packageSetting) {
//        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
//        // Change PAYMENT_INTENT_SALE to
//        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
//        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
//        //     later via calls from your server.
//
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(packageSetting.getPackageAmount()),
//                "USD", "sample item", PayPalPayment.PAYMENT_INTENT_SALE);
//
//        Intent intent = new Intent(getActivity(), PaymentActivity.class);
//
//        // send the same configuration for restart resiliency
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//
//        startActivityForResult(intent, 0);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 0) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        // Log.i("paymentExample", confirm.toJSONObject().toString(4));
//                        Debug.trace("paymentExample", confirm.toJSONObject().toString(4));
//                        ToastHelper.getInstance().showMessage("Payment Successful");
//                        // TODO: send 'confirm' to your server for verification.
//                        // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
//                        // for more details.
//
//                        confirmPayment();
//
//                    } catch (JSONException e) {
//                        Debug.trace("paymentExample", "an extremely unlikely failure occurred: " + e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//
//                ToastHelper.getInstance().showMessage("transaction cancelled");
//
//                Debug.trace("paymentExample", "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//
//                ToastHelper.getInstance().showMessage("An invalid Payment was submitted");
//                Debug.trace("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//    }
//
//    private void confirmPayment() {
//        try {
//            params = new JSONObject();
//            params.put("op", ApiList.VET_SUBSCRIPTION_INSERT);
//            params.put("AuthKey", ApiList.AUTH_KEY);
//            if (loginUser == Constants.VET_LOGIN) {
//                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
//            } else if (loginUser == Constants.CLINIC_LOGIN) {
//                params.put("VetId", PractiseLoginModel.getPractiseCredentials().getVetId());
//            }
//            params.put("SessionBuy", packageSetting.getSessionCount());
//            params.put("PaymentAmount", packageSetting.getPackageAmount());
//            //   params.put("op",ApiList.VET_SUBSCRIPTION_INSERT);
//
//            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.VET_SUBSCRIPTION_INSERT,
//                    true, RequestCode.vetSubscriptionInsert, this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.other_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_userProfile) {
//            switch (loginUser) {
//                case Constants.CLIENT_LOGIN:
//                    homeActivity.removeFragmentUntil(ClientHomeFragment.class);
//                    break;
//                case Constants.CLINIC_LOGIN:
//                    homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
//                    break;
//                case Constants.VET_LOGIN:
//                    homeActivity.removeFragmentUntil(VetHomeFragment.class);
//                    break;
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onDestroy() {
//        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
//        super.onDestroy();
//    }
//}
