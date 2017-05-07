package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.bean.Comment;
import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;

import com.it.zzb.niceweibo.util.ViewUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;

import static com.it.zzb.niceweibo.util.ToastUtils.showToast;

public class WriteCommentActivity extends AppCompatActivity implements View.OnClickListener {
    // 评论输入框

    private EditText etComment;

    private ImageView weiboEmotion;
    private ImageView weiboTopic;
    private ImageView weiboAt;
    private ImageView sendComment;
    private LinearLayout llEmotionDashboard;
    private ViewPager vpEmotionDashboard;

    // 待评论的微博
    private Status status;
    private Comment comment;
    private Oauth2AccessToken mAccessToken;

    private CommentsAPI commentsAPI;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("写评论");
        //设置为ActionBar setTitle要在上面
        setSupportActionBar(toolbar);
        //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 获取Intent传入的微博
        status = (Status) getIntent().getSerializableExtra("status");


        initView();
    }
    public void  initView(){
        etComment = (EditText) findViewById(R.id.et_comment);

        weiboEmotion = (ImageView) findViewById(R.id.weibo_emotion);
        weiboTopic = (ImageView) findViewById(R.id.weibo_topic);
        weiboAt = (ImageView) findViewById(R.id.weibo_at);
        sendComment = (ImageView) findViewById(R.id.send_comment);
        //表情面板
        llEmotionDashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vpEmotionDashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
        new ViewUtil(this,vpEmotionDashboard,etComment).initEmotion();

        sendComment.setOnClickListener(this);
        weiboAt.setOnClickListener(this);
        weiboEmotion.setOnClickListener(this);
        weiboTopic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_comment:
                sendComment();
                break;
            case R.id.weibo_at:
                showToast(WriteCommentActivity.this,"还未实现~",200);
                break;
            case R.id.weibo_topic:
                insertTopic();
                showToast(WriteCommentActivity.this,"插入话题",200);
                break;
            case R.id.weibo_emotion:
                sendEmotion();
                break;
        }
    }
    public void insertTopic(){
        int curPosition = etComment.getSelectionStart();
        StringBuilder sb = new StringBuilder(etComment.getText().toString());
        sb.insert(curPosition,"##");
        // 特殊文字处理,将表情等转换一下
        etComment.setText(sb);
        // 将光标设置到新增完表情的右侧
        etComment.setSelection(curPosition + 1);
    }
    public  void sendEmotion(){
        //软键盘，显示或隐藏
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(llEmotionDashboard.getVisibility() == View.VISIBLE) {
            // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_emotion);
            llEmotionDashboard.setVisibility(View.GONE);
            imm.showSoftInput(etComment, 0);
        } else {
            // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_keyboard);
            llEmotionDashboard.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
        }
    }
    public void sendComment(){
        String comment = etComment.getText().toString();
        if(TextUtils.isEmpty(comment)) {
            showToast(WriteCommentActivity.this,"评论内容不能为空",200);
            return;
        }
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        commentsAPI = new CommentsAPI(this, Constants.APP_KEY,mAccessToken);
        long id = Long.parseLong(status.id);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            commentsAPI.create(comment,id,false,mListener);

        } else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(final String response) {

            showToast(WriteCommentActivity.this,"发送成功",200);

            // 微博发送成功后,设置Result结果数据,然后关闭本页面
            Intent data = new Intent();
            data.putExtra("sendCommentSuccess", true);
            setResult(RESULT_OK, data);

            WriteCommentActivity.this.finish();

        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
        }
    };
}
