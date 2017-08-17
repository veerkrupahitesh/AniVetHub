package com.veeritsolution.android.anivethub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.SearchVetModel;
import com.veeritsolution.android.anivethub.models.VetLoginModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;
import com.veeritsolution.android.anivethub.utility.ZoomOutPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veer on 19/10/2016.
 */
public class VetDetailActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private TextView /*tvUserName,*/ tvName, tvHeader, tvExperience, tvVetAddress, tvVetSession;
    private ImageView imgVetProfilePhoto/*, imgSelectBannerPhoto, imgSelectProfilePhoto, imgVetBannerPhoto*/;
    // private Button btnVideoCall, btnBookAppointment;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private RatingBar rbRating;

    // objects and variable declaration
    private JSONObject params;
    private JSONArray list;
    private Intent intent;
    private VetLoginModel vetLoginModel;
    //    private ExpandableListView expandableListView;
//    private ExpandableListAdapter expandableListAdapter;
    private View rootView;
    //    private List<String> listDataHeader;
//    private HashMap<String, List<?>> listDataChild;
//    private List<VetEducation> vetEducationList;
//    private List<VetExperience> vetExperienceList;
//    private List<VetSpecialization> vetSpecializationList;
    // private List<VetClinic> vetClinicList;
    private SearchVetModel searchVetModel;
    private int vetPractiseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_vet_details, null, false);
        rootView.setVisibility(View.INVISIBLE);
        setContentView(rootView);
        init();
        intent = getIntent();
        if (intent != null) {
            searchVetModel = (SearchVetModel) intent.getSerializableExtra(Constants.VET_ID);
            vetPractiseId = intent.getIntExtra(Constants.VET_PRACTICE_ID, 0);

            try {
                params = new JSONObject();
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("op", ApiList.GET_VET_INFO_ALL);
                params.put("VetId", searchVetModel.getVetId());
                params.put("VetPractiseId", vetPractiseId);

                RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.GET_VET_INFO_ALL, true,
                        RequestCode.GetVetInfoAll, this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_vet_profile));

        tvName = (TextView) findViewById(R.id.tv_vetName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        //tvName.setText(VetLoginModel.getVetCredentials().getVetName());

        //tvUserName = (TextView) findViewById(R.id.tv_vetUserName);
        //tvUserName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        // tvUserName.setText("@" + VetLoginModel.getVetCredentials().getUserName());

        imgVetProfilePhoto = (ImageView) findViewById(R.id.img_vetProfilePic);
//        imgVetBannerPhoto = (ImageView) findViewById(R.id.img_vet_bannerPic);
//
//        imgSelectBannerPhoto = (ImageView) findViewById(R.id.img_selectBannerPhoto);
//        imgSelectBannerPhoto.setVisibility(View.INVISIBLE);
//
//        imgSelectProfilePhoto = (ImageView) findViewById(R.id.img_selectProfilePhoto);
//        imgSelectProfilePhoto.setVisibility(View.INVISIBLE);

        tvVetAddress = (TextView) findViewById(R.id.tv_address);
        tvVetAddress.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvVetSession = (TextView) findViewById(R.id.tv_sessionRate);
        tvVetSession.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvExperience = (TextView) findViewById(R.id.tv_experienceYear);
        tvExperience.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        rbRating = (RatingBar) findViewById(R.id.rb_rating);
//        expandableListView = (ExpandableListView) findViewById(R.id.listView_vetDetails);

//        btnVideoCall = (Button) findViewById(R.id.btn_videoChat);
//        btnVideoCall.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
//
//        btnBookAppointment = (Button) findViewById(R.id.btn_bookAppointment);
//        btnBookAppointment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

//        listDataHeader = new ArrayList<>();
//        listDataHeader.add("Education");
//        listDataHeader.add("Experience");
//        listDataHeader.add("Specialization");
        // listDataHeader.add("Clinic Information");

//        listDataChild = new HashMap<>();
//        vetEducationList = new ArrayList<>();
//        vetExperienceList = new ArrayList<>();
//        vetSpecializationList = new ArrayList<>();
        //vetClinicList = new ArrayList<>();

        mViewPager = (ViewPager) findViewById(R.id.container);
        //setupViewpager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager, true);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetVetInfoAll:

                rootView.setVisibility(View.VISIBLE);
                list = (JSONArray) mObject;

                bindData();
                break;
        }

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
        // finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_videoChat:
                Utils.buttonClickEffect(view);
                if (vetLoginModel.getOnlineStatus() == 0 || vetLoginModel.getOnlineStatus() == 2) {
                    CustomDialog.getInstance().showAlert(this, getString(R.string.str_vet_busy_offline), true);
                } else {
                    Intent intent = new Intent(this, ClientPetsActivity.class);
                    intent.putExtra(Constants.VET_DATA, vetLoginModel);
                    intent.putExtra(Constants.IS_FROM_VIDEO_CALL, true);
                    intent.putExtra(Constants.VET_PRACTICE_ID, vetPractiseId);
                    // intent.putExtra(Constants.GENERAL_SETTING_DATA, generalSettingsModel);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                break;

            case R.id.btn_alertOk:

                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().dismiss();
                finish();
                break;

            case R.id.btn_bookAppointment:
                Utils.buttonClickEffect(view);
                if (vetLoginModel.getIsTimeSlot() == 1) {
                    intent = new Intent(this, ClientPetsActivity.class);
                    intent.putExtra(Constants.VET_DATA, vetLoginModel);
                    intent.putExtra(Constants.IS_FROM_VIDEO_CALL, false);
                    intent.putExtra(Constants.VET_PRACTICE_ID, vetPractiseId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    CustomDialog.getInstance().showAlert(this, "Currently veterinary surgeon unable to take appointment", true);
                }

                break;

            case R.id.lin_reviewAndRating:
                intent = new Intent(this, ReviewAndRatingActivity.class);
                intent.putExtra(Constants.VET_DATA, vetLoginModel);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewpager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new com.veeritsolution.android.anivethub.activity.VetReviewRating(), "Reviews");
        adapter.addFragment(new com.veeritsolution.android.anivethub.activity.VetBio(), "Biography");
        adapter.addFragment(new com.veeritsolution.android.anivethub.activity.VetEducation(), "Education");
        adapter.addFragment(new com.veeritsolution.android.anivethub.activity.VetExperience(), "Experience");
        adapter.addFragment(new com.veeritsolution.android.anivethub.activity.VetSpecialization(), "Specialization");

        //adapter.addFragment(new SearchVetPractiseFragment(), "Vet practices");
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // on changing the page
                // make respected tab selected
                // actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //onClickEvent = (OnClickEvent) fragment;
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.VET_DATA, vetLoginModel);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void bindData() {

        try {

            JSONObject jsonObject = list.getJSONObject(0).getJSONArray("VetProfile").getJSONObject(0);

            vetLoginModel = RestClient.getGsonInstance().fromJson(jsonObject.toString(), VetLoginModel.class);

            tvName.setText(vetLoginModel.getVetName());
            //tvUserName.setText(vetLoginModel.getUserName());
            tvVetAddress.setText(vetLoginModel.getAddress1() + "\n" + vetLoginModel.getAddress2()
                    + "\n" + vetLoginModel.getCountry().toLowerCase() + "\n" + vetLoginModel.getState().toLowerCase() + "\n"
                    + vetLoginModel.getCity().toLowerCase() + "-" + vetLoginModel.getPOBox());
            tvVetSession.setText(String.valueOf(vetLoginModel.getSessionRate() + " $/Session"));
            tvExperience.setText(String.valueOf(vetLoginModel.getTotalExp()) + " " + "year");

            rbRating.setRating(vetLoginModel.getRating());

            Glide.with(this)
                    .load(vetLoginModel.getProfilePic())
                    .placeholder(R.drawable.img_vet_profile)
                    .error(R.drawable.img_vet_profile)
                    .into(imgVetProfilePhoto);

            setupViewpager(mViewPager);
            //      Utils.setProfileImage(this, vetLoginModel.getProfilePic(),
            //      R.drawable.img_vet_profile, imgVetProfilePhoto);

            // Utils.setBannerImage(this, vetLoginModel.getBannerPic(), R.drawable.img_vet_banner, imgVetBannerPhoto);

            /*JSONArray VetEducationArray = list.getJSONObject(1).getJSONArray("VetEducation");

            for (int i = 0; i < VetEducationArray.length(); i++) {

                JSONObject jsonObjVetEducation = list.getJSONObject(1).getJSONArray("VetEducation").getJSONObject(i);
                VetEducation vetEducation = RestClient.getGsonInstance().fromJson(jsonObjVetEducation.toString(), VetEducation.class);
                vetEducationList.add(vetEducation);

            }

            JSONArray VetExperienceArray = list.getJSONObject(2).getJSONArray("VetExperience");
            for (int j = 0; j < VetExperienceArray.length(); j++) {
                JSONObject jsonObjVetExperience = list.getJSONObject(2).getJSONArray("VetExperience").getJSONObject(j);
                VetExperience vetExperience = RestClient.getGsonInstance().fromJson(jsonObjVetExperience.toString(), VetExperience.class);
                vetExperienceList.add(vetExperience);

            }

            JSONArray VetExpertiseArray = list.getJSONObject(3).getJSONArray("VetExperience");
            for (int j = 0; j < VetExpertiseArray.length(); j++) {
                JSONObject jsonObjExpertise = list.getJSONObject(3).getJSONArray("VetExperience").getJSONObject(j);

                VetSpecialization vetSpecialization = RestClient.getGsonInstance().fromJson(jsonObjExpertise.toString(), VetSpecialization.class);

                vetSpecializationList.add(vetSpecialization);
            }

            JSONArray VetClinicArray = list.getJSONObject(4).getJSONArray("VetClinic");
            for (int j = 0; j < VetClinicArray.length(); j++) {

                JSONObject jsonObjClinic = list.getJSONObject(4).getJSONArray("VetClinic").getJSONObject(j);

                VetClinic vetClinic = RestClient.getGsonInstance().fromJson(jsonObjClinic.toString(), VetClinic.class);

                vetClinicList.add(vetClinic);
            }

            listDataChild.put(listDataHeader.get(0), vetEducationList);
            listDataChild.put(listDataHeader.get(1), vetExperienceList);
            listDataChild.put(listDataHeader.get(2), vetSpecializationList);

              listDataChild.put(listDataHeader.get(3), vetClinicList);

            expandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expandableListView.setAdapter(expandableListAdapter);

            for (int i = 0; i < 3; i++) {
                expandableListView.expandGroup(i);
            }
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
