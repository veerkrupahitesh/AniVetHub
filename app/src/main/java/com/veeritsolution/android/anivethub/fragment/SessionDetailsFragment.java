package com.veeritsolution.android.anivethub.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.activity.HomeActivity;
import com.veeritsolution.android.anivethub.api.ApiList;
import com.veeritsolution.android.anivethub.api.DataObserver;
import com.veeritsolution.android.anivethub.api.RequestCode;
import com.veeritsolution.android.anivethub.api.RestClient;
import com.veeritsolution.android.anivethub.customdialog.CustomDialog;
import com.veeritsolution.android.anivethub.fragment.client.ClientHomeFragment;
import com.veeritsolution.android.anivethub.fragment.client.ClientSupportCenterFragment;
import com.veeritsolution.android.anivethub.fragment.practise.PractiseHomeFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetHomeFragment;
import com.veeritsolution.android.anivethub.fragment.vet.VetSupportCenterFragment;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.helper.ToastHelper;
import com.veeritsolution.android.anivethub.listener.OnBackPressedEvent;
import com.veeritsolution.android.anivethub.listener.OnClickEvent;
import com.veeritsolution.android.anivethub.models.ClientLoginModel;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ${hitesh} on 2/6/2017.
 */
public class SessionDetailsFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    //xml components
    private Toolbar toolbar;
    private TextView tvHeader, tvPetName, tvSymptomsDesc, tvStartTime, tvEndTime, tvPayment, txvSessionFeedback, tvSessionNo, tvName,
            lblPetName, tvSymptoms, tvSessionStartTime, tvSessionEndTIme, tvAmount, tvSessionRating, tvSessionFeedback, tvVetPractise,tvvideoconsultsummary;
    private ImageView imgProfilePic;
    private Button btnGenerateTicket;
    private View rootView;
    private RatingBar ratings;
    private RelativeLayout relativeLayout;
    private Bitmap myBitmap;

    // object and variable declaration
    private SessionModel sessionModel;
    private Bundle bundle;
    private HomeActivity homeActivity;
    private JSONObject params;
    private LinearLayout llMain;
    private ImageView imgPrint;
    private int loginUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        bundle = getArguments();

        if (bundle != null) {
            sessionModel = (SessionModel) bundle.getSerializable(Constants.SESSION_DATA);
        }

        loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_session_details, container, false);

        // rootView.setVisibility(View.INVISIBLE);
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

        // imgback = (ImageView) getView().findViewById(R.id.img_back_header);

        imgPrint = (ImageView)rootView.findViewById(R.id.img_print);

        llMain = (LinearLayout)rootView.findViewById(R.id.ll_session_Detail);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
        tvHeader.setText(getString(R.string.str_session_detail));

        lblPetName = (TextView) rootView.findViewById(R.id.tvPetName);
        lblPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSymptoms = (TextView) rootView.findViewById(R.id.tvSymptoms);
        tvSymptoms.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionStartTime = (TextView) rootView.findViewById(R.id.tvSessionStartTime);
        tvSessionStartTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionEndTIme = (TextView) rootView.findViewById(R.id.tvSessionEndTime);
        tvSessionEndTIme.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvAmount = (TextView) rootView.findViewById(R.id.tvAmount);
        tvAmount.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionRating = (TextView) rootView.findViewById(R.id.tvSessionRating);
        tvSessionRating.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSessionFeedback = (TextView) rootView.findViewById(R.id.tvSessionFeedback);
        tvSessionFeedback.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvName = (TextView) rootView.findViewById(R.id.tv_clientName);
        tvName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        tvSessionNo = (TextView) rootView.findViewById(R.id.tv_sessionId);
        tvSessionNo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPetName = (TextView) rootView.findViewById(R.id.txvpetname);
        tvPetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvSymptomsDesc = (TextView) rootView.findViewById(R.id.txvsympromsdesc);
        tvSymptomsDesc.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvStartTime = (TextView) rootView.findViewById(R.id.txvsessionstarttime);
        tvStartTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvEndTime = (TextView) rootView.findViewById(R.id.txvendtime);
        tvEndTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvPayment = (TextView) rootView.findViewById(R.id.txvpaymentamount);
        tvPayment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        txvSessionFeedback = (TextView) rootView.findViewById(R.id.txvfeedback);
        txvSessionFeedback.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        tvvideoconsultsummary = (TextView) rootView.findViewById(R.id.tvvideoconsultsummary);
        tvvideoconsultsummary.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        ratings = (RatingBar) rootView.findViewById(R.id.rating);

        btnGenerateTicket = (Button) rootView.findViewById(R.id.btn_createTicket);
        btnGenerateTicket.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        imgProfilePic = (ImageView) rootView.findViewById(R.id.img_vetProfilePic);
        tvVetPractise = (TextView) rootView.findViewById(R.id.tv_vetPractise);
        tvVetPractise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        if (loginUser == Constants.CLINIC_LOGIN) {
            tvVetPractise.setVisibility(View.VISIBLE);
        } else {
            tvVetPractise.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        //GetVetSessionDetail();
        tvSessionNo.setText(sessionModel.getVetSessionNo());
        tvPetName.setText(sessionModel.getPetName());
        tvSymptomsDesc.setText(sessionModel.getSymptomsDescription());
        tvStartTime.setText(sessionModel.getSessionStartOn());
        tvEndTime.setText(sessionModel.getSessionEndOn());
        tvPayment.setText(String.valueOf(sessionModel.getPaymentAmount()));
        txvSessionFeedback.setText(String.valueOf(sessionModel.getReview()));
        ratings.setRating(Float.valueOf(String.valueOf(sessionModel.getAvgRating())));
        tvvideoconsultsummary.setText(sessionModel.getVideoConsultSummary());
        if (loginUser == Constants.CLIENT_LOGIN) {

            tvName.setText(sessionModel.getVetName());
            Utils.setProfileImage(getActivity(), sessionModel.getVetProfilePic(), R.drawable.img_vet_profile, imgProfilePic);

            if (sessionModel.getTicketStatus() == Constants.GENERATE_TICKET) {

                btnGenerateTicket.setVisibility(View.VISIBLE);
                btnGenerateTicket.setText(R.string.str_ticket_status);

            } else {

                btnGenerateTicket.setVisibility(View.VISIBLE);
                btnGenerateTicket.setText(R.string.str_generate_ticket);
            }
        } else if (loginUser == Constants.VET_LOGIN) {
            tvName.setText(sessionModel.getClientName());
            Utils.setProfileImage(getActivity(), sessionModel.getClientProfilePic(), R.drawable.img_client_profile, imgProfilePic);
            btnGenerateTicket.setText(R.string.str_ticket_status);

            if (sessionModel.getTicketStatus() == Constants.GENERATE_TICKET) {
                btnGenerateTicket.setVisibility(View.VISIBLE);
            } else {
                btnGenerateTicket.setVisibility(View.GONE);
            }
        } else if (loginUser == Constants.CLINIC_LOGIN) {

            tvName.setText(sessionModel.getClientName());
            tvVetPractise.setText(sessionModel.getVetPractiseName());
            Utils.setProfileImage(getActivity(), sessionModel.getClientProfilePic(), R.drawable.img_client_profile, imgProfilePic);
            btnGenerateTicket.setText(R.string.str_ticket_status);

            if (sessionModel.getTicketStatus() == Constants.GENERATE_TICKET) {
                btnGenerateTicket.setVisibility(View.VISIBLE);
            } else {
                btnGenerateTicket.setVisibility(View.GONE);
            }
        }

        imgPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMain.post(new Runnable() {
                    public void run() {

                        //take screenshot
                        myBitmap = captureScreen(llMain);

                        Toast.makeText(getApplicationContext(), "Screenshot captured..!", Toast.LENGTH_LONG).show();

                        try {
                            if(myBitmap!=null){
                                //save image to SD card
                                saveImage(myBitmap);
                            }
                            Toast.makeText(getApplicationContext(), "Screenshot saved..!", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case VetSessionTicketInsert:

                ToastHelper.getInstance().showMessage("Your Message sent !");
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

            case R.id.btn_createTicket:

                if (loginUser == Constants.CLIENT_LOGIN) {
                    if (sessionModel.getTicketStatus() == Constants.GENERATE_TICKET) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.SESSION_DATA, sessionModel);
                        homeActivity.pushFragment(new ClientSupportCenterFragment(), true, true, bundle);
                    } else {
                        CustomDialog.getInstance().showTicketDialog(homeActivity, false, "Ticket Reason");
                    }

                } else if (loginUser == Constants.VET_LOGIN) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.SESSION_DATA, sessionModel);
                    homeActivity.pushFragment(new VetSupportCenterFragment(), true, true, bundle);
                } else if (loginUser == Constants.CLINIC_LOGIN) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.SESSION_DATA, sessionModel);
                    homeActivity.pushFragment(new VetSupportCenterFragment(), true, true, bundle);
                }
                break;

            case R.id.img_cancel:

                CustomDialog.getInstance().dismiss();
                break;

            case R.id.btn_submit_ticket:

                Object object = view.getTag();
                if (object != null) {
                    CustomDialog.getInstance().dismiss();
                    insertTicket(object);
                } else {
                    ToastHelper.getInstance().showMessage("Please enter your message");
                }

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

    private void insertTicket(Object object) {

        String remarks = (String) object;

        try {
            params = new JSONObject();
            params.put("op", ApiList.VET_SESSION_TICKET_INSERT);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("VetSessionId", sessionModel.getVetSessionId());
            params.put("ClientId", ClientLoginModel.getClientCredentials().getClientId());
            params.put("VetId", 0);
            params.put("LoginId", 0);
            params.put("Remarks", remarks);
            params.put("Flag", Constants.CLIENT_TICKET_FLAG);

            RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                    ApiList.VET_SESSION_TICKET_INSERT, true, RequestCode.VetSessionTicketInsert, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap captureScreen(View v) {

        Bitmap screenshot = null;
        try {

            if(v!=null) {

                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }

        }catch (Exception e){
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }

        return screenshot;
    }

    public static void saveImage(Bitmap bitmap) throws IOException{

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.png");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
    }

}
