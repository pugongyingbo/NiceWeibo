package com.it.zzb.niceweibo;

import android.app.Application;
import android.content.Context;


import com.it.zzb.niceweibo.util.ImageOptHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApplication extends Application {

	private static Context mContext;

	public static Context getContext() {
		return mContext;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		
		initImageLoader(this);
	}
	
	// 初始化图片处理
	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(ImageOptHelper.getImgOptions())
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
