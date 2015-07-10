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
	public String week_json;
	public boolean []b_week = new boolean [7];
	//提醒开关
	public int on_off = 0;
	//提醒震动开关
	public int vibration_on_off = 0;
	//铃声URL
	public String bell_url = "";
	
	public MusicInfo musicInfo = null;
	//标签
	public int table = 0;
			
}
