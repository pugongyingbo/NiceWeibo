package com.it.zzb.niceweibo.ui.home;


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
import com.it.zzb.niceweibo.activity.UserActivity;
import com.it.zzb.niceweibo.activity.WeiboDetailActivity;
import com.it.zzb.niceweibo.activity.WriteCommentActivity;
import com.it.zzb.niceweibo.bean.FavoriteList;
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

import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.bean.StatusList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zzb on 2017/3/27.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>  {
    private Context context;
    private FavoritesAPI mFavouritesAPI;

    private Oauth2AccessToken mAccessToken;

    private Context mContext;
    private int flag=0;

    /**
     * 微博信息列表
     */
    private StatusList mStatusList;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.timeline_image_loading)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public HomeAdapter(Context context,StatusList list) {
        mContext = context;
        mStatusList = list;
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
        mFavouritesAPI = new FavoritesAPI(context, Constants.APP_KEY,
                mAccessToken);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
         LinearLayout ll_card_content;//内容布局
         CircleImageView iv_avatar;//头像
         RelativeLayout rl_content;//用户名布局
         TextView tv_subhead;//用户，名
         TextView tv_caption;//来源和时间
         TextView tv_content;//内容

         NineGridImageView gv_images;//九宫格图片
         NineGridImageView gv_retweeted_images;//转发九宫格图片

         LinearLayout include_retweeted_status;//转发布局
         TextView tv_retweeted_content;//转发内容

         LinearLayout ll_share_bottom;//转发布局
         ImageView iv_share_bottom;
         TextView tv_share_bottom;//转发
         LinearLayout ll_comment_bottom;
         ImageView iv_comment_bottom;
         TextView tv_comment_bottom;//评论
         LinearLayout ll_like_bottom;
         ImageView iv_like_bottom;
         TextView tv_like_bottom;//点赞
         final ImageView iv_more;//更多

        public ViewHolder(View view) {
            super(view);

            ll_card_content = (LinearLayout)view.findViewById(R.id.ll_card_content);
            iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) view.findViewById(R.id.tv_caption);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_more = (ImageView) view.findViewById(R.id.iv_more);

            gv_images = (NineGridImageView) view.findViewById(R.id.gv_image);
            gv_retweeted_images = (NineGridImageView) view.findViewById(R.id.gv_retweeted_image);

            include_retweeted_status = (LinearLayout) view.findViewById(R.id.include_retweeted_status);
            tv_retweeted_content = (TextView) view.findViewById(R.id.tv_retweeted_content);

            ll_share_bottom = (LinearLayout) view.findViewById(R.id.ll_share_bottom);
            iv_share_bottom = (ImageView) view.findViewById(R.id.iv_share_bottom);
            tv_share_bottom = (TextView) view.findViewById(R.id.tv_share_bottom);
            ll_comment_bottom = (LinearLayout) view.findViewById(R.id.ll_comment_bottom);
            iv_comment_bottom = (ImageView) view.findViewById(R.id.iv_comment_bottom);
            tv_comment_bottom = (TextView) view.findViewById(R.id.tv_comment_bottom);
            ll_like_bottom = (LinearLayout) view.findViewById(R.id.ll_like_bottom);
            iv_like_bottom = (ImageView) view.findViewById(R.id.iv_like_bottom);
            tv_like_bottom = (TextView) view.findViewById(R.id.tv_like_bottom);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final Status status = getItem(position);

            ImageLoader.getInstance().displayImage(status.user.avatar_hd, holder.iv_avatar);
            holder.tv_subhead.setText(status.user.name);

            String time = DataUtil.showTime(status.created_at);
            String from = String.format("%s", Html.fromHtml(status.source));
            holder.tv_caption.setText(time + " 来自 " + from);

            SpannableString weiboContent = StringUtil.getWeiBoText(
                    context, status.text);
            holder.tv_content.setText(weiboContent);

            if (status.pic_urls != null) {
                holder.gv_images.setVisibility(View.VISIBLE);
                holder.gv_images
                        .setAdapter(new NineGridImageViewAdapter<String>() {

                            @Override
                            protected void onDisplayImage(Context context,
                                                          ImageView imageView, String t) {
                                // TODO Auto-generated method stub
                                ImageLoader.getInstance().displayImage(t,
                                        imageView, options);
                            }

                            @Override
                            protected ImageView generateImageView(Context context) {
                                return super.generateImageView(context);
                            }

                            @Override
                            protected void onItemImageClick(Context context,
                                                            int index, java.util.List<String> list) {
                                list.add(index, status.bmiddle_pic);
                                ToastUtils.showToast(context, "点击图片~", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(mContext, PictureActivity.class);
                                intent.putExtra("image_position", index);
                                intent.putExtra("status", status);
                                intent.putStringArrayListExtra("imagelist_url", (ArrayList<String>) list);
                                mContext.startActivity(intent);
                            }

                        });
                holder.gv_images.setImagesData(status.pic_urls);
            } else {

                holder.gv_images.setVisibility(View.GONE);
            }

            //转发的微博
            final Status retweeted_status = status.retweeted_status;

            if (retweeted_status != null) {
                User retUser = retweeted_status.user;

                holder.include_retweeted_status.setVisibility(View.VISIBLE);

                SpannableString retweetContent = StringUtil.getWeiBoText(
                        context, retweeted_status.text);

                holder.tv_retweeted_content.setText("@" + retUser.name + ":"
                        + retweetContent);
                if (retweeted_status.pic_urls != null) {
                    holder.gv_retweeted_images.setVisibility(View.VISIBLE);
                    holder.gv_retweeted_images
                            .setAdapter(new NineGridImageViewAdapter<String>() {

                                @Override
                                protected void onDisplayImage(Context context,
                                                              ImageView imageView, String t) {
                                    // TODO Auto-generated method stub
                                    ImageLoader.getInstance().displayImage(t,
                                            imageView, options);
                                }

                                @Override
                                protected ImageView generateImageView(Context context) {
                                    return super.generateImageView(context);
                                }

                                @Override
                                protected void onItemImageClick(Context context,
                                                                int index, java.util.List<String> list) {
                                    ToastUtils.showToast(context, "点击图片~", Toast.LENGTH_SHORT);
                                    list.add(index, retweeted_status.bmiddle_pic);
                                    Intent intent = new Intent(mContext, PictureActivity.class);
                                    intent.putExtra("image_position", index);
                                    intent.putStringArrayListExtra("imagelist_url", (ArrayList<String>) list);
                                    mContext.startActivity(intent);
                                }
                            });
                    holder.gv_retweeted_images.setImagesData(status.retweeted_status.pic_urls);
                } else {
                    holder.gv_retweeted_images.setVisibility(View.GONE);
                }

            } else {
                holder.include_retweeted_status.setVisibility(View.GONE);
            }

            holder.tv_share_bottom.setText(status.reposts_count == 0 ?
                    "转发" : status.reposts_count + "");

            holder.tv_comment_bottom.setText(status.comments_count == 0 ?
                    "评论" : status.comments_count + "");

            holder.tv_like_bottom.setText(status.attitudes_count == 0 ?
                    "赞" : status.attitudes_count + "");

            //点击微博跳转到详情页面
            holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WeiboDetailActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });

            //点击头像跳到用户页面
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(context, "用户", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });
            //点击转发微博跳转到详情页面
            holder.include_retweeted_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WeiboDetailActivity.class);
                    intent.putExtra("status", retweeted_status);
                    context.startActivity(intent);
                }
            });
            //点击
            holder.ll_share_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(context, "转发~", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(context, RepostActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });
            //点击评论跳转到详情页
            holder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.comments_count > 0) {
                        Intent intent = new Intent(context, WeiboDetailActivity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("scroll2Comment", true);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, WriteCommentActivity.class);
                        intent.putExtra("status", status);
                        context.startActivity(intent);
                        ToastUtils.showToast(context, "评个论~", Toast.LENGTH_SHORT);
                    }
                }
            });

            //点赞
            holder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (flag){
                        case 0:
                            holder.ll_like_bottom.setActivated(false);
                            flag = 1;
                            break;
                        case 1:
                            holder.ll_like_bottom.setActivated(true);
                            flag = 0;
                            break;
                    }

                    ToastUtils.showToast(context, "接口限制点赞", Toast.LENGTH_SHORT);
                }
            });
            //点击显示收藏
            holder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, holder.iv_more);
                    popup.getMenuInflater().inflate(R.menu.favorite_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.favorite:
                                    //收藏
                                    long ids = Long.parseLong(status.id);
                                    mFavouritesAPI.create(ids, mListener);
                                    break;
                                //取消收藏
                                case R.id.cancle_favorite:
                                    long id = Long.parseLong(status.id);
                                    mFavouritesAPI.destroy(id, mCancleListener);
                                case R.id.repost:
                                    //转发
                                    Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, RepostActivity.class);
                                    intent.putExtra("status", status);
                                    context.startActivity(intent);
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }

    private RequestListener mListener = new RequestListener() {

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            if (!TextUtils.isEmpty(response)) {
                FavoriteList list = FavoriteList.parse(response);
                if (list != null) {
                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(mContext, "你已经收藏过这条微博了", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    };
    private RequestListener mCancleListener = new RequestListener() {

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            if (!TextUtils.isEmpty(response)) {
                FavoriteList list = FavoriteList.parse(response);
                if (list != null) {
                    Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(mContext, "你还未收藏过这条微博", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    };
    @Override
    public int getItemCount() {
        return mStatusList.statusList == null ? 0 : mStatusList.statusList
                .size();
    }


    public Status getItem(int position) {
        // TODO Auto-generated method stub
        return mStatusList == null ? null : mStatusList.statusList
                .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
