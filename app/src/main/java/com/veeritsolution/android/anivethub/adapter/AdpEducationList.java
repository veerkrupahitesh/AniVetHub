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
import com.veeritsolution.android.anivethub.models.VetEducation;

import java.util.List;

/**
 * Created by admin on 1/2/2017.
 */

public class AdpEducationList extends BaseAdapter {

    private Context context;
    private List<VetEducation> vetEducationList;

    public AdpEducationList(Context context, List<VetEducation> vetEducationList) {
        this.context = context;
        this.vetEducationList = vetEducationList;
    }

    @Override
    public int getCount() {
        return vetEducationList.size();
    }

    @Override
    public Object getItem(int i) {
        return vetEducationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_education, viewGroup, false);
            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        VetEducation vetEducation = vetEducationList.get(position);

        if (position == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        viewHolder.imgEdit.setTag(vetEducation);
        viewHolder.imgDelete.setTag(vetEducation);

        viewHolder.tvEducation.setText(vetEducation.getDegreeName());
        viewHolder.tvYear.setText(vetEducation.getPassingYear());

        return view;
    }

    public void refreshList(List<VetEducation> vetEducationList) {
        this.vetEducationList = vetEducationList;
        notifyDataSetChanged();
    }

    public class ViewHolder {

        public LinearLayout linearLayout;
        TextView tvEducation, tvYear;
        ImageView imgEdit, imgDelete;

        public ViewHolder(View view) {

            tvEducation = (TextView) view.findViewById(R.id.tv_Education);
            tvEducation.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            tvYear = (TextView) view.findViewById(R.id.tv_Year);
            tvEducation.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgEdit = (ImageView) view.findViewById(R.id.img_Edit);
            imgDelete = (ImageView) view.findViewById(R.id.img_Delete);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_educationList);
        }
    }
}
