package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.TimeSlotModel;

import java.util.List;

/**
 * Created by ${hitesh} on 2/16/2017.
 */

public class SpinnerTimeSlotAdapter extends ArrayAdapter<TimeSlotModel> {

    private Context context;
    private List<TimeSlotModel> timeSlotModelList;
    private int resource;

    public SpinnerTimeSlotAdapter(Context context, int resource, List<TimeSlotModel> timeSlotModelList) {
        super(context, resource, timeSlotModelList);
        this.context = context;
        this.timeSlotModelList = timeSlotModelList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return dropDownView(position, parent);

    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        convertView.setTag(timeSlotModelList.get(position));

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }/* else {
            spinnerItem.setBackgroundColor(Color.parseColor("#faebd7"));
            spinnerItem.setTextColor(Color.BLACK);
        }*/
        spinnerItem.setText(timeSlotModelList.get(position).getTimeSlotName());

        return convertView;
        // return null;
    }

    private View dropDownView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.spinner_drop_down_view, parent, false);
        convertView.setTag(timeSlotModelList.get(position));

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }/* else {
            spinnerItem.setBackgroundColor(Color.parseColor("#faebd7"));
            spinnerItem.setTextColor(Color.BLACK);
        }*/
        spinnerItem.setText(timeSlotModelList.get(position).getTimeSlotName());

        return convertView;
    }
}