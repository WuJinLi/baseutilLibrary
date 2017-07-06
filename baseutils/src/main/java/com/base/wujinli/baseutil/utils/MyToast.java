package com.base.wujinli.baseutil.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {

	private static Toast mToast;

	public static void showToast(Context context, String text) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

}
