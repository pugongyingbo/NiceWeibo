package com.it.zzb.niceweibo.adapter;

import android.content.Context;


import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.ReplyCommentActivity;
import com.it.zzb.niceweibo.bean.Comment;
import com.it.zzb.niceweibo.bean.CommentList;
import com.it.zzb.niceweibo.util.DataUtil;

import com.it.zzb.niceweibo.util.StringUtil;

import com.it.zzb.niceweibo.util.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zzb on 2017/4/5.
 */

public class CommentAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private Context context;
    private CommentList commentList;
    private ViewHolder viewHolder;


    public CommentAdapter(Context context, CommentList list){
        this.context=context;
        this.commentList =list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentList.commentList == null ? 0 :commentList.commentList.size();

    }

    @Override
    public Comment getItem(int position) {
        return commentList.commentList == null ? null :commentList.commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_weibo_comment, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tvSubhead = (TextView) convertView.findViewById(R.id.tv_subhead);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tv_caption);

            viewHolder.content = (TextView) convertView.findViewById(R.id.weibo_comment);
            viewHolder.reply_at_comment = (ImageView) convertView.findViewById(R.id.reply_at_comment);
            viewHolder.ll_comment = (LinearLayout) convertView.findViewById(R.id.ll_comment);
            convertView.setTag(viewHolder);
        }else{

        viewHolder = (ViewHolder) convertView.getTag();
    }

        final Comment comment = getItem(position);
        ImageLoader.getInstance().displayImage(comment.user.avatar_hd,viewHolder.ivAvatar);
        String time= DataUtil.showTime(comment.created_at);
        viewHolder.tvSubhead.setText(comment.user.screen_name);
        viewHolder.tvCaption.setText(time);
        SpannableString weiboContent = StringUtil.getWeiBoText(
                context, comment.text);
        viewHolder.content.setText(weiboContent);

        //点击评论
        viewHolder.reply_at_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "回复评论", Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, ReplyCommentActivity.class);
                intent.putExtra("comment", comment);
                intent.putExtra("status", comment.status);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    return convertView;
    }

    public static class ViewHolder {
         TextView content;//评论内容
         CircleImageView ivAvatar;//头像
         TextView tvSubhead;//用户名
         TextView tvCaption;//来源和时间
         ImageView reply_at_comment;//回复图片
         LinearLayout ll_comment;//评论布局
    }
}
