package com.veeritsolution.android.anivethub.adapter;

import android.app.Activity;
import android.graphics.Color;
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
import com.veeritsolution.android.anivethub.models.VetTimeSlotModel;

import java.util.List;

/**
 * Created by ${hitesh} on 2/15/2017.
 */

public class AdpVetTimeSlot extends BaseAdapter {


    private Activity activity;
    private List<VetTimeSlotModel> vetTimeSlotModelList;

    public AdpVetTimeSlot(Activity activity, List<VetTimeSlotModel> vetTimeSlotModelList) {
        this.activity = activity;
        this.vetTimeSlotModelList = vetTimeSlotModelList;
    }

    @Override
    public int getCount() {
        return vetTimeSlotModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return vetTimeSlotModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_item_time_slot, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color.colourListBack, null));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }

        holder.imgDelete.setTag(position);

        VetTimeSlotModel timeSlotModel = vetTimeSlotModelList.get(position);

        holder.imgDelete.setTag(timeSlotModel);

        holder.tvTimeSlot.setText(timeSlotModel.getTimeSlotName());

        return convertView;
    }

    public void setList(List<VetTimeSlotModel> vetTimeSlotModelList) {
        this.vetTimeSlotModelList = vetTimeSlotModelList;
        notifyDataSetChanged();
    }

    class Holder {

        public LinearLayout linearLayout;
        TextView tvTimeSlot/*, tvAmount, tvDate*/;
        ImageView imgDelete;

        public Holder(View view) {

            tvTimeSlot = (TextView) view.findViewById(R.id.tv_timeSlot);
            tvTimeSlot.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgDelete = (ImageView) view.findViewById(R.id.img_delete);
            //  tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            //  tvAmount.setTypeface(MyApplication.getInstance().FONT_ROBOTO_BOLD);

            //   tvDate = (TextView) view.findViewById(R.id.tv_date);
            //   tvDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_timeSlot);
        }
    }
}
