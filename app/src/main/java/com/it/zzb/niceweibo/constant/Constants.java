package com.it.zzb.niceweibo.constant;

/**
 * Created by zzb on 2017/3/23.
 */

public class Constants {
    public static final String APP_KEY      = "3430037248";          // 应用的APP_KEY
    public static final String REDIRECT_URL = "http://www.sina.com";// 应用的回调页根据注册的回调页填写
    public static final String SCOPE =                          // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
