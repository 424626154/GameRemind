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
	//���ѿ���
	public int on_off = 0;
	//�����𶯿���
	public int vibration_on_off = 0;
	//����URL
	public String bell_url = "";
	
	public MusicInfo musicInfo = null;
			
}
