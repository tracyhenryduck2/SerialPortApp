package com.siterwell.seriallibrary.usbserial.util;


import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.siterwell.seriallibrary.R;

import cz.msebera.android.httpclient.Header;

public class Config {
	private static final String TAG = "Config";

	public final static String RootPath = "www.china-siter.cn";
	public final static String ApkVerUrl = "http://" + RootPath
			+ "/Siterwell_GS188X_Series.ver";
	public final static String ApkUrl = "http://" + RootPath
			+ "/Siterwell_GS188X_Series.apk";

	public static final String UPDATE_APKNAME="Siterwell_GS188X_Series.apk";


	public static int getVerCode(Context context,String packageName) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	
	public static String getVerName(Context context,String packageName) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;	

	}
	
	public static String getAppName(Context context) {
		String verName = context.getResources()
		.getText(R.string.app_name).toString();
		return verName;
	}

	/**
	 * 3.5 获取APP更新信息
	 *
	 * @param loginListener 回调接口
	 */

//	public static void getUpdateInfo(Context mContext, final HekrUser.LoginListener loginListener) {
//
//		BaseHttpUtil.getData(mContext, ApkVerUrl, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int i, Header[] headers, byte[] bytes) {
//				loginListener.loginSuccess(new String(bytes));
//			}
//
//			@Override
//			public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//				loginListener.loginFail(CodeUtil.getErrorCode(i, bytes));
//			}
//		});
//	}



	public static class UpdateInfo {
		public int code;
		public String name;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	};
}

