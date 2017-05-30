package com.it.zzb.niceweibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.PictureActivity;
import com.it.zzb.niceweibo.activity.RepostActivity;
import com.it.zzb.niceweibo.activity.WeiboDetailActivity;
import com.it.zzb.niceweibo.activity.WriteCommentActivity;
import com.it.zzb.niceweibo.bean.Favorite;
import com.it.zzb.niceweibo.bean.FavoriteList;
import com.it.zzb.niceweibo.bean.Msg;
import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.util.DataUtil;
import com.it.zzb.niceweibo.util.StringUtil;
import com.it.zzb.niceweibo.util.ToastUtils;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zzb on 2017/4/8.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
   private List<Msg> msgList;
    private Context context;

    public MsgAdapter(List<Msg> list,Context mContext) {
       msgList = list;
       context = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout left;
        LinearLayout right;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            left = (LinearLayout) view.findViewById(R.id.left_layout);
            right = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.msg_item, parent, false);
       ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if(msg.getType() == Msg.TYPE_RECEIVED){
            //如果是接受的消息，显示左边，将右边隐藏
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType() == Msg.TYPE_SENT){
            //如果是发送的，显示右边，隐藏左边
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }


    @Override
    public int getItemCount() {
        return msgList.size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
