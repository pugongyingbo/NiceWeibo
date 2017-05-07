package com.it.zzb.niceweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.it.zzb.niceweibo.BaseActivity;
import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.UsersAPI;
import com.it.zzb.niceweibo.bean.User;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.fragment.FindFragment;
import com.it.zzb.niceweibo.ui.home.HomeFragment;
import com.it.zzb.niceweibo.ui.message.MessageActivity;
import com.it.zzb.niceweibo.ui.message.MessageFragment;
import com.it.zzb.niceweibo.ui.profile.ProfileFragment;
import com.it.zzb.niceweibo.util.CheckNetWork;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private NavigationView navigationView;
    private BottomNavigationBar bottomNavigationBar;
    private ArrayList<Fragment> fragments;
    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private ProfileFragment profileFragment;
    private DrawerLayout drawer;
    private Oauth2AccessToken mAccessToken;
    private ViewGroup viewGroup;
    private UsersAPI userApi;
    private ImageView icon_image;//头像
    private TextView userName;//名字
    private ImageLoader imageLoader = ImageLoader.getInstance();

    long waitTime = 2000;
    long touchTime = 0;
    //漂浮按钮
    private RapidFloatingActionButton fab_button_group;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionLayout fab_layout;

    public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
            String response=(String)msg.obj;
            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Message message=new Message();
        if(!CheckNetWork.isNetworkAvailable(MainActivity.this)){
            message.obj="网络错误!";
            handler.sendMessage(message);}

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        userName = (TextView) view.findViewById(R.id.user_name);
        icon_image = (ImageView) view.findViewById(R.id.icon_image);

//        fab_layout = (RapidFloatingActionLayout) findViewById(R.id.fab_layout);
//        fab_button_group = (RapidFloatingActionButton) findViewById(R.id.fab_button_group);
//        initFab();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SendWeiboActivity.class);
                startActivity(intent);

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         loadData();


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

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (position) {
                    case 0:
                        if (homeFragment == null) {
                            homeFragment = HomeFragment.newInstance("home");
                        } else {
                            fragmentTransaction.replace(R.id.layFrame, homeFragment);
                        }
                        break;
                    case 1:
//                        if (messageFragment==null){
//                            messageFragment=MessageFragment.newInstance("message");
//                        }else {
//                            fragmentTransaction.replace(R.id.layFrame,messageFragment);
//                        }
                        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;
                    case 2:
                        if (findFragment == null) {
                            findFragment = FindFragment.newInstance("find");
                        } else {
                            fragmentTransaction.replace(R.id.layFrame, findFragment);
                        }
                        break;
                    case 3:
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment.newInstance("profile");
                        } else {
                            fragmentTransaction.replace(R.id.layFrame, profileFragment);
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

    public void loadData() {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        userApi = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        if(mAccessToken != null && mAccessToken.isSessionValid()){
        long uid = Long.parseLong(mAccessToken.getUid());
        userApi.show(uid, mListener);
        }else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);

                userName.setText(user.screen_name);
                imageLoader.displayImage(user.avatar_hd,icon_image);

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }
    };


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
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = HomeFragment.newInstance("home");
        transaction.replace(R.id.layFrame, homeFragment);
        transaction.commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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


        } else if (id == R.id.nav_favorite) {

            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            new LogoutAPI(MainActivity.this, Constants.APP_KEY,
                    AccessTokenKeeper.readAccessToken(MainActivity.this)).logout(mLogoutListener);
            Toast.makeText(MainActivity.this, "已退出应用，请重新登录" ,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
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
           // super.onBackPressed();
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(this,"再按一次退出应用", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
            }
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private RequestListener mLogoutListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(MainActivity.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }

    };


    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {

    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if(i==0) {
            Intent intent = new Intent(this, SendWeiboActivity.class);
            startActivity(intent);
        }else{
        //    Intent intent = new Intent(this, SendPicActivity.class);

         //   startActivity(intent);
        }
    }
}
