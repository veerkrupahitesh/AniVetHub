package com.veeritsolution.android.anivethub.fragment.client;

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
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${hitesh} on 2/6/2017.
 */

public class ClientSessionFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private Toolbar toolbar;
    private TextView tvHeader;
    private ListView lvSession;
    //  private View footerView;
    private CoordinatorLayout snackBarView;
    private View rootView;
    private TextView tvNoSessionInfo;

    private JSONObject params;
    //  private List<SessionModel> list;
    private AdpSessionList adapter;
    //Context mcontext;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private List<SessionModel> sessionList;
    private int totalRecords = 0;
    //  private int page = 2;
    private int totalPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_session_summary, container, false);
        // rootView.setVisibility(View.INVISIBLE);
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

        //  imgback = (ImageView) getView().findViewById(R.id.img_back_header);
        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_session_summary));

        tvNoSessionInfo = (TextView) rootView.findViewById(R.id.tv_noSessionInfo);
        tvNoSessionInfo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        // footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_item, null, false);

        lvSession = (ListView) rootView.findViewById(R.id.lv_session);
        //  lvSession.addFooterView(footerView, null, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionList = new ArrayList<>();
        adapter = new AdpSessionList(getActivity(), sessionList);
        lvSession.setAdapter(adapter);

        lvSession.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {

                if (totalItemCount < totalRecords && page <= totalPage) {

                    Utils.showSnackBar(homeActivity, snackBarView);
                    getClientSessionInfo(page, false);
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
                    if (totalItemCount < totalRecords && page <= totalPage) {

                        Utils.showSnackBar(homeActivity, snackBarView);
                        getClientSessionInfo(page, false);

                        page++;

                    } else if (totalItemCount > totalRecords) {
                        Utils.dismissSnackBar();
                    }

                }
            }
        });*/

        getClientSessionInfo(1, true);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetSessionInfoAllClient:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);

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

            case GetVetSessionInfoAllClient:
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

           /* case R.id.img_back_header:

                homeActivity.popBackFragment();

                break;*/
            case R.id.lin_session:

                SessionModel vs = (SessionModel) view.getTag();

                bundle = new Bundle();
                bundle.putSerializable(Constants.SESSION_DATA, vs);
                homeActivity.pushFragment(new SessionDetailsFragment(), true, true, bundle);
                //Intent i = new Intent(ClientSessionListingActivity.this, SessionDetailActivity.class);
                //i.putExtra(Constants.SESSION_ID, vs);
                //startActivity(i);
                break;

            /*case R.id.img_supportCenter:

                SessionModel sessionModel = (SessionModel) view.getTag();

                bundle = new Bundle();
                bundle.putSerializable(Constants.SESSION_DATA, sessionModel);
                homeActivity.pushFragment(new ClientSupportCenterFragment(), true, true, bundle);

                break;*/

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // if (!fromSignUp) {
        inflater.inflate(R.menu.other_menu, menu);
        // }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            // if (!fromSignUp) {
            homeActivity.popBackFragment();
            //  }
        }

        return super.onOptionsItemSelected(item);
    }


    private void getClientSessionInfo(int pageNo, boolean isDialogRequired) {

        try {
            params = new JSONObject();
            params.put("op", "getClientSessionInfo");
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("PageNumber", pageNo);
            params.put("Records", 10);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_VET_SESSION_INFO_ALL_CLIENT,
                    isDialogRequired, RequestCode.GetVetSessionInfoAllClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
