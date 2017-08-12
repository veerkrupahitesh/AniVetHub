package com.veeritsolution.android.anivethub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.MyWebViewClient;
import com.veeritsolution.android.anivethub.utility.Utils;

/**
 * Created by admin on 12/31/2016.
 */

public class TermsAndConditionsFragment extends Fragment implements OnBackPressedEvent, OnClickEvent {

    // xml components
    private Toolbar toolbar;
    private TextView tvHeader;
    private ProgressBar prgWebView;
    private View rootView;
    private WebView webview;


    // object and variable declaration
    private HomeActivity homeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_terms_and_conditions));

        prgWebView = (ProgressBar) rootView.findViewById(R.id.prg_webView);

        webview = (WebView) rootView.findViewById(R.id.webview);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // CustomDialog.getInstance().showProgress(homeActivity, getString(R.string.str_please_wait), false);
        if (Utils.isInternetAvailable()) {
            prgWebView.setVisibility(View.VISIBLE);
            webview.setWebChromeClient(new WebChromeClient());
            webview.setWebViewClient(new MyWebViewClient());
            //  webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {

                    if (progress == 100) {

                        prgWebView.setVisibility(View.GONE);
                        //CustomDialog.getInstance().dismiss();
                    }
                }
            });
            webview.loadUrl(Constants.TERMS_AND_POLICY);
        } else {
            CustomDialog.getInstance().showAlert(getActivity(), getString(R.string.str_no_internet_connection_available), true);
        }
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_alertOk:
                CustomDialog.getInstance().dismiss();
                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
            homeActivity.popBackFragment();
            //homeActivity.removeFragmentUntil(VetHomeFragment.class);
            // homeActivity.pushFragment(new ClientHomeFragment(), true, true, null);
        }

        return super.onOptionsItemSelected(item);
    }
}
