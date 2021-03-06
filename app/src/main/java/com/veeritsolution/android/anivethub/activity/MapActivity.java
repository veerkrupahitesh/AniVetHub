package com.veeritsolution.android.anivethub.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.ErrorModel;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.PermissionClass;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DataObserver {

    private MapView mMapView;
    private TextView tvHeader;
    private Toolbar toolbar;

    private GoogleMap mGoogleMap;
    private JSONObject params;
    private int isOnline = 1, isBusy = 1, isOffline = 1, minRate = 0, maxRate = 0, minDistance = 0,
            maxDistance = 0, sortBy = 0, sortType = 0;
    private String vetName = "";
    private ArrayList<SearchVetModel> normalVetList;
    private float latitude;
    private float longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_search_result));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        normalVetList = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (PrefHelper.getInstance().containKey(PrefHelper.LATITUDE)) {
            latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
        }
        if (PrefHelper.getInstance().containKey(PrefHelper.LONGITUDE)) {
            longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
        }
        // mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(getString(R.string.your_location)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 15);
        mGoogleMap.animateCamera(cameraUpdate);

        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                SearchVetModel searchVetModel = (SearchVetModel) marker.getTag();
                if (searchVetModel != null) {
                    if (searchVetModel.getIsVetPractise() == 0) {
                        Intent intent = new Intent(MapActivity.this, VetDetailActivity.class);
                        intent.putExtra(Constants.VET_ID, searchVetModel);
                        startActivity(new Intent(intent));
                        // getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        Intent intent = new Intent(MapActivity.this, SearchVetPractiseActivity.class);
                        intent.putExtra(Constants.VET_ID, searchVetModel);
                        startActivity(new Intent(intent));
                        // getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
                return true;
            }
        });

        List<String> permission = new ArrayList<>();
        permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permission.add(Manifest.permission.ACCESS_NETWORK_STATE);

        if (PermissionClass.checkPermission(this, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, permission)) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        getNormalVetData(0, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_listView:
                Utils.buttonClickEffect(view);
                Intent intent = new Intent(this, TabActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case NormalSearchVet:
                ArrayList<SearchVetModel> list;
                Utils.dismissSnackBar();

                try {
                    if (mObject instanceof ErrorModel) {
                        ErrorModel errorModel = (ErrorModel) mObject;
                        if (normalVetList.isEmpty())
                            ToastHelper.getInstance().showMessage(errorModel.getError());
                    } else {

                        list = (ArrayList<SearchVetModel>) mObject;

                        if (!list.isEmpty()) {
                            normalVetList.addAll(list);

                            for (SearchVetModel searchVetModel : normalVetList) {

                                if (searchVetModel.getIsVetPractise() == 0) {
                                    Bitmap result = BitmapFactory.decodeResource(getResources(), R.drawable.img_vet_pin);
                                    result = Bitmap.createScaledBitmap(result, 70, 90, true);
                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(searchVetModel.getLatitude(), searchVetModel.getLongitude()))
                                            .title("Distance " + searchVetModel.getDistance() + "Km")
                                            // .snippet(searchVetModel.getFirstName() + " " + arViewModel[0].getLastName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(result)))
                                            .setTag(searchVetModel);
                                } else {

                                    Bitmap result = BitmapFactory.decodeResource(getResources(), R.drawable.img_vet_practice_pin);
                                    result = Bitmap.createScaledBitmap(result, 70, 90, true);
                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(searchVetModel.getLatitude(), searchVetModel.getLongitude()))
                                            .title("Distance " + searchVetModel.getDistance() + "Km")
                                            // .snippet(arViewModel[0].getFirstName() + " " + arViewModel[0].getLastName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(result)))
                                            .setTag(searchVetModel);
                                }
                                // mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(searchVetModel.getLatitude(), searchVetModel.getLongitude())));
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
        ToastHelper.getInstance().showMessage(mError);
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
            params.put("Records", -1);
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("IsVet", 1);
            params.put("IsVetPractise", 1);
            params.put("VetName", vetName);

            RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.SEARCH_VET,
                    isDialogRequired, RequestCode.NormalSearchVet, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}