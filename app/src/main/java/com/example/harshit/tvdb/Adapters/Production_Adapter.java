package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.harshit.tvdb.Pojo.Bean_Production;
import com.example.harshit.tvdb.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harshit on 14/11/17.
 */
public class Production_Adapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Bean_Production>> listChildData;

    public Production_Adapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Bean_Production>> listChildData) {
        this._context = context;
        this.listChildData = listChildData;
        this.listDataHeader = listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChildData.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Bean_Production bean_production = (Bean_Production) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child, null);
        }

        TextView txtListChild =convertView.findViewById(R.id.tv_child);
        txtListChild.setText(bean_production.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group, null);
        }
        TextView lblListHeader = convertView.findViewById(R.id.tv_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }





    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
