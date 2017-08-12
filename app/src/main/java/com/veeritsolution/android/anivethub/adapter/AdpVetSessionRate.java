package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
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
import com.veeritsolution.android.anivethub.models.VetSessionRateModel;
import com.veeritsolution.android.anivethub.utility.Debug;

import java.util.List;

/**
 * Created by admin on 3/17/2017.
 */

public class AdpVetSessionRate extends BaseAdapter {

    private Context context;
    private List<VetSessionRateModel> vetSessionRateModelList;
    // private LayoutInflater inflater = null;

    public AdpVetSessionRate(Context context, List<VetSessionRateModel> vetSessionRateModelList) {
        this.context = context;
        this.vetSessionRateModelList = vetSessionRateModelList;
        //  inflater = (LayoutInflater) context
        //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vetSessionRateModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return vetSessionRateModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.fragment_session_rate_list_item, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VetSessionRateModel vetSessionRateModel = vetSessionRateModelList.get(position);
        // holder.linearLayout.setTag(petSymptomsModel);
        holder.img_delete.setTag(vetSessionRateModel);
        holder.img_edit.setTag(vetSessionRateModel);


        if (holder != null) {

            Debug.trace("id", "" + vetSessionRateModel.getVetRateId());
            Debug.trace("day", "" + vetSessionRateModel.getDayNo());
            Debug.trace("normal  time hours:", "" + vetSessionRateModel.getNormalSessionRate());
            Debug.trace("special  time hours:", "" + vetSessionRateModel.getSpecialSessionRate());
            if (position == 0) {
                holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
            } else if ((position % 2) == 0) {
                holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
            } else {
                holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
            }
            if (vetSessionRateModel.getDayNo() == 1) {
                holder.tv_session_day.setText("Monday");
            } else if (vetSessionRateModel.getDayNo() == 2) {
                holder.tv_session_day.setText("Tuesday");
            } else if (vetSessionRateModel.getDayNo() == 3) {
                holder.tv_session_day.setText("Wednesday");
            } else if (vetSessionRateModel.getDayNo() == 4) {
                holder.tv_session_day.setText("Thrusday");
            } else if (vetSessionRateModel.getDayNo() == 5) {
                holder.tv_session_day.setText("Friday");
            } else if (vetSessionRateModel.getDayNo() == 6) {
                holder.tv_session_day.setText("Saturday");
            } else if (vetSessionRateModel.getDayNo() == 7) {
                holder.tv_session_day.setText("Sunday");
            }

            holder.tv_session_normal_rate.setText(String.valueOf(vetSessionRateModel.getNormalSessionRate()));
            holder.tv_session_special_rate.setText(String.valueOf(vetSessionRateModel.getSpecialSessionRate()));
        }
        return view;
    }

    private class Holder {

        TextView tv_session_day, tv_session_normal_rate, tv_session_special_rate, lbl_normal_hrs, lbl_special_hrs;
        ImageView img_edit, img_delete;
        RelativeLayout linearLayout;

        private Holder(View view) {

            linearLayout = (RelativeLayout) view.findViewById(R.id.lin_session_rate);
            tv_session_day = (TextView) view.findViewById(R.id.txv_session_day);
            tv_session_day.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tv_session_normal_rate = (TextView) view.findViewById(R.id.txv_normal_rate);
            tv_session_normal_rate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tv_session_special_rate = (TextView) view.findViewById(R.id.txv_special_rate);
            tv_session_special_rate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            lbl_normal_hrs = (TextView) view.findViewById(R.id.lbl_normal_rate);
            lbl_normal_hrs.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            lbl_special_hrs = (TextView) view.findViewById(R.id.lbl_special_rate);
            lbl_special_hrs.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            img_edit = (ImageView) view.findViewById(R.id.img_edit);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);

            //linearLayout = (LinearLayout) view.findViewById(R.id.lin_symptoms);
        }
    }
}
