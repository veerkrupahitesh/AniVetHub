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
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;

import java.util.List;

/**
 * Created by admin on 3/6/2017.
 */

public class SpinnerSymptomsAdapter extends ArrayAdapter<PetSymptomsModel> {

    private Context context;
    private List<PetSymptomsModel> petSymptomsModelList;
    private int resource;

    public SpinnerSymptomsAdapter(Context context, int resource, List<PetSymptomsModel> petSymptomsModelList) {
        super(context, resource, petSymptomsModelList);
        this.context = context;
        this.petSymptomsModelList=petSymptomsModelList;
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

        PetSymptomsModel petSymptomsModel = petSymptomsModelList.get(position);
        convertView.setTag(petSymptomsModel);

        TextView spinnerItem = (TextView)convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }

        spinnerItem.setText(petSymptomsModelList.get(position).getSymptomsName());

        return convertView;
        // return null;
    }

    private View dropDownView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.spinner_drop_down_view, parent, false);
        PetSymptomsModel petSymptomsModel = petSymptomsModelList.get(position);
        convertView.setTag(petSymptomsModel);

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }

        spinnerItem.setText(petSymptomsModelList.get(position).getSymptomsName());

        return convertView;
    }
}
