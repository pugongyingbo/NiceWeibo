package com.it.zzb.niceweibo.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.ReplyCommentActivity;
import com.it.zzb.niceweibo.activity.WeiboDetailActivity;

import com.it.zzb.niceweibo.util.DataUtil;
import com.it.zzb.niceweibo.util.StringUtils;
import com.it.zzb.niceweibo.util.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.it.zzb.niceweibo.bean.CommentList;
import com.it.zzb.niceweibo.bean.Comment;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zzb on 2017/4/2.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private Context context;




    /**
     * 微博信息列表
     */
    private CommentList commentList = new CommentList();
    private ImageLoader imageLoader;
    private Comment comment;

    public ViewAdapter(Context context,CommentList commentList) {
        this.context = context;
        this.commentList = commentList;

        imageLoader = ImageLoader.getInstance();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCardContent;
        private CircleImageView ivAvatar;//头像
        private TextView tvAtCommentUserName;//评论人名字
        private TextView tvCaption;//创建时间，地址
        private TextView tvAtCommentText;//评论内容
        private ImageView replyAtComment;//回复图片
        private LinearLayout ll_comment_status;//评论微博布局
        private TextView commentStatusUserNameText;//登陆人微博名字和内容

        public ViewHolder(View view) {
            super(view);
            llCardContent = (LinearLayout) view.findViewById(R.id.ll_card_content);
            ivAvatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
            tvAtCommentUserName = (TextView) view.findViewById(R.id.tv_subhead);
            tvCaption = (TextView) view.findViewById(R.id.tv_caption);
            tvAtCommentText = (TextView)view. findViewById(R.id.tv_at_comment_text);
            replyAtComment = (ImageView) view.findViewById(R.id.reply_at_comment);
            ll_comment_status = (LinearLayout) view.findViewById(R.id.ll_commnet_status);
            commentStatusUserNameText = (TextView) view.findViewById(R.id.comment_status_userName_text);
        }
    }


    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        ViewAdapter.ViewHolder viewHolder = new ViewAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewAdapter.ViewHolder holder, int position) {

        comment = commentList.commentList.get(position);
        //评论的内容
        imageLoader.displayImage(comment.user.avatar_hd, holder.ivAvatar);
        holder.tvAtCommentUserName.setText(comment.user.name);
        String time= DataUtil.showTime(comment.created_at);
        String from = String.format("%s", Html.fromHtml(comment.source));
        holder.tvCaption.setText(time
                + " 来自 " + from);
        //处理文字
        SpannableString commentContent = StringUtils.getWeiboContent(
                context, holder.tvAtCommentText, comment.text);

        holder.tvAtCommentText.setText(commentContent);

        //评论微博的内容
        holder.commentStatusUserNameText.setText("@" + comment.status.user.name + ":"
                    + comment.status.text);

        //点击回复评论跳到写评论页面
        holder.replyAtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "回复评论", Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, ReplyCommentActivity.class);
                intent.putExtra("comment", comment);
                intent.putExtra("status",comment.status);
                context.startActivity(intent);
            }
        });

        //点击微博跳转到详情页面
        holder.ll_comment_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WeiboDetailActivity.class);
                intent.putExtra("status", comment.status);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.commentList == null ? 0 : commentList.commentList
                .size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
