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
import com.veeritsolution.android.anivethub.models.VetSubscriptionModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by ${hitesh} on 2/1/2017.
 */

public class AdpVetSubscription extends BaseAdapter {

    private Context context;
    private List<VetSubscriptionModel> vetSubscriptionList;

    public AdpVetSubscription(Context context, List<VetSubscriptionModel> vetSubscriptionList) {
        this.context = context;
        this.vetSubscriptionList = vetSubscriptionList;
    }

    @Override
    public int getCount() {
        return vetSubscriptionList.size();
    }

    @Override
    public Object getItem(int position) {
        return vetSubscriptionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_subscription, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        VetSubscriptionModel vetSubscriptionModel = vetSubscriptionList.get(position);

        if (position == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }

        holder.tvSessions.setText(vetSubscriptionModel.getSessionBuy() + " Credits");
        holder.tvAmount.setText("$ " + vetSubscriptionModel.getPaymentAmount());
        holder.tvDate.setText(Utils.formatDate(vetSubscriptionModel.getCreatedOn(), Constants.MM_DD_YYYY_HH_MM_SS_A, Constants.DATE_MM_DD_YYYY));
        return convertView;
    }

    public class Holder {

        public LinearLayout linearLayout;
        TextView tvSessions, tvAmount, tvDate;

        public Holder(View view) {

            tvSessions = (TextView) view.findViewById(R.id.tv_sessions);
            tvSessions.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvAmount.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvDate.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_vetSubscriptionList);
        }
    }
}
