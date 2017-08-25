package com.veeritsolution.android.anivethub.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;


/**
 * Created by Veer on 28/09/2016.
 */
public class AboutUsFragment extends Fragment implements OnBackPressedEvent, OnClickEvent {

    String fbUrl = "https://www.facebook.com/anivethub";
    String twitterUrl = "https://twitter.com/anivethub";
    String gplusUrl = "https://plus.google.com/u/0/113707138210597096384";
    // xml components
    private Toolbar toolbar;
    private TextView tvHeader, tvAboutUsDetails;
    private HomeActivity homeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(R.string.str_about_us);

        tvAboutUsDetails = (TextView) getView().findViewById(R.id.tv_aboutUsDetails);
        tvAboutUsDetails.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_fb:
                Utils.buttonClickEffect(view);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(fbUrl));
                startActivity(i);
                homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.img_twitter:
                Utils.buttonClickEffect(view);

                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(twitterUrl));
                startActivity(i1);
                homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            case R.id.img_gplus:
                Utils.buttonClickEffect(view);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(gplusUrl));
                startActivity(intent);
                homeActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
            // homeActivity.removeAllFragment();
            if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {
                homeActivity.removeFragmentUntil(ClientHomeFragment.class);
            } else if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.VET_LOGIN) {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
            } else {
                homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
