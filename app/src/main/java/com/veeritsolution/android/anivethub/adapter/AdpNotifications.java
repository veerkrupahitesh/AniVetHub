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
import com.veeritsolution.android.anivethub.models.NotificationModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.ArrayList;

/**
 * Created by veerk on 3/22/2017.
 */

public class AdpNotifications extends BaseAdapter {

    private Context context;
    private ArrayList<NotificationModel> notificationList;


    public AdpNotifications(Context context, ArrayList<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_notifications, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NotificationModel notificationModel = notificationList.get(position);

        viewHolder.rlNotification.setTag(notificationModel);

        if (position == 0) {
            viewHolder.rlNotification.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            viewHolder.rlNotification.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.rlNotification.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }
        Utils.setProfileImage(context, notificationModel.getImagePath(), R.color.grey, viewHolder.imgNotification);
        viewHolder.tvNotificationTitle.setText(notificationModel.getTitle());
        viewHolder.tvNotificationDetails.setText(notificationModel.getRemarks());

        return convertView;
    }

    private class ViewHolder {

        private ImageView imgNotification;
        private TextView tvNotificationTitle, tvNotificationDetails;
       // private LinearLayout linNotifications;
        private RelativeLayout rlNotification;

        ViewHolder(View convertView) {

            imgNotification = (ImageView) convertView.findViewById(R.id.img_notification);

            tvNotificationTitle = (TextView) convertView.findViewById(R.id.tv_notification_title);
            tvNotificationDetails = (TextView) convertView.findViewById(R.id.tv_notification_details);

           // linNotifications = (LinearLayout) convertView.findViewById(R.id.lin_notification);
            rlNotification = (RelativeLayout) convertView.findViewById(R.id.rl_notification);

            //  Set Font Type
            tvNotificationTitle.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
            tvNotificationDetails.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        }
    }
}
