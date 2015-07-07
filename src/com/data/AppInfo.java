package com.data;

import java.io.Serializable;

import android.content.Intent;
import android.graphics.drawable.Drawable;

//Model类 ，用来存储应用程序信息
public class AppInfo implements Serializable{
  
	public String appLabel;    //应用程序标签
	public Drawable appIcon ;  //应用程序图像
	public Intent intent ;     //启动应用程序的Intent ，一般是Action为Main和Category为Lancher的Activity
	public String pkgName ;    //应用程序所对应的包名
	
	public int num ;//提醒个数
	
	public AppInfo(){}
	
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appName) {
		this.appLabel = appName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public String getPkgName(){
		return pkgName ;
	}
	public void setPkgName(String pkgName){
		this.pkgName=pkgName ;
	}
}
