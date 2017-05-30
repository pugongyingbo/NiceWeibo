package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private Intent mStartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        } else {
            mStartIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }, 500);
    }
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(mStartIntent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}
