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
import com.veeritsolution.android.anivethub.models.PetDetailsModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.Collections;
import java.util.List;

/**
 * Created by ${hitesh} on 2/1/2017.
 */

public class AdpPetList extends BaseAdapter {

    private Context context;
    private List<PetDetailsModel> petList = Collections.emptyList();
    private boolean fromActivity;

    public AdpPetList(Context context, List<PetDetailsModel> petList, boolean fromActivity) {
        this.context = context;
        this.petList = petList;
        this.fromActivity = fromActivity;
    }

    public void refreshList(List<PetDetailsModel> list) {
        petList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public Object getItem(int position) {
        return petList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder myViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_pets, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        PetDetailsModel petDetailsModel = petList.get(position);

        if (position == 0) {
            myViewHolder.linPetList.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else if ((position % 2) == 0) {
            myViewHolder.linPetList.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colourListBack, null));
        } else {
            myViewHolder.linPetList.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        }

        String picPath = petDetailsModel.getClientPetPicPath();

        if (picPath.length() != 0) {
           /* Glide.with(context).load(picPath)
                    .dontAnimate()
                    .placeholder(R.drawable.img_pets)
                    .error(R.drawable.img_pets)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(myViewHolder.imgpet);*/

            Utils.setProfileImage(context, petDetailsModel.getClientPetPicPath(), R.drawable.img_pets, myViewHolder.imgpet);
        } else {
            Utils.setProfileImage(context, R.drawable.img_pets, R.drawable.img_pets, myViewHolder.imgpet);
            // myViewHolder.imgpet.setImageResource(R.drawable.img_pets);
        }

        //myViewHolder.imgdelete.setTag(petDetailsModel);
        if (!fromActivity) {
            myViewHolder.imgdelete.setTag(position);
            myViewHolder.imgedit.setTag(position);
            myViewHolder.imgGraph.setTag(position);
            myViewHolder.imgTreatment.setTag(position);
            myViewHolder.linPetList.setTag(position);
        } else {
            myViewHolder.linPetList.setTag(petDetailsModel);
            myViewHolder.imgedit.setVisibility(View.INVISIBLE);
            myViewHolder.imgdelete.setVisibility(View.INVISIBLE);
            myViewHolder.imgGraph.setVisibility(View.INVISIBLE);
            myViewHolder.imgTreatment.setVisibility(View.INVISIBLE);
        }

        myViewHolder.txvpetname.setText(petDetailsModel.getPetName());
        myViewHolder.txvpettypename.setText(petDetailsModel.getPetTypeName());
        return convertView;
    }

    public class MyViewHolder {

        ImageView imgpet, imgedit, imgdelete, imgGraph, imgTreatment;
        TextView txvpetname, txvpettypename;
        LinearLayout linPetList;

        public MyViewHolder(View itemView) {

            imgpet = (ImageView) itemView.findViewById(R.id.img_pet);

            txvpetname = (TextView) itemView.findViewById(R.id.tv_petName);
            txvpetname.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            txvpettypename = (TextView) itemView.findViewById(R.id.tv_petTypeName);
            txvpettypename.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgGraph = (ImageView) itemView.findViewById(R.id.img_graph);
            imgedit = (ImageView) itemView.findViewById(R.id.img_edit);
            imgdelete = (ImageView) itemView.findViewById(R.id.img_delete);
            linPetList = (LinearLayout) itemView.findViewById(R.id.lin_petList);
            imgTreatment = (ImageView) itemView.findViewById(R.id.img_treatment);
        }
    }
}
