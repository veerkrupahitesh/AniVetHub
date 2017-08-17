package com.veeritsolution.android.anivethub.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.adapter.AdpSearchVetList;
import com.veeritsolution.android.anivethub.adapter.SpinnerAdapter;
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
import java.util.List;

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
    private ArrayList<SearchVetModel> normalVetList/*, filterVetList, sortVetList*/;
    // private View footerView;
    private AdpSearchVetList adpSearchVetList;
    private SearchVetModel searchVetModel;
    //private int apiCallType = Constants.NORMAL_SEARCH_VET;
    private int totalRecords = 0;
    //private int page = 2;
    //private int totalPage;
    private Dialog mDialog;
    private int isOnline = 1, isBusy = 1, isOffline = 1, minRate = 0, maxRate = 0, minDistance = 0,
            maxDistance = 0, sortBy = 0, sortType = 0;
    private String vetName = "";

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
       // filterVetList = new ArrayList<>();
       // sortVetList = new ArrayList<>();
        // footerView = LayoutInflater.from(this).inflate(R.layout.footer_item, null, false);

        //lvSearchVet.addFooterView(footerView, null, false);

        adpSearchVetList = new AdpSearchVetList(getActivity(), normalVetList, this);
        lvSearchVet.setAdapter(adpSearchVetList);

        lvSearchVet.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {

                if (totalItemCount <= totalRecords) {

                    Utils.showSnackBar(getActivity(), snackBarView);
                    getNormalVetData(page, false);

                   /* if (apiCallType == Constants.NORMAL_SEARCH_VET) {

                        getNormalVetData(page, false);
                    } else if (apiCallType == Constants.FILTER_SEARCH_VET) {

                        getFilterVetData(page, false);
                    } else if (apiCallType == Constants.SORT_SEARCH_VET) {
                        getSortVetData(page, false);
                    }*/
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
                isOnline = 1;
                isBusy = 1;
                isOffline = 1;
                minRate = 0;
                maxRate = 0;
                minDistance = 0;
                maxDistance = 0;
                sortBy = 0;
                sortType = 0;
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
                            //totalPage = searchVetModel.getTotalPage();
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

            /*case FilterSearchVet:
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
                break;*/
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

            /*case R.id.img_sortCancel:

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
                break;*/
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
                    showFilterDialog(getActivity(), getString(R.string.str_filterby), false, searchVetModel, this, true);
                break;

            case R.id.action_sortBy:

                if (searchVetModel != null)
                    showSortDialog(getActivity(), getString(R.string.str_sortby), false, searchVetModel, this, true);
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
            params.put("IsOnline", isOnline);
            params.put("IsBusy", isBusy);
            params.put("IsOffline", isOffline);
            params.put("MinRate", minRate);
            params.put("MaxRate", maxRate);
            params.put("MinDistance", minDistance);
            params.put("MaxDistance", maxDistance);
            params.put("SortBy", sortBy);
            params.put("SortType", sortType);
            params.put("PageNumber", pageNo);
            params.put("Records", 5);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("IsVet", 1);
            params.put("IsVetPractise", 0);
            params.put("VetName", vetName);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.SEARCH_VET,
                    isDialogRequired, RequestCode.NormalSearchVet, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*private void getFilterVetData(int pageNo, boolean isDialogRequired) {

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
    }*/

    public void showFilterDialog(final Context mContext, String mTitle, boolean mIsCancelable,
                                 final SearchVetModel searchVetModel, View.OnClickListener onClickListener, boolean isVet) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //@SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_custom_filter_dialog, null, false);
        mDialog.setContentView(view);
      /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //  mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);
        Utils.setupOutSideTouchHideKeyboard(view);
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_filterCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView tvTitle, tvStatus, tvOnline, tvBusy, tvOffline, tvSessionRate, tvDistance, tvLabelVetName;
        //  final EditText edtVetName;

        LinearLayout linOnlineStatus = (LinearLayout) mDialog.findViewById(R.id.lin_onlineStatus);


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
        }

        chkOnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isOnline = 1;
                    searchVetModel.setIsOnline(1);
                } else {
                    isOnline = 0;
                    searchVetModel.setIsOnline(0);
                }
            }
        });

        chkBusy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isBusy = 1;
                    searchVetModel.setIsBusy(1);
                } else {
                    isBusy = 0;
                    searchVetModel.setIsBusy(0);
                }
            }
        });

        chkOffLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isOffline = 1;
                    searchVetModel.setIsOffline(1);
                } else {
                    isOffline = 0;
                    searchVetModel.setIsOffline(0);
                }
            }
        });

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

        rangPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minRate = minValue.intValue();
                tvMinPrice.setText(String.valueOf(minRate));
                maxRate = maxValue.intValue();
                tvMaxPrice.setText(String.valueOf(maxRate));
            }
        });

        rangeDistance = (CrystalRangeSeekbar) mDialog.findViewById(R.id.range_kilometer);

        rangeDistance.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                minDistance = minValue.intValue();
                maxDistance = maxValue.intValue();
                tvMinDistance.setText(String.valueOf(minDistance));
                tvMaxDistance.setText(String.valueOf(maxDistance));
                //searchVetModel.setMinDistance(minDistance);
                //searchVetModel.setMaxDistance(maxDistance);
            }
        });

        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radioGroup_distance);
        RadioButton rbKilometer/*, rbMeter*/;

        rbKilometer = (RadioButton) mDialog.findViewById(R.id.rb_kilometer);
        rbKilometer.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        // rbMeter = (RadioButton) mDialog.findViewById(R.id.rb_meter);
        // rbMeter.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);


        //btnFilterSearch.setOnClickListener(onClickListener);
        final EditText edtVetName = (EditText) mDialog.findViewById(R.id.edt_vetName);
        edtVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        edtVetName.setText(searchVetModel.getVetName());
        edtVetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vetName = edtVetName.getText().toString().trim();
                searchVetModel.setVetName(vetName);
            }
        });

        Button btnFilterSearch = (Button) mDialog.findViewById(R.id.btn_filterSearch);
        btnFilterSearch.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        //btnFilterSearch.setTag(searchVetModel);
        btnFilterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                v.setTag(searchVetModel);
                sortBy = 0;
                sortType = 0;
                normalVetList.clear();
                if (adpSearchVetList != null) {
                    adpSearchVetList.refreshList(normalVetList);
                }
                getNormalVetData(1, true);
            }
        });

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
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView tvTitle, tvSortBy, tvSortType;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_search_sortBy);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvTitle.setText(mTitle);

        tvSortBy = (TextView) mDialog.findViewById(R.id.tvSortBy);
        tvSortType = (TextView) mDialog.findViewById(R.id.tvSortType);

        tvSortBy.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        tvSortType.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        final Spinner spSortBy, spSortType;

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
        if (searchVetModel != null) {
            spSortBy.setSelection(searchVetModel.getSortBy());
            spSortType.setSelection(searchVetModel.getSortType());
        }
        Button btnSortSearch = (Button) mDialog.findViewById(R.id.btn_sortSearch);
        btnSortSearch.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnSortSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spSortBy.getSelectedItemPosition() == 0) {
                    ToastHelper.getInstance().showMessage("Select Sort by");
                    return;
                }
                if (spSortType.getSelectedItemPosition() == 0) {
                    ToastHelper.getInstance().showMessage("Select Sort type");
                    return;
                }
                dismiss();
                isOnline = 1;
                isBusy = 1;
                isOffline = 1;
                minRate = 0;
                maxRate = 0;
                minDistance = 0;
                maxDistance = 0;
                normalVetList.clear();
                if (adpSearchVetList != null) {
                    adpSearchVetList.refreshList(normalVetList);
                }
                getNormalVetData(1, true);
            }
        });
        btnSortSearch.setTag(searchVetModel);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }
}
