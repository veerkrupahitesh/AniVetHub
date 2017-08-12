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
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;

import java.util.List;

/**
 * Created by admin on 3/30/2017.
 */

public class AdpVetPracticeUser  extends BaseAdapter {

    private Context context;
    private List<VetPracticeUserModel> vetPracticeUserModels;

//    public AdpVetPracticeUser(Context context, List<VetPracticeUserModel> vetPractiseUserList) {
//        this.context = context;
//        this.vetPracticeUserModels = vetPractiseUserList;
//    }
    public AdpVetPracticeUser(Context context, List<VetPracticeUserModel> vetPractiseUserList) {
        this.context = context;
        this.vetPracticeUserModels = vetPractiseUserList;
    }
    @Override
    public int getCount() {
        return vetPracticeUserModels.size();
    }

    @Override
    public Object getItem(int i) {
        return vetPracticeUserModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_user_practise, viewGroup, false);
            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        VetPracticeUserModel vetPracticeUserModel = vetPracticeUserModels.get(i);

        if (i == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((i % 2) == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        if (vetPracticeUserModels != null) {

            viewHolder.imgDelete.setTag(vetPracticeUserModel);

            viewHolder.tvVetUser.setText(vetPracticeUserModel.getVetPractiseName());
        }
        return view;
    }
    public void refreshList(List<VetPracticeUserModel> vetPractiseUserList) {
        this.vetPracticeUserModels = vetPractiseUserList;
        notifyDataSetChanged();
    }

    public class ViewHolder {

        public LinearLayout linearLayout;
       // TextView tvExpertise/*, tvYear*/;
        ImageView /*imgEdit,*/ imgDelete;
        TextView tvVetUser;


        public ViewHolder(View view) {

            tvVetUser = (TextView) view.findViewById(R.id.tv_vetUserName);
            tvVetUser.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgDelete = (ImageView) view.findViewById(R.id.img_Delete);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_userPractise);

        }
    }
}
