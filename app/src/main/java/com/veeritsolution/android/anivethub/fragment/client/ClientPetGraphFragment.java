package com.veeritsolution.android.anivethub.fragment.client;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.models.PetWeightModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ${hitesh} on 3/3/2017.
 */

public class ClientPetGraphFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    // xml components
    private GraphView graph;
    private Toolbar toolbar;
    private TextView tvHeader;

    // object and variable declaration
    private JSONObject params;
    private ArrayList<PetWeightModel> petWeightList;
    private HomeActivity homeActivity;
    private DataPoint[] dataPoint;
    private PetDetailsModel petDetailsModel;
    private Bundle bundle;
    private View rootView;
    private Calendar calendar = Calendar.getInstance();
    // private LineGraphSeries<DataPoint> lineGraphSeries;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            homeActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();

        bundle = getArguments();

        if (bundle != null) {
            petDetailsModel = (PetDetailsModel) bundle.getSerializable(Constants.PET_DATA);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            homeActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            homeActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pet_graph, container, false);

        //int orientation = getActivity().getResources().getConfiguration().orientation;

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
        tvHeader.setText(getString(R.string.str_pet_weigth_history));

        graph = (GraphView) rootView.findViewById(R.id.graph);
        petWeightList = new ArrayList<>();
        graph.setTitle(petDetailsModel.getPetName() + "'s" + " weight history");
        graph.getGridLabelRenderer().setLabelsSpace(30);
        graph.getGridLabelRenderer().setPadding(50);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getPetWeightInfo();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetPetWeightHistory:

                if (!(mObject instanceof ErrorModel)) {
                    petWeightList = (ArrayList<PetWeightModel>) mObject;

                    if (!petWeightList.isEmpty()) {
                        setGraphData(petWeightList);
                    }
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

            case R.id.btn_actionOk:
                CustomDialog.getInstance().dismiss();
                homeActivity.popBackFragment();
                break;
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
            homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            //  }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setGraphData(ArrayList<PetWeightModel> petWeightList) {

        dataPoint = new DataPoint[petWeightList.size()];
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < petWeightList.size(); i++) {

            PetWeightModel petWeightModel = petWeightList.get(i);

            // generate Dates
            Date date = Utils.stringToDate(petWeightModel.getCreatedOnDate(), Constants.DATE_YYYY_MM_DD);
            dateList.add(date);
            calendar.setTime(date);
            //  Debug.trace("date", date.toString());
            dataPoint[i] = new DataPoint(calendar.getTime(), petWeightModel.getWeight());
            // lineGraphSeries.appendData(dataPoint[i], false, petWeightList.size());
            // Debug.trace("time y", dataPoint[i].toString());
        }

        LineGraphSeries lineGraphSeries = new LineGraphSeries<>(dataPoint);
        graph.addSeries(lineGraphSeries);
        // set date label formatter.
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(160);
        // graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        //graph.getGridLabelRenderer().setLabelHorizontalHeight(0);
        //graph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Weight");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        // graph.getGridLabelRenderer().setHighlightZeroLines(false);
        // graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(dateList.size() - 1); // the number of dates
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);


        // set manual x bounds to have nice steps
       // graph.getViewport().setMinX(dateList.get(0).getTime());
       // graph.getViewport().setMaxX(dateList.get(dateList.size() - 1).getTime());
       // graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setDrawBorder(true);

        // custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));

        lineGraphSeries.setCustomPaint(paint);
        lineGraphSeries.setColor(ResourcesCompat.getColor(getResources(), R.color.homeCategory, null));
        lineGraphSeries.setAnimated(true);
        lineGraphSeries.setDrawDataPoints(true);
        lineGraphSeries.setDataPointsRadius(10);
        lineGraphSeries.setDrawAsPath(true);
        lineGraphSeries.setDrawBackground(true);
        lineGraphSeries.setThickness(10);
        lineGraphSeries.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.graphBackColor, null));

    }

    private void getPetWeightInfo() {

        try {
            params = new JSONObject();
            params.put("op", ApiList.GET_CLIENT_PET_WEIGHT_HISTORY);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientPetId", petDetailsModel.getClientPetId());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_PET_WEIGHT_HISTORY,
                    true, RequestCode.GetPetWeightHistory, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
