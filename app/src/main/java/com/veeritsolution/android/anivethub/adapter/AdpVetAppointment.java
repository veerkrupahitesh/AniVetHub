package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.AppointmentModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by Jaymin on 2/8/2017.
 */

public class AdpVetAppointment extends BaseAdapter {

    List<AppointmentModel> appointmentModelList;
    private Context context;

    public AdpVetAppointment(Context context, List<AppointmentModel> appointmentList) {

        this.context = context;
        this.appointmentModelList = appointmentList;
    }

    @Override
    public int getCount() {
        return appointmentModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        MyViewHolder myViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_appointment, viewGroup, false);
            myViewHolder = new MyViewHolder(view);
            view.setTag(myViewHolder);

        } else {
            myViewHolder = (MyViewHolder) view.getTag();
        }

        ImageView imgProfilePic = (ImageView) view.findViewById(R.id.img_vetProfilePic);

        TextView tvClientName = (TextView) view.findViewById(R.id.tv_clientName);
        tvClientName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView tvAppointmentNo = (TextView) view.findViewById(R.id.tv_appointmentNo);
        tvAppointmentNo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvVetPractise = (TextView) view.findViewById(R.id.tv_vetPractise);
        tvVetPractise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        TextView tvDateAndTime = (TextView) view.findViewById(R.id.tv_timeAndDate);
        tvDateAndTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvAppointmentStatus = (TextView) view.findViewById(R.id.tv_appointmentStatus);
        tvAppointmentStatus.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

        LinearLayout linAppointment = (LinearLayout) view.findViewById(R.id.lin_appointment);

        AppointmentModel appointmentModel = appointmentModelList.get(position);

        if (myViewHolder != null) {

            if (position == 0) {
                myViewHolder.linAppointment.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
            } else if ((position % 2) == 0) {
                myViewHolder.linAppointment.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
            } else {
                myViewHolder.linAppointment.setBackgroundColor(Color.WHITE);
            }

            linAppointment.setTag(appointmentModel);
            tvAppointmentStatus.setTag(appointmentModel);
            linAppointment.setGravity(Gravity.CENTER);
            tvVetPractise.setText(appointmentModel.getVetPractiseName());

            Utils.setProfileImage(context, appointmentModel.getClientProfilePic(),
                    R.drawable.img_client_profile, imgProfilePic);
            tvClientName.setText(appointmentModel.getClientName());
            tvAppointmentNo.setText(appointmentModel.getAppointmentNo());
            setAppointmentStatus(appointmentModel, tvDateAndTime, tvAppointmentStatus);

        }
        return view;
    }

    private void setAppointmentStatus(AppointmentModel appointmentModel, TextView tvDateAndTime, TextView tvAppointmentStatus) {

        String dateAndTime;

        switch (appointmentModel.getAppointmentStatus()) {

            case Constants.REQUESTED:

                tvDateAndTime.setText(Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getTimeSlotName());
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));
                tvAppointmentStatus.setText("New");
                break;

            case Constants.APPROVED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;

            case Constants.REJECTED:

                tvDateAndTime.setText(appointmentModel.getTimeSlotName());
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.orange, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;

            case Constants.CANCELLED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;

            case Constants.READY_TO_CALL:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;

            case Constants.ARCHIVED:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                       /* + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime()*/;
                tvDateAndTime.setText(dateAndTime);
                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;

            case Constants.APPOINTMENT_CANCEL:

                dateAndTime = Utils.formatDate(appointmentModel.getAppointmentDate(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY)
                        + "\n" + appointmentModel.getTimeSlotName();
                // + "\n" + appointmentModel.getFromTime() + "-" + appointmentModel.getToTime();
                tvDateAndTime.setText(dateAndTime);

                tvAppointmentStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null));
                tvAppointmentStatus.setText(appointmentModel.getAppointmentStatusDisp());
                break;
        }
    }

    public void setList(List<AppointmentModel> appointmentList) {
        this.appointmentModelList = appointmentList;
        notifyDataSetChanged();
    }

    public class MyViewHolder {

        //TextView tvClientName, tvDateAndTime, tvAppointmentStatus;
        // ImageView imgProfilePic;
        LinearLayout linAppointment;

        public MyViewHolder(View view) {

         /*   imgProfilePic = (ImageView) view.findViewById(R.id.img_vetProfilePic);

            tvClientName = (TextView) view.findViewById(R.id.tv_clientName);
            tvClientName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvDateAndTime = (TextView) view.findViewById(R.id.tv_timeAndDate);
            tvDateAndTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvAppointmentStatus = (TextView) view.findViewById(R.id.tv_appointmentStatus);
            tvAppointmentStatus.setTypeface(MyApplication.g
            etInstance().FONT_ROBOTO_BOLD);*/
            linAppointment = (LinearLayout) view.findViewById(R.id.lin_appointment);

        }
    }

}

