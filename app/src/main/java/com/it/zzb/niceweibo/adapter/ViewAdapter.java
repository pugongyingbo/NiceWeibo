package com.it.zzb.niceweibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.util.DataUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.StatusList;


/**
 * Created by zzb on 2017/4/2.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private Context context;
    private FavoritesAPI mFavouritesAPI;

    private Oauth2AccessToken mAccessToken;

    private Context mContext;

    /**
     * 微博信息列表
     */
    private CommentList commentList = new CommentList();
    private ImageLoader imageLoader;
    private Comment comment;

    public ViewAdapter(Context context,CommentList commentList) {
        mContext = context;
        this.commentList = commentList;
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
        mFavouritesAPI = new FavoritesAPI(context, Constants.APP_KEY,
                mAccessToken);
        imageLoader = ImageLoader.getInstance();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCardContent;
        private ImageView ivAvatar;//头像
        private TextView tvAtCommentUserName;//评论人名字
        private TextView tvCaption;//创建时间，地址
        private TextView tvAtCommentText;//评论内容
        private ImageView replyAtComment;//回复图
        private TextView commentStatusUserNameText;//登陆人微博名字和内容

        public ViewHolder(View view) {
            super(view);
            llCardContent = (LinearLayout) view.findViewById(R.id.ll_card_content);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvAtCommentUserName = (TextView) view.findViewById(R.id.tv_subhead);
            tvCaption = (TextView) view.findViewById(R.id.tv_caption);
            tvAtCommentText = (TextView)view. findViewById(R.id.tv_at_comment_text);
            replyAtComment = (ImageView) view.findViewById(R.id.reply_at_comment);
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
        holder.tvAtCommentText.setText(comment.text);

        //评论微博的内容
        holder.commentStatusUserNameText.setText("@" + comment.status.user.name + ":"
                    + comment.status.text);

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
