package com.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtil {
	/** 
	 * ���ص�ǰ����汾�� 
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
		// ��ȡpackagemanager��ʵ��  
		PackageManager packageManager = context.getPackageManager();  
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);  
		String version = packInfo.versionName;  
		return version;  
	}  
}
