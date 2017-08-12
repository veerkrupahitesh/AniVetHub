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
import com.veeritsolution.android.anivethub.models.VetSpecialization;

import java.util.List;

/**
 * Created by veerk on 1/5/2017.
 */

public class AdpVetSpecialisation extends BaseAdapter {

    private Context context;
    private List<VetSpecialization> vetSpecializationList;

    public AdpVetSpecialisation(Context context, List<VetSpecialization> vetSpecializationList) {
        this.context = context;
        this.vetSpecializationList = vetSpecializationList;
    }

    @Override
    public int getCount() {
        return vetSpecializationList.size();
    }

    @Override
    public Object getItem(int i) {
        return vetSpecializationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_vet_expertise, viewGroup, false);
            viewHolder = new ViewHolder(view);

            view.setTag(viewHolder);
        } else {
            viewHolder = (AdpVetSpecialisation.ViewHolder) view.getTag();
        }

        VetSpecialization vetSpecialization = vetSpecializationList.get(position);

        if (position == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.linearLayout.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        if (vetSpecialization != null) {
//            viewHolder.imgEdit.setTag(vetSpecialization);
            viewHolder.imgDelete.setTag(vetSpecialization);

            viewHolder.tvExpertise.setText(vetSpecialization.getPetTypeGroupName());

           // viewHolder.ratings.setRating(vetSpecialization.getProficiency());
          /*  if (vetSpecialization.getPetBreedName()==null) {
                viewHolder.tvYear.setText("No Breed");
            } else {
                viewHolder.tvYear.setText(vetSpecialization.getPetBreedName());
            }*/
        }
        return view;
    }

    public void refreshList(List<VetSpecialization> vetSpecializationList) {
        this.vetSpecializationList = vetSpecializationList;
        notifyDataSetChanged();
    }

    public class ViewHolder {

        public LinearLayout linearLayout;
        TextView tvExpertise/*, tvYear*/;
      //  RatingBar ratings;
        ImageView imgEdit, imgDelete;

        public ViewHolder(View view) {

            tvExpertise = (TextView) view.findViewById(R.id.tv_Expertise);
            tvExpertise.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

           // ratings = (RatingBar) view.findViewById(R.id.rating_specialisation);

            //tvYear = (TextView) view.findViewById(R.id.tv_desc);
            //tvYear.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            // = (ImageView) view.findViewById(R.id.img_Edit);
            imgDelete = (ImageView) view.findViewById(R.id.img_Delete);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_expertiseList);
        }
    }
}
