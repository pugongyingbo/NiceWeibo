package com.it.zzb.niceweibo.constant;

import android.text.TextUtils;

/**
 * Created by zzb on 2017/3/23.
 */

public class Constants {
    public static final String APP_KEY      = "3430037248";          // 应用的APP_KEY
    public static final String REDIRECT_URL = "http://www.sina.com";// 应用的回调页根据注册的回调页填写
    //应用申请的高级权限
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

    public static final String AppSecret = "c8fa049d8914496bcbba6f61b947ae94";
    public static final String PackageName = "com.it.zzb.niceweibo";


    public static final String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + Constants.APP_KEY
            + "&response_type=token&redirect_uri=" + Constants.REDIRECT_URL
            + "&key_hash=" + Constants.AppSecret + (TextUtils.isEmpty(Constants.PackageName) ? "" : "&packagename=" + Constants.PackageName)
            + "&display=mobile" + "&scope=" + Constants.SCOPE;
}
