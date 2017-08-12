package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;

import java.util.List;

/**
 * Created by admin on 4/3/2017.
 */

public class SpinnerUserAdapter extends BaseAdapter {

    private Context context;
    private List<VetPracticeUserModel> vetPracticeUserModelList;

    // private LayoutInflater inflater = null;

    public SpinnerUserAdapter(Context context, List<VetPracticeUserModel> vetPracticeUserModelList) {
        this.context = context;
        this.vetPracticeUserModelList = vetPracticeUserModelList;
        //  inflater = (LayoutInflater) context
        //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vetPracticeUserModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return vetPracticeUserModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.spinner_row_list, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VetPracticeUserModel vetPracticeUserModel = vetPracticeUserModelList.get(position);
        holder.tvSpinnerItem.setTag(vetPracticeUserModel);
        if (holder != null) {
            holder.tvSpinnerItem.setText(vetPracticeUserModel.getVetPractiseName());
        }
        return view;
    }

    private class Holder {

        TextView tvSpinnerItem;
        LinearLayout linearLayout;

        public Holder(View view) {

            tvSpinnerItem = (TextView) view.findViewById(R.id.spinnerItem);
            tvSpinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_symptoms);
        }
    }
}
