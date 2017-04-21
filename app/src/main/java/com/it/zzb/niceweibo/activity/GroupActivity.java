package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.adapter.GroupAdapter;
import com.it.zzb.niceweibo.api.GroupAPI;
import com.it.zzb.niceweibo.bean.Group;
import com.it.zzb.niceweibo.bean.GroupList;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Group> groups;
    private Oauth2AccessToken mAccessToken;
    private ListView listView;
    private GroupAPI groupAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        listView = (ListView) findViewById(R.id.group_list);
        loadData();

    }
    private void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        groupAPI = new GroupAPI(this,Constants.APP_KEY,mAccessToken);

        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            groupAPI.groups(mListener);

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
                Log.d("d",response);
                GroupList groupList = GroupList.parse(response);

                Toast.makeText(GroupActivity.this,
                        "获取分组成功, 条数: " + groupList.groupList.size(),
                        Toast.LENGTH_LONG).show();

                    if (groupList != null && groupList.total_number > 0) {

                        GroupAdapter groupAdapter = new GroupAdapter(getBaseContext(),groupList);
                        listView.setAdapter(groupAdapter);
                    }

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
        }
    };
}
