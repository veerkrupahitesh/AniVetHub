package com.veeritsolution.android.anivethub.fragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

/**
 * Created by Jaymin on 2/10/2017.
 */

public class AppointmentDetailFragment extends Fragment implements OnClickEvent/*, DataObserver*/, OnBackPressedEvent {

    // xml components
    private Toolbar toolbar;
    private TextView tvHeader, tvName, tvAppointmentDate, tvAppointmentDateHeader, tvAppointmentTime, tvAppointmentTimeHeader,
            tvAppointmentCode, tvPetName, tvPetNameHeader, tvSymptoms, tvSymptomsHeader,
            tvStatus, tvRejectionHeader, tvRejectionReason;
    private ImageView imgProfPic;
    private LinearLayout /*linAppointmentTime,*/ linRejection;
    // private RatingBar rbSessionRatings;
    private View rootView;

    // object and variable declaration
    private AppointmentModel appointmentModel;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        if (bundle != null) {
            //fromSignUp = bundle.getBoolean(Constants.IS_FROM_SIGN_UP);
            appointmentModel = (AppointmentModel) bundle.getSerializable(Constants.APPOINTMENT_DATA);
        }
        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_vet_appointment_detail, container, false);

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
        tvHeader.setText(getString(R.string.str_appointment_detail));

        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvAppointmentCode = (TextView) rootView.findViewById(R.id.tv_appointmentCode);
        tvAppointmentCode.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvAppointmentTime = (TextView) rootView.findViewById(R.id.tv_appointmentTime);
        tvAppointmentTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvAppointmentTimeHeader = (TextView) rootView.findViewById(R.id.tv_Appointment_time);
        tvAppointmentTimeHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvAppointmentDate = (TextView) rootView.findViewById(R.id.tv_appointmentDate);
        tvAppointmentDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvAppointmentDateHeader = (TextView) rootView.findViewById(R.id.tv_appointment_date);
        tvAppointmentDateHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetName = (TextView) rootView.findViewById(R.id.tv_petName);
        tvPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetNameHeader = (TextView) rootView.findViewById(R.id.tv_pet_name);
        tvPetNameHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSymptoms = (TextView) rootView.findViewById(R.id.tv_symptomsDesc);
        tvSymptoms.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSymptomsHeader = (TextView) rootView.findViewById(R.id.tv_symptoms_desc);
        tvSymptomsHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        imgProfPic = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        tvStatus = (TextView) rootView.findViewById(R.id.tv_status);

        //linAppointmentTime = (LinearLayout) rootView.findViewById(R.id.lin_appointmenttime);

        linRejection = (LinearLayout) rootView.findViewById(R.id.lin_rejection);

        tvRejectionHeader = (TextView) rootView.findViewById(R.id.tv_rejectionHeader);
        tvRejectionHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvRejectionReason = (TextView) rootView.findViewById(R.id.tv_rejectionReason);
        tvRejectionReason.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // GetVetAppointmentInfo();
        if (appointmentModel != null) {
            setData();
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
            switch (loginUser) {
                case Constants.CLIENT_LOGIN:
                    homeActivity.removeFragmentUntil(ClientHomeFragment.class);
                    break;
                case Constants.CLINIC_LOGIN:
                    homeActivity.removeFragmentUntil(PractiseHomeFragment.class);
                    break;
                case Constants.VET_LOGIN:
                    homeActivity.removeFragmentUntil(VetHomeFragment.class);
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setData() {

        tvAppointmentCode.setText(appointmentModel.getAppointmentNo());
        int status = appointmentModel.getAppointmentStatus();

        switch (status) {

            case Constants.APPROVED:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(this.getResources(), R.color.green, null));
                tvAppointmentTime.setText(appointmentModel.getFromTime() + "-" + appointmentModel.getToTime());
                String dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY);
                tvAppointmentDate.setText(dateAndTime);
                linRejection.setVisibility(View.GONE);
                break;

            case Constants.REJECTED:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(this.getResources(), R.color.orange, null));
                tvAppointmentTime.setText(appointmentModel.getTimeSlotName());
                //dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                //        + "  " + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                //tvRejectionTitle.setText(appointmentModel.getRejectReasonName());
                tvRejectionReason.setText(appointmentModel.getRejectReasonRemarks());
                break;

            case Constants.CANCELLED:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(this.getResources(), R.color.red, null));
                //dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                //        + "  " + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                tvAppointmentTime.setText(appointmentModel.getFromTime() + "-" + appointmentModel.getToTime());
                linRejection.setVisibility(View.GONE);
                break;

            case Constants.REQUESTED:
                tvStatus.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));

                if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {
                    tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                } else {
                    tvStatus.setText("New");
                }
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                tvAppointmentTime.setText(appointmentModel.getTimeSlotName());
                linRejection.setVisibility(View.GONE);
                break;

            case Constants.READY_TO_CALL:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorAccent, null));
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                tvAppointmentTime.setText(appointmentModel.getFromTime() + "-" + appointmentModel.getToTime());
                linRejection.setVisibility(View.GONE);
                break;

            case Constants.ARCHIVED:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.hint, null));
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                tvAppointmentTime.setText(appointmentModel.getTimeSlotName());
                linRejection.setVisibility(View.GONE);
                break;

            case Constants.APPOINTMENT_CANCEL:
                tvStatus.setText(appointmentModel.getAppointmentStatusDisp());
                tvStatus.setTextColor(ResourcesCompat.getColor(this.getResources(), R.color.red, null));
                tvAppointmentDate.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
                tvAppointmentTime.setText(appointmentModel.getTimeSlotName());
                linRejection.setVisibility(View.GONE);
                break;
        }

        tvPetName.setText(appointmentModel.getPetName());
        tvSymptoms.setText(appointmentModel.getSymptomsDescription());

        if (PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0) == Constants.CLIENT_LOGIN) {

            Utils.setProfileImage(getActivity(), appointmentModel.getVetProfilePic(),
                    R.drawable.img_vet_profile, imgProfPic);
            tvName.setText(appointmentModel.getVetName());

        } else {
            Utils.setProfileImage(getActivity(), appointmentModel.getClientProfilePic(),
                    R.drawable.img_client_profile, imgProfPic);
            tvName.setText(appointmentModel.getClientName());
        }
    }
}
