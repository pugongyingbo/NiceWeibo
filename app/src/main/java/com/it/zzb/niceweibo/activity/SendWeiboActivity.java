package com.it.zzb.niceweibo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.api.StatusesAPI;
import com.it.zzb.niceweibo.bean.Status;
import com.it.zzb.niceweibo.constant.AccessTokenKeeper;
import com.it.zzb.niceweibo.constant.Constants;
import com.it.zzb.niceweibo.util.StringUtil;
import com.it.zzb.niceweibo.util.ToastUtils;
import com.it.zzb.niceweibo.util.ViewUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.it.zzb.niceweibo.util.ToastUtils.showToast;

/**
 * 发微博页面
 */
public class SendWeiboActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 2 ;
    private EditText etWeibo;
    private RecyclerView weiboPhotoGrid;
    private LinearLayout option;
    private ImageView weiboLocal;//地址
    private ImageView weiboPhoto;//图片
    private ImageView weiboTopic;//主题
    private ImageView weiboAt;//@
    private ImageView weiboEmotion;//表情
    private ImageView sendWeibo;//发送
    private TextView tvWeiboNumber;
    private LinearLayout llEmotionDashboard;
    private ViewPager vpEmotionDashboard;
    // 待评论的微博
    private Status status;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI statusesAPI;
    private Toolbar toolbar;

    private static final int REQUEST_CAMERA_CODE = 11;
    private static final int REQUEST_PREVIEW_CODE = 22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_weibo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        toolbar.setTitle("发送微博");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                ToastUtils.showToast(getBaseContext(),"storage",200);
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {
                ToastUtils.showToast(getBaseContext(),"storage",200);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }
        initView();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
    public void initView(){
        etWeibo = (EditText) findViewById(R.id.et_weibo);
        weiboPhotoGrid = (RecyclerView) findViewById(R.id.weibo_photo_grid);
        option = (LinearLayout) findViewById(R.id.option);
        //weiboLocal = (ImageView) findViewById(R.id.weibo_local);
        sendWeibo = (ImageView) findViewById(R.id.send_weibo);
        weiboPhoto = (ImageView) findViewById(R.id.weibo_photo);
        weiboTopic = (ImageView) findViewById(R.id.weibo_topic);
        weiboAt = (ImageView) findViewById(R.id.weibo_at);
        weiboEmotion = (ImageView) findViewById(R.id.weibo_emotion);
        tvWeiboNumber = (TextView) findViewById(R.id.tv_weibo_number);
        llEmotionDashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
        vpEmotionDashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
        sendWeibo = (ImageView) findViewById(R.id.send_weibo);
        new ViewUtil(this,vpEmotionDashboard,etWeibo).initEmotion();
        etWeibo.addTextChangedListener(StringUtil.textNumberListener(etWeibo,tvWeiboNumber,this));
        weiboAt.setOnClickListener(this);
        weiboPhoto.setOnClickListener(this);
        weiboEmotion.setOnClickListener(this);
        weiboTopic.setOnClickListener(this);
        sendWeibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weibo_photo:
                sendPic();
                break;
            case R.id.send_weibo:
                sendWeibo();
                break;
            case R.id.weibo_at:
                showToast(SendWeiboActivity.this,"还未实现~",200);
                break;
            case R.id.weibo_topic:
                insertTopic();
                showToast(SendWeiboActivity.this,"插入话题",200);
                break;
            case R.id.weibo_emotion:
                sendEmotion();
                break;

        }
    }

    public void sendPic(){
        PhotoPickerIntent intent = new PhotoPickerIntent(SendWeiboActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
// intent.setImageConfig(config);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }
    private ImageCaptureManager captureManager;

//    btnCarema.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            try {
//                if(captureManager == null){
//                    captureManager = new ImageCaptureManager(SendWeiboActivity.this);
//                }
//                Intent intent = captureManager.dispatchTakePictureIntent();
//                startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
//            } catch (IOException e) {
//                Toast.makeText(SendWeiboActivity.this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if(captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        String imagePaht = captureManager.getCurrentPhotoPath();
                        // ...
                    }
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
            }
        }
    }

    private void refreshAdpater(ArrayList<String> paths){
        // 处理返回照片地址
    }
    public void insertTopic(){
        int curPosition = etWeibo.getSelectionStart();
        StringBuilder sb = new StringBuilder(etWeibo.getText().toString());
        sb.insert(curPosition,"##");
        // 特殊文字处理,将表情等转换一下
        etWeibo.setText(sb);
        // 将光标设置到新增完表情的右侧
        etWeibo.setSelection(curPosition + 1);
    }
    public  void sendWeibo(){
        String content = etWeibo.getText().toString();
        if(TextUtils.isEmpty(content)) {
            showToast(SendWeiboActivity.this,"内容不能为空",200);
            return;
        }
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取用户信息接口
        statusesAPI = new StatusesAPI(this,Constants.APP_KEY,mAccessToken);

        if (mAccessToken != null && mAccessToken.isSessionValid()) {

            statusesAPI.update(content,"0.0","0.0",mListener);

        } else {
            Toast.makeText(this, "token不存在，请重新授权", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(final String response) {

            showToast(SendWeiboActivity.this,"发送成功",200);

            // 微博发送成功后,设置Result结果数据,然后关闭本页面
            Intent data = new Intent();
            data.putExtra("sendSuccess", true);
            setResult(RESULT_OK, data);
            SendWeiboActivity.this.finish();

        }

        @Override
        public void onWeiboException(WeiboException e) {
            // TODO Auto-generated method stub
            e.printStackTrace();
        }
    };

    public  void sendEmotion(){
        //软键盘，显示或隐藏
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(llEmotionDashboard.getVisibility() == View.VISIBLE) {
            // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_emotion);
            llEmotionDashboard.setVisibility(View.GONE);
            imm.showSoftInput(etWeibo, 0);
        } else {
            // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
            weiboEmotion.setImageResource(R.drawable.btn_insert_keyboard);
            llEmotionDashboard.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(etWeibo.getWindowToken(), 0);
        }
    }
}
