package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.VetPracticeUserModel;

import java.util.List;

/**
 * Created by admin on 4/3/2017.
 */

public class SpinnerUserAdapter extends ArrayAdapter<VetPracticeUserModel> {

    private Context context;
    private List<VetPracticeUserModel> vetPracticeUserModelList;

    // private LayoutInflater inflater = null;

    public SpinnerUserAdapter(@NonNull Context context, @LayoutRes int resource, List<VetPracticeUserModel> vetPracticeUserModelList) {
        super(context, resource);
        this.context = context;
        this.vetPracticeUserModelList = vetPracticeUserModelList;
    }

    /*public SpinnerUserAdapter(Context context, List<VetPracticeUserModel> vetPracticeUserModelList) {
        this.context = context;
        this.vetPracticeUserModelList = vetPracticeUserModelList;
        //  inflater = (LayoutInflater) context
        //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    @Override
    public int getCount() {
        return vetPracticeUserModelList.size();
    }

    /*  @Override
      public Object getItem(int i) {
          return vetPracticeUserModelList.get(i);
      }
  */
    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return dropDownView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.spinner_row_list, parent, false);
        VetPracticeUserModel vetPracticeUserModel = vetPracticeUserModelList.get(position);
        convertView.setTag(vetPracticeUserModel);

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }

        spinnerItem.setText(vetPracticeUserModel.getVetPractiseName());

        return convertView;
    }

    private View dropDownView(int position, ViewGroup parent) {

        View convertView = LayoutInflater.from(context).inflate(R.layout.spinner_drop_down_view, parent, false);
        VetPracticeUserModel vetPracticeUserModel = vetPracticeUserModelList.get(position);
        convertView.setTag(vetPracticeUserModel);

        TextView spinnerItem = (TextView) convertView.findViewById(R.id.spinnerItem);
        spinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        if (position == 0) {
            // spinnerItem.setBackgroundColor(Color.parseColor("#b0a3fc"));
            spinnerItem.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.hint, null));
        }

        spinnerItem.setText(vetPracticeUserModel.getVetPractiseName());

        return convertView;
    }
    // return null;

    private class Holder {

        TextView tvSpinnerItem;
        LinearLayout linearLayout;

        public Holder(View view) {

            tvSpinnerItem = (TextView) view.findViewById(R.id.spinnerItem);
            tvSpinnerItem.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
            linearLayout = (LinearLayout) view.findViewById(R.id.lin_symptoms);
        }
    }
}
