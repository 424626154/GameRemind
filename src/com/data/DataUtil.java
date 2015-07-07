package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

public class DataUtil {
	public static final int FILTER_ALL_APP = 0; // 所有应用程序
	public static final int FILTER_SYSTEM_APP = 1; // 系统程序
	public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
	public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序
	// 根据查询条件，查询特定的ApplicationInfo
	public static List<AppInfo> queryFilterAppInfo(PackageManager pm,int filter) {
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> listAppcations = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,
				new ApplicationInfo.DisplayNameComparator(pm));// 排序
		List<AppInfo> appInfos = new ArrayList<AppInfo>(); // 保存过滤查到的AppInfo
		// 根据条件来过滤
		switch (filter) {
		case FILTER_ALL_APP: // 所有应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				appInfos.add(getAppInfo(pm,app));
			}
			return appInfos;
		case FILTER_SYSTEM_APP: // 系统程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					appInfos.add(getAppInfo(pm,app));
				}
			}
			return appInfos;
		case FILTER_THIRD_APP: // 第三方应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					appInfos.add(getAppInfo(pm,app));
				}
			}
			break;
		case FILTER_SDCARD_APP: // 安装在SDCard的应用程序
			appInfos.clear();
			for (ApplicationInfo app : listAppcations) {
				if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
					appInfos.add(getAppInfo(pm,app));
				}
			}
			return appInfos;
		default:
			return null;
		}
		return appInfos;
	}
	// 构造一个AppInfo对象 ，并赋值
	private static AppInfo getAppInfo(PackageManager pm ,ApplicationInfo app) {
		AppInfo appInfo = new AppInfo();
		appInfo.setAppLabel((String) app.loadLabel(pm));
		appInfo.setAppIcon(app.loadIcon(pm));
		appInfo.setPkgName(app.packageName);
		return appInfo;
	}
	
	
	public static int adjustFontSize(WindowManager windowmanager) {
		int screenWidth = windowmanager.getDefaultDisplay().getWidth();
		int screenHeight = windowmanager.getDefaultDisplay().getHeight();
		if (screenWidth <= 240) { // 240X320 屏幕
			return 20;
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return 24;
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return 34;
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return 36;
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return 40;
		} else { // 大于 800X1280
			return 40;
		}
	}

}
