package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.TreatmentModel;

import java.util.List;

import static com.veeritsolution.android.anivethub.R.attr.position;

/**
 * Created by admin on 3/1/2017.
 */

public class AdpPetTreatment extends BaseAdapter {

    private Context context;
    private List<TreatmentModel> treatmentModelList;
    private boolean forShowOnly;

    public AdpPetTreatment(Context context, List<TreatmentModel> treatmentModelList, boolean forShowOnly) {
        this.context = context;
        this.treatmentModelList = treatmentModelList;
        this.forShowOnly = forShowOnly;

    }

    @Override
    public int getCount() {
        return treatmentModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return treatmentModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.list_item_treatment, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tv_treatment.setTag(position);

        if (forShowOnly) {
            holder.imgedit.setVisibility(View.GONE);
            holder.imgdelete.setVisibility(View.GONE);
        } else {
            holder.imgedit.setVisibility(View.VISIBLE);
            holder.imgdelete.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            holder.lin_treatment.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.lin_treatment.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            holder.lin_treatment.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
        TreatmentModel petTreatmentModel = treatmentModelList.get(i);
        holder.lin_treatment.setTag(petTreatmentModel);
        holder.imgedit.setTag(petTreatmentModel);
        holder.imgdelete.setTag(petTreatmentModel);

        if (holder != null) {
            holder.tv_symptomps.setText(petTreatmentModel.getSymptomsName());
            // holder.tv_symptomps.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            holder.tv_treatment.setText(petTreatmentModel.getFromDate() + " - " + petTreatmentModel.getToDate());
            // holder.tv_treatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        }

        return view;
    }

    private class Holder {
        TextView tv_treatment, tv_symptomps;
        LinearLayout lin_treatment;
        ImageView imgedit, imgdelete;

        public Holder(View view) {

            tv_treatment = (TextView) view.findViewById(R.id.txv_treatment);
            tv_treatment.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tv_symptomps = (TextView) view.findViewById(R.id.txv_symptomps);
            tv_symptomps.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            lin_treatment = (LinearLayout) view.findViewById(R.id.lin_treatment);
            imgedit = (ImageView) view.findViewById(R.id.img_edit);
            imgdelete = (ImageView) view.findViewById(R.id.img_delete);

        }
    }
}
