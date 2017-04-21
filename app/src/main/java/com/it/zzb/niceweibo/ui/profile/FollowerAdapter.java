package com.it.zzb.niceweibo.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.bean.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zzb on 2017/4/7.
 */

public class FollowerAdapter extends BaseAdapter {
    private Context mContext;
    private ViewHolder viewHolder;
    private ArrayList<User> users;
    private User user;

    public FollowerAdapter(Context context, ArrayList<User> userList){
        this.mContext = context;
        this.users = userList;
    }

    public static class ViewHolder {
        public TextView content;//评论内容
        public ImageView ivAvatar;//头像
        public RelativeLayout rlContent;//
        public TextView tvSubhead;//用户名
        public LinearLayout ll_follower;//布局

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =LayoutInflater.from(mContext).inflate(R.layout.item_follower, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tvSubhead = (TextView) convertView.findViewById(R.id.tv_subhead);
            viewHolder.ll_follower = (LinearLayout) convertView.findViewById(R.id.ll_follower);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        user = users.get(position);
        ImageLoader.getInstance().displayImage(user.avatar_hd,viewHolder.ivAvatar);
        viewHolder.tvSubhead.setText(user.screen_name);

        //点击用户跳到用户界面
        viewHolder.ll_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UserActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user" ,user);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
