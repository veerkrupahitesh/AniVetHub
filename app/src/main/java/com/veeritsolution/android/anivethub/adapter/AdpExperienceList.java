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
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by veerk on 1/5/2017.
 */

public class AdpExperienceList extends BaseAdapter {

    private Context context;
    private List<VetExperience> vetExpertiseList;
    private boolean isFromActivity;

    public AdpExperienceList(Context context, List<VetExperience> vetExpertiseList, boolean isFromActivity) {
        this.context = context;
        this.vetExpertiseList = vetExpertiseList;
        this.isFromActivity = isFromActivity;
    }

    @Override
    public int getCount() {
        return vetExpertiseList.size();
    }

    @Override
    public Object getItem(int i) {
        return vetExpertiseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_experience, viewGroup, false);
            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        VetExperience vetExpertise = vetExpertiseList.get(position);

        if (position == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        viewHolder.imgEdit.setTag(vetExpertise);
        viewHolder.imgDelete.setTag(vetExpertise);

        if (isFromActivity){
            viewHolder.imgEdit.setVisibility(View.GONE);
            viewHolder.imgDelete.setVisibility(View.GONE);
        }else {
            viewHolder.imgEdit.setVisibility(View.VISIBLE);
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
        }
        viewHolder.tvExpertise.setText(vetExpertise.getTitle());

        viewHolder.tvYear.setText(Utils.formatDate(vetExpertise.getFromDate(), Constants.DATE_MM_DD_YYYY, Constants.DATE_YYYY)
                + " - " + Utils.formatDate(vetExpertise.getToDate(), Constants.DATE_MM_DD_YYYY, Constants.DATE_YYYY));

        return view;
    }

    public void refreshList(List<VetExperience> vetExpertiseList) {
        this.vetExpertiseList = vetExpertiseList;
        notifyDataSetChanged();
    }

    private class ViewHolder {

        private TextView tvExpertise, tvYear;
        private ImageView imgEdit, imgDelete;
        private LinearLayout linearLayout;

        private ViewHolder(View view) {

            tvExpertise = (TextView) view.findViewById(R.id.tv_Experience);
            tvExpertise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvYear = (TextView) view.findViewById(R.id.tv_desc);
            tvExpertise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgEdit = (ImageView) view.findViewById(R.id.img_Edit);
            imgDelete = (ImageView) view.findViewById(R.id.img_Delete);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_experienceList);
        }
    }
}