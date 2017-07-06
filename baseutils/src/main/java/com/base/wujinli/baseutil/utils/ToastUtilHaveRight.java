package com.base.wujinli.baseutil.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.base.wujinli.baseutil.R;


public class ToastUtilHaveRight {
	private Activity mActvity;
	private Toast mToast = null;
	private TextView mTextView;
	private Context mContext;

	public ToastUtilHaveRight(Activity activity) {
		this.mActvity = activity;
		init();
	}

	public ToastUtilHaveRight(Context mContext) {
		this.mContext = mContext;
		init1();
	}

	private void init() {
		LayoutInflater inflater = mActvity.getLayoutInflater();
		View view = inflater.inflate(R.layout.toastright, (ViewGroup) mActvity.findViewById(R.id.realayout_id));
		mTextView = (TextView) view.findViewById(R.id.toastid);
		mToast = new Toast(mActvity.getApplicationContext());
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(view);
	}

	private void init1() {
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View view = inflater.inflate(R.layout.toast, (ViewGroup) ((Activity) mContext).findViewById(R.id.realayout_id));
		mTextView = (TextView) view.findViewById(R.id.toastid);
		mToast = new Toast(mContext.getApplicationContext());
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(view);
	}

	public void show(String tishi) {
		mTextView.setText(tishi);
		mToast.show();
	}
	
	public void showShort(String tishi) {
		mTextView.setText(tishi);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}
	
	public void showShort(int tishi) {
		mTextView.setText(tishi);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}
	
	public void show(int res) {
		mTextView.setText(res);
		mToast.show();
	}
}
