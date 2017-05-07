package com.it.zzb.niceweibo.util;

import android.app.Activity;

import com.it.zzb.niceweibo.R;

/**
 * Created by zzb on 2017/5/2.
 */

public class ThemeTool {
    public static void changeTheme(Activity activity) {
        if (PrefUtils.isDarkMode()) {
 //           activity.setTheme(R.style.AppThemeDark);
        }
    }
}

