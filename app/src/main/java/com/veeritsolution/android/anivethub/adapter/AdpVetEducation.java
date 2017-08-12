package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.DegreeModel;
import com.veeritsolution.android.anivethub.models.UniversityModel;

import java.util.List;

/**
 * Created by ${Hitesh} on 4/1/2017.
 */

public class AdpVetEducation extends BaseAdapter {

    private Context context;
    private List<?> list;

    public AdpVetEducation(Context context, List<?> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_location, parent, false);
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
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        Object obj = list.get(position);
        holder.tvVetEdu.setTag(obj);
//        holder.linearLayout.setTag(obj);

        if (obj instanceof UniversityModel) {
            //holder.tvVetEdu.setText(list.get(i));
            UniversityModel universityModel = (UniversityModel) obj;

            holder.tvVetEdu.setText(universityModel.getUniversityName());
        } else if (obj instanceof DegreeModel) {
            DegreeModel degreeModel = (DegreeModel) obj;

            holder.tvVetEdu.setText(degreeModel.getDegreeName());
        }

        return view;
    }

    private class Holder {

        private TextView tvVetEdu;
        LinearLayout linearLayout;

        public Holder(View view) {

            tvVetEdu = (TextView) view.findViewById(R.id.txtLocationName);
            tvVetEdu.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            linearLayout = (LinearLayout) view.findViewById(R.id.lin_location);

        }
    }
}
