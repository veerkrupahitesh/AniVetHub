package com.veeritsolution.android.anivethub.fragment.vet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpSessionList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.fragment.SessionDetailsFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PractiseLoginModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hitesh} on 2/6/2017.
 */

public class VetSessionFragmnet extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private ListView lvSession;
    private Toolbar toolbar;
    private TextView tvHeader, tvNoSessionInfo;
    private CoordinatorLayout snackBarView;

    // object and variable declaration
    private AdpSessionList adapter;
    private JSONObject params;
    private List<SessionModel> sessionList;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private View rootView;
    private int totalRecords = 0;
    private int totalPage;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_session_summary, container, false);
        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);
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

        // imgback = (ImageView) getView().findViewById(R.id.img_back_header);
        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_session_summary));

        tvNoSessionInfo = (TextView) rootView.findViewById(R.id.tv_noSessionInfo);
        tvNoSessionInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //  footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_item, null, false);

        lvSession = (ListView) rootView.findViewById(R.id.lv_session);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // lvSession.addFooterView(footerView, null, false);
        sessionList = new ArrayList<>();
        adapter = new AdpSessionList(getActivity(), sessionList);
        lvSession.setAdapter(adapter);

        lvSession.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {

                if (totalItemCount < totalRecords && page <= totalPage) {

                    Utils.showSnackBar(homeActivity, snackBarView);
                    getVetSessionInfoAll(page, false);
                    return true;
                    // page++;

                } else if (totalItemCount > totalRecords) {
                    Utils.dismissSnackBar();
                    return false;
                } else {
                    return false;
                }
            }
        });

        /*lvSession.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

                if (loadMore) {
                    if (totalItemCount <= totalRecords && page <= totalPage) {

                        GetVetSessionInfoAll(page, false);

                        Utils.showSnackBar(homeActivity, snackBarView);
                        page++;

                    } else if (totalItemCount > totalRecords) {
                        Utils.dismissSnackBar();
                    }

                }
            }
        });*/
        getVetSessionInfoAll(1, true);
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetSessionInfoAll:

                rootView.setVisibility(View.VISIBLE);

                Utils.dismissSnackBar();

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        //ToastHelper.getInstance().showMessage(errorModel.getError());
                        if (sessionList.isEmpty()) {
                            tvNoSessionInfo.setVisibility(View.VISIBLE);
                            lvSession.setVisibility(View.GONE);
                        }

                    } else {

                        List<SessionModel> list = (List<SessionModel>) mObject;

                        if (!list.isEmpty()) {

                            sessionList.addAll(list);

                            if (sessionList.isEmpty()) {
                                tvNoSessionInfo.setVisibility(View.VISIBLE);
                                lvSession.setVisibility(View.GONE);
                            } else {
                                tvNoSessionInfo.setVisibility(View.GONE);
                                lvSession.setVisibility(View.VISIBLE);
                                totalRecords = list.get(0).getTotalRowNo();
                                totalPage = list.get(0).getTotalPage();
                                adapter.notifyDataSetChanged();

                            }
                        }
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

            case GetVetSessionInfoAll:

                Utils.dismissSnackBar();
                ToastHelper.getInstance().showMessage(mError);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_session:

                SessionModel vs = (SessionModel) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.SESSION_DATA, vs);
                homeActivity.pushFragment(new SessionDetailsFragment(), true, true, bundle);
                break;

           /* case R.id.img_supportCenter:

                SessionModel sessionModel = (SessionModel) view.getTag();
                bundle = new Bundle();
                bundle.putSerializable(Constants.SESSION_DATA, sessionModel);
                homeActivity.pushFragment(new VetSupportCenterFragment(), true, true, bundle);
                break;
*/
        }
    }


    private void getVetSessionInfoAll(int pageNo, boolean isDialogRequired) {

        try {
            if (loginUser == Constants.VET_LOGIN) {
                params = new JSONObject();
                params.put("op", "GetVetSessionInfoAll");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetId", VetLoginModel.getVetCredentials().getVetId());
                params.put("PageNumber", pageNo);
                params.put("Records", 10);

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_SESSION_INFO_ALL,
                        isDialogRequired, RequestCode.GetVetSessionInfoAll, this);

            } else if (loginUser == Constants.CLINIC_LOGIN) {
                params = new JSONObject();
                params.put("op", "GetVetSessionInfoAll_VetPractise");
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("VetPractiseId", PractiseLoginModel.getPractiseCredentials().getVetId());
                params.put("PageNumber", pageNo);
                params.put("Records", 10);

                RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_SESSION_INFO_ALL_VET_PRACTISE,
                        isDialogRequired, RequestCode.GetVetSessionInfoAll, this);
            }

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
            homeActivity.popBackFragment();
        }

        return super.onOptionsItemSelected(item);
    }

}
