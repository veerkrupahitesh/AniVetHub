package com.veeritsolution.android.anivethub.fragment.client;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.adapter.AdpViewPager;
import com.veeritsolution.android.anivethub.fragment.vet.VetHomeFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.ZoomOutPageTransformer;

/**
 * Created by Jaymin on 2/16/2017.
 */

public class ClientHIWFragment extends Fragment implements OnClickEvent, OnBackPressedEvent {

   // String[] header;
    String[] contents;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnPrev, btnNext, btnSkip;
    private HomeActivity homeActivity;
    private View headerView;
    private TextView tvHeader;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        layouts = new int[]{
                R.drawable.img_hiw_first_client,
                R.drawable.img_hiw_sec_client,
                R.drawable.img_hiw_third_client,
                R.drawable.img_hiw_forth_client,
                R.drawable.img_hiw_fifth_vet
        };
        contents = new String[]{getString(R.string.str_hiw_first_client), getString(R.string.str_hiw_sec_client),
                getString(R.string.str_hiw_third_client), getString(R.string.str_hiw_forth_client),
                getString(R.string.str_hiw_fifth_client)};
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        homeActivity.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.popBackFragment();
            }
        });

        headerView = getView().findViewById(R.id.headerView);
        tvHeader = (TextView) getView().findViewById(R.id.tv_headerTitle);
        tvHeader.setText(getString(R.string.str_how_it_works));

        viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) getView().findViewById(R.id.layoutDots);
        btnPrev = (Button) getView().findViewById(R.id.btn_prev);
        btnPrev.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnNext = (Button) getView().findViewById(R.id.btn_next);
        btnNext.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnSkip = (Button) getView().findViewById(R.id.btn_skip);
        btnSkip.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        btnSkip.setVisibility(View.INVISIBLE);
        btnPrev.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        //changeStatusBarColor();

        viewPager.setAdapter(new AdpViewPager(getActivity(), layouts, contents));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                addBottomDots(position);

               /* if (position == 0) {

                    btnNext.setText(getString(R.string.next));
                    btnPrev.setVisibility(View.INVISIBLE);

                } else if (position + 1 == layouts.length) {

                    btnNext.setText(getString(R.string.str_got_it));
                    //  btnPrev.setVisibility(View.GONE);

                } else {
                    btnNext.setText(getString(R.string.next));
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {
        final int current = viewPager.getCurrentItem() + 1;

        switch (view.getId()) {

            case R.id.btn_next:

                if (current < layouts.length) {

                    /*viewPager.postDelayed(new Runnable() {

                        @Override
                        public void run() {*/
                    viewPager.setCurrentItem(current, true);
                    /*    }
                    }, 100);*/

                } else if (current == layouts.length) {

                    ToastHelper.getInstance().showMessage(getString(R.string.str_hope_understand));
                   /* Intent i = new Intent(getActivity(), SignInActivity.class);
                    startActivity(i);
                    getActivity().finish();*/
                }
                break;

            case R.id.btn_prev:

                if (current > 0) {

                   /* viewPager.postDelayed(new Runnable() {
s
                        @Override
                        public void run() {*/
                    viewPager.setCurrentItem(current - 2, true);
                    /*    }
                    }, 100);*/
                }

                break;

           /* case R.id.btn_skip:

                Intent i = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(i);
                finish();

                break;*/

           /* case R.id.img_back_header:

                homeActivity.popBackFragment();

                break;*/
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
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
            } else {
                homeActivity.removeFragmentUntil(VetHomeFragment.class);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
