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

import java.util.List;

/**
 * Created by aas2 on 03-06-2016.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> spinnerList;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        this.context = context;
        spinnerList = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);

    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.spinner_row_list, parent, false);

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }/* else {
            spinnerItem.setBackgroundColor(Color.parseColor("#faebd7"));
            spinnerItem.setTextColor(Color.BLACK);
        }*/

        spinnerItem.setText(spinnerList.get(position));

        return convertView;

    }
}
