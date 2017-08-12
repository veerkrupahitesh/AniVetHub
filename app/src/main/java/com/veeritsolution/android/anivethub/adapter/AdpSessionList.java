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
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.models.SessionModel;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;


/**
 * Created by Jaymin on 1/31/2017.
 */

public class AdpSessionList extends BaseAdapter {

    private Context context;
    private List<SessionModel> sessionList;
    //  private LayoutInflater inflater = null;

    public AdpSessionList(Context context, List<SessionModel> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
        //  inflater = (LayoutInflater) context
        //         .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sessionList.size();
    }

    @Override
    public Object getItem(int i) {
        return sessionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_session, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }
        SessionModel sessionModel = sessionList.get(position);


        if (position == 0) {
            holder.llSession.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            holder.llSession.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            holder.llSession.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        if (holder != null) {
            int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);

            if (loginUser == Constants.CLIENT_LOGIN) {
                holder.tvVetPractise.setVisibility(View.GONE);
                holder.tvSessionNo.setText(sessionModel.getVetSessionNo());
                holder.tvClientName.setText(sessionModel.getVetName());
                holder.tvDateTime.setText(sessionModel.getCreatedOn());

                Utils.setProfileImage(context, sessionModel.getVetProfilePic(),
                        R.drawable.img_vet_profile, holder.imgProfilePic);

                holder.llSession.setTag(sessionModel);
              //  holder.imgSupportCenter.setTag(sessionModel);
            } else if (loginUser == Constants.VET_LOGIN) {
                holder.tvVetPractise.setVisibility(View.GONE);
                holder.tvSessionNo.setText(sessionModel.getVetSessionNo());
                holder.tvClientName.setText(sessionModel.getClientName());
                holder.tvDateTime.setText(sessionModel.getCreatedOn());

                Utils.setProfileImage(context, sessionModel.getClientProfilePic(),
                        R.drawable.img_client_profile, holder.imgProfilePic);

                holder.llSession.setTag(sessionModel);
             //   holder.imgSupportCenter.setTag(sessionModel);
            } else if (loginUser == Constants.CLINIC_LOGIN) {
                holder.tvVetPractise.setVisibility(View.VISIBLE);
                holder.tvVetPractise.setText(sessionModel.getVetPractiseName());
                holder.tvSessionNo.setText(sessionModel.getVetSessionNo());
                holder.tvClientName.setText(sessionModel.getClientName());
                holder.tvDateTime.setText(sessionModel.getCreatedOn());

                Utils.setProfileImage(context, sessionModel.getClientProfilePic(),
                        R.drawable.img_client_profile, holder.imgProfilePic);

                holder.llSession.setTag(sessionModel);
               // holder.imgSupportCenter.setTag(sessionModel);
            }
        }
        return view;
    }

    public class Holder {

        TextView tvClientName, tvDateTime, tvSessionNo, tvVetPractise;
        ImageView imgProfilePic/*, imgSupportCenter*/;
        LinearLayout llSession;
        //  public LinearLayout linearLayout;

        public Holder(View view) {

            tvVetPractise = (TextView) view.findViewById(R.id.tv_vetPractise);
            tvVetPractise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvClientName = (TextView) view.findViewById(R.id.tv_clientName);
            tvClientName.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvSessionNo = (TextView) view.findViewById(R.id.tv_session_number);
            tvSessionNo.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvDateTime = (TextView) view.findViewById(R.id.tv_timeAndDate);
            tvDateTime.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgProfilePic = (ImageView) view.findViewById(R.id.img_vetProfilePic);
            llSession = (LinearLayout) view.findViewById(R.id.lin_session);

           // imgSupportCenter = (ImageView) view.findViewById(R.id.img_supportCenter);

        }
    }
}
