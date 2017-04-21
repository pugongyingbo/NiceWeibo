package com.it.zzb.niceweibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.bean.Group;
import com.it.zzb.niceweibo.bean.GroupList;


/**
 * Created by zzb on 2017/4/2.
 */
public class GroupAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ViewHolder viewHolder;
    private GroupList groupList;

    public GroupAdapter(Context context, GroupList group) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        groupList = group;
    }

    @Override
    public int getCount() {
        return groupList.groupList.size();
    }

    @Override
    public Group getItem(int position) {
        return  groupList == null ? null:groupList.groupList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group,null);
            viewHolder = new ViewHolder();
            viewHolder.groupName =(TextView) convertView.findViewById(R.id.group_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Group group =getItem(position);
        viewHolder.groupName.setText(group.name);
        return convertView;
    }

    public static class ViewHolder {
        public TextView groupName;
    }

}
