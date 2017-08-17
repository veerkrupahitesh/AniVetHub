package com.veeritsolution.android.anivethub.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;

/**
 * Created by hitesh on 8/18/2017.
 */

public class VetBio extends Fragment {

    private View rootView;
    private TextView tvBio;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vet_bio, container, false);
        tvBio = (TextView) rootView.findViewById(R.id.tv_vetBio);
        tvBio.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        Bundle bundle = getArguments();
        VetLoginModel vetLoginModel = (VetLoginModel) bundle.getSerializable(Constants.VET_DATA);
        tvBio.setText(vetLoginModel.getBiography());
        return rootView;
    }
}
