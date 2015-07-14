package com.data;

import java.io.Serializable;

import com.module.MusicInfo;

import android.graphics.drawable.Drawable;

public class Remind implements Serializable{
	public long _id ;
	public String appLabel;  
	public Drawable appIcon ;
	public String pkgName ;
	public int hour;
	public int minute;
	public String remarks;
	//提醒开关
	public int on_off = 0;
	//提醒震动开关
	public int vibration_on_off = 0;
	//铃声URL
	public String bell_url = "";
	
	public MusicInfo musicInfo = null;
	//标签
	public int table = 0;
	// 0月 1小时 2天 3周
	public int time_type = -1;
	public int time_index = -1;
			
}
