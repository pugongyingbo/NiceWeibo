package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;

import com.it.zzb.niceweibo.adapter.CommentAdapter;
import com.it.zzb.niceweibo.bean.CommentList;
import com.it.zzb.niceweibo.bean.FavoriteList;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.util.DataUtil;
import com.it.zzb.niceweibo.util.StringUtil;
import com.it.zzb.niceweibo.util.StringUtils;
import com.it.zzb.niceweibo.util.ToastUtils;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.bean.User;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博详情页
 */
public class WeiboDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    // 跳转到写评论页面code
    private static final int REQUEST_CODE_WRITE_COMMENT = 2333;

    private Oauth2AccessToken mAccessToken;
    private CommentsAPI commentsAPI;
    private Context context =getBaseContext();
    //添加数据
    private Status status ;

    //漂浮按钮
    private RapidFloatingActionButton fab_button_group;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionLayout fab_layout;

    ListView commentList;

    // 评论当前已加载至的页数
    private long curPage = 1;



    //微博详情
    LinearLayout ll_card_content;
    ImageView iv_avatar;//头像
    RelativeLayout rl_content;
    TextView tv_subhead;
    TextView tv_caption;
    TextView tv_content;
    NineGridImageView gv_images;//九宫格
    NineGridImageView gv_retweeted_images;
    LinearLayout include_retweeted_status;
    TextView tv_retweeted_content;

    // bottom_control - 底部互动栏,包括转发/评论/点赞

    LinearLayout ll_share_bottom;
    TextView tv_share_bottom;
    LinearLayout ll_comment_bottom;
    TextView tv_comment_bottom;
    LinearLayout ll_like_bottom;
    TextView tv_like_bottom;

    ImageView iv_more;//更多
    Toolbar toolbar;
    private FavoritesAPI mFavouritesAPI;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.timeline_image_loading)
            .showImageOnFail(R.drawable.timeline_image_failure)
            .bitmapConfig(Bitmap.Config.ARGB_8888).cacheInMemory(true)
            .cacheOnDisk(true).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_detail);

        fab_layout = (RapidFloatingActionLayout) findViewById(R.id.fab_layout);
        fab_button_group = (RapidFloatingActionButton) findViewById(R.id.fab_button_group);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        initFab();
        initView();
        setData();
        loadData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);

            }
        });

    }


   public void initView(){
       ll_card_content = (LinearLayout)findViewById(R.id.ll_card_content);
       iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
       rl_content = (RelativeLayout) findViewById(R.id.rl_content);
       tv_subhead = (TextView) findViewById(R.id.tv_subhead);
       tv_caption = (TextView) findViewById(R.id.tv_caption);
       tv_content = (TextView) findViewById(R.id.tv_content);
       iv_more = (ImageView) findViewById(R.id.iv_more);


       gv_images = (NineGridImageView) findViewById(R.id.gv_image);
       gv_retweeted_images = (NineGridImageView) findViewById(R.id.gv_retweeted_image);

       include_retweeted_status = (LinearLayout) findViewById(R.id.include_retweeted_status);
       tv_retweeted_content = (TextView) findViewById(R.id.tv_retweeted_content);

       ll_share_bottom = (LinearLayout)findViewById(R.id.ll_share_bottom);

       tv_share_bottom = (TextView) findViewById(R.id.tv_share_bottom);
       ll_comment_bottom = (LinearLayout) findViewById(R.id.ll_comment_bottom);

       tv_comment_bottom = (TextView) findViewById(R.id.tv_comment_bottom);
       ll_like_bottom = (LinearLayout) findViewById(R.id.ll_like_bottom);

       tv_like_bottom = (TextView) findViewById(R.id.tv_like_bottom);

       commentList = (ListView) findViewById(R.id.weibo_comment_list);

       ll_share_bottom.setOnClickListener(this);
       ll_comment_bottom.setOnClickListener(this);
       ll_like_bottom.setOnClickListener(this);
   }


    public  void initFab(){
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("转发")
                .setResId(R.drawable.skip_16px)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("评论")
                .setResId(R.drawable.comment_16px)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                fab_layout,
                fab_button_group,
                rfaContent
        ).build();
    }


    /**
     * 初始化详情微博界面的数据
     *
     */
    public void setData(){

        status = (Status) getIntent().getSerializableExtra("status");

        ImageLoader.getInstance().displayImage(status.user.avatar_hd, iv_avatar);
        tv_subhead.setText(status.user.name);

        String time= DataUtil.showTime(status.created_at);
        String from = String.format("%s", Html.fromHtml(status.source));
        tv_caption.setText(time + " 来自 " + from);
        //微博内容的处理
        SpannableString weiboContent = StringUtil.getWeiBoText(
                WeiboDetailActivity.this, status.text);
        tv_content.setText(weiboContent);

        if(status.pic_urls != null) {
            gv_images.setVisibility(View.VISIBLE);
            gv_images
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

                            list.add(index,status.bmiddle_pic);
                            ToastUtils.showToast(context, "点击图片~", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(context, PictureActivity.class);
                            intent.putExtra("image_position", index);
                            intent.putExtra("status",status);
                            intent.putStringArrayListExtra("imagelist_url", (ArrayList<String>) list);
                            context.startActivity(intent);
                        }

                    });
            gv_images.setImagesData(status.pic_urls);
        } else{

            gv_images.setVisibility(View.GONE);}

        final Status retweeted_status = status.retweeted_status;

        if(retweeted_status != null) {
            User retUser = retweeted_status.user;

           include_retweeted_status.setVisibility(View.VISIBLE);
            tv_retweeted_content.setText("@" + retUser.name + ":"
                    + retweeted_status.text);
            if(retweeted_status.pic_urls != null){
                gv_retweeted_images.setVisibility(View.VISIBLE);
                gv_retweeted_images
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
                            protected void onItemImageClick(Context context, int index,
                                                            java.util.List<String> list) {
                                list.add(index,retweeted_status.bmiddle_pic);
                                ToastUtils.showToast(context, "点击图片~", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(context, PictureActivity.class);
                                intent.putExtra("image_position", index);
                                intent.putStringArrayListExtra("imagelist_url", (ArrayList<String>) list);
                                context.startActivity(intent);
                            }
                        });
                gv_retweeted_images.setImagesData(status.retweeted_status.pic_urls);
            }else{
                gv_retweeted_images.setVisibility(View.GONE);
            }

        } else {
            include_retweeted_status.setVisibility(View.GONE);
        }

        // bottom_control - 底部互动栏,包括转发/评论/点赞
        tv_share_bottom.setText(status.reposts_count == 0 ?
                "转发" : status.reposts_count + "");

        tv_comment_bottom.setText(status.comments_count == 0 ?
                "评论" : status.comments_count + "");

        tv_like_bottom.setText(status.attitudes_count == 0 ?
                "赞" : status.attitudes_count + "");

        //点击显示收藏
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavouritesAPI = new FavoritesAPI(context,Constants.APP_KEY,mAccessToken);
                PopupMenu popup = new PopupMenu(getBaseContext(),iv_more);
                popup.getMenuInflater().inflate(R.menu.favorite_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.favorite:
                                //收藏
                                long ids = Long.parseLong(status.id);
                                mFavouritesAPI.create(ids, favoriteListener);
                                break;
                            //取消收藏
                            case R.id.cancle_favorite:
                                long id = Long.parseLong(status.id);
                                mFavouritesAPI.destroy(id, mCancleListener);
                            case R.id.repost:
                                //转发
                                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
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
        }) ;

    }

    private void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        commentsAPI = new CommentsAPI(this, Constants.APP_KEY,mAccessToken);
        status = (Status) getIntent().getSerializableExtra("status");
        long id = Long.parseLong(status.id);

        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            commentsAPI.show(id,0,0,20,1,0,mListener);
        } else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(final String response) {

            if (!TextUtils.isEmpty(response)) {

                if (response.startsWith("{\"comments\"")) {

                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    CommentList mCommentList = CommentList.parse(response);

                    if (mCommentList != null && mCommentList.total_number > 0) {
                        CommentAdapter commentAdapter = new CommentAdapter(getBaseContext(), mCommentList);
                        commentList.setAdapter(commentAdapter);
                    }
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_share_bottom:
                Toast.makeText(WeiboDetailActivity.this, "转发" ,
                        Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, RepostActivity.class);
                intent1.putExtra("status", status);
                startActivity(intent1);

                break;
            case R.id.ll_comment_bottom:
                // 跳转至写评论页面
                Intent intent = new Intent(this, WriteCommentActivity.class);
                intent.putExtra("status", status);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
                break;
            case R.id.ll_like_bottom:
                Toast.makeText(WeiboDetailActivity.this, "点个赞" ,
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果Back键返回,取消发评论等情况,则直接return,不做后续处理
        if(resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_COMMENT:
                // 如果是评论发送成功的返回结果,则重新加载最新评论,同时要求滚动至评论部分
                boolean sendCommentSuccess = data.getBooleanExtra("sendCommentSuccess", false);
                if(sendCommentSuccess) {
                    loadData();
                }
                break;

            default:
                break;
        }
    }
    private RequestListener favoriteListener = new RequestListener() {

        @Override
        public void onComplete(String response) {
            // TODO Auto-generated method stub
            if (!TextUtils.isEmpty(response)) {
                FavoriteList list = FavoriteList.parse(response);
                if (list != null) {
                    Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(context, "你已经收藏过这条微博了", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(context, "取消收藏成功", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            Toast.makeText(context, "你还未收藏过这条微博", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    };

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {

    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if(i==0) {
            Intent intent = new Intent(this, RepostActivity.class);
            intent.putExtra("status" ,status);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, WriteCommentActivity.class);
            intent.putExtra("status",status);
            startActivity(intent);
        }
    }
}
