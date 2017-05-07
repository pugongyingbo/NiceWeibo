package com.it.zzb.niceweibo;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.it.zzb.niceweibo.util.PrefUtils;
import com.it.zzb.niceweibo.util.ThemeTool;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
        tintManager.setTintColor(getResources().getColor(R.color.colorPrimary));
//        if (PrefUtils.isDarkMode()) {
//            tintManager.setTintColor(getResources(R.color.colorPrimaryDarkDarkTheme));
//        } else {
//            tintManager.setTintColor(getResources(R.color.primary_dark));
//        }
//        ThemeTool.changeTheme(this);
    }
}

