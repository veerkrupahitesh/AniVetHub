package com.veeritsolution.android.anivethub.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.PetPicsModel;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.List;

/**
 * Created by ${Hitesh} on 4/8/2017.
 */

public class AdpPetPics extends RecyclerView.Adapter<AdpPetPics.MyViewHolder> {

    private Activity context;
    private List<PetPicsModel> petPicsList;


    public AdpPetPics(Activity context, List<PetPicsModel> petPicsList) {
        this.context = context;
        this.petPicsList = petPicsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pet_pics, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        PetPicsModel petPicsModel = petPicsList.get(position);
        String petPicPath = petPicsModel.getPicPath();
        holder.imgDelete.setTag(petPicsModel);

        Utils.setProfileImage(context, petPicPath, R.drawable.img_pets,
                holder.imgPet, holder.progressBar);
    }


    @Override
    public int getItemCount() {
        return petPicsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPet, imgDelete;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        private MyViewHolder(View view) {
            super(view);

            imgPet = (ImageView) view.findViewById(R.id.img_petPic);
            imgDelete = (ImageView) view.findViewById(R.id.img_petPicDelete);
            progressBar = (ProgressBar) view.findViewById(R.id.prg_petPicLoading);
            //linearLayout = (LinearLayout) view.findViewById(R.id.lin_package_settings);
        }
    }
}

