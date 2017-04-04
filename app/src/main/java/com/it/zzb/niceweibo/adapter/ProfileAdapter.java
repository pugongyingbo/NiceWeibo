package com.it.zzb.niceweibo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.util.DataUtil;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by zzb on 2017/4/1.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context mContext;
    private FavoritesAPI mFavouritesAPI;
    private Oauth2AccessToken mAccessToken;


    /**
     * 微博信息列表
     */
    private StatusList mStatusList = new StatusList();
    private ImageLoader imageLoader;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.timeline_image_loading)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true)
            .cacheOnDisk(true).build();

    public ProfileAdapter(Context context, StatusList list) {
        mContext = context;
        mStatusList = list;
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
        mFavouritesAPI = new FavoritesAPI(context, Constants.APP_KEY,
                mAccessToken);
        imageLoader = ImageLoader.getInstance();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_card_content;
        ImageView iv_avatar;//头像
        RelativeLayout rl_content;
        TextView tv_subhead;
        TextView tv_caption;

        TextView tv_content;

        NineGridImageView gv_images;//九宫格
        NineGridImageView gv_retweeted_images;

        LinearLayout include_retweeted_status;//转发布局
        TextView tv_retweeted_content;//转发内容

        LinearLayout ll_share_bottom;
        ImageView iv_share_bottom;
        TextView tv_share_bottom;
        LinearLayout ll_comment_bottom;
        ImageView iv_comment_bottom;
        TextView tv_comment_bottom;
        LinearLayout ll_like_bottom;
        ImageView iv_like_bottom;
        TextView tv_like_bottom;

        public ViewHolder(View view) {
            super(view);

            ll_card_content = (LinearLayout)view.findViewById(R.id.ll_card_content);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
            tv_caption = (TextView) view.findViewById(R.id.tv_caption);
            tv_content = (TextView) view.findViewById(R.id.tv_content);

            gv_images = (NineGridImageView) view.findViewById(R.id.gv_image);
            gv_retweeted_images = (NineGridImageView) view.findViewById(R.id.gv_retweeted_image);

            include_retweeted_status = (LinearLayout) view.findViewById(R.id.include_retweeted_status);
            tv_retweeted_content = (TextView) view.findViewById(R.id.tv_retweeted_content);

            ll_share_bottom = (LinearLayout) view.findViewById(R.id.ll_share_bottom);
            iv_share_bottom = (ImageView) view.findViewById(R.id.iv_share_bottom);
            tv_share_bottom = (TextView) view.findViewById(R.id.tv_share_bottom);
            ll_comment_bottom = (LinearLayout) view
                    .findViewById(R.id.ll_comment_bottom);
            iv_comment_bottom = (ImageView) view
                    .findViewById(R.id.iv_comment_bottom);
            tv_comment_bottom = (TextView) view
                    .findViewById(R.id.tv_comment_bottom);
            ll_like_bottom = (LinearLayout) view
                    .findViewById(R.id.ll_like_bottom);
            iv_like_bottom = (ImageView) view
                    .findViewById(R.id.iv_like_bottom);
            tv_like_bottom = (TextView) view
                    .findViewById(R.id.tv_like_bottom);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_profile_weibo, parent, false);
        ViewHolder viewHolder = new ProfileAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Status status =mStatusList.statusList.get(position);
        imageLoader.displayImage(status.user.avatar_hd, holder.iv_avatar);
        holder.tv_subhead.setText(status.user.name);

        String time= DataUtil.showTime(status.created_at);
        String from = String.format("%s", Html.fromHtml(status.source));
        holder.tv_caption.setText(time
                + " 来自 " + from);
        holder.tv_content.setText(status.text);

        if(status.pic_urls != null) {
            holder.gv_images.setVisibility(View.VISIBLE);
            holder.gv_images
                    .setAdapter(new NineGridImageViewAdapter<String>() {

                        @Override
                        protected void onDisplayImage(Context context,
                                                      ImageView imageView, String t) {
                            // TODO Auto-generated method stub
                            imageLoader.displayImage(t,
                                    imageView, options);
                        }
                        @Override
                        protected ImageView generateImageView(Context context) {
                            return super.generateImageView(context);
                        }
                        @Override
                        protected void onItemImageClick(Context context,
                                                        int index, java.util.List<String> list) {

                        }

                    });
            holder.gv_images.setImagesData(status.pic_urls);
        } else{
            holder.gv_images.setVisibility(View.GONE);}

        Status retweeted_status = status.retweeted_status;
        if(retweeted_status != null) {
            User retUser = retweeted_status.user;

            holder.include_retweeted_status.setVisibility(View.VISIBLE);
            holder.tv_retweeted_content.setText("@" + retUser.name + ":"
                    + retweeted_status.text);
            if(retweeted_status.pic_urls !=null) {
                holder.gv_retweeted_images.setVisibility(View.VISIBLE);
                holder.gv_retweeted_images
                        .setAdapter(new NineGridImageViewAdapter<String>() {

                            @Override
                            protected void onDisplayImage(Context context,
                                                          ImageView imageView, String t) {
                                // TODO Auto-generated method stub
                                imageLoader.displayImage(t,
                                        imageView, options);
                            }

                            @Override
                            protected ImageView generateImageView(Context context) {
                                return super.generateImageView(context);
                            }

                            @Override
                            protected void onItemImageClick(Context context,
                                                            int index, java.util.List<String> list) {
                            }
                        });
                holder.gv_retweeted_images
                        .setImagesData(status.retweeted_status.pic_urls);
            }else{
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

    }


    @Override
    public int getItemCount() {
        return mStatusList.statusList == null ? 0 : mStatusList.statusList
                .size();
    }
}
