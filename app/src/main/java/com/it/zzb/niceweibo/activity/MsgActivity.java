package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.it.zzb.niceweibo.R;

public class MsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout layout1;
    private LinearLayout layout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
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

        layout1 = (LinearLayout) findViewById(R.id.ll_msg);
        layout2 = (LinearLayout) findViewById(R.id.ll_msg1);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MsgActivity.this, PrivateMsgActivity.class);
                startActivity(intent);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MsgActivity.this, PrivateMsgActivity.class);
                startActivity(intent);
            }
        });

    }
}
