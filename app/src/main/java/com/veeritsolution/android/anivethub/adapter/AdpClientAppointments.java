package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Debug;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by ${hitesh} on 2/10/2017.
 */

public class AdpClientAppointments extends BaseAdapter {

    private List<AppointmentModel> appointmentList;
    private Context context;

    public AdpClientAppointments(List<AppointmentModel> appointmentList, Context context) {
        //  super(context, appointmentList);
        this.appointmentList = appointmentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_client_appointment, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }


        AppointmentModel appointmentModel = appointmentList.get(position);

        //int status = appointmentModel.getAppointmentStatus();

        setAppointmentStatus(appointmentModel, myViewHolder.tvDateAndTime,
                myViewHolder.tvAppointmentStatus, myViewHolder.imgCancel);

        if (position == 0) {
            myViewHolder.linClientAppointment
                    .setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            myViewHolder.linClientAppointment
                    .setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            myViewHolder.linClientAppointment.setBackgroundColor(Color.WHITE);
        }

        // appointmentModel.setAppointmentStatus(Constants.READY_TO_CALL);

        myViewHolder.tvAppointmentStatus.setTag(appointmentModel);
        myViewHolder.linClientAppointment.setTag(appointmentModel);
        myViewHolder.tvAppointmentNo.setTag(appointmentModel);
        myViewHolder.imgCancel.setTag(appointmentModel);
        Debug.trace("appointment id is:", appointmentModel.getAppointmentNo());
        myViewHolder.tvAppointmentNo.setText(appointmentModel.getAppointmentNo());

        myViewHolder.tvVetName.setText(appointmentModel.getVetName());
        Utils.setProfileImage(context, appointmentModel.getVetProfilePic(),
                R.drawable.img_vet_profile, myViewHolder.imgClientProfilePic);

        return convertView;
    }


    private void setAppointmentStatus(AppointmentModel appointmentModel, TextView tvDateAndTime, TextView tvAppointmentStatus, ImageView imgCancel) {

        String dateAndTime;

        switch (appointmentModel.getAppointmentStatus()) {

            case Constants.REQUESTED:

                tvDateAndTime.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getTimeSlotName());

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.VISIBLE);
                break;

            case Constants.APPROVED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;

            case Constants.REJECTED:

                tvDateAndTime.setText(appointmentModel.getTimeSlotName());
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.orange, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;

            case Constants.CANCELLED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        // + "\n" + appointmentModel.getTimeSlotName();
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;

            case Constants.READY_TO_CALL:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);

                //   myViewHolder.tvAppointmentStatus.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;

            case Constants.ARCHIVED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                       /* + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime()*/;
                tvDateAndTime.setText(dateAndTime);

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;

            case Constants.APPOINTMENT_CANCEL:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getTimeSlotName();
                // + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                imgCancel.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private class MyViewHolder {

        private ImageView imgClientProfilePic, imgCancel;
        private TextView tvVetName, tvAppointmentNo, tvDateAndTime, tvAppointmentStatus;
        private RelativeLayout linClientAppointment;

        private MyViewHolder(View view) {

            imgClientProfilePic = (ImageView) view.findViewById(R.id.img_clientProfilePic);
            imgCancel = (ImageView) view.findViewById(R.id.img_requestCancel);

            tvVetName = (TextView) view.findViewById(R.id.tv_vetName);
            tvVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvDateAndTime = (TextView) view.findViewById(R.id.tv_timeAndDate);
            tvDateAndTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvAppointmentNo = (TextView) view.findViewById(R.id.tv_appointmentNo);
            tvAppointmentNo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvAppointmentStatus = (TextView) view.findViewById(R.id.tv_appointmentStatus);
            tvAppointmentStatus.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

            linClientAppointment = (RelativeLayout) view.findViewById(R.id.lin_clientAppointment);

        }
    }
}
