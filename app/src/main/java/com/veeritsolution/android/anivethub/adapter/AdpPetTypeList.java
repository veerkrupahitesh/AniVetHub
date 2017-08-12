package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.PetBreedModel;
import com.veeritsolution.android.anivethub.models.PetTypeModel;

import java.util.List;

/**
 * Created by admin on 3/10/2017.
 */

public class AdpPetTypeList extends BaseAdapter {

    private Context context;
    private List<?> list;

    public AdpPetTypeList(Context context, List<?> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_location, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        Object obj = list.get(i);
        holder.tvPetTypeAndBreed.setTag(obj);
//        holder.linearLayout.setTag(obj);

        if (obj instanceof PetTypeModel) {
            //holder.tvPetTypeAndBreed.setText(list.get(i));
            PetTypeModel petTypeModel = (PetTypeModel) obj;
            holder.tvPetTypeAndBreed.setText(petTypeModel.getPetTypeName());
        } else if (obj instanceof PetBreedModel) {
            PetBreedModel petBreedModel = (PetBreedModel) obj;
            holder.tvPetTypeAndBreed.setText(petBreedModel.getPetBreedName());
        }

        return view;
    }

    private class Holder {

        private TextView tvPetTypeAndBreed;
        //  LinearLayout linearLayout;

        public Holder(View view) {

            tvPetTypeAndBreed = (TextView) view.findViewById(R.id.txtLocationName);
            tvPetTypeAndBreed.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);
            // linearLayout = (LinearLayout) view.findViewById(R.id.lstitem);

        }
    }


}
