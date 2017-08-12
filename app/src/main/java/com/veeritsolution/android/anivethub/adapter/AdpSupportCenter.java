package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.helper.PrefHelper;
import com.veeritsolution.android.anivethub.models.SupportCenterModel;
import com.veeritsolution.android.anivethub.utility.Constants;

import java.util.ArrayList;

/**
 * Created by veerk on 3/15/2017.
 */

public class AdpSupportCenter extends BaseAdapter {

    Context context;
    ArrayList<SupportCenterModel> supportCenterList;

    public AdpSupportCenter(Context context, ArrayList<SupportCenterModel> supportCenterList) {
        this.context = context;
        this.supportCenterList = supportCenterList;
    }

    @Override
    public int getCount() {
        return supportCenterList.size();
    }

    @Override
    public Object getItem(int position) {
        return supportCenterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_support_center, parent, false);
            holder = new Holder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        SupportCenterModel supportCenterModel = supportCenterList.get(position);
        int loginUser = PrefHelper.getInstance().getInt(PrefHelper.LOGIN_USER, 0);

          if (loginUser == Constants.CLIENT_LOGIN) {

            if (supportCenterModel.getFlag() == Constants.CLIENT_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getClientName());
                holder.tvAuthor.setGravity(Gravity.END);
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.tvMessage.setGravity(Gravity.END);
                holder.linChatParent.setGravity(Gravity.END);
                holder.linChat.setBackgroundResource(R.drawable.client_writer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chatright);

            } else if (supportCenterModel.getFlag() == Constants.VET_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getVetName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.vet_doctor_viewer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chat);
            } else if (supportCenterModel.getFlag() == Constants.ADMIN_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getLoginName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.vet_clinic_viewer);
            }

        } else if (loginUser == Constants.VET_LOGIN) {

            if (supportCenterModel.getFlag() == Constants.VET_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getVetName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.END);
                holder.linChat.setBackgroundResource(R.drawable.vet_doctor_writer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chat);
            }
            if (supportCenterModel.getFlag() == Constants.CLIENT_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getClientName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.client_viewer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chatright);

            } else if (supportCenterModel.getFlag() == Constants.ADMIN_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getLoginName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.vet_clinic_viewer);
            }
        } else if (loginUser == Constants.CLINIC_LOGIN) {

            if (supportCenterModel.getFlag() == Constants.VET_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getVetName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.END);
                holder.linChat.setBackgroundResource(R.drawable.vet_doctor_writer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chat);
            }
            if (supportCenterModel.getFlag() == Constants.CLIENT_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getClientName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.client_viewer);
                //holder.tvMessage.setBackgroundResource(R.drawable.chatright);

            } else if (supportCenterModel.getFlag() == Constants.ADMIN_SUPPORT_CENTER_ID) {

                holder.tvAuthor.setText(supportCenterModel.getLoginName());
                holder.tvMessage.setText(supportCenterModel.getRemarks());
                holder.linChatParent.setGravity(Gravity.START);
                holder.linChat.setBackgroundResource(R.drawable.vet_clinic_viewer);
            }
        }

        return convertView;
    }

    class Holder {

        TextView tvAuthor, tvMessage;
        LinearLayout linChat, linChatParent;

        public Holder(View view) {

            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvAuthor.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

            tvMessage = (TextView) view.findViewById(R.id.tv_message);
            tvMessage.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            linChat = (LinearLayout) view.findViewById(R.id.lin_chat);
            linChatParent = (LinearLayout) view.findViewById(R.id.lin_chat_parent);
        }
    }
}
