package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.adapter.MsgAdapter;
import com.it.zzb.niceweibo.bean.Msg;
import com.it.zzb.niceweibo.util.StringUtil;
import com.it.zzb.niceweibo.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class PrivateMsgActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private EditText input;
    private Button send;
    private RecyclerView msgView;
    private MsgAdapter adapter;
    private Toolbar toolbar;
    private ImageView weiboEmotion;//表情

    private LinearLayout llEmotionDashboard;
    private ViewPager vpEmotionDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_msg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("私信");
        //设置为ActionBar
        setSupportActionBar(toolbar);
        //显示那个箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initMsgs();



        input = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send_msg);
        msgView = (RecyclerView) findViewById(R.id.msg_view);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        msgView.setLayoutManager(layoutmanager);
        adapter = new MsgAdapter(msgList,getBaseContext());
        msgView.setAdapter(adapter);

        weiboEmotion = (ImageView) findViewById(R.id.weibo_emotion);
        llEmotionDashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vpEmotionDashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
        new ViewUtil(this,vpEmotionDashboard,input).initEmotion();

        weiboEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmotion();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input.getText().toString();
                SpannableString content1 = StringUtil.getWeiBoText(PrivateMsgActivity.this,content);
                if(!"".equals(content1)){
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgView.scrollToPosition(msgList.size()-1);
                    input.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("谢谢关注!", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
    }
    public  void sendEmotion(){
        //软键盘，显示或隐藏
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(llEmotionDashboard.getVisibility() == View.VISIBLE) {
            // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_emotion);
            llEmotionDashboard.setVisibility(View.GONE);
            imm.showSoftInput(input, 0);
        } else {
            // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_keyboard);
            llEmotionDashboard.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
}
