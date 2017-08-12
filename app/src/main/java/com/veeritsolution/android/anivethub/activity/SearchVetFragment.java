package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpSearchVetList;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.EndlessScrollListener;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 5/6/2017.
 */

public class SearchVetFragment extends Fragment implements /*OnClickEvent, OnBackPressedEvent,*/ DataObserver, View.OnClickListener {


    private ListView lvSearchVet;
    private View rootView;
    private CoordinatorLayout snackBarView;
    private SwipeRefreshLayout swipeRefreshLayout;
    // object and variable declaration
    private JSONObject params;
    private ArrayList<SearchVetModel> normalVetList, filterVetList, sortVetList;
    // private View footerView;
    private AdpSearchVetList adpSearchVetList;
    private SearchVetModel searchVetModel;
    private int apiCallType = Constants.NORMAL_SEARCH_VET;
    private int totalRecords = 0;
    //private int page = 2;
    private int totalPage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_vet_search, container, false);
        //PrefHelper.getInstance().setBoolean(PrefHelper.IS_VET_PRACTICE_USER, false);
        lvSearchVet = (ListView) rootView.findViewById(R.id.recycler_vetSearch);
        snackBarView = (CoordinatorLayout) rootView.findViewById(R.id.parentView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_vet);
        swipeRefreshLayout.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        normalVetList = new ArrayList<>();
        filterVetList = new ArrayList<>();
        sortVetList = new ArrayList<>();
        // footerView = LayoutInflater.from(this).inflate(R.layout.footer_item, null, false);

        //lvSearchVet.addFooterView(footerView, null, false);

        adpSearchVetList = new AdpSearchVetList(getActivity(), normalVetList, this);
        lvSearchVet.setAdapter(adpSearchVetList);

        lvSearchVet.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {

                if (totalItemCount <= totalRecords) {

                    Utils.showSnackBar(getActivity(), snackBarView);

                    if (apiCallType == Constants.NORMAL_SEARCH_VET) {

                        getNormalVetData(page, false);
                    } else if (apiCallType == Constants.FILTER_SEARCH_VET) {

                        getFilterVetData(page, false);
                    } else if (apiCallType == Constants.SORT_SEARCH_VET) {
                        getSortVetData(page, false);
                    }
                    return true;
                } else {
                    Utils.dismissSnackBar();
                    return false;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                normalVetList.clear();
                if (adpSearchVetList != null) {
                    adpSearchVetList.refreshList(normalVetList);
                }
                getNormalVetData(1, false);
            }
        });
        getNormalVetData(1, true);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        ArrayList<SearchVetModel> list;

        switch (mRequestCode) {

            case NormalSearchVet:
                Utils.dismissSnackBar();
                swipeRefreshLayout.setRefreshing(false);
                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        if (normalVetList.isEmpty())
                            ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = (ArrayList<SearchVetModel>) mObject;

                        if (!list.isEmpty()) {

                            normalVetList.addAll(list);
                            searchVetModel = normalVetList.get(0);
                            //tvSearchResult.setText(searchVetModel.getTotalRowNo() + " results");
                            totalRecords = searchVetModel.getTotalRowNo();
                            totalPage = searchVetModel.getTotalPage();
                            //pageNo = searchVetModel.getTotalPage();
                            adpSearchVetList.refreshList(normalVetList);
                            //adpSearchVetList.notifyDataSetChanged();

                            // setAdapter(normalVetList);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case FilterSearchVet:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        if (filterVetList.isEmpty())
                            ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = (ArrayList<SearchVetModel>) mObject;

                        if (!list.isEmpty()) {

                            filterVetList.addAll(list);
                            searchVetModel = list.get(0);
                            // tvSearchResult.setText(searchVetModel.getTotalRowNo() + " results");
                            totalRecords = searchVetModel.getTotalRowNo();
                            totalPage = searchVetModel.getTotalPage();
                            // adpSearchVetList.notifyDataSetChanged();
                            adpSearchVetList.refreshList(filterVetList);
                            //   setAdapter(filterVetList);
                        } else {
                            // tvSearchResult.setText(getString(R.string.str_no_value_found));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case SortSearchVet:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        if (sortVetList.isEmpty())
                            ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = (ArrayList<SearchVetModel>) mObject;

                        if (!list.isEmpty()) {

                            sortVetList.addAll(list);
                            searchVetModel = list.get(0);
                            // tvSearchResult.setText(searchVetModel.getTotalRowNo() + " results");
                            totalRecords = searchVetModel.getTotalRowNo();
                            totalPage = searchVetModel.getTotalPage();
                            adpSearchVetList.refreshList(sortVetList);
                            //adpSearchVetList.notifyDataSetChanged();
                            //  setAdapter(sortVetList);
                        } else {
                            //  tvSearchResult.setText(getString(R.string.str_no_value_found));
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

            case NormalSearchVet:
                Utils.dismissSnackBar();
                swipeRefreshLayout.setRefreshing(false);
                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;

            case FilterSearchVet:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;

            case SortSearchVet:
                Utils.dismissSnackBar();
                rootView.setVisibility(View.VISIBLE);
                ToastHelper.getInstance().showMessage(mError);
                break;

        }
    }


    @Override
    public void onClick(View view) {

        Object object;

        switch (view.getId()) {

            case R.id.lin_vetSearch:

                Utils.buttonClickEffect(view);
                searchVetModel = (SearchVetModel) view.getTag();

                if (searchVetModel != null) {
                    if (searchVetModel.getIsVetPractise() == 0) {
                        Intent intent = new Intent(getActivity(), VetDetailActivity.class);
                        intent.putExtra(Constants.VET_ID, searchVetModel);
                        startActivity(new Intent(intent));
                        // getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Intent intent = new Intent(getActivity(), SearchVetPractiseActivity.class);
                        intent.putExtra(Constants.VET_ID, searchVetModel);
                        startActivity(new Intent(intent));
                        // getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.img_sortCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.img_filterCancel:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_filterSearch:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();

                apiCallType = Constants.FILTER_SEARCH_VET;
                filterVetList.clear();
                adpSearchVetList = new AdpSearchVetList(getActivity(), filterVetList, this);
                lvSearchVet.setAdapter(adpSearchVetList);

                object = view.getTag();
                if (object != null) {
                    searchVetModel = (SearchVetModel) object;
                }
                getFilterVetData(1, true);
                break;

            case R.id.btn_sortSearch:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();

                apiCallType = Constants.SORT_SEARCH_VET;

                sortVetList.clear();
                adpSearchVetList = new AdpSearchVetList(getActivity(), sortVetList, this);
                lvSearchVet.setAdapter(adpSearchVetList);

                object = view.getTag();
                if (object != null) {
                    searchVetModel = (SearchVetModel) object;
                }
                getSortVetData(1, true);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.serachvet_menu, menu);
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                getActivity().finish();
                break;

            case R.id.action_filterBy:

                if (searchVetModel != null)
                    CustomDialog.getInstance().showFilterDialog(getActivity(), getString(R.string.str_filterby), false, searchVetModel, this, true);
                break;

            case R.id.action_sortBy:

                if (searchVetModel != null)
                    CustomDialog.getInstance().showSortDialog(getActivity(), getString(R.string.str_sortby), false, searchVetModel, this, true);
                break;

            default:

                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }


    private void getNormalVetData(int pageNo, boolean isDialogRequired) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.SEARCH_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("IsOnline", 1);
            params.put("IsBusy", 1);
            params.put("IsOffline", 1);
            params.put("MinRate", 0);
            params.put("MaxRate", 0);
            params.put("MinDistance", 0);
            params.put("MaxDistance", 0);
            params.put("SortBy", 0);
            params.put("SortType", 0);
            params.put("PageNumber", pageNo);
            params.put("Records", 5);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("IsVet", 1);
            params.put("IsVetPractise", 0);
            params.put("VetName", "");

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.SEARCH_VET,
                    isDialogRequired, RequestCode.NormalSearchVet, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFilterVetData(int pageNo, boolean isDialogRequired) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.SEARCH_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("IsOnline", searchVetModel.getIsOnline());
            params.put("IsBusy", searchVetModel.getIsBusy());
            params.put("IsOffline", searchVetModel.getIsOffline());
            params.put("MinRate", (int) searchVetModel.getMinRate());
            params.put("MaxRate", (int) searchVetModel.getMaxRate());
            params.put("MinDistance", (int) searchVetModel.getMinDistance());
            params.put("MaxDistance", (int) searchVetModel.getMaxDistance());
            params.put("SortBy", 0);
            params.put("SortType", 0);
            params.put("PageNumber", pageNo);
            params.put("Records", 5);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("IsVet", 1);
            params.put("IsVetPractise", 0);
            params.put("VetName", searchVetModel.getVetName());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.SEARCH_VET, isDialogRequired, RequestCode.FilterSearchVet, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getSortVetData(int pageNo, boolean isDialogRequired) {

        try {
            params = new JSONObject();
            params.put("op", ApiList.SEARCH_VET);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("IsOnline", 1);
            params.put("IsBusy", 1);
            params.put("IsOffline", 1);
            params.put("MinRate", 0);
            params.put("MaxRate", 0);
            params.put("MinDistance", 0);
            params.put("MaxDistance", 0);
            params.put("SortBy", searchVetModel.getSortBy());
            params.put("SortType", searchVetModel.getSortType());
            params.put("PageNumber", pageNo);
            params.put("Records", 5);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("IsVet", 1);
            params.put("IsVetPractise", 0);
            params.put("VetName", "");

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params,
                    ApiList.SEARCH_VET, isDialogRequired, RequestCode.SortSearchVet, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
