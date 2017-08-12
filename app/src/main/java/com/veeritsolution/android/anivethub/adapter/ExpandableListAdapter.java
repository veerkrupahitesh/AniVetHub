package com.veeritsolution.android.anivethub.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.veeritsolution.android.anivethub.MyApplication;
import com.veeritsolution.android.anivethub.R;
import com.veeritsolution.android.anivethub.models.VetEducation;
import com.veeritsolution.android.anivethub.models.VetExperience;
import com.veeritsolution.android.anivethub.models.VetSpecialization;
import com.veeritsolution.android.anivethub.utility.Constants;
import com.veeritsolution.android.anivethub.utility.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${hitesh} on 2/1/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<?>> _listDataChild;
    private boolean isExpand;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<?>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Object childObject = getChild(groupPosition, childPosition);

        if (convertView == null) {
            //LayoutInflater infalInflater = (LayoutInflater) this._context
            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //convertView = infalInflater.inflate(R.layout.expandable_child_list_item, null);
            convertView = LayoutInflater.from(_context).inflate(R.layout.expandable_child_list_item, parent, false);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.name_textview);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        TextView tvYear = (TextView) convertView.findViewById(R.id.year_textview);
        tvYear.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_specialisation);

        if (childObject instanceof VetEducation) {
            ratingBar.setVisibility(View.GONE);
            tvYear.setVisibility(View.VISIBLE);

            VetEducation vetEducation = (VetEducation) childObject;
            tvHeader.setText(vetEducation.getDegreeName());
            tvYear.setText(vetEducation.getPassingYear());
        } else if (childObject instanceof VetExperience) {
            ratingBar.setVisibility(View.GONE);
            tvYear.setVisibility(View.VISIBLE);

            VetExperience vetExperience = (VetExperience) childObject;
            tvHeader.setText(vetExperience.getTitle());
            tvYear.setText(Utils.formatDate(vetExperience.getFromDate(), Constants.DATE_MM_DD_YYYY, Constants.DATE_YYYY)
                    + " - " + Utils.formatDate(vetExperience.getToDate(), Constants.DATE_MM_DD_YYYY, Constants.DATE_YYYY));
        } else if (childObject instanceof VetSpecialization) {
            VetSpecialization vetSpecialization = (VetSpecialization) childObject;

            ratingBar.setVisibility(View.VISIBLE);
            tvYear.setVisibility(View.GONE);

            tvHeader.setText(vetSpecialization.getPetTypeGroupName());
            //ratingBar.setRating(vetSpecialization.getProficiency());

        } /*else {
            ratingBar.setVisibility(View.GONE);
            tvYear.setVisibility(View.VISIBLE);

            VetClinic vetClinic = (VetClinic) childObject;
            tvHeader.setText(vetClinic.getClinicName());
            tvYear.setText(vetClinic.getCountry().toLowerCase());
        }*/
        //  txtListChild.setText(childObject);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        ViewHolder viewHolder;
        this.isExpand = isExpanded;
        if (convertView == null) {
            //  LayoutInflater infalInflater = (LayoutInflater) this._context
            //          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  convertView = infalInflater.inflate(R.layout.expandable_parent_list_item, null);
            convertView = LayoutInflater.from(_context).inflate(R.layout.expandable_parent_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getChildrenCount(groupPosition)==0) {
            convertView.setVisibility(View.GONE);
        }
        if (groupPosition == 0) {
            viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.colourListBack, null));
        } else if ((groupPosition % 2) == 0) {
            viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.colourListBack, null));
        } else {
            viewHolder.linExpand.setBackgroundColor(Color.WHITE);
        }

        viewHolder.setExpanded(isExpanded);

        if (isExpanded) {
            viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.listSelectColor, null));
        } else {
            if (groupPosition == 0) {
                viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.colourListBack, null));
            } else if ((groupPosition % 2) == 0) {
                viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.colourListBack, null));
            } else {
                viewHolder.linExpand.setBackgroundColor(Color.WHITE);
            }
            // viewHolder.linExpand.setBackgroundColor(ResourcesCompat.getColor(_context.getResources(), R.color.listSelectColor, null));
        }
        //  viewHolder.onExpansionToggled(isExpanded);
        viewHolder.lblListHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);
        viewHolder.lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {

        TextView lblListHeader;
        ImageView imgExpand;
        LinearLayout linExpand;

        public ViewHolder(View itemView) {

            linExpand = (LinearLayout) itemView.findViewById(R.id.lin_expand);

            lblListHeader = (TextView) itemView.findViewById(R.id.recipe_textview);
            lblListHeader.setTypeface(MyApplication.getInstance().FONT_ROBOTO_LIGHT);

            imgExpand = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
        }

        public void setExpanded(boolean expanded) {

            if (expanded) {
                imgExpand.setImageResource(R.drawable.img_minus);
                imgExpand.setVisibility(View.GONE);



            } else {
                imgExpand.setImageResource(R.drawable.img_add);
            }
        }

        public void onExpansionToggled(boolean expanded) {

            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            imgExpand.startAnimation(rotateAnimation);
        }
    }

    /**
     * Expands the given group with an animation.
     * @param groupPos The position of the group to expand
     * @return Returns true if the group was expanded. False if the group was
     *          already expanded.
     */

}
