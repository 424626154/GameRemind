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
	//���ѿ���
	public int on_off = 0;
	//�����𶯿���
	public int vibration_on_off = 0;
	//����URL
	public String bell_url = "";
	
	public MusicInfo musicInfo = null;
	//��ǩ
	public int table = 0;
	// 0�� 1Сʱ 2�� 3��
	public int time_type = -1;
	public int time_index = -1;
			
}
