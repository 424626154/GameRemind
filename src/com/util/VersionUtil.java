package com.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtil {
	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
		String versionName = "";  
		int versioncode = 0;
		try {  
			// ---get the package info---  
			PackageManager pm = context.getPackageManager();  
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
			versionName = pi.versionName;  
			versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {  
				return "";  
			}  
		} catch (Exception e) {  
			e.printStackTrace();
		}  
		return versionName;  
	} 
	private String getVersionName(Context context) throws Exception  
	{  
		// 获取packagemanager的实例  
		PackageManager packageManager = context.getPackageManager();  
		// getPackageName()是你当前类的包名，0代表是获取版本信息  
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);  
		String version = packInfo.versionName;  
		return version;  
	}  
}
