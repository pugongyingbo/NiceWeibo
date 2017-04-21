package com.it.zzb.niceweibo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.it.zzb.niceweibo.R;


public class StringUtils {

	public static SpannableString getWeiboContent(final Context context, final TextView tv, String source) {
		String regexAt = "@[\u4e00-\u9fa5\\w]+";
		String regexTopic = "#[\u4e00-\u9fa5\\w]+#";
		String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";
		
		String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")";
		
		SpannableString spannableString = new SpannableString(source);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(spannableString);
		
		if(matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		
		while(matcher.find()) {
			final String atStr = matcher.group(1);
			final String topicStr = matcher.group(2);
			String emojiStr = matcher.group(3);
			
			if(atStr != null) {
				int start = matcher.start(1);
				
				BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {
					
					@Override
					public void onClick(View widget) {
						ToastUtils.showToast(context, "用户: " + atStr, Toast.LENGTH_SHORT);
					}
				};
				spannableString.setSpan(clickableSpan, start, start + atStr.length(), 
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(topicStr != null) {
				int start = matcher.start(2);
				
				BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {
					
					@Override
					public void onClick(View widget) {
						ToastUtils.showToast(context, "话题: " + topicStr, Toast.LENGTH_SHORT);
					}
				};
				spannableString.setSpan(clickableSpan, start, start + topicStr.length(), 
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(emojiStr != null) {
				int start = matcher.start(3);
				
				int imgRes = EmotionUtils.getImgByName(emojiStr);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
				
				if(bitmap != null) {
					int size = (int) tv.getTextSize();
					bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
					
					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start + emojiStr.length(), 
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
			
			
		}
		

		return spannableString;
	}

	static class BoreClickableSpan extends ClickableSpan {

		private Context context;
		
		public BoreClickableSpan(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(context.getResources().getColor(R.color.txt_at_blue));
			ds.setUnderlineText(false);
		}
	}
	/**
	 * 用于weibo text中的连接跳转
	 */
	private static class MyURLSpan extends ClickableSpan {
		private String mUrl;
		private Context context;

		MyURLSpan(Context ctx, String url) {
			context = ctx;
			mUrl = url;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.parseColor("#f44336"));
		}

		@Override
		public void onClick(View widget) {


		}
	}

	/**
	 * 用于转发 weibo 中 @名字的点击跳转
	 */
	private static class MyAtSpan extends ClickableSpan {
		private String mName;
		private Context context;

		MyAtSpan(Context ctx, String name) {
			context = ctx;
			mName = name;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.parseColor("#3F51B5"));
			ds.setUnderlineText(false); //去掉下划线
		}

		@Override
		public void onClick(View widget) {
			mName = mName.substring(1);
		}
	}

	/**
	 * 用于转发 weibo 中 Tag 的点击跳转
	 */
	private static class MyTagSpan extends ClickableSpan {
		private String mTag;
		private Context context;

		MyTagSpan(Context ctx, String tag) {
			context = ctx;
			mTag = tag;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.parseColor("#3F51B5"));
			ds.setUnderlineText(false); //去掉下划线
		}

		@Override
		public void onClick(View widget) {
//            mTag = mTag.substring(1,mTag.length()-1);
//            System.out.println("---tag--"+mTag);
//            Intent intent = TagListActivity.newIntent(context,mTag);
//            context.startActivity(intent);
		}
	}
	public static TextWatcher textNumberListener(final EditText editText, final TextView textView, final Context context) {
		//输入字符监听
		TextWatcher mTextWatcher = new TextWatcher() {
			private CharSequence temp;
			private int editStart;
			private int editEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				temp = s;
				if (140 - s.length() == 0) {
					textView.setText("超出字数限制！");
					textView.setTextColor(Color.RED);
				} else {
					textView.setText(String.valueOf(140 - s.length()));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editStart = editText.getSelectionStart();
				editEnd = editText.getSelectionEnd();
				if (temp.length() > 140) {
					Toast.makeText(context,
							"你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
							.show();
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					editText.setText(s);
					editText.setSelection(tempSelection);
				}
			}
		};

		return mTextWatcher;
	}

	/**
	 * 实现文本复制功能
	 * add by wangqianzhou
	 *
	 * @param content
	 */
	public static void copy(String content, Context context) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 实现粘贴功能
	 * add by wangqianzhou
	 *
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}
}
