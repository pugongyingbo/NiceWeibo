package com.it.zzb.niceweibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.it.zzb.niceweibo.MessageActivity;
import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.UsersAPI;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.fragment.FindFragment;
import com.it.zzb.niceweibo.fragment.HomeFragment;
import com.it.zzb.niceweibo.fragment.MessageFragment;
import com.it.zzb.niceweibo.fragment.ProfileFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.GroupAPI;
import com.sina.weibo.sdk.openapi.models.Group;

import com.sina.weibo.sdk.openapi.models.User;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private BottomNavigationBar bottomNavigationBar;
    private ArrayList<Fragment> fragments;
    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private ProfileFragment profileFragment;
    private DrawerLayout drawer;
    private ArrayList<Group> groups;
    private Oauth2AccessToken mAccessToken;

    private UsersAPI userApi;
    private GroupAPI groupAPI;
    private ImageView icon_image;//头像
    private TextView userName;//名字
    private User user;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        userName = (TextView) findViewById(R.id.user_name);
        icon_image = (ImageView) findViewById(R.id.icon_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       // loadData();


        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
        //添加标记
        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.red)
                .setText("5")
                .setHideOnSelect(true);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_active, "主页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.message_active, "通知").setActiveColorResource(R.color.blue).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.discover_active, "探索").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.profile_active, "个人").setActiveColorResource(R.color.blue))
                .setFirstSelectedPosition(0)
                .initialise();

        setDefaultFragment();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                switch (position){
                    case 0:
                        if (homeFragment==null){
                            homeFragment=HomeFragment.newInstance("home");
                        }else {
                            fragmentTransaction.replace(R.id.layFrame,homeFragment);
                        }
                        break;
                    case 1:
                        if (messageFragment==null){
                            messageFragment=MessageFragment.newInstance("message");
                        }else {
                            fragmentTransaction.replace(R.id.layFrame,messageFragment);
                        }
                        break;
                    case 2:
                        if (findFragment==null){
                            findFragment = FindFragment.newInstance("find");
                        }else {
                            fragmentTransaction.replace(R.id.layFrame,findFragment);
                        }
                        break;
                    case 3:
                        if (profileFragment==null){
                            profileFragment=ProfileFragment.newInstance("profile");
                        }else {
                            fragmentTransaction.replace(R.id.layFrame,profileFragment);
                        }
                        break;

                }
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
    public void loadData(){
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        userApi = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        userApi.show(uid, mListener);
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment=HomeFragment.newInstance("home");
        transaction.replace(R.id.layFrame,homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_group) {
            //分组
            Intent intent = new Intent(MainActivity.this, GroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_message) {

            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(MainActivity.this,
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();
                    userName.setText(user.screen_name);
                } else {

                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

}
