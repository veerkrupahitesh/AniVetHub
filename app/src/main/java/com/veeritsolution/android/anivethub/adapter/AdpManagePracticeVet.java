package com.veeritsolution.android.anivethub.adapter;

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
import com.veeritsolution.android.anivethub.models.PracticeModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitesh on 8/22/2017.
 */

public class AdpManagePracticeVet extends BaseAdapter {

    //private Context context;
    private List<PracticeModel> list;

    public AdpManagePracticeVet(List<PracticeModel> list) {
        // this.context = context;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_manage_practice_vet, parent, false);
            holder = new Holder(view);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }
        if (position == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(parent.getResources(), R.color.colourListBack, null));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        PracticeModel obj = list.get(position);
        obj.setPosition(position);
        holder.tvVetName.setTag(obj);
        holder.imgAcceptVet.setTag(obj);
        holder.imgRejectVet.setTag(obj);
//        holder.linearLayout.setTag(obj);
        holder.tvVetName.setText(obj.getVetName());
        int flag = obj.getFlag();
        if (flag == 0) {
            holder.imgAcceptVet.setImageResource(R.drawable.img_accept_gray);
            holder.imgRejectVet.setImageResource(R.drawable.img_reject_gray);

        } else if (flag == Constants.VET_ACCEPTED) {
            holder.imgAcceptVet.setImageResource(R.drawable.img_accept_green);
            holder.imgRejectVet.setImageResource(R.drawable.img_reject_gray);

        } else if (flag == Constants.VET_REJECTED) {
            holder.imgAcceptVet.setImageResource(R.drawable.img_accept_gray);
            holder.imgRejectVet.setImageResource(R.drawable.img_reject_red);
        }
        return view;
    }

    public void refreshList(ArrayList<PracticeModel> practiceList) {
        this.list = practiceList;
        notifyDataSetChanged();
    }

    private class Holder {

        LinearLayout linearLayout;
        ImageView imgAcceptVet, imgRejectVet;
        private TextView tvVetName;

        public Holder(View view) {

            tvVetName = (TextView) view.findViewById(R.id.tv_vetName);
            tvVetName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            imgAcceptVet = (ImageView) view.findViewById(R.id.img_acceptVet);
            imgRejectVet = (ImageView) view.findViewById(R.id.img_rejectVet);

            linearLayout = (LinearLayout) view.findViewById(R.id.lin_vetInfo);
        }
    }
}
