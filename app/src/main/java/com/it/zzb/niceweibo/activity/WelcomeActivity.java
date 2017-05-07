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
    private static final int WHAT_INTENT2LOGIN = 1;
    private static final int WHAT_INTENT2MAIN = 2;
    private static final long SPLASH_DUR_TIME = 1000;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case WHAT_INTENT2LOGIN:
                    mStartIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    finish();
                    break;
                case WHAT_INTENT2MAIN:
                    mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        if (AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
//        mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);
//    } else {
//        mStartIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
//    }

        if(AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            mHandler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
        }

}
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
