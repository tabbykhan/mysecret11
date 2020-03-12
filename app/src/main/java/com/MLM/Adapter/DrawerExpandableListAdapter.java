package com.MLM.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.MLM.model.ExpandableListModel;
import com.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawerExpandableListAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater mLayoutInflater;
    private final String tag="Draweradapter";
    Context context;
   ArrayList<ExpandableListModel>  expand_title_list;
   HashMap<String,ArrayList<String>> expandListData;

    public DrawerExpandableListAdapter(Context baseContext, HashMap<String, ArrayList<String>> expandListData, ArrayList<ExpandableListModel> expandableTitleList) {
        this.expand_title_list=expandableTitleList;
        this.expandListData=expandListData;
        Log.d(tag,"----expand list--"+expandListData);
        this.context=baseContext;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return expand_title_list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return expandListData.get(expand_title_list.get(i).title)
                .size();
    }

    @Override
    public ExpandableListModel getGroup(int i) {
        return expand_title_list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String child_title=expandListData.get(expand_title_list.get(i).title).get(i1);
        Log.d(tag,"---child title--"+child_title);
        return child_title;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        ExpandableListModel headerTitle = (ExpandableListModel) getGroup(i);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_drawer_expandable_list_item, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.txt_title);
        listTitleTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        listTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        listTitleTextView.setText(headerTitle.title);

//        ImageView listImageView = (ImageView) convertView
//                .findViewById(R.id.expand_list_view_iv_icon);

     //   listImageView.setImageResource(headerTitle.image);
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        //final String expandedListText = (String) getChild(i, i1);
        String expandedListText=expandListData.get(expand_title_list.get(i).title).get(i1);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_expandable_child_view, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.txt_sub_title);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
