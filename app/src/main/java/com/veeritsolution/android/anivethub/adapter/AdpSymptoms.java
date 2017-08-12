package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.PetSymptomsModel;

import java.util.List;

/**
 * Created by Jaymin on 12/31/2016.
 */

public class AdpSymptoms extends BaseAdapter {

    private Context context;
    private List<PetSymptomsModel> symptomsList;
    // private LayoutInflater inflater = null;

    public AdpSymptoms(Context context, List<PetSymptomsModel> symptomsList) {
        this.context = context;
        this.symptomsList = symptomsList;
        //  inflater = (LayoutInflater) context
        //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return symptomsList.size();
    }

    @Override
    public Object getItem(int i) {
        return symptomsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.list_item_symptoms, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if (position == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
        PetSymptomsModel petSymptomsModel = symptomsList.get(position);
        holder.linearLayout.setTag(petSymptomsModel);
        if (holder != null) {
            holder.tv_symptoms.setText(petSymptomsModel.getSymptomsName());
        }
        return view;
    }

    private class Holder {

        TextView tv_symptoms;
        LinearLayout linearLayout;

        public Holder(View view) {

            tv_symptoms = (TextView) view.findViewById(R.id.tv_symptoms);
            tv_symptoms.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            linearLayout = (LinearLayout) view.findViewById(R.id.lin_symptoms);
        }
    }
}
